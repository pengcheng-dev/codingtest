import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import EntryTransactionList from './component/List';
import EntryTransactionForm from './component/Form';
import './styles.css';

const App = () => (
    <Router>
        <Routes>
            <Route path="/" element={<EntryTransactionList />}  />
            <Route path="/create" element={<EntryTransactionForm />} />
            <Route path="/edit/:id" element={<EntryTransactionForm />} />
        </Routes>
    </Router>
);

export default App;
