import axios from 'axios';

// Environment-specific API base URL configuration
const API_BASE_URL = process.env.REACT_APP_API_BASE_URL || 'http://localhost:8080/entrytransaction';

axios.defaults.baseURL = API_BASE_URL;
axios.defaults.headers.common['Accept'] = 'application/json';
axios.defaults.headers.get['Content-Type'] = 'application/json';
axios.defaults.headers.post['Content-Type'] = 'application/json';
axios.defaults.headers.put['Content-Type'] = 'application/json';
axios.defaults.headers.delete['Content-Type'] = 'application/json';
axios.defaults.timeout = 30000; // Set a global timeout of 30 seconds

class EntryTransactionService {

    constructor() {
        // Handle global axios errors
        axios.interceptors.response.use(this.handleSuccess, this.handleError);
    }

    handleSuccess(response) {
        return response;
    }

    handleError(error) {
        // Generic error handling
        console.error("Error in API call", error);
        return Promise.reject(error);
    }

    getAll(page = 0, size = 10) {
        // Fetch all entries with pagination
        return axios.get('', {
            params: { page, size }
        });
    }

    getById(id) {
        // Fetch a single entry by ID
        return axios.get(`/${id}`);
    }

    create(entryTransaction) {
        // Create a new entry
        return axios.post('', entryTransaction);
    }

    update(id, entryTransaction) {
        // Update an existing entry
        return axios.put(`/${id}`, entryTransaction);
    }

    delete(id) {
        // Delete an entry
        return axios.delete(`/${id}`);
    }
    
    fetchAccounts = () => {
        return axios.get("http://localhost:8080/accounts");
    };

    fetchAccount(accountId) {
        return axios.get("http://localhost:8080/accounts" + `/${accountId}`);
    };

}

export default new EntryTransactionService();
