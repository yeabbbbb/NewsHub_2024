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

async function fetchSearchResult(keyword, page) {
    const resultContainer = document.querySelector('.result__news');
    resultContainer.innerHTML = '<span class="no-result">Loading...</span>';
	
    try {
        const url = new URL('https://hyunewshub.shop/api/news/search');
        const params = new URLSearchParams({ keyword, page });
        url.search = params.toString();

        const response = await fetch(url, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        });

        if (!response.ok) {
            throw new Error(`HTTP error! Status: ${response.status}`);
        }

        const data = await response.json();
        console.log('Fetched Data:', data);

        if(data.success) {
            const newsList = data.result.news;
            const totalPages = data.result.total_pages;
            resultContainer.innerHTML = '';
            
            newsList.forEach(news => {
                const truncatedSummary = news.summary.length > 400
                    ? news.summary.slice(0, 400) + "..."
                    : news.summary;
                const listItem = document.createElement('li');
                listItem.classList.add('result__item');

                listItem.innerHTML = `
                <a href="${news.url}" target="_blank" rel="noopener noreferrer">
                    <img src="${news.image_url}" class="result__item-image" alt="${news.title}">
                    <div class="result__item-info">
                        <span class="result__item-title">${news.title}</span>
                        <div class="category-date">
                            <span class="category">${news.category}</span>
                            <span class="date">${new Date(news.publish_date).toLocaleDateString()}</span>
                        </div>
                        <span class="content">${truncatedSummary}</span>
                    </div>
                </a>
                `;

                resultContainer.appendChild(listItem);
            });

            pagination(totalPages, page);

            resultContainer.addEventListener('click', async (event) => {
                const newsLink = event.target.closest('a');
                if (newsLink) {
                    const newsId = newsLink.getAttribute('data-news-id');

                    try {
                        const response = await fetch('https://hyunewshub.shop/api/click-log', {
                            method: 'POST',
                            headers: {
                                'Content-Type': 'application/json'
                            },
                            body: JSON.stringify({ news_id: newsId })
                        });

                        if (!response.ok) {
                            const errorMessage = `서버로 ID 전송 실패: ${response.status} - ${response.statusText}`;
                            throw new Error(errorMessage);
                        }

                        console.log('ID 전송 성공:', newsId);
                    } catch (error) {
                        console.error('ID 전송 에러:', error.message);
                    }
                }
            });
        } else {
            document.querySelector('.result__news').innerHTML = 'No news items found.';
        }        
    } catch (error) {
        console.error('Error:', error);
        document.querySelector('.result__news').innerHTML = 'Failed to load news.';
    }
}

function pagination(totalPages, currentPage) {
    const paginationControls = document.getElementById('pagination');
    const prevBtn = document.getElementById('prev-btn');
    const nextBtn = document.getElementById('next-btn');
    const pageNumContainer = document.getElementById('page-num');

    pageNumContainer.innerHTML = '';

    if (totalPages <= 1) {
        paginationControls.style.display = 'none';
        return;
    }

    paginationControls.style.display = 'flex';

    prevBtn.disabled = currentPage === 1;
    nextBtn.disabled = currentPage === totalPages;

    const s = Math.floor((currentPage - 1) / 5);

    for (let i = s * 5 + 1; i <= Math.min((s + 1) * 5, totalPages); i++) {
        const pageButton = document.createElement('button');
        pageButton.classList.add('page-num');
        pageButton.textContent = i;

        if (i === currentPage) {
            pageButton.classList.add('active');
        }

        pageButton.addEventListener('click', () => {
            if (currentPage !== i) {
                fetchSearchResult(localStorage.getItem('keyword'), i);
                window.scrollTo({ top: 0});
            }
        });

        pageNumContainer.appendChild(pageButton);
    }

    prevBtn.onclick = () => {
        if (currentPage > 1) {
            fetchSearchResult(localStorage.getItem('keyword'), currentPage-1);
            window.scrollTo({ top: 0});
        }
    };

    nextBtn.onclick = () => {
        if (currentPage < totalPages) {
            fetchSearchResult(localStorage.getItem('keyword'), currentPage + 1);
            window.scrollTo({ top: 0});
        }
    };
}

async function fetchTopNews(category) {
    try {
        const url = new URL('https://hyunewshub.shop/api/news/top');
        const params = new URLSearchParams({ category });
        url.search = params.toString();

        const response = await fetch(url, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        });

        if (!response.ok) {
            throw new Error(`HTTP error! Status: ${response.status}`);
        }

        const data = await response.json();
        console.log('Fetched Data:', data);

        if(data.success) {
            const newsList = data.result.news;
            const topNewsContainer = document.querySelector('.top-news__');
            topNewsContainer.innerHTML = '';

            newsList.forEach(news => {
                const listItem = document.createElement('li');
                listItem.classList.add('top-news__item');

                listItem.innerHTML = `
                <a href="${news.url}" target="_blank" rel="noopener noreferrer">
                    <span class="top-news__item-title">${news.title}</span>
                    <div class="category-date">
                        <span class="category">${news.category}</span>
                        <span class="date">${new Date(news.publish_date).toLocaleDateString()}</span>
                    </div>
                </a>
                `;

                topNewsContainer.appendChild(listItem);
            });

            topNewsContainer.addEventListener('click', async (event) => {
                const newsLink = event.target.closest('a');
                if (newsLink) {
                    const newsId = newsLink.getAttribute('data-news-id');

                    try {
                        const response = await fetch('https://hyunewshub.shop/api/click-log', {
                            method: 'POST',
                            headers: {
                                'Content-Type': 'application/json'
                            },
                            body: JSON.stringify({ news_id: newsId })
                        });

                        if (!response.ok) {
                            const errorMessage = `서버로 ID 전송 실패: ${response.status} - ${response.statusText}`;
                            throw new Error(errorMessage);
                        }

                        console.log('ID 전송 성공:', newsId);
                    } catch (error) {
                        console.error('ID 전송 에러:', error.message);
                    }
                }
            });
        } else {
            document.querySelector('.top-news__').innerHTML = 'No news items found.';
        }        
    } catch (error) {
        console.error('Error:', error);
        document.querySelector('.top-news__').innerHTML = 'Failed to load news.';
    }
}

document.addEventListener('DOMContentLoaded', () => {
    const keyword = localStorage.getItem('keyword');
    if (keyword) {
        document.getElementById('result-h1').textContent = 'Search results: ' + keyword;
        fetchSearchResult(keyword, 1);
        fetchTopNews('ALL');
    } else {
        document.getElementById('result__news').innerHTML = 'No search term provided.';
    }
    checkLoginStatus();
});
