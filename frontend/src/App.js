import React, { Suspense, lazy } from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import './styles.css';

const EntryTransactionList = lazy(() => import('./component/List'));
const EntryTransactionForm = lazy(() => import('./component/Form'));
const NotFound = lazy(() => import('./component/NotFound'));

const App = () => (
    <Router>
        <Suspense fallback={<div>Loading...</div>}>
            <Routes>
                <Route path="/" element={<EntryTransactionList />}  />
                <Route path="/create" element={<EntryTransactionForm />} />
                <Route path="/edit/:id" element={<EntryTransactionForm />} />
                <Route path="*" element={<NotFound/>} />
            </Routes>
        </Suspense>
    </Router>
);

export default App;
