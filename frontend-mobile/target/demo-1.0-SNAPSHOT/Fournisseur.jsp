<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Gestion des Fournisseurs - Pharmacie</title>

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
            <li><a href="Utilisateur.jsp"><i class="fas fa-users"></i> Utilisateurs</a></li>
            <li><a href="Fournisseur.jsp" class="active"><i class="fas fa-users"></i> Fournisseur</a></li>
            <li><a href="Medicament.jsp"><i class="fas fa-pills"></i> Medicament</a></li>
            <li><a href="bon_de_commande.jsp"><i class="fas fa-shopping-cart"></i> Bon de Commande</a></li>
            <li><a href="#" onclick="logout()"><i class="fas fa-sign-out-alt"></i> Déconnexion</a></li>

        </ul>
    </div>

    <!-- Main Content -->
    <div class="main-content">
        <!-- Fournisseur List Section -->
        <div id="fournisseurList" class="page-content active">
            <h2>Liste des Fournisseurs</h2>

            <div style="margin-bottom: 15px;">
                <button class="btn btn-primary" onclick="showSection('addfournisseur')">+ Ajouter</button>
            </div>

            <div id="statusMessage"></div>

            <div class="search-bar">
                <input type="text" id="searchFournisseur" placeholder="Rechercher des fournisseurs..." class="form-control" style="width: 300px; display: inline-block;">
                <button class="btn btn-primary" onclick="searchFournisseurs()">Rechercher</button>
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
                <tbody id="fournisseurTableBody">
                <!-- Data will be loaded here -->
                </tbody>
            </table>
        </div>

        <!-- Add Fournisseur Section -->
        <div id="addfournisseur" class="page-content">
            <h2>Ajouter un Fournisseur</h2>
            <form id="addfournisseur-form" class="form-grid">
                <div class="row">
                    <div class="col-md-6">
                        <div class="form-group">
                            <label for="nomFournisseur">Nom:</label>
                            <input type="text" id="nomFournisseur" name="nom" class="form-control" required>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="form-group">
                            <label for="prenomFournisseur">Prénom:</label>
                            <input type="text" id="prenomFournisseur" name="prenom" class="form-control" required>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-6">
                        <div class="form-group">
                            <label for="mailFournisseur">Email:</label>
                            <input type="email" id="mailFournisseur" name="email" class="form-control" required>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="form-group">
                            <label for="telFournisseur">Téléphone:</label>
                            <input type="tel" id="telFournisseur" name="telephone" class="form-control" required>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-12">
                        <div class="form-group">
                            <label for="adresseFournisseur">Adresse:</label>
                            <textarea id="adresseFournisseur" name="adresse" class="form-control" required></textarea>
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
    let currentFournisseurs = [];

    // Load fournisseurs when page loads
    document.addEventListener('DOMContentLoaded', function() {
        loadFournisseurs();
        setupEventListeners();
    });

    function setupEventListeners() {
        // Add fournisseur form submission
        const addForm = document.getElementById('addfournisseur-form');
        if (addForm) {
            addForm.addEventListener('submit', function(e) {
                e.preventDefault();
                addFournisseur();
            });
        }
    }

    function loadFournisseurs() {
        const token = localStorage.getItem('authToken');
        const statusDiv = document.getElementById('statusMessage');
        const tbody = document.getElementById('fournisseurTableBody');

        if (!token) {
            showError("Token manquant. Veuillez vous reconnecter.");
            window.location.href = 'index.jsp';
            return;
        }

        showLoading(statusDiv, tbody);

        axios.get('http://localhost:8080/api/fournisseurs', {
            headers: {
                'Authorization': 'Bearer ' + token
            }
        })
            .then(response => {
                if (!Array.isArray(response.data)) {
                    throw new Error("Format de données invalide");
                }

                currentFournisseurs = response.data.map(fournisseur => ({
                    id: fournisseur.id,
                    nom: fournisseur.nom || "N/A",
                    prenom: fournisseur.prenom || "N/A",
                    email: fournisseur.email || "N/A",
                    telephone: fournisseur.telephone || "N/A",
                    adresse: fournisseur.adresse || "N/A"
                }));

                if (currentFournisseurs.length === 0) {
                    showNoData(tbody, statusDiv);
                } else {
                    displayFournisseurs(currentFournisseurs, tbody, statusDiv);
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
        tbody.innerHTML = '<tr><td colspan="6">Aucun fournisseur trouvé</td></tr>';
        statusDiv.textContent = "Aucun fournisseur trouvé";
        statusDiv.style.color = "#666";
    }

    function displayFournisseurs(fournisseurs, tbody, statusDiv) {
        tbody.innerHTML = fournisseurs.map(fournisseur => `
            <tr>
                <td>${escapeHtml(fournisseur.nom)}</td>
                <td>${escapeHtml(fournisseur.prenom)}</td>
                <td>${escapeHtml(fournisseur.email)}</td>
                <td>${escapeHtml(fournisseur.telephone)}</td>
                <td>${escapeHtml(fournisseur.adresse)}</td>
                <td>
                    <button class="btn-edit" onclick="editFournisseur(${fournisseur.id})">Modifier</button>
                    <button class="btn-delete" onclick="deleteFournisseur(${fournisseur.id})">Supprimer</button>
                </td>
            </tr>
        `).join('');

        statusDiv.textContent = `${fournisseurs.length} fournisseur(s) chargé(s)`;
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

    function addFournisseur() {
        const token = localStorage.getItem('authToken');
        if (!token) {
            showError("Session expirée. Veuillez vous reconnecter.");
            window.location.href = 'index.jsp';
            return;
        }

        const form = document.getElementById('addfournisseur-form');
        const isEditing = form.dataset.editingId !== undefined;
        const fournisseurData = {
            nom: document.getElementById('nomFournisseur').value.trim(),
            prenom: document.getElementById('prenomFournisseur').value.trim(),
            email: document.getElementById('mailFournisseur').value.trim(),
            telephone: document.getElementById('telFournisseur').value.trim(),
            adresse: document.getElementById('adresseFournisseur').value.trim()
        };

        const request = isEditing
            ? axios.put(`http://localhost:8080/api/fournisseurs/${form.dataset.editingId}`, fournisseurData, {
                headers: {
                    'Authorization': 'Bearer ' + token,
                    'Content-Type': 'application/json'
                }
            })
            : axios.post('http://localhost:8080/api/fournisseurs', fournisseurData, {
                headers: {
                    'Authorization': 'Bearer ' + token,
                    'Content-Type': 'application/json'
                }
            });

        request.then(() => {
            const message = isEditing
                ? "Fournisseur modifié avec succès !"
                : "Fournisseur ajouté avec succès !";
            alert(message);

            // Réinitialiser le formulaire
            form.reset();
            delete form.dataset.editingId;

            // Remettre le bouton en mode "Ajout"
            const submitBtn = form.querySelector('button[type="submit"]');
            submitBtn.textContent = "Enregistrer";
            submitBtn.classList.remove('btn-primary');
            submitBtn.classList.add('btn-success');

            showSection('fournisseurList');
            loadFournisseurs();
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

    function editFournisseur(id) {
        const fournisseur = currentFournisseurs.find(f => f.id === id);
        if (fournisseur) {
            // Pré-remplir le formulaire
            document.getElementById('nomFournisseur').value = fournisseur.nom;
            document.getElementById('prenomFournisseur').value = fournisseur.prenom;
            document.getElementById('mailFournisseur').value = fournisseur.email;
            document.getElementById('telFournisseur').value = fournisseur.telephone;
            document.getElementById('adresseFournisseur').value = fournisseur.adresse;

            // Stocker l'ID en cours d'édition
            const form = document.getElementById('addfournisseur-form');
            form.dataset.editingId = id;

            // Changer le texte du bouton
            const submitBtn = form.querySelector('button[type="submit"]');
            submitBtn.textContent = "Modifier";
            submitBtn.classList.remove('btn-success');
            submitBtn.classList.add('btn-primary');

            showSection('addfournisseur');
        }
    }

    function deleteFournisseur(id) {
        if (confirm("Êtes-vous sûr de vouloir supprimer ce fournisseur ?")) {
            const token = localStorage.getItem('authToken');

            axios.delete(`http://localhost:8080/api/fournisseurs/${id}`, {
                headers: {
                    'Authorization': 'Bearer ' + token
                }
            })
                .then(() => {
                    alert("Fournisseur supprimé avec succès");
                    loadFournisseurs();
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

    function searchFournisseurs() {
        const searchTerm = document.getElementById('searchFournisseur').value.toLowerCase();
        const filteredFournisseurs = currentFournisseurs.filter(fournisseur =>
            fournisseur.nom.toLowerCase().includes(searchTerm) ||
            fournisseur.prenom.toLowerCase().includes(searchTerm) ||
            fournisseur.email.toLowerCase().includes(searchTerm) ||
            fournisseur.telephone.toLowerCase().includes(searchTerm) ||
            fournisseur.adresse.toLowerCase().includes(searchTerm)
        );

        displayFournisseurs(filteredFournisseurs, document.getElementById('fournisseurTableBody'), document.getElementById('statusMessage'));
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