<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Gestion des Médicaments - Pharmacie</title>
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
            <li><a href="Fournisseur.jsp"><i class="fas fa-users"></i> Fournisseur</a></li>
            <li><a href="Medicament.jsp" class="active"><i class="fas fa-pills"></i> Medicament</a></li>
            <li><a href="bon_de_commande.jsp"><i class="fas fa-shopping-cart"></i> Bon de Commande</a></li>
            <li><a href="#" onclick="logout()"><i class="fas fa-sign-out-alt"></i> Déconnexion</a></li>
        </ul>
    </div>
    <!-- Main Content -->
    <div class="main-content">
        <!-- Medicament List Section -->
        <div id="medicamentList" class="page-content active">
            <h2>Liste des Médicaments</h2>
            <div style="margin-bottom: 15px;">
                <button class="btn btn-primary" onclick="showSection('addmedicament')">+ Ajouter</button>
            </div>
            <div id="statusMessage"></div>
            <div class="search-bar">
                <input type="text" id="searchMedicament" placeholder="Rechercher des médicaments..." class="form-control" style="width: 300px; display: inline-block;">
                <button class="btn btn-primary" onclick="searchMedicaments()">Rechercher</button>
            </div>
            <table>
                <thead>
                <tr>
                    <th>Code</th>
                    <th>Libellé</th>
                    <th>Famille</th>
                    <th>Prix</th>
                    <th>Stock</th>
                    <th>Expiration</th>
                    <th>Action</th>
                </tr>
                </thead>
                <tbody id="medicamentTableBody">
                <!-- Data will be loaded here -->
                </tbody>
            </table>
        </div>
        <!-- Add Medicament Section -->
        <div id="addmedicament" class="page-content">
            <h2>Ajouter un Médicament</h2>
            <form id="addmedicament-form" class="form-grid" enctype="multipart/form-data">
                <div class="row">
                    <div class="col-md-6">
                        <div class="form-group">
                            <label for="codeMed">Code Médicament:</label>
                            <input type="text" id="codeMed" name="codeMed" class="form-control" required>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="form-group">
                            <label for="libelle">Libellé:</label>
                            <input type="text" id="libelle" name="libelle" class="form-control" required>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-6">
                        <div class="form-group">
                            <label for="familleMed">Famille:</label>
                            <input type="text" id="familleMed" name="familleMed" class="form-control" required>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="form-group">
                            <label for="prixUnitaire">Prix Unitaire:</label>
                            <input type="number" step="0.01" id="prixUnitaire" name="prixUnitaire" class="form-control" required>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-6">
                        <div class="form-group">
                            <label for="qteStock">Quantité en Stock:</label>
                            <input type="number" id="qteStock" name="qteStock" class="form-control" required>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="form-group">
                            <label for="stockMin">Stock Minimum:</label>
                            <input type="number" id="stockMin" name="stockMin" class="form-control" required>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-6">
                        <div class="form-group">
                            <label for="dateExpiration">Date d'Expiration:</label>
                            <input type="date" id="dateExpiration" name="dateExpiration" class="form-control" required>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="form-group">
                            <label for="image">Image:</label>
                            <div class="upload-area" onclick="document.getElementById('image').click()">
                                <i class="fas fa-cloud-upload-alt fa-2x"></i>
                                <p>Cliquez pour télécharger une image</p>
                                <input type="file" id="image" name="image" accept="image/*" style="display: none;" onchange="previewImage(this)">
                                <img id="imagePreview" class="image-preview" alt="Aperçu de l'image">
                            </div>
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
    let currentMedicaments = [];

    document.addEventListener('DOMContentLoaded', function () {
        loadMedicaments();
        setupEventListeners();
    });

    function setupEventListeners() {
        const addForm = document.getElementById('addmedicament-form');
        if (addForm) {
            addForm.addEventListener('submit', function (e) {
                e.preventDefault();
                addMedicament();
            });
        }
    }

    function previewImage(input) {
        const preview = document.getElementById('imagePreview');
        const file = input.files[0];
        const reader = new FileReader();
        reader.onload = function (e) {
            preview.src = e.target.result;
            preview.style.display = 'block';
        };
        if (file) {
            reader.readAsDataURL(file);
        }
    }

    function loadMedicaments() {
        const token = localStorage.getItem('authToken');
        const statusDiv = document.getElementById('statusMessage');
        const tbody = document.getElementById('medicamentTableBody');
        if (!token) {
            showError("Token manquant. Veuillez vous reconnecter.");
            window.location.href = 'index.jsp';
            return;
        }

        showLoading(statusDiv, tbody);

        axios.get('http://localhost:8080/api/medicaments', {
            headers: {
                'Authorization': 'Bearer ' + token
            }
        })
            .then(response => {
                if (!Array.isArray(response.data)) {
                    throw new Error("Format de données invalide");
                }
                currentMedicaments = response.data.map(medicament => ({
                    id: medicament.id,
                    codeMed: medicament.codeMed || "N/A",
                    libelle: medicament.libelle || "N/A",
                    familleMed: medicament.familleMed || "N/A",
                    prixUnitaire: medicament.prixUnitaire || 0,
                    qteStock: medicament.qteStock || 0,
                    stockMin: medicament.stockMin || 0,
                    dateExpiration: medicament.dateExpiration ? new Date(medicament.dateExpiration).toLocaleDateString() : "N/A",
                }));

                if (currentMedicaments.length === 0) {
                    showNoData(tbody, statusDiv);
                } else {
                    displayMedicaments(currentMedicaments, tbody, statusDiv);
                }
            })
            .catch(error => {
                handleError(error, tbody, statusDiv);
            });
    }

    function showLoading(statusDiv, tbody) {
        statusDiv.textContent = "Chargement en cours...";
        statusDiv.style.color = "inherit";
        tbody.innerHTML = '<tr><td colspan="8">Chargement des données...</td></tr>';
    }

    function showNoData(tbody, statusDiv) {
        tbody.innerHTML = '<tr><td colspan="8">Aucun médicament trouvé</td></tr>';
        statusDiv.textContent = "Aucun médicament trouvé";
        statusDiv.style.color = "#666";
    }

    function displayMedicaments(medicaments, tbody, statusDiv) {
        tbody.innerHTML = medicaments.map(medicament => `
        <tr>
            <td>${escapeHtml(medicament.codeMed)}</td>
            <td>${escapeHtml(medicament.libelle)}</td>
            <td>${escapeHtml(medicament.familleMed)}</td>
            <td>${medicament.prixUnitaire.toFixed(2)} DH</td>
            <td class="${medicament.qteStock < medicament.stockMin ? 'text-danger' : ''}">
                ${medicament.qteStock}
            </td>
            <td>${medicament.dateExpiration}</td>
            <td>
                <button class="btn-edit" onclick="editMedicament(${medicament.id})">Modifier</button>
                <button class="btn-delete" onclick="deleteMedicament(${medicament.id})">Supprimer</button>
            </td>
        </tr>
    `).join('');
        statusDiv.textContent = `${medicaments.length} médicament(s) chargé(s)`;
        statusDiv.style.color = "green";
    }

    function handleError(error, tbody, statusDiv) {
        console.error("Erreur:", error);
        let errorMessage = "Erreur lors du chargement";
        if (error.response) {
            if (error.response.status === 401) {
                errorMessage = "Session expirée. Veuillez vous reconnecter.";
                window.location.href = 'index.jsp';
            } else if (error.response.data && error.response.data.message) {
                errorMessage = error.response.data.message;
            }
        }
        tbody.innerHTML = `<tr><td colspan="8" style="color: red;">${errorMessage}</td></tr>`;
        statusDiv.innerHTML = `<span style="color: red;">${errorMessage}</span>`;
    }

    function escapeHtml(unsafe) {
        if (unsafe === null || unsafe === undefined) return '';
        return unsafe.toString()
            .replace(/&/g, "&amp;")
            .replace(/</g, "<")
            .replace(/>/g, ">")
            .replace(/"/g, "&quot;")
            .replace(/'/g, "&#039;");
    }

    function addMedicament() {
        const token = localStorage.getItem('authToken');
        if (!token) {
            showError("Session expirée. Veuillez vous reconnecter.");
            window.location.href = 'index.jsp';
            return;
        }

        const form = document.getElementById('addmedicament-form');
        const isEditing = form.dataset.editingId !== undefined;
        const imageFile = document.getElementById('image').files[0];
        const existingImage = form.dataset.existingImage || "";

        const formData = new FormData();
        formData.append('codeMed', document.getElementById('codeMed').value.trim());
        formData.append('libelle', document.getElementById('libelle').value.trim());
        formData.append('dateExpiration', document.getElementById('dateExpiration').value);
        formData.append('prixUnitaire', document.getElementById('prixUnitaire').value);
        formData.append('stockMin', document.getElementById('stockMin').value);
        formData.append('familleMed', document.getElementById('familleMed').value.trim());
        formData.append('qteStock', document.getElementById('qteStock').value);

        if (imageFile) {
            formData.append('image', imageFile);
        } else if (isEditing) {
            formData.append('existingImage', existingImage);
        }

        const config = {
            headers: {
                'Authorization': 'Bearer ' + token,
                'Content-Type': 'multipart/form-data'
            }
        };

        const endpoint = isEditing
            ? `http://localhost:8080/api/medicaments/${form.dataset.editingId}`
            : 'http://localhost:8080/api/medicaments/upload';

        const method = isEditing ? 'put' : 'post';

        axios[method](endpoint, formData, config)
            .then(() => {
                alert(isEditing ? "Médicament modifié avec succès !" : "Médicament ajouté avec succès !");
                // Réinitialisation du formulaire
                form.reset();
                document.getElementById('imagePreview').style.display = 'none';
                delete form.dataset.editingId;
                delete form.dataset.existingImage;
                const submitBtn = form.querySelector('button[type="submit"]');
                submitBtn.textContent = "Enregistrer";
                submitBtn.classList.remove('btn-primary');
                submitBtn.classList.add('btn-success');

                // ✅ Toujours rediriger vers la liste après succès
                showSection('medicamentList');
                loadMedicaments(); // Rafraîchit la liste
            })
            .catch(error => {
                console.error("Erreur lors de l'opération :", error);

                let errorMsg = "Erreur lors de l'opération.";
                if (error.response?.data?.message) {
                    errorMsg = error.response.data.message;
                } else if (error.message) {
                    errorMsg = error.message;
                }

                // Afficher le message d'erreur
                document.getElementById('error-message').textContent = errorMsg;

                // ✅ Rediriger quand même vers la liste pour voir les changements (si ajout réussi mais parsing échoué)
                showSection('medicamentList');
                loadMedicaments();
            });
    }

    function editMedicament(id) {
        const medicament = currentMedicaments.find(m => m.id === id);
        if (medicament) {
            document.getElementById('codeMed').value = medicament.codeMed;
            document.getElementById('libelle').value = medicament.libelle;
            document.getElementById('familleMed').value = medicament.familleMed;
            document.getElementById('prixUnitaire').value = medicament.prixUnitaire;
            document.getElementById('qteStock').value = medicament.qteStock;
            document.getElementById('stockMin').value = medicament.stockMin;

            if (medicament.dateExpiration && medicament.dateExpiration !== "N/A") {
                const dateParts = medicament.dateExpiration.split('/');
                if (dateParts.length === 3) {
                    const formattedDate = `${dateParts[2]}-${dateParts[1].padStart(2, '0')}-${dateParts[0].padStart(2, '0')}`;
                    document.getElementById('dateExpiration').value = formattedDate;
                }
            }

            const form = document.getElementById('addmedicament-form');
            form.dataset.existingImage = medicament.image || "";

            const preview = document.getElementById('imagePreview');
            if (medicament.image) {
                preview.src = `http://localhost:8080/uploads/${medicament.image}`;
                preview.style.display = 'block';
            } else {
                preview.style.display = 'none';
            }

            form.dataset.editingId = id;

            const submitBtn = form.querySelector('button[type="submit"]');
            submitBtn.textContent = "Modifier";
            submitBtn.classList.remove('btn-success');
            submitBtn.classList.add('btn-primary');

            showSection('addmedicament');
        }
    }

    function deleteMedicament(id) {
        if (confirm("Êtes-vous sûr de vouloir supprimer ce médicament ?")) {
            const token = localStorage.getItem('authToken');
            axios.delete(`http://localhost:8080/api/medicaments/${id}`, {
                headers: {
                    'Authorization': 'Bearer ' + token
                }
            })
                .then(() => {
                    alert("Médicament supprimé avec succès");
                    loadMedicaments();
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

    function searchMedicaments() {
        const searchTerm = document.getElementById('searchMedicament').value.toLowerCase();
        const filteredMedicaments = currentMedicaments.filter(medicament =>
            medicament.codeMed.toLowerCase().includes(searchTerm) ||
            medicament.libelle.toLowerCase().includes(searchTerm) ||
            medicament.familleMed.toLowerCase().includes(searchTerm) ||
            medicament.prixUnitaire.toString().includes(searchTerm) ||
            medicament.qteStock.toString().includes(searchTerm) ||
            medicament.dateExpiration.toLowerCase().includes(searchTerm)
        );
        displayMedicaments(filteredMedicaments, document.getElementById('medicamentTableBody'), document.getElementById('statusMessage'));
    }

    function showError(message) {
        const statusDiv = document.getElementById('statusMessage');
        statusDiv.innerHTML = `<span style="color: red;">${message}</span>`;
    }

    function logout() {
        const token = localStorage.getItem('authToken');
        axios.post('http://localhost:8080/api/auth/logout', {}, {
            headers: {
                'Authorization': 'Bearer ' + token
            }
        })
            .then(() => {
                localStorage.removeItem('authToken');
                sessionStorage.removeItem('userData');
                window.location.href = 'index.jsp';
            })
            .catch(() => {
                localStorage.removeItem('authToken');
                window.location.href = 'index.jsp';
            });
    }
</script>
</body>
</html>