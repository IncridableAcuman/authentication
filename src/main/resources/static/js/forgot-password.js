document.addEventListener('DOMContentLoaded', () => {
    const forgotForm = document.getElementById('forgotPasswordForm');

    if (forgotForm) {
        forgotForm.addEventListener('submit', async (e) => {
            e.preventDefault();

            const emailInput = document.getElementById('email');
            const submitBtn = document.getElementById('submitBtn');
            const errorBox = document.getElementById('errorMessage');
            const successBox = document.getElementById('successMessage');

            hideAlert(errorBox);
            hideAlert(successBox);

            const email = emailInput.value.trim();
            submitBtn.disabled = true;
            submitBtn.innerText = 'Yuborilmoqda...';

            try {
                const response = await fetchWithAuth('/auth/forgot-password', {
                    method: 'POST',
                    body: JSON.stringify({ email })
                });

                const data = await response.json();

                if (response.ok) {
                    showAlert(successBox, data.message || "Parolni tiklash havolasi emailingizga yuborildi!");
                    forgotForm.reset();
                } else {
                    showAlert(errorBox, data.message || "Xatolik yuz berdi. Emailni qayta tekshiring.");
                }
            } catch (error) {
                console.error("Forgot Password Error:", error);
                showAlert(errorBox, "Server bilan aloqa o'rnatib bo'lmadi.");
            } finally {
                submitBtn.disabled = false;
                submitBtn.innerText = 'Havolani yuborish';
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