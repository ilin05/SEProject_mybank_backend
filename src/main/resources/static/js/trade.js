/* trade.js */
function populateCurrencyOptions(currencies) {
    const baseCurrencySelect = document.getElementById('baseCurrency');
    const targetCurrencySelect = document.getElementById('targetCurrency');
    currencies.forEach(currency => {
        const option = document.createElement('option');
        option.value = currency.currency_id;
        option.textContent = currency.currency_name;
        baseCurrencySelect.appendChild(option.cloneNode(true));
        targetCurrencySelect.appendChild(option);
    });
    updateCurrencyOptionsWithHoldings();
}

document.addEventListener('DOMContentLoaded', function () {
    fetchCurrencies();
    fetchUserHoldings();
    document.getElementById('confirmButton').addEventListener('click', confirmTrade);
});

let userHoldings = {};

function fetchCurrencies() {
    fetch(`http://localhost:8000/api/account/currencies`)
        .then(response => response.json())
        .then(data => {
            populateCurrencySelect(data);
        })
        .catch(error => {
            console.error('Error:', error);
            alert("获取货币信息时出错");
        });
}

async function fetchUserHoldings() {
    try {
        const response = await fetch('http://localhost:8000/api/account/holdings');
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        const data = await response.json();
        userHoldings = data.holdings.reduce((acc, holding) => {
            acc[holding.currency_id] = holding.amount;
            return acc;
        }, {});
        updateCurrencyOptionsWithHoldings();
    } catch (error) {
        console.error('Error fetching user holdings:', error);
    }
}

function updateCurrencyOptionsWithHoldings() {
    const baseCurrencySelect = document.getElementById('baseCurrency');
    const targetCurrencySelect = document.getElementById('targetCurrency');
    updateSelectOptionsWithHoldings(baseCurrencySelect);
    updateSelectOptionsWithHoldings(targetCurrencySelect);
}

function updateSelectOptionsWithHoldings(selectElement) {
    Array.from(selectElement.options).forEach(option => {
        const currencyId = parseInt(option.value, 10);
        if (userHoldings[currencyId] !== undefined) {
            option.textContent = `${option.textContent} (持仓: ${userHoldings[currencyId]})`;
        }
    });
}



function populateCurrencySelect(currencies) {
    const baseCurrencySelect = document.getElementById('baseCurrency');
    const targetCurrencySelect = document.getElementById('targetCurrency');
    currencies.forEach(currency => {
        const option = document.createElement('option');
        option.value = currency.currency_id;
        option.textContent = currency.currency_name;
        baseCurrencySelect.appendChild(option.cloneNode(true));
        targetCurrencySelect.appendChild(option);
    });
}

function toggleTradeDirection() {
    const tradeType = document.getElementById('tradeType').value;
    const baseCurrencyLabel = document.getElementById('baseCurrencyLabel');
    const targetCurrencyLabel = document.getElementById('targetCurrencyLabel');
    if (tradeType === 'buy') {
        baseCurrencyLabel.textContent = '基准货币 (from):';
        targetCurrencyLabel.textContent = '目标货币 (to):';
    } else {
        baseCurrencyLabel.textContent = '基准货币 (to):';
        targetCurrencyLabel.textContent = '目标货币 (from):';
    }
}

function fetchExchangeRate() {
    const baseCurrencyId = document.getElementById('baseCurrency').value;
    const targetCurrencyId = document.getElementById('targetCurrency').value;
    if (!baseCurrencyId || !targetCurrencyId) {
        alert("请选择基准货币和目标货币");
        return;
    }

    const url = `http://localhost:8000/api/currency/exchangeRate?currencyFromId=${baseCurrencyId}&currencyToId=${targetCurrencyId}`;
    fetch(url)
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            displayExchangeRate(data.rate);
        })
        .catch(error => {
            console.error('Error:', error);
            alert("获取汇率信息时出错");
        });
}

function displayExchangeRate(rate) {
    const resultDisplay = document.getElementById('conversionResult');
    resultDisplay.innerHTML = `当前汇率: ${rate.toFixed(4)} (基准/目标)<br>`; // Use <br> for line break
}

function calculateExchange() {
    const amount = parseFloat(document.getElementById('amount').value);
    if (isNaN(amount) || amount <= 0) {
        alert("请输入有效的金额");
        return;
    }

    const rateText = document.getElementById('conversionResult').textContent.split(': ')[1];
    const rate = parseFloat(rateText);
    if (isNaN(rate)) {
        alert("请先查询汇率");
        return;
    }

    const tradeType = document.getElementById('tradeType').value;
    let convertedAmount;
    if (tradeType === 'buy') {
        convertedAmount = amount / rate;
    } else {
        convertedAmount = amount * rate;
    }

    const resultDisplay = document.getElementById('conversionResult');
    resultDisplay.innerHTML = `当前汇率: ${rate.toFixed(4)} (基准->目标)<br>转换金额: ${convertedAmount.toFixed(4)} (from->to)`;
}

function promptForPayPassword() {
    return new Promise((resolve, reject) => {
        const password = prompt("请输入支付密码:");
        if (password) {
            resolve(password);
        } else {
            reject(new Error("支付密码输入为空"));
        }
    });
}

function confirmTrade() {
    const tradeType = document.getElementById('tradeType').value;
    const baseCurrency = document.getElementById('baseCurrency').value;
    const targetCurrency = document.getElementById('targetCurrency').value;
    const amount = parseFloat(document.getElementById('amount').value);
    if (!baseCurrency || !targetCurrency || isNaN(amount) || amount <= 0) {
        alert("请输入有效的信息");
        return;
    }

    const username = sessionStorage.getItem('username'); // 假设用户名存储在 sessionStorage 中

    promptForPayPassword()
        .then(password => {
            const url = 'http://localhost:8000/api/account/deal';
            const payload = {
                username: username,
                paypassword: password,
                currency_from: tradeType === 'buy' ? baseCurrency : targetCurrency,
                currency_to: tradeType === 'buy' ? targetCurrency : baseCurrency,
                amount: amount
            };

            return fetch(url, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json;charset=UTF-8"
                },
                body: JSON.stringify(payload)
            });
        })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                alert('交易成功');
                location.reload(); // 重新加载页面
            } else {
                alert('交易失败: ' + data.message);
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert("交易失败: " + error.message);
        });
}
