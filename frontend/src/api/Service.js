import axios from 'axios';

//environment viarable for API base URL
const API_BASE_URL = process.env.REACT_APP_API_BASE_URL || 'http://localhost:8080';

const apiClient = axios.create({
    baseURL: API_BASE_URL,
    headers: {
        'Content-Type': 'application/json',
    },
    timeout: 3000, // 3 seconds timeout
});

//handling errors globally
apiClient.interceptors.response.use(
    response => response,
    error => {
        if (error.response) {
            const status = error.response.status;
            let errorMessage = 'An error occurred';

            if (status === 404) {
                errorMessage = 'Resource not found (404).';
            } else if (status === 500) {
                errorMessage = 'Internal server error (500). Please try again later.';
            } else {
                errorMessage = `Unexpected error (${status}): ${error.response.data.message || error.message}`;
            }

            return Promise.reject(new Error(errorMessage));
        } else if (error.request) {
            console.error('Network Error:', error.message);
            return Promise.reject(new Error('Network error: Please check your internet connection.'));
        } else {
            console.error('Error:', error.message);
            return Promise.reject(new Error(`Error: ${error.message}`));
        }
    }
);

//retry
const retryRequest = async (fn, retries = 3, delay = 1000) => {
    for (let i = 0; i < retries; i++) {
        try {
            return await fn();
        } catch (error) {
            if (i < retries - 1) {
                console.warn(`Retrying request (${i + 1}/${retries})...`);
                await new Promise(res => setTimeout(res, delay));
            } else {
                throw error;
            }
        }
    }
};

//service methods
const EntryTransactionService = {
    // EntryTransaction endpoints
    getAll: (page, size) => retryRequest(() => apiClient.get(`/entrytransaction?page=${page}&size=${size}`)),

    getById: id => retryRequest(() => apiClient.get(`/entrytransaction/${id}`)),

    create: data => retryRequest(() => apiClient.post('/entrytransaction', data)),

    update: (id, data) => retryRequest(() => apiClient.put(`/entrytransaction/${id}`, data)),

    delete: id => retryRequest(() => apiClient.delete(`/entrytransaction/${id}`)),

    // Account endpoints
    fetchAccounts: () => retryRequest(() => apiClient.get('/accounts')),

    fetchAccount: accountId => retryRequest(() => apiClient.get(`/accounts/${accountId}`)),
};

export default EntryTransactionService;