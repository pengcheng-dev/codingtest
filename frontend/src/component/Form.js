import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { Formik, Form, Field, ErrorMessage, useFormikContext } from 'formik';
import * as Yup from 'yup';
import { format, parse } from 'date-fns'; // date-fns for date manipulation
import EntryTransactionService from '../api/Service';
import styles from '../Form.module.css';

// Configuration for dynamic fields based on entry type
// This part need to be changed synchonized with the back-end changes
const fieldConfigurations = {
    BasicBank: {
        Field1: { label: "Field1", type: "string" },
        Field2: { label: "Field2", type: "number" }
    },
    DistributionInterest: {
        Field1: { label: "Field1", type: "string" },
        Field2: { label: "Field2", type: "number" }
    },
    Dividend: {
        Field1: { label: "Field1", type: "string" },
        Field2: { label: "Field2", type: "number" }
    },
    Contribution: {
        Field1: { label: "Field1", type: "string" },
        Field2: { label: "Field2", type: "number" }
    },
    Investment: {
        Field1: { label: "Field1", type: "string" },
        Field2: { label: "Field2", type: "number" }
    }
};

function formatDate(dateStr) {
    const parsedDate = parse(dateStr, 'dd/MM/yyyy', new Date());
    return format(parsedDate, 'yyyy-MM-dd');
}

// Initial values based on whether it's an update or create
const getInitialValues = (isUpdate, data = {}) => {
 
    // Setup initial values for dynamic fields based on the selected entryType
    const dynamicFields = {};
    if (data.entryType && fieldConfigurations[data.entryType]) {
        Object.keys(fieldConfigurations[data.entryType]).forEach(key => {
            // Ensure every dynamic field has a default value, avoiding 'undefined'
            dynamicFields[key] = data.nameValueMap && data.nameValueMap[key] !== undefined ? data.nameValueMap[key] : '';
        });
    } else {
        // If no data or entryType, setup all possible dynamic fields with empty defaults
        Object.values(fieldConfigurations).forEach(config => {
            Object.keys(config).forEach(key => {
                dynamicFields[key] = '';
            });
        });
    }

    return {
        type: data.type || '',
        amount: data.amount || '',
        transactionDate: isUpdate && data.transactionDate ? formatDate(data.transactionDate) : '',
        fundId: data.fundId || '',
        accountIncrementalId: data.accountIncrementalId || '',
        accountId: data.accountId || '',
        accountCode: data.accountCode || '',
        accountName: data.accountName || '',
        accountClass: data.accountClass || '',
        accountType: data.accountType || '',
        parentId: data.parentId || '',
        entryId: isUpdate ? data.entryId : '',
        entryAmount: data.entryAmount || '',
        entryGstAmount: data.entryGstAmount || '',
        entryType: data.entryType || '',
        nameValueMap: dynamicFields
    };

};

// Dynamic Yup validation schema based on entryType
const validationSchema = (entryType) => {
    let schema = {
        type: Yup.string().required('Type is required'),
        amount: Yup.number().required('Amount is required').positive('Must be a positive number'),
        transactionDate: Yup.date().required('Transaction date is required'),
        fundId: Yup.string().required('Fund ID is required'),
        
        accountIncrementalId: Yup.number().required('Selecting an account is required'),
        // accountId: Yup.number().required('Account ID is required').positive('Must be a positive number'),
        // accountCode: Yup.string().required('Account Code is required'),
        // accountName: Yup.string().required('Account Name is required'),
        // accountClass: Yup.string().required('Account Class is required'),
        // accountType: Yup.string().required('Account Type is required'),
        // parentId: Yup.number().positive('Parent ID must be a positive number'),
        
        entryId: Yup.number().nullable().strip(), //can't be edited manually
        entryAmount: Yup.number().required('Entry Amount is required').positive('Must be a positive number'),
        entryGstAmount: Yup.number().required('Entry GST Amount is required').positive('Must be a positive number'),
        entryType: Yup.string().required('Entry Type is required')
    };

    // Add dynamic fields to the schema
    Object.entries(fieldConfigurations[entryType] || {}).forEach(([key, value]) => {
        schema[`nameValueMap.${key}`] = value.type === 'number'
            ? Yup.number().notRequired().positive(`${value.label} must be a positive number`)
            : Yup.string().notRequired();
    });

    return Yup.object().shape(schema);
};

