document.addEventListener('DOMContentLoaded', () => {
    const resetForm = document.getElementById('resetPasswordForm');
    const errorBox = document.getElementById('errorMessage');
    const successBox = document.getElementById('successMessage');

    // URL parametridan token-ni ajratib olish (?token=XYZ_TOKEN)
    const urlParams = new URLSearchParams(window.location.search);
    const token = urlParams.get('token');

    // Agar URL tarkibida token bo'lmasa, ariza topshirishni bloklash
    if (!token) {
        showAlert(errorBox, "Yaroqsiz yoki eskirgan havola. Iltimos, qaytadan tiklash havolasini so'rang.");
        if (resetForm) {
            resetForm.querySelector('button[type="submit"]').disabled = true;
        }
        return;
    }

    if (resetForm) {
        resetForm.addEventListener('submit', async (e) => {
            e.preventDefault();

            hideAlert(errorBox);
            hideAlert(successBox);

            const newPassword = document.getElementById('newPassword').value;
            const confirmPassword = document.getElementById('confirmPassword').value;

            // Parollar mosligini tekshirish
            if (newPassword !== confirmPassword) {
                showAlert(errorBox, "Kiritilgan parollar bir-biriga mos kelmadi!");
                return;
            }

            const submitBtn = document.getElementById('submitBtn');
            submitBtn.disabled = true;
            submitBtn.innerText = 'Saqlanmoqda...';

            try {
                const response = await fetchWithAuth('/auth/reset-password', {
                    method: 'POST',
                    body: JSON.stringify({
                        token: token,
                        newPassword: newPassword
                    })
                });

                const data = await response.json();

                if (response.ok) {
                    showAlert(successBox, "Parolingiz muvaffaqiyatli yangilandi! Login sahifasiga o'tilmoqda...");
                    resetForm.reset();

                    setTimeout(() => {
                        window.location.href = '/login.html';
                    }, 2000);
                } else {
                    showAlert(errorBox, data.message || "Parolni o'zgartirishda xatolik yuz berdi.");
                }
            } catch (error) {
                console.error("Reset Password Error:", error);
                showAlert(errorBox, "Server bilan aloqa o'rnatib bo'lmadi.");
            } finally {
                submitBtn.disabled = false;
                submitBtn.innerText = 'Parolni yangilash';
            }
        });
    }
});

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