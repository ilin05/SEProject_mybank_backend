/* manager.js */
function navigateTo(page) {
    window.location.href = page;
}

document.getElementById('logout').addEventListener('click', function() {
    if (confirm('您确定要退出登录吗？')) {
        navigateTo('/login.html');
    }
});