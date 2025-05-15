<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Gestion des Utilisateurs - Pharmacie</title>


    <!-- Font Awesome -->
    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css"
          crossorigin="anonymous"
          referrerpolicy="no-referrer"/>
    <link rel="stylesheet" href="assets/css/styles.css">

    <!-- Axios -->
    <script src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js"></script>
</head>
<body>

<div class="container">
    <!-- Sidebar -->
    <div class="sidebar">
        <div class="logo">
            <img src="assets/img/logo.jpg" alt="Logo">
        </div>
        <ul class="menu">
            <li><a href="dashboard.jsp"><i class="fas fa-home"></i> Home</a></li>
            <li><a href="Utilisateur.jsp" class="active"><i class="fas fa-users"></i> Utilisateurs</a></li>
            <li><a href="Fournisseur.jsp"><i class="fas fa-users"></i> Fournisseur</a></li>
            <li><a href="Medicament.jsp"><i class="fas fa-pills"></i> Medicament</a></li>
            <li><a href="bon_de_commande.jsp"><i class="fas fa-shopping-cart"></i> Bon de Commande</a></li>
            <li><a href="#" onclick="logout()"><i class="fas fa-sign-out-alt"></i> Déconnexion</a></li>
        </ul>
    </div>

    <!-- Main Content -->
    <div class="main-content">
        <!-- User List Section -->
        <div id="userList" class="page-content active">
            <h2>Liste des Utilisateurs</h2>

            <div style="margin-bottom: 15px;">
                <button class="btn btn-primary" onclick="showSection('adduser')">+ Ajouter</button>
            </div>

            <div id="statusMessage"></div>

            <div class="search-bar">
                <input type="text" id="searchUtilisateur" placeholder="Rechercher des utilisateurs..." class="form-control" style="width: 300px; display: inline-block;">
                <button class="btn btn-primary" onclick="searchUtilisateurs()">Rechercher</button>
            </div>

            <table>
                <thead>
                <tr>
                    <th>Nom</th>
                    <th>Prénom</th>
                    <th>Email</th>
                    <th>Téléphone</th>
                    <th>Adresse</th>
                    <th>Action</th>
                </tr>
                </thead>
                <tbody id="userTableBody">
                <!-- Data will be loaded here -->
                </tbody>
            </table>
        </div>

        <!-- Add User Section -->
        <div id="adduser" class="page-content">
            <h2>Ajouter un Utilisateur</h2>
            <form id="adduser-form" class="form-grid">
                <div class="row">
                    <div class="col-md-6">
                        <div class="form-group">
                            <label for="nomUtilisateur">Nom:</label>
                            <input type="text" id="nomUtilisateur" name="nom" class="form-control" required>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="form-group">
                            <label for="prenomUtilisateur">Prénom:</label>
                            <input type="text" id="prenomUtilisateur" name="prenom" class="form-control" required>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-6">
                        <div class="form-group">
                            <label for="mailUtilisateur">Email:</label>
                            <input type="email" id="mailUtilisateur" name="email" class="form-control" required>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="form-group">
                            <label for="passwordUtilisateur">Mot de passe:</label>
                            <input type="password" id="passwordUtilisateur" name="motDePasse" class="form-control" required>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-6">
                        <div class="form-group">
                            <label for="telUtilisateur">Téléphone:</label>
                            <input type="tel" id="telUtilisateur" name="telephone" class="form-control" required>
                        </div>
                    </div>
                    <div class="col-md-12">
                        <div class="form-group">
                            <label for="adresseUtilisateur">Adresse:</label>
                            <textarea id="adresseUtilisateur" name="adresse" class="form-control" required></textarea>
                        </div>
                    </div>
                </div>
                <button type="submit" class="btn btn-success">Enregistrer</button>
                <div id="error-message" class="error-message"></div>
            </form>
        </div>
    </div>
</div>

