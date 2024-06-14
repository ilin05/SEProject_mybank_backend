/* transaction_records.js */
let currentPage = 1;
const pageSize = 7;
let allTransactions = [];

async function fetchTransactionHistory() {
    const response = await fetch(`http://localhost:8000/api/transaction/history`);
    if (!response.ok) {
        throw new Error('Network response was not ok');
    }
    return await response.json();
}

function renderTransactions(transactions) {
    const tableBody = document.getElementById('transactionHistory').getElementsByTagName('tbody')[0];
    tableBody.innerHTML = '';

    const noDataMessage = document.querySelector('.no-data-message');
    if (transactions.length === 0) {
        if (!noDataMessage) {
            const noDataMessage = document.createElement('div');
            noDataMessage.textContent = '您从未进行过交易';
            noDataMessage.className = 'no-data-message';
            tableBody.parentElement.parentElement.appendChild(noDataMessage);
        }
        tableBody.parentElement.style.display = 'none';
        return;
    } else {
        if (noDataMessage) {
            noDataMessage.remove();
        }
        tableBody.parentElement.style.display = 'table';
    }

    transactions.forEach(transaction => {
        let row = tableBody.insertRow();
        const date = new Date(transaction.transactionTime);
        row.insertCell(0).textContent = isNaN(date) ? 'Invalid Date' : date.toLocaleString();
        row.insertCell(1).textContent = transaction.currencyFromName;
        row.insertCell(2).textContent = transaction.currencyToName;
        row.insertCell(3).textContent = transaction.amountFrom !== undefined ? transaction.amountFrom.toFixed(2) : 'N/A';
        row.insertCell(4).textContent = transaction.amountTo !== undefined ? transaction.amountTo.toFixed(2) : 'N/A';
    });
}

function getPaginatedTransactions(transactions, page, size) {
    const start = (page - 1) * size;
    const end = start + size;
    return transactions.slice(start, end);
}

function updatePaginationButtons() {
    const totalPages = Math.ceil(allTransactions.length / pageSize);
    document.getElementById('prevPage').disabled = currentPage <= 1;
    document.getElementById('nextPage').disabled = currentPage >= totalPages;
}

async function loadPage(page) {
    try {
        const paginatedTransactions = getPaginatedTransactions(allTransactions, page, pageSize);
        renderTransactions(paginatedTransactions);
        document.getElementById('currentPage').textContent = `Page ${page}`;
        updatePaginationButtons();
    } catch (error) {
        console.error('Error:', error);
        alert("获取交易历史记录时出错");
    }
}

document.getElementById('prevPage').addEventListener('click', () => {
    if (currentPage > 1) {
        currentPage--;
        loadPage(currentPage);
    }
});

document.getElementById('nextPage').addEventListener('click', () => {
    const totalPages = Math.ceil(allTransactions.length / pageSize);
    if (currentPage < totalPages) {
        currentPage++;
        loadPage(currentPage);
    }
});

document.addEventListener('DOMContentLoaded', async function() {
    try {
        allTransactions = await fetchTransactionHistory();
        loadPage(currentPage);
    } catch (error) {
        console.error('Error:', error);
        alert("加载交易记录时出错");
    }
});