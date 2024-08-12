import React from 'react';

const Paginator = ({ currentPage, setPage }) => {

    return (
        <div>
            <button onClick={() => setPage(currentPage - 1)} disabled={currentPage === 0}>Previous</button>
            <button onClick={() => setPage(currentPage + 1)}>Next</button>
        </div>
    );
};

export default Paginator;
