document.addEventListener('DOMContentLoaded', () => {
    // 1. Sahifa yuklanganda foydalanuvchi ma'lumotlarini olish
    loadUserProfile();

    // 2. Logout tugmasiga event listener biriktirish
    const logoutBtn = document.getElementById('logoutBtn');
    if (logoutBtn) {
        logoutBtn.addEventListener('click', handleLogout);
    }
});

/**
 * Serverdan joriy foydalanuvchi profilini yuklash
 */
async function loadUserProfile() {
    const loadingEl = document.getElementById('loading');
    const profileContentEl = document.getElementById('profileContent');

    try {
        const response = await fetchWithAuth('/auth/me');

        if (response.ok) {
            const user = await response.json();
            renderUserData(user);

            loadingEl.style.display = 'none';
            profileContentEl.style.display = 'block';
        } else {
            // Token yaroqsiz bo'lsa yoki topilmasa login sahifasiga yo'naltirish
            window.location.href = '/login.html';
        }
    } catch (error) {
        console.error("Profil ma'lumotlarini yuklashda xatolik:", error);
        window.location.href = '/login.html';
    }
}

/**
 * Kelgan user ma'lumotlarini HTML elementlarga joylash
 */
function renderUserData(user) {
    document.getElementById('userAvatar').src = user.avatar || 'https://github.com/shadcn.png';
    document.getElementById('userFullName').innerText = `${user.firstName || ''} ${user.lastName || ''}`.trim() || user.username;
    document.getElementById('userRole').innerText = user.role;
    document.getElementById('userName').innerText = `@${user.username}`;
    document.getElementById('userEmail').innerText = user.email;
    document.getElementById('userId').innerText = `#${user.id}`;

    // Sanani formatlash
    if (user.createdAt) {
        const createdDate = new Date(user.createdAt);
        document.getElementById('userCreatedAt').innerText = createdDate.toLocaleDateString('uz-UZ', {
            year: 'numeric',
            month: 'long',
            day: 'numeric'
        });
    }

    // Agar foydalanuvchi ADMIN bo'lsa, Admin bo'limini ko'rsatish
    if (user.role === 'ADMIN') {
        const adminSection = document.getElementById('adminSection');
        if (adminSection) {
            adminSection.style.display = 'block';
        }
    }
}

/**
 * Logout Jarayoni
 */
async function handleLogout() {
    try {
        // Backend'ga logout so'rovini yuborish (RefreshToken o'chiriladi va Cookie tozalanadi)
        await fetchWithAuth('/auth/logout', {
            method: 'POST'
        });
    } catch (error) {
        console.error("Logout so'rovida xatolik:", error);
    } finally {
        // Har qanday holatda ham brauzerdagi Access Token-ni o'chirish va loginga yo'naltirish
        localStorage.removeItem('accessToken');
        window.location.href = '/login.html';
    }
}