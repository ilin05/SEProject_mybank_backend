/* login.js */
document.addEventListener('DOMContentLoaded', function() {
    var transactionRecords = document.getElementById('transaction_records');
    transactionRecords.addEventListener('click', function() {
        window.location.href = '/transaction-records.html'; // 指定跳转页面
    });
});

document.addEventListener('DOMContentLoaded', function() {
    var transactionRecords = document.getElementById('search_currency');
    transactionRecords.addEventListener('click', function() {
        window.location.href = '/search_currency.html'; // 指定跳转页面
    });
});

document.addEventListener('DOMContentLoaded', function() {
    var transactionRecords = document.getElementById('trade');
    transactionRecords.addEventListener('click', function() {
        window.location.href = '/trade.html'; // 指定跳转页面
    });
});

document.addEventListener('DOMContentLoaded', function() {
    var transactionRecords = document.getElementById('account');
    transactionRecords.addEventListener('click', function() {
        window.location.href = '/account.html'; // 指定跳转页面
    });
});


document.addEventListener('DOMContentLoaded', function() {
    var otherButton = document.getElementById('other');
    otherButton.addEventListener('click', function() {
        var userConfirmed = confirm("您确定要返回主页吗？");
        if (userConfirmed) {
            window.location.href = 'http://localhost:5173/login';
        }
    });
});
