document.addEventListener('DOMContentLoaded', function () {
    // Basculer entre connexion et inscription
    window.showSignIn = function () {
        document.querySelector('.auth-box-signin').style.display = 'block';
        document.querySelector('.auth-box-signup').style.display = 'none';
        document.title = "Connexion";
    };

    window.showSignUp = function () {
        document.querySelector('.auth-box-signin').style.display = 'none';
        document.querySelector('.auth-box-signup').style.display = 'block';
        document.title = "Inscription";
    };

    // Gestion du formulaire de connexion
    const signinForm = document.getElementById('signin-form');
    if (signinForm) {
        signinForm.addEventListener('submit', function(event) {
            event.preventDefault();

            const email = document.getElementById('signin-email').value.trim();
            const password = document.getElementById('signin-password').value.trim();

            // Vérifier l'existence de #signin-error
            const errorDiv = document.getElementById('signin-error');
            if (!errorDiv) {
                console.error("L'élément #signin-error n'a pas été trouvé.");
                return;
            }
            errorDiv.textContent = '';

            fetch('http://localhost:8080/api/auth/authenticate', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ email, motDePasse: password })
            })
                .then(response => {
                    if (!response.ok) {
                        return response.json().then(data => {
                            throw new Error(data.error || "Erreur d'authentification");
                        });
                    }
                    return response.json();
                })
                .then(data => {
                    if (data.token) {
                        localStorage.setItem('authToken', data.token);
                        alert("Connexion réussie !");
                        window.location.href = 'dashboard.jsp';
                    } else {
                        throw new Error("Aucun token reçu");
                    }
                })
                .catch(err => {
                    console.error("Échec de connexion :", err);
                    errorDiv.textContent = err.message;
                });
        });
    }

    // Gestion du formulaire d'inscription
    const signupForm = document.getElementById('signup-form');
    if (signupForm) {
        signupForm.addEventListener('submit', function(event) {
            event.preventDefault();

            const firstName = document.getElementById('signup-firstName').value.trim();
            const lastName = document.getElementById('signup-lastName').value.trim();
            const email = document.getElementById('signup-email').value.trim();
            const password = document.getElementById('signup-password').value.trim();
            const confirmPassword = document.getElementById('signup-confirmPassword').value.trim();
            const telephone = document.getElementById('signup-telephone').value.trim();
            const adresse = document.getElementById('signup-adresse').value.trim();

            const errorDiv = document.getElementById('register-error');
            if (!errorDiv) {
                console.error("L'élément #register-error n'a pas été trouvé.");
                return;
            }
            errorDiv.textContent = '';

            if (password !== confirmPassword) {
                errorDiv.textContent = "Les mots de passe ne correspondent pas.";
                return;
            }

            fetch('http://localhost:8080/api/auth/register', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    prenom: firstName,
                    nom: lastName,
                    email,
                    motDePasse: password,
                    confirmPassword,
                    telephone,
                    adresse
                })
            })
                .then(response => {
                    if (!response.ok) {
                        return response.json().then(data => {
                            let errorMsg = data.error || "Erreur lors de l'inscription";
                            if (data.errors && Array.isArray(data.errors)) {
                                errorMsg = data.errors.map(e => e.message).join("; ");
                            }
                            throw new Error(errorMsg);
                        });
                    }
                    return response.json();
                })
                .then(data => {
                    alert("Inscription réussie ! Vous pouvez maintenant vous connecter.");
                    showSignIn(); // Rediriger vers le formulaire de connexion
                })
                .catch(error => {
                    console.error("Échec de l'inscription :", error);
                    errorDiv.textContent = error.message;
                });
        });
    }
});