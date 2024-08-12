import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import EntryTransactionService from '../api/Service';
import Paginator from './Paginator';

const EntryTransactionList = () => {
    const [entryTransactions, setEntryTransactions] = useState([]);
    const [error, setError] = useState('');
    const [currentPage, setCurrentPage] = useState(0);
    const pageSize = 10;

    useEffect(() => {
        loadEntryTransactions(currentPage);
    }, [currentPage]);

    const loadEntryTransactions = async (page) => {
        try {
            const response = await EntryTransactionService.getAll(page, pageSize);
            setEntryTransactions(response.data.content);
        } catch (err) {
            setError('Failed to fetch data');
        }
    };

    const handleDelete = async (id) => {
        try {
            await EntryTransactionService.delete(id);
            loadEntryTransactions(currentPage); // Refresh the list
        } catch (err) {
            setError('Failed to delete entry');
        }
    };

    return (
        <div>
            <h1>Entry Transactions</h1>
            <Link to="/create"><button>Create New EntryTransaction</button></Link>
            {error && <p>{error}</p>}
            <table>
                <thead>
                    <tr>
                        {["ID", "Type", "Amount", "Transaction Date", "Fund ID", "Date Created", "Last Updated", "Account Incremental ID", "Account ID", 
                            "Account Code", "Account Name", "Account Class", "Account Type", "Entry ID", "Entry Amount", "Entry GST Amount", "Entry Type", "Actions"].map(header => <th key={header}>{header}</th>)}
                    </tr>
                </thead>
                <tbody>
                    {entryTransactions.map(entryTransaction => (
                        <tr key={entryTransaction.id}>
                            <td>{entryTransaction.id}</td>
                            <td>{entryTransaction.type}</td>
                            <td>{entryTransaction.amount}</td>
                            <td>{entryTransaction.transactionDate}</td>
                            <td>{entryTransaction.fundId}</td>
                            <td>{entryTransaction.dateCreated}</td>
                            <td>{entryTransaction.lastUpdated}</td>
                            <td>{entryTransaction.accountIncrementalId}</td>
                            <td>{entryTransaction.accountId}</td>
                            <td>{entryTransaction.accountCode}</td>
                            <td>{entryTransaction.accountName}</td>
                            <td>{entryTransaction.accountClass}</td>
                            <td>{entryTransaction.accountType}</td>
                            <td>{entryTransaction.entryId}</td>
                            <td>{entryTransaction.entryAmount}</td>
                            <td>{entryTransaction.entryGstAmount}</td>
                            <td>{entryTransaction.entryType}</td>
                            <td>
                                <Link to={`/edit/${entryTransaction.id}`}><button>Edit</button></Link>
                                <button onClick={() => handleDelete(entryTransaction.id)}>Delete</button>
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>
            <Paginator currentPage={currentPage} setPage={setCurrentPage} />
        </div>
    );
};

export default EntryTransactionList;
