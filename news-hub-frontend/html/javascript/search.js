const Btn = document.querySelector('.header__Btn');
const menu = document.querySelector('.header__menu');

Btn.addEventListener('click', () => {
    menu.classList.toggle('active');
})

function checkLoginStatus() {
    if (localStorage.getItem('loggedIn') === 'true') {
        document.getElementById('login-button').style.display = 'none';
        document.getElementById('logout-button').style.display = 'block';
    } else {
        document.getElementById('login-button').style.display = 'block';
        document.getElementById('logout-button').style.display = 'none';
    }
}

function logout() {
    fetch('https://hyunewshub.shop/api/logout', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        }
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            localStorage.removeItem('loggedIn');
            window.location.reload()
        }
        else {
            alert('Log out failed: ' + data.message);
        }
    })
    .catch((error) => {
        console.error('Error: ', error);
        alert('There was a problem with the log out process: ' + error.message);
    });
}

document.getElementById('search-form').addEventListener('submit', function(event) {
    event.preventDefault();
    Search();
});

function Search() {
    var keyword = document.getElementById("search-term").value;

    if (keyword == "") {
        alert("Please enter search term.");
        return false;
    }
    else {
        localStorage.setItem('keyword', keyword);
        window.location.href = 'search_result.html';
    }
}

window.onload = checkLoginStatus;
