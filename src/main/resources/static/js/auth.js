document.addEventListener('DOMContentLoaded', () => {
    const registerForm = document.getElementById('registerForm');
    const loginForm = document.getElementById('loginForm');

    // ==========================================
    // 1. RO'YXATDAN O'TISH (REGISTER) HARAKATI
    // ==========================================
    if (registerForm) {
        registerForm.addEventListener('submit', async (e) => {
            e.preventDefault();

            const errorBox = document.getElementById('errorMessage');
            const successBox = document.getElementById('successMessage');

            // Xabarnomalarni tozalash
            hideAlert(errorBox);
            hideAlert(successBox);

            // Formadagi ma'lumotlarni yig'ib olish
            const registerData = {
                firstName: document.getElementById('firstName').value.trim(),
                lastName: document.getElementById('lastName').value.trim(),
                username: document.getElementById('username').value.trim(),
                email: document.getElementById('email').value.trim(),
                password: document.getElementById('password').value
            };

            try {
                // API ga so'rov yuborish
                const response = await fetchWithAuth('/auth/register', {
                    method: 'POST',
                    body: JSON.stringify(registerData)
                });

                const data = await response.json();

                if (response.ok) {
                    // Access token-ni brauzer xotirasiga saqlash
                    if (data.accessToken) {
                        localStorage.setItem('accessToken', data.accessToken);
                    }

                    showAlert(successBox, "Muvaffaqiyatli ro'yxatdan o'tdingiz! Yo'naltirilmoqda...");

                    // 1.5 soniyadan so'ng dashboard sahifasiga o'tkazish
                    setTimeout(() => {
                        window.location.href = '/dashboard.html';
                    }, 1500);

                } else {
                    // Backend ma'lumotlarni tekshirish (Validation) xatolarini ko'rsatish
                    const message = data.message || "Ro'yxatdan o'tishda xatolik yuz berdi.";
                    showAlert(errorBox, message);
                }
            } catch (error) {
                console.error("Register Error:", error);
                showAlert(errorBox, "Server bilan aloqa o'rnatib bo'lmadi.");
            }
        });
    }

    // ==========================================
    // 2. TIZIMGA KIRISH (LOGIN) HARAKATI
    // ==========================================
    if (loginForm) {
        loginForm.addEventListener('submit', async (e) => {
            e.preventDefault();

            const errorBox = document.getElementById('errorMessage');
            hideAlert(errorBox);

            const loginData = {
                email: document.getElementById('email').value.trim(),
                password: document.getElementById('password').value
            };

            try {
                const response = await fetchWithAuth('/auth/login', {
                    method: 'POST',
                    body: JSON.stringify(loginData)
                });

                const data = await response.json();

                if (response.ok) {
                    if (data.accessToken) {
                        localStorage.setItem('accessToken', data.accessToken);
                    }
                    window.location.href = '/dashboard.html';
                } else {
                    showAlert(errorBox, data.message || "Email yoki parol noto'g'ri.");
                }
            } catch (error) {
                console.error("Login Error:", error);
                showAlert(errorBox, "Server bilan aloqa yo'q.");
            }
        });
    }
});

// ==========================================
// YORDAMCHI FUNKSIYALAR (ALERTS)
// ==========================================
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