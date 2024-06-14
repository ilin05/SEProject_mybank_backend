/* login.js */
function login() {
    const name = document.getElementById("name").value;
    const password = document.getElementById("password").value;

    if (!name) {
        if (!password) {
            alert("请输入用户名和密码");
        } else {
            alert("请输入用户名");
        }
    } else if (!password) {
        alert("请输入密码");
    } else {
        console.log("Sending login request");
        fetch('http://localhost:8000/api/login', {
            method: "POST",
            headers: {
                "Content-Type": "application/json;charset=UTF-8"
            },
            body: JSON.stringify({ name: name, password: password })
        })
        .then(res => res.json())
        .then(data => {
            console.log("Received response:", data);
            if (data.success) {
                const userType = data.userType;
                if (userType === 1) {
                    alert("登录成功");
                    sessionStorage.setItem('username', name); // 存储用户名
                    location.href = '/index.html';
                } else if (userType === 2) {
                    alert("登录成功");
                    location.href = '/currency_management.html';
                } else if (userType === 3) {
                    alert("登录成功");
                    location.href = '/manager.html';
                } else {
                    alert("登录失败");
                    location.href = '/login.html';
                }
            } else {
                alert(data.message || "登录失败");
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert("登录时出错: " + error.message);
        });
    }
}



