const Btn = document.querySelector('.header__Btn');
const menu = document.querySelector('.header__menu');

Btn.addEventListener('click', () => {
    menu.classList.toggle('active');
})

const recommendedNewsContainer = document.querySelector('.recommend__news');

function checkLoginStatus() {
    if (localStorage.getItem('loggedIn') === 'true') {
        document.getElementById('login-button').style.display = 'none';
        document.getElementById('logout-button').style.display = 'block';
	console.log('ID:', localStorage.getItem('id'));
        recommendedNewsContainer.innerHTML = '<span class="no-recommend">Loading...</span>';
	fetchRecommendedNews('BUSINESS');
    } else {
        document.getElementById('login-button').style.display = 'block';
        document.getElementById('logout-button').style.display = 'none';
	recommendedNewsContainer.innerHTML = '<span class="no-recommend">Please log in.</span>';
    }
}

function logout() {
    fetch('https://hyunewshub.shop/api/logout', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
	credentials: 'include'
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

async function fetchHeadlineNews(category) {
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
            const newsList = data.result.news.slice(0, 2);
            const topNewsContainer = document.querySelector('.headline__news');
            topNewsContainer.innerHTML = '';

            newsList.forEach(news => {
                const truncatedTitle = news.summary.length > 300
                    ? news.title.slice(0, 300) + "..."
                    : news.title;
                const truncatedSummary = news.summary.length > 400
                    ? news.summary.slice(0, 400) + "..."
                    : news.summary;
                const listItem = document.createElement('li');
                listItem.classList.add('headline__item');

                listItem.innerHTML = `<a href="${news.url}" target="_blank" rel="noopener noreferrer">
                    <div class="headline__item-info">
                        <span class="headline__item-title">${truncatedTitle}</span>
                        <div class="category-date">
                            <span class="category">${news.category}</span>
                            <span class="date">${new Date(news.publish_date).toLocaleDateString()}</span>
                        </div>
                    </div>
                    <img src="${news.image_url}" class="headline-img">
                    <div class="gradient-overlay"></div>
                    <div class="text-overlay">
                        <span class="headline__item-content">${truncatedSummary}</span>
                    </div></a>
                `;

                topNewsContainer.appendChild(listItem);
	    });

            topNewsContainer.addEventListener('click', async (event) => {
                const newsLink = event.target.closest('a');
                if (newsLink) {
                    const newsId = newsList.find(news => news.url === newsLink.href).id;
                    try {
                        const response = await fetch('https://hyunewshub.shop/api/click-log', {
                            method: 'POST',
                            headers: {
                                'Content-Type': 'application/json'
                            },
			    credentials: 'include',
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
            document.querySelector('.headline__news').innerHTML = 'No news items found.';
        }        
    } catch (error) {
        console.error('Error:', error);
        document.querySelector('.headline__news').innerHTML = 'Failed to load news.';
    }
}

async function fetchRecentNews(category, page) {
    try {
        const url = new URL('https://hyunewshub.shop/api/news');
        const params = new URLSearchParams({ category, page });
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

        if (data.success) {
            const newsList = data.result.news;
            const totalPages = data.result.total_pages;
            const resultContainer = document.querySelector('.recent__news');
            resultContainer.innerHTML = '';

            newsList.forEach(news => {
                const truncatedSummary = news.summary.length > 400
                    ? news.summary.slice(0, 200) + "..."
                    : news.summary;
                const listItem = document.createElement('li');
                listItem.classList.add('recent__item');

                listItem.innerHTML = `
                <a href="${news.url}" target="_blank" rel="noopener noreferrer">
                    <img src="${news.image_url}" class="recent__item-image">
                    <div class="recent__item-info">
                        <span class="recent__item-title">${news.title}</span>
                        <div class="category-date">
                            <span class="category">${news.category}</span>
                            <span class="date">${new Date(news.publish_date).toLocaleDateString()}</span>
                        </div>
                        <span class="content">"${truncatedSummary}"</span>
                    </div>
                </a>
                `;

                resultContainer.appendChild(listItem);
            });

            resultContainer.addEventListener('click', async (event) => {
                const newsLink = event.target.closest('a');
                if (newsLink) {
                    const newsId = newsList.find(news => news.url === newsLink.href)?.id;
                    if (newsId) {
                        try {
                            const response = await fetch('https://hyunewshub.shop/api/click-log', {
                                method: 'POST',
                                headers: {
                                    'Content-Type': 'application/json'
                                },
                                credentials: 'include',
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
                }
            });

            pagination(totalPages, page);
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
                fetchRecentNews('BUSINESS', i);
                window.scrollTo({ top: 1200});
            }
        });

        pageNumContainer.appendChild(pageButton);
    }

    prevBtn.onclick = () => {
        if (currentPage > 1) {
            fetchRecentNews('BUSINESS', currentPage-1);
            window.scrollTo({ top: 1200});
        }
    };

    nextBtn.onclick = () => {
        if (currentPage < totalPages) {
            fetchRecentNews('BUSINESS', currentPage + 1);
            window.scrollTo({ top: 1200});
        }
    };
}

async function fetchRecommendedNews(category) {
    try {   
        const url = new URL('https://hyunewshub.shop/api/news/recommended');
        const params = new URLSearchParams({ category });
        url.search = params.toString();

        const response = await fetch(url, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            },              
            credentials: 'include'
        });
                            
        if (!response.ok) { 
            throw new Error(`HTTP error! Status: ${response.status}`);
        }

        const data = await response.json();
        console.log('Fetched Data:', data);

        if(data.success) {
            const newsList = data.result.news.slice(6);
            const recommendedNewsContainer = document.querySelector('.recommend__news');
            recommendedNewsContainer.innerHTML = '';
                        
            newsList.forEach(news => {
                const listItem = document.createElement('li');
                listItem.classList.add('recommend__item');
        
                listItem.innerHTML = `
                <a href="${news.url}" target="_blank" rel="noopener noreferrer" data-news-id="${news.id}">
                    <img src="${news.image_url}" class="recommend__item-image">
                    <div class="recommend__item-info">
                        <span class="recommend__item-title">${news.title}</span>
                        <div class="recommend__category-date">
                            <span class="category">${news.category}</span>
                            <span class="date">${new Date(news.publish_date).toLocaleDateString()}</span>
                        </div>
                    </div>
                </a>
                `;

                recommendedNewsContainer.appendChild(listItem);
            });

            recommendedNewsContainer.addEventListener('click', async (event) => {
                const newsLink = event.target.closest('a');
                if (newsLink) {
                    const newsId = newsLink.getAttribute('data-news-id');

                    try {
                        const response = await fetch('https://hyunewshub.shop/api/click-log', {
                            method: 'POST',
                            headers: {
                                'Content-Type': 'application/json'
                            },
                            credentials: 'include',
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
            document.querySelector('.recommend__news').innerHTML = 'No news items found.';
        }
    } catch (error) {
        console.error('Error:', error);
        document.querySelector('.recommend__news').innerHTML = 'Failed to load news.';
    }
}

document.addEventListener('DOMContentLoaded', (event) => {
    fetchHeadlineNews('BUSINESS');
    fetchRecentNews('BUSINESS', 1);
    checkLoginStatus();
});