<script>
    // Global variables
    let currentUsers = [];

    // Load users when page loads
    document.addEventListener('DOMContentLoaded', function() {
        loadUsers();
        setupEventListeners();
    });

    function setupEventListeners() {
        // Add user form submission
        const addForm = document.getElementById('adduser-form');
        if (addForm) {
            addForm.addEventListener('submit', function(e) {
                e.preventDefault();
                addUser();
            });
        }
    }

    function loadUsers() {
        const token = localStorage.getItem('authToken');
        const statusDiv = document.getElementById('statusMessage');
        const tbody = document.getElementById('userTableBody');

        if (!token) {
            showError("Token manquant. Veuillez vous reconnecter.");
            window.location.href = 'index.jsp';
            return;
        }

        showLoading(statusDiv, tbody);

        axios.get('http://localhost:8080/api/utilisateurs', {
            headers: {
                'Authorization': 'Bearer ' + token
            }
        })
            .then(response => {
                if (!Array.isArray(response.data)) {
                    throw new Error("Format de données invalide");
                }

                currentUsers = response.data.map(user => ({
                    id: user.id,
                    nom: user.nom || "N/A",
                    prenom: user.prenom || "N/A",
                    email: user.email || "N/A",
                    telephone: user.telephone || "N/A",
                    adresse: user.adresse || "N/A"
                }));

                if (currentUsers.length === 0) {
                    showNoData(tbody, statusDiv);
                } else {
                    displayUsers(currentUsers, tbody, statusDiv);
                }
            })
            .catch(error => {
                handleError(error, tbody, statusDiv);
            });
    }

    function showLoading(statusDiv, tbody) {
        statusDiv.textContent = "Chargement en cours...";
        statusDiv.style.color = "inherit";
        tbody.innerHTML = '<tr><td colspan="6">Chargement des données...</td></tr>';
    }

    function showNoData(tbody, statusDiv) {
        tbody.innerHTML = '<tr><td colspan="6">Aucun utilisateur trouvé</td></tr>';
        statusDiv.textContent = "Aucun utilisateur trouvé";
        statusDiv.style.color = "#666";
    }

    function displayUsers(users, tbody, statusDiv) {
        tbody.innerHTML = users.map(user => `
            <tr>
                <td>${escapeHtml(user.nom)}</td>
                <td>${escapeHtml(user.prenom)}</td>
                <td>${escapeHtml(user.email)}</td>
                <td>${escapeHtml(user.telephone)}</td>
                <td>${escapeHtml(user.adresse)}</td>
                <td>
                    <button class="btn-edit" onclick="editUser(${user.id})">Modifier</button>
                    <button class="btn-delete" onclick="deleteUser(${user.id})">Supprimer</button>
                </td>
            </tr>
        `).join('');

        statusDiv.textContent = `${users.length} utilisateur(s) chargé(s)`;
        statusDiv.style.color = "green";
    }

    function handleError(error, tbody, statusDiv) {
        console.error("Erreur:", error);

        let errorMessage = "Erreur lors du chargement";
        if (error.response) {
            if (error.response.status === 401) {
                errorMessage = "Session expirée. Veuillez vous reconnecter.";
                window.location.href = 'index.jsp';
            } else if (error.response.data) {
                errorMessage = error.response.data.message || JSON.stringify(error.response.data);
            }
        }

        tbody.innerHTML = `<tr><td colspan="6" style="color: red;">${errorMessage}</td></tr>`;
        statusDiv.innerHTML = `<span style="color: red;">${errorMessage}</span>`;
    }

    function escapeHtml(unsafe) {
        if (unsafe === null || unsafe === undefined) return '';
        return unsafe.toString()
            .replace(/&/g, "&amp;")
            .replace(/</g, "&lt;")
            .replace(/>/g, "&gt;")
            .replace(/"/g, "&quot;")
            .replace(/'/g, "&#039;");
    }

    function addUser() {
        const token = localStorage.getItem('authToken');
        if (!token) {
            showError("Session expirée. Veuillez vous reconnecter.");
            window.location.href = 'index.jsp';
            return;
        }

        const form = document.getElementById('adduser-form');
        const isEditing = form.dataset.editingId !== undefined;
        const userData = {
            nom: document.getElementById('nomUtilisateur').value.trim(),
            prenom: document.getElementById('prenomUtilisateur').value.trim(),
            email: document.getElementById('mailUtilisateur').value.trim(),
            telephone: document.getElementById('telUtilisateur').value.trim(),
            adresse: document.getElementById('adresseUtilisateur').value.trim()
        };

        // Gestion spécifique du mot de passe
        const password = document.getElementById('passwordUtilisateur').value.trim();
        if (!isEditing) {
            // Mode création - mot de passe obligatoire
            if (!password) {
                document.getElementById('error-message').textContent = "Le mot de passe est obligatoire";
                return;
            }
            userData.motDePasse = password;
        } else if (password) {
            // Mode édition - mot de passe fourni (optionnel)
            userData.motDePasse = password;
        }

        const request = isEditing
            ? axios.put(`http://localhost:8080/api/utilisateurs/${form.dataset.editingId}`, userData, {
                headers: {
                    'Authorization': 'Bearer ' + token,
                    'Content-Type': 'application/json'
                }
            })
            : axios.post('http://localhost:8080/api/utilisateurs', userData, {
                headers: {
                    'Authorization': 'Bearer ' + token,
                    'Content-Type': 'application/json'
                }
            });

        request.then(() => {
            const message = isEditing
                ? "Utilisateur modifié avec succès !"
                : "Utilisateur ajouté avec succès !";
            alert(message);

            // Réinitialiser le formulaire
            form.reset();
            delete form.dataset.editingId;

            // Remettre l'attribut required pour le mot de passe
            document.getElementById('passwordUtilisateur').setAttribute('required', '');
            document.getElementById('passwordUtilisateur').placeholder = "";

            // Remettre le bouton en mode "Ajout"
            const submitBtn = form.querySelector('button[type="submit"]');
            submitBtn.textContent = "Enregistrer";
            submitBtn.classList.remove('btn-primary');
            submitBtn.classList.add('btn-success');

            showSection('userList');
            loadUsers();
        })
            .catch(error => {
                console.error("Erreur:", error);
                let errorMsg = isEditing
                    ? "Erreur lors de la modification"
                    : "Erreur lors de l'ajout";

                if (error.response && error.response.data && error.response.data.message) {
                    errorMsg = error.response.data.message;
                }
                document.getElementById('error-message').textContent = errorMsg;
            });
    }

    function editUser(id) {
        const user = currentUsers.find(u => u.id === id);
        if (user) {
            // Pré-remplir le formulaire
            document.getElementById('nomUtilisateur').value = user.nom;
            document.getElementById('prenomUtilisateur').value = user.prenom;
            document.getElementById('mailUtilisateur').value = user.email;
            document.getElementById('telUtilisateur').value = user.telephone;
            document.getElementById('adresseUtilisateur').value = user.adresse;

            // Rendre le mot de passe non obligatoire en mode édition
            document.getElementById('passwordUtilisateur').removeAttribute('required');
            document.getElementById('passwordUtilisateur').placeholder = "Laisser vide pour ne pas modifier";

            // Stocker l'ID en cours d'édition
            const form = document.getElementById('adduser-form');
            form.dataset.editingId = id;

            // Changer le texte du bouton
            const submitBtn = form.querySelector('button[type="submit"]');
            submitBtn.textContent = "Modifier";
            submitBtn.classList.remove('btn-success');
            submitBtn.classList.add('btn-primary');

            showSection('adduser');
        }
    }

    function deleteUser(id) {
        if (confirm("Êtes-vous sûr de vouloir supprimer cet utilisateur ?")) {
            const token = localStorage.getItem('authToken');

            axios.delete(`http://localhost:8080/api/utilisateurs/${id}`, {
                headers: {
                    'Authorization': 'Bearer ' + token
                }
            })
                .then(() => {
                    alert("Utilisateur supprimé avec succès");
                    loadUsers();
                })
                .catch(error => {
                    console.error("Erreur:", error);
                    alert("Erreur lors de la suppression");
                });
        }
    }

    function showSection(sectionId) {
        document.querySelectorAll('.page-content').forEach(section => {
            section.classList.remove('active');
        });
        document.getElementById(sectionId).classList.add('active');
    }

    function searchUtilisateurs() {
        const searchTerm = document.getElementById('searchUtilisateur').value.toLowerCase();
        const filteredUsers = currentUsers.filter(user =>
            user.nom.toLowerCase().includes(searchTerm) ||
            user.prenom.toLowerCase().includes(searchTerm) ||
            user.email.toLowerCase().includes(searchTerm) ||
            user.telephone.toLowerCase().includes(searchTerm) ||
            user.adresse.toLowerCase().includes(searchTerm)
        );

        displayUsers(filteredUsers, document.getElementById('userTableBody'), document.getElementById('statusMessage'));
    }

    function showError(message) {
        const statusDiv = document.getElementById('statusMessage');
        statusDiv.innerHTML = `<span style="color: red;">${message}</span>`;
    }
    function logout() {
        const token = localStorage.getItem('authToken');

        // Appel API pour invalider le token côté serveur
        axios.post('http://localhost:8080/api/auth/logout', {}, {
            headers: {
                'Authorization': 'Bearer ' + token
            }
        })
            .then(() => {
                // Nettoyage côté client
                localStorage.removeItem('authToken');
                sessionStorage.removeItem('userData');

                // Redirection vers la page de login
                window.location.href = 'index.jsp';
            })
            .catch(error => {
                console.error("Erreur lors de la déconnexion:", error);
                // Nettoyage quand même en cas d'erreur
                localStorage.removeItem('authToken');
                window.location.href = 'index.jsp';
            });
    }
</script>

</body>
</html>