/* currency_management.js */
function addCurrency() {
    let name = document.getElementById('currency_name').value;
    let baseExchangeRate = parseFloat(document.getElementById('currency_rate').value);

    if (!name || isNaN(baseExchangeRate)) {
        alert('非法输入');
        return;
    }

    const payload = { name: name, baseExchangeRate: baseExchangeRate };
    console.log("Sending payload:", payload);

    fetch('http://localhost:8000/api/currency/add', {
        method: "POST",
        headers: {
            "Content-Type": "application/json;charset=UTF-8"
        },
        body: JSON.stringify(payload)
    })
    .then(response => response.json())
    .then(data => {
        console.log("Received response:", data);
        if (data.success) {
            alert('添加成功');
            updateTable();
        } else {
            alert('添加失败: ' + data.message);
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert("添加货币时出错");
    });
}


function modifyRate() {
    let id = parseInt(document.getElementById('currency_code').value); // 确保 id 是整数
    let newRate = parseFloat(document.getElementById('exchange_rate').value);

    if (isNaN(id) || isNaN(newRate)) {
        alert('非法输入');
        return;
    }

    fetch('http://localhost:8000/api/currency/modify', {
        method: "POST",
        headers: {
            "Content-Type": "application/json;charset=UTF-8"
        },
        body: JSON.stringify({ id: id, newRate: newRate })
    })
    .then(response => {
        if (!response.ok) {
            throw new Error("Network response was not ok");
        }
        return response.json();
    })
    .then(data => {
        if (data.success) {
            alert('修改成功');
            updateTable();
        } else {
            alert('修改失败: ' + data.message);
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert("修改汇率时出错");
    });
}


function removeCurrency() {
    let name = document.getElementById('currency_name').value;

    fetch('http://localhost:8000/api/currency/delete', {
        method: "POST",
        headers: {
            "Content-Type": "application/json;charset=UTF-8"
        },
        body: JSON.stringify({ name: name })
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            alert('删除成功');
            updateTable();
        } else {
            alert('删除失败: ' + data.message);
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert("删除货币时出错");
    });
}

function formatRate(rate) {
    return parseFloat(rate.toString()).toString(); // 去掉不必要的零
}

function updateTable() {
    fetch('http://localhost:8000/api/currency/list', {
        method: "GET",
        headers: {
            "Content-Type": "application/json;charset=UTF-8"
        }
    })
    .then(response => response.json())
    .then(data => {
        console.log(data);  // 打印获取到的数据
        let tableBody = document.getElementById('currencyTable').getElementsByTagName('tbody')[0];
        tableBody.innerHTML = '';
        data.currencies.forEach(currency => {
            let row = tableBody.insertRow();
            let idCell = row.insertCell(0);
            let nameCell = row.insertCell(1);
            let rateCell = row.insertCell(2);

            idCell.innerHTML = currency.currency_id;
            nameCell.innerHTML = currency.currency_name;
            let rate = currency.currency_rate;
            if (typeof rate === 'number' && !isNaN(rate)) {
                rateCell.innerHTML = formatRate(rate);
            } else {
                rateCell.innerHTML = "无效数据";
            }
        });
    })
    .catch(error => {
        console.error('Error:', error);
        alert("获取货币列表时出错");
    });
}

window.onload = updateTable;  // 在页面加载时填充表格数据


function resetInputs() {
    document.getElementById('currency_name').value = '';
    document.getElementById('currency_code').value = '';
    document.getElementById('exchange_rate').value = '';
}

document.getElementById('logout').addEventListener('click', function() {
    if (confirm('您确定要退出登录吗？')) {
        window.location.href = 'login.html';  // 修改为实际的登录页面地址
    }
});

document.addEventListener('DOMContentLoaded', function() {
    let backHomeButton = document.getElementById('backHome');
    let referrer = document.referrer;
    let allowedReferrer = "/manager.html";

    if (referrer.includes(allowedReferrer)) {
        backHomeButton.style.display = 'block';  // 显示返回按钮
        backHomeButton.onclick = function() {
            window.location.href = referrer; // 返回之前的网站页面
        };
    }
});



