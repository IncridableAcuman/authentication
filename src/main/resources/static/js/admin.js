document.addEventListener('DOMContentLoaded', async () => {
    // 1. Dastlab foydalanuvchining admin ekanligini va auth holatini tekshirish
    const isAdmin = await checkAdminAccess();
    if (!isAdmin) return;

    // 2. Foydalanuvchilar ro'yxatini yuklash
    loadUsersList();

    // 3. Logout tugmasi hodisasa
    const logoutBtn = document.getElementById('logoutBtn');
    if (logoutBtn) {
        logoutBtn.addEventListener('click', handleLogout);
    }
});

/**
 * Admin ruxsatini tekshirish
 */
async function checkAdminAccess() {
    try {
        const response = await fetchWithAuth('/auth/me');
        if (!response.ok) {
            window.location.href = '/login.html';
            return false;
        }

        const user = await response.json();
        if (user.role !== 'ADMIN') {
            alert("Sizda ushbu sahifaga kirish huquqi yo'q!");
            window.location.href = '/dashboard.html';
            return false;
        }
        return true;
    } catch (error) {
        console.error("Auth verification error:", error);
        window.location.href = '/login.html';
        return false;
    }
}

/**
 * Serverdan barcha foydalanuvchilar ro'yxatini olish
 */
async function loadUsersList() {
    const loadingEl = document.getElementById('loading');
    const tableContainer = document.getElementById('tableContainer');
    const errorBox = document.getElementById('errorMessage');

    hideAlert(errorBox);

    try {
        const response = await fetchWithAuth('/admin/users');

        if (response.ok) {
            const users = await response.json();
            renderUsersTable(users);

            loadingEl.style.display = 'none';
            tableContainer.style.display = 'block';
        } else {
            const data = await response.json();
            showAlert(errorBox, data.message || "Foydalanuvchilar ro'yxatini yuklashda xatolik yuz berdi.");
            loadingEl.style.display = 'none';
        }
    } catch (error) {
        console.error("Users fetch error:", error);
        showAlert(errorBox, "Server bilan aloqa o'rnatib bo'lmadi.");
        loadingEl.style.display = 'none';
    }
}

/**
 * Jadvalni foydalanuvchilar ma'lumotlari bilan to'ldirish
 */
function renderUsersTable(users) {
    const tbody = document.getElementById('usersTableBody');
    const userCountEl = document.getElementById('userCount');

    tbody.innerHTML = '';
    userCountEl.innerText = `${users.length} ta foydalanuvchi`;

    users.forEach(user => {
        const tr = document.createElement('tr');

        const createdDate = user.createdAt
            ? new Date(user.createdAt).toLocaleDateString('uz-UZ')
            : '—';

        tr.innerHTML = `
            <td>#${user.id}</td>
            <td>
                <div class="user-cell">
                    <strong>${escapeHtml(user.firstName || '')} ${escapeHtml(user.lastName || '')}</strong>
                    <small>@${escapeHtml(user.username)}</small>
                </div>
            </td>
            <td>${escapeHtml(user.email)}</td>
            <td>
                <select class="role-select" onchange="changeUserRole(${user.id}, this.value)">
                    <option value="USER" ${user.role === 'USER' ? 'selected' : ''}>USER</option>
                    <option value="ADMIN" ${user.role === 'ADMIN' ? 'selected' : ''}>ADMIN</option>
                </select>
            </td>
            <td>${createdDate}</td>
            <td>
                <button class="btn-action btn-delete" onclick="deleteUser(${user.id}, '${escapeHtml(user.username)}')">
                    O'chirish
                </button>
            </td>
        `;

        tbody.appendChild(tr);
    });
}

/**
 * Foydalanuvchi rolini o'zgartirish
 */
async function changeUserRole(userId, newRole) {
    const errorBox = document.getElementById('errorMessage');
    const successBox = document.getElementById('successMessage');

    hideAlert(errorBox);
    hideAlert(successBox);

    try {
        const response = await fetchWithAuth(`/admin/users/${userId}/role`, {
            method: 'PATCH',
            body: JSON.stringify({ role: newRole })
        });

        const data = await response.json();

        if (response.ok) {
            showAlert(successBox, `Foydalanuvchi roli ${newRole} ga o'zgartirildi!`);
            setTimeout(() => hideAlert(successBox), 3000);
        } else {
            showAlert(errorBox, data.message || "Rolni o'zgartirishda xatolik.");
            loadUsersList(); // Xatolik bo'lsa jadvalni qayta tiklash
        }
    } catch (error) {
        console.error("Role update error:", error);
        showAlert(errorBox, "Server bilan aloqa xatosi.");
        loadUsersList();
    }
}

/**
 * Foydalanuvchini o'chirish
 */
async function deleteUser(userId, username) {
    const isConfirmed = confirm(`Haqiqatan ham @${username} foydalanuvchisini o'chirib tashlamoqchimisiz?`);
    if (!isConfirmed) return;

    const errorBox = document.getElementById('errorMessage');
    const successBox = document.getElementById('successMessage');

    hideAlert(errorBox);
    hideAlert(successBox);

    try {
        const response = await fetchWithAuth(`/admin/users/${userId}`, {
            method: 'DELETE'
        });

        if (response.ok) {
            showAlert(successBox, `@${username} muvaffaqiyatli o'chirildi!`);
            setTimeout(() => hideAlert(successBox), 3000);
            loadUsersList(); // Jadvalni yangilash
        } else {
            const data = await response.json();
            showAlert(errorBox, data.message || "Foydalanuvchini o'chirishda xatolik.");
        }
    } catch (error) {
        console.error("Delete user error:", error);
        showAlert(errorBox, "Server bilan aloqa o'rnatib bo'lmadi.");
    }
}

/**
 * Logout bajarish
 */
async function handleLogout() {
    try {
        await fetchWithAuth('/auth/logout', { method: 'POST' });
    } catch (error) {
        console.error("Logout error:", error);
    } finally {
        localStorage.removeItem('accessToken');
        window.location.href = '/login.html';
    }
}

// Xabarnomalar uchun yordamchi funksiyalar
function showAlert(element, message) {
    if (element) {
        element.innerText = message;
        element.style.display = 'block';
    }
}

function hideAlert(element) {
    if (element) {
        element.innerText = '';
        element.style.display = 'none';
    }
}

function escapeHtml(str) {
    return String(str)
        .replace(/&/g, '&amp;')
        .replace(/</g, '&lt;')
        .replace(/>/g, '&gt;')
        .replace(/"/g, '&quot;');
}