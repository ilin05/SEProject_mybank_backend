/* search_currency.js */
let currentPage = 1;
let itemsPerPage = 7;
let totalItems = 0;
let ratesData = [];

function fetchExchangeRates() {
    const currencyId = document.getElementById("currencyInput").value;
    if (!currencyId) {
        alert("请输入货币ID");
        return;
    }

    fetch('http://localhost:8000/api/SearchCurrency', {
        method: "POST",
        headers: {
            "Content-Type": "application/json;charset=UTF-8"
        },
        body: JSON.stringify({ currencyId: currencyId })
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        return response.json();
    })
    .then(data => {
        displayCurrencyName(data.currencyName);
        ratesData = Object.entries(data.rates);
        totalItems = ratesData.length;
        currentPage = 1; // 重置为第一页
        updateTable();
        updatePaginationInfo();
        updatePaginationButtons();
    })
    .catch(error => {
        console.error('Error:', error);
        alert("获取汇率信息时出错");
    });
}

function displayCurrencyName(name) {
    const currencyNameElement = document.getElementById("currencyName");
    currencyNameElement.textContent = `货币名称: ${name}`;
}

function updateTable() {
    const ratesList = document.getElementById("ratesList");
    ratesList.innerHTML = ''; // 清空之前的内容

    const startIndex = (currentPage - 1) * itemsPerPage;
    const endIndex = Math.min(startIndex + itemsPerPage, totalItems);
    const pageData = ratesData.slice(startIndex, endIndex);

    pageData.forEach(([currency, rate]) => {
        const row = document.createElement("tr");
        const currencyCell = document.createElement("td");
        const rateCell = document.createElement("td");

        currencyCell.textContent = currency;
        rateCell.textContent = typeof rate === 'number' ? rate.toFixed(6) : '无效汇率';

        row.appendChild(currencyCell);
        row.appendChild(rateCell);
        ratesList.appendChild(row);
    });

    updatePaginationButtons();
}

function prevPage() {
    if (currentPage > 1) {
        currentPage--;
        updateTable();
        updatePaginationInfo();
    }
}

function nextPage() {
    if (currentPage * itemsPerPage < totalItems) {
        currentPage++;
        updateTable();
        updatePaginationInfo();
    }
}

function updatePaginationInfo() {
    const pageInfo = document.getElementById("pageInfo");
    if (pageInfo) {
        const totalPages = Math.ceil(totalItems / itemsPerPage);
        pageInfo.textContent = `Page ${currentPage} of ${totalPages}`;
    }
}

function updatePaginationButtons() {
    const totalPages = Math.ceil(totalItems / itemsPerPage);
    document.getElementById('prevPage').disabled = currentPage <= 1;
    document.getElementById('nextPage').disabled = currentPage >= totalPages;
}

document.addEventListener('DOMContentLoaded', function() {
    document.getElementById('prevPage').disabled = true; // 初始禁用上一页按钮
    document.getElementById('nextPage').disabled = true; // 初始禁用下一页按钮
});





