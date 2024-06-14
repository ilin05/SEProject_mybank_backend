/* System-administration.js */
document.addEventListener("DOMContentLoaded", function () {
    fetchOperators();
});

function fetchOperators() {
    fetch('http://localhost:8000/api/operators', {
        method: "GET",
        headers: {
            "Content-Type": "application/json;charset=UTF-8"
        }
    })
    .then(response => response.json())
    .then(data => {
        updateTable(data);
    })
    .catch(error => {
        console.error('Error:', error);
        alert("获取用户信息时出错");
    });
}

function addUser() {
    let username = document.getElementById('username').value;
    let password = document.getElementById('password').value;

    fetch('http://localhost:8000/api/operators', {
        method: "POST",
        headers: {
            "Content-Type": "application/json;charset=UTF-8"
        },
        body: JSON.stringify({ op_name: username, op_password: password })
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            fetchOperators(); // Refresh the table
            resetInputs();
        } else {
            alert("添加用户失败");
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert("添加用户时出错");
    });
}

function modifyUserDialog(index) {
    // 修改按钮行为，转换为保存按钮和激活勾选框
    const row = document.getElementById('userTable').rows[index + 1];
    const currencyCheckbox = row.cells[2].children[0];
    const rateCheckbox = row.cells[3].children[0];
    currencyCheckbox.disabled = false;
    rateCheckbox.disabled = false;

    // 更改按钮为保存按钮
    const actionsCell = row.cells[4];
    actionsCell.innerHTML = `<button onclick="saveChanges('${row.cells[0].textContent}', ${index})">保存</button>`;
}

function saveChanges(op_name, index) {
    const row = document.getElementById('userTable').rows[index + 1];
    const control_currency = row.cells[2].querySelector('input').checked;
    const control_rate = row.cells[3].querySelector('input').checked;

    // Send the updated permissions to the server
    fetch(`/api/operators/changeAuthority`, {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({op_name, control_currency, control_rate})
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            alert('权限更新成功');
            location.reload(); // Optionally, only update the table instead of reloading
        } else {
            alert('权限更新失败');
        }
    })
    .catch(error => {
        console.error('保存权限更新失败:', error);
        alert('保存权限更新失败');
    });
}

function updateTable(data) {
    let tableBody = document.getElementById('userTable').getElementsByTagName('tbody')[0];
    tableBody.innerHTML = '';
    data.forEach((user, index) => {
        let row = tableBody.insertRow();
        let usernameCell = row.insertCell(0);
        let passwordCell = row.insertCell(1);
        let currencyPermissionCell = row.insertCell(2);
        let ratePermissionCell = row.insertCell(3);
        let actionsCell = row.insertCell(4);

        usernameCell.textContent = user.opt_name;
        passwordCell.textContent = user.opt_password;

        // 设置权限显示
        currencyPermissionCell.innerHTML = `<input type="checkbox" ${user.control_currency ? 'checked' : ''} disabled>`;
        ratePermissionCell.innerHTML = `<input type="checkbox" ${user.control_rate ? 'checked' : ''} disabled>`;

        // 添加修改和删除按钮
        actionsCell.innerHTML = `<button onclick="modifyUserDialog(${index})">修改</button>
                                 <button onclick="deleteUser('${user.opt_name}')">删除</button>`;
    });
}


function deleteUser(opt_name) {
    if (!opt_name) {
        alert("Username is required.");
        return;
    }
    const url = `http://localhost:8081/api/operators/${opt_name}`;
    fetch(url, {
        method: "DELETE"
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            fetchOperators(); // Refresh the table
            alert("用户删除成功");
        } else {
            alert("删除用户失败");
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert("删除用户时出错");
    });
}

function resetInputs() {
    document.getElementById('username').value = '';
    document.getElementById('password').value = '';
}

function goBack() {
    window.location.href = '/manager.html';
}