const EntryTransactionForm = () => {
    const { id } = useParams();
    const navigate = useNavigate();
    const [accounts, setAccounts] = useState([]);
    const [entryType, setEntryType] = useState('');
    const [accountIncrementalId, setAccountIncrementalId] = useState('');
    const [accountDetails, setAccountDetails] = useState({});
    const [initialValues, setInitialValues] = useState(getInitialValues(false));
    const [error, setError] = useState(null);

    useEffect(() => {
        if (id) {
            EntryTransactionService.getById(id)
                .then(response => {
                    setEntryType(response.data.entryType);
                    setAccountIncrementalId(response.data.accountIncrementalId);
                    setInitialValues(getInitialValues(true, response.data));
                    handleAccountChange(response.data.accountIncrementalId); 
                })
                .catch(error => handleError(error, navigate, setError));
                
        }

        EntryTransactionService.fetchAccounts()
            .then(response => {
                setAccounts(response.data);
            })
            .catch(error => console.error('Failed to fetch accounts', error));

    }, [id]);

    const handleAccountChange = (accountId) => {
        if (!accountId) {
            setAccountDetails({});
            return;
        }
        EntryTransactionService.fetchAccount(accountId)
            .then(response => {
                setAccountDetails(response.data);
            })
            .catch(error => console.error('Error fetching account details', error));
    };

    const handleError = (error, navigate, setError) => {
        if (error.response) {
            if (error.response.status === 404) {
                navigate('/not-found');
            } else if (error.response.status === 500) {
                setError('Internal server error (500). Please try again later.');
            } else {
                setError(`Unexpected error: ${error.response.status}. Please try again later.`);
            }
        } else {
            setError('Network error. Please check your connection.');
        }
    };
    
    if (error) {
        return <div className={styles.error}>{error}</div>;
    }

    return (
        <Formik
        initialValues={initialValues}
        enableReinitialize={true}
        validationSchema={validationSchema(entryType)}
        onSubmit={(values, { setSubmitting, setErrors }) => {
            if (values.transactionDate) {
                // Format the date from 'yyyy-MM-dd' to 'dd/MM/yyyy'
                const formattedDate = format(parse(values.transactionDate, 'yyyy-MM-dd', new Date()), 'dd/MM/yyyy');
                values.transactionDate = formattedDate;
            }
            const action = id ? EntryTransactionService.update(id, values) : EntryTransactionService.create(values);
            action.then(() => {
                navigate('/');
            }).catch(error => {
                console.error('Failed to process form', error);
                setErrors({ submit: 'Failed to process the form. Please try again.' });
            }).finally(() => {
                setSubmitting(false);
            });
        }}
        >

            {formik => (
                <Form className={styles.verticalForm}>
                <div key="entryTransactionPart">
                    <div>
                        <label htmlFor="type">Type:</label>
                        <Field name="type" type="text" />
                        <ErrorMessage name="type" component="div" />
                    </div>
                    <div>
                        <label htmlFor="amount">Amount:</label>
                        <Field id="amount" name="amount" type="number" />
                        <ErrorMessage name="amount" component="div" />
                    </div>
                    <div>
                        <label htmlFor="transactionDate">Transaction Date:</label>
                        <Field id="transactionDate" name="transactionDate" type="date" />
                        <ErrorMessage name="transactionDate" component="div" />
                    </div>
                    <div>
                        <label htmlFor="fundId">Fund ID:</label>
                        <Field id="fundId" name="fundId" type="text" />
                        <ErrorMessage name="fundId" component="div" />
                    </div>
                </div>

                <div key="accountPart">
                    <div>
                        <label htmlFor="accountId">Account:</label>
                        <Field as="select" name="accountIncrementalId"  onChange={e => {
                                formik.handleChange(e);
                                handleAccountChange(e.target.value);
                            }}>
                            <option value="">Select Account</option>
                            {accounts.map(account => (
                                <option key={account.id} value={account.id}>{account.name}</option>
                            ))}
                        </Field>
                        <ErrorMessage name="accountIncrementalId" component="div" />
                    </div>

                    <div><label htmlFor="accountId">  Account ID: {accountDetails.accountID}  </label></div>

                    <div><label htmlFor="accountCode">  Account Code: {accountDetails.code}  </label></div>

                    <div><label htmlFor="accountName">  Account Name: {accountDetails.name}  </label></div>

                    <div><label htmlFor="accountClass">  Account Class:  {accountDetails.accountClass}  </label></div>

                    <div><label htmlFor="accountType">  Account Type:  {accountDetails.accountType}  </label></div>

                    <div><label htmlFor="parentId">  Parent ID:  {accountDetails.pid}  </label></div>
                </div>
                
                <div key="entryPart">
                    <div>
                        <label htmlFor="entryId">Entry ID (disabled):</label>
                        <Field id="entryId" name="entryId" type="number" disabled={true} />
                        <ErrorMessage name="entryId" component="div" />
                    </div>
                    <div>
                        <label htmlFor="entryAmount">Entry Amount:</label>
                        <Field id="entryAmount" name="entryAmount" type="number" />
                        <ErrorMessage name="entryAmount" component="div" />
                    </div>
                    <div>
                        <label htmlFor="entryGstAmount">Entry GST Amount:</label>
                        <Field id="entryGstAmount" name="entryGstAmount" type="number" />
                        <ErrorMessage name="entryGstAmount" component="div" />
                    </div>
                    <div>
                        <label htmlFor="entryType">Entry Type:</label>
                        <Field as="select" name="entryType" onChange={e => {

                            const newType = e.target.value;
                            setEntryType(newType);

                            // Reset the dynamic fields based on the new type selected
                            const updatedNameValueMap = {};
                            Object.keys(fieldConfigurations[newType] || {}).forEach(key => {
                                updatedNameValueMap[key] = ''; // Reset to empty string or appropriate default
                            });

                            formik.setValues({
                                ...formik.values,
                                entryType: newType,
                                nameValueMap: updatedNameValueMap
                            });

                        }}>
                            <option value="">Select Type</option>
                            {Object.keys(fieldConfigurations).map(type => (
                                <option key={type} value={type}>{type}</option>
                            ))}
                        </Field>
                        <ErrorMessage name="entryType" component="div" />

                        {entryType && Object.entries(fieldConfigurations[entryType] || {}).map(([key, config]) => (
                            <div key={key}>
                                <label htmlFor={`nameValueMap.${key}`}>{config.label}</label>
                                <Field name={`nameValueMap.${key}`} type={config.type === "number" ? "number" : "text"} />
                                <ErrorMessage name={`nameValueMap.${key}`} component="div" />
                            </div>
                        ))}
                    </div>
                </div>
                
                <button type="submit" className={styles.button} disabled={formik.isSubmitting}>{id ? 'Update' : 'Create'}</button>
                <ErrorMessage name="submit" component="div" className={styles.error} />
            </Form>
            )}

        </Formik>
    );
    
};

export default EntryTransactionForm;
