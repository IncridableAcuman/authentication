const API_BASE_URL = 'http://localhost:8080/api';

async function fetchWithAuth(url, options = {}) {
    let token = localStorage.getItem('accessToken');

    options.headers = {
        ...options.headers,
        'Content-Type': 'application/json',
    };

    if (token) {
        options.headers['Authorization'] = `Bearer ${token}`;
    }

    // Cookie-da refresh token yuborilishi uchun:
    options.credentials = 'include';

    let response = await fetch(`${API_BASE_URL}${url}`, options);

    // Access token muddati o'tgan bo'lsa (401), avtomatik refresh qilish
    if (response.status === 401 && !url.includes('/auth/refresh')) {
        const refreshed = await refreshToken();
        if (refreshed) {
            options.headers['Authorization'] = `Bearer ${localStorage.getItem('accessToken')}`;
            response = await fetch(`${API_BASE_URL}${url}`, options);
        } else {
            window.location.href = '/login.html';
        }
    }

    return response;
}

async function refreshToken() {
    try {
        const response = await fetch(`${API_BASE_URL}/auth/refresh`, {
            method: 'GET',
            credentials: 'include'
        });

        if (response.ok) {
            const data = await response.json();
            localStorage.setItem('accessToken', data.accessToken);
            return true;
        }
    } catch (err) {
        console.error("Tokenni yangilab bo'lmadi:", err);
    }
    localStorage.removeItem('accessToken');
    return false;
}