/* account.js */
let isExpanded = false;

document.addEventListener('DOMContentLoaded', function() {
    fetchHoldings();
    document.getElementById('fundTransferForm').addEventListener('submit', handleTransaction);
});

function fetchHoldings() {
    fetch(`http://localhost:8000/api/account/holdings`)
        .then(response => response.json())
        .then(data => {
            if (data.error) {
                alert(data.error);
                return;
            }
            displayHoldings(data.holdings);
            displayRMBalance(data.rmbBalance);
        })
        .catch(error => {
            console.error('Error:', error);
            alert("获取持仓信息时出错");
        });
}


function displayRMBalance(balance) {
    const balanceElement = document.getElementById('balance');
    balanceElement.textContent = `¥${balance.toFixed(2)} RMB`;
}

function displayHoldings(holdings) {
    const holdingsContainer = document.getElementById('holdingsContainer');
    holdingsContainer.innerHTML = ''; // 清空之前的内容

    const holdingsTitle = document.createElement('h3');
    holdingsTitle.textContent = '持仓信息';
    holdingsContainer.appendChild(holdingsTitle);

    if (holdings.length === 0) {
        const noHoldingsMessage = document.createElement('p');
        noHoldingsMessage.textContent = '您目前没有持仓信息。';
        holdingsContainer.appendChild(noHoldingsMessage);
    } else {
        const holdingsToShow = isExpanded ? holdings : holdings.slice(0, 5);
        holdingsToShow.forEach(holding => {
            const holdingElement = document.createElement('p');
            holdingElement.textContent = `${holding.currency_name}: ${holding.amount}`;
            holdingsContainer.appendChild(holdingElement);
        });

        if (holdings.length > 5) {
            const toggleButton = document.createElement('button');
            toggleButton.textContent = isExpanded ? '收起' : '展开更多';
            toggleButton.addEventListener('click', () => {
                isExpanded = !isExpanded;
                displayHoldings(holdings);
            });
            holdingsContainer.appendChild(toggleButton);
        }
    }
}

function handleTransaction(event) {
    event.preventDefault();

    const transactionType = document.getElementById('transactionType').value;
    const amount = parseFloat(document.getElementById('amount').value);
    const currencyId = 0;
    const accountId = document.getElementById('accountId').value;
    const password = document.getElementById('password').value;

    if (isNaN(amount) || amount <= 0 || isNaN(currencyId) || !accountId || !password) {
        alert("请输入有效的金额、货币、账户ID和密码");
        return;
    }

    const url = transactionType === 'deposit' ? 'http://localhost:8000/api/account/deposit' : 'http://localhost:8000/api/account/withdraw';
    const requestBody = { amount, currencyId, account_id: accountId, password };

    fetch(url, {
        method: "POST",
        headers: {
            "Content-Type": "application/json;charset=UTF-8"
        },
        body: JSON.stringify(requestBody)
    })
        .then(response => response.json())
        .then(data => {
            if (data.error) {
                alert(data.error);
            } else {
                alert(data.message);
                fetchHoldings(); // 更新持仓信息和余额
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert("交易失败");
        });
}

