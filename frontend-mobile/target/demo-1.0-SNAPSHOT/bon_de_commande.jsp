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

    <style>
        /* Reset */
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
            font-family: Arial, sans-serif;
        }
        .submenu {
            display: none;
            list-style: none;
            padding-left: 20px;
        }

        .submenu.active {
            display: block;
        }

        body {
            background-color: #fff; /* Fond principal en couleur douce */
            color: #333;
            display: flex;
            justify-content: center;
            align-items: center;
            min-height: 100vh;
            margin: 0; /* Supprimer les marges par défaut */
        }

        .container {
            display: flex;
            width: 100%;
            max-width: 100%; /* Pas de limite de largeur maximale */
            background-color: #ffffff; /* Fond blanc pour le contenu principal */
            box-shadow: none; /* Supprimer l'ombre */
            border-radius: 0; /* Supprimer les coins arrondis */
            height: 100vh; /* Hauteur complète de l'écran */
        }

        /* Sidebar */
        .sidebar {
            width: 20%;
            background-color: #328E6E;
            color: #fff;
            padding: 20px;
            height: 100vh;
            overflow-y: auto;
        }

        /* Logo */
        .logo {
            text-align: center;
            margin-bottom: 1px;
        }

        .logo img {
            width: 130px;
            height: auto;
            border-radius: 50%;
            object-fit: cover;
            overflow: hidden;
        }

        .menu {
            list-style: none;
        }

        .menu li {
            margin-bottom: 10px;
        }

        .menu a {
            display: block;
            padding: 10px;
            color: #fff;
            text-decoration: none;
            transition: background-color 0.3s ease;
        }

        .menu a:hover {
            background-color: #2b7a5f; /* Vert plus foncé au survol */
        }

        .menu a i {
            margin-right: 10px;
        }

        .arrow {
            float: right;
            margin-top: 2px;
        }

        .submenu {
            display: none;
            list-style: none;
            padding-left: 20px;
        }

        .submenu li {
            margin-bottom: 5px;
        }

        .submenu a {
            color: #fff;
            text-decoration: none;
            transition: color 0.3s ease;
        }

        .submenu a:hover {
            color: #ccc;
        }

        /* Main Content */
        .main-content {
            width: 80%;
            padding: 20px;
            background-color: #fff; /* Fond principal pour le contenu */
        }

        header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 20px;
            border-bottom: 1px solid #ddd;
            padding-bottom: 20px;
        }

        .user-info {
            display: flex;
            align-items: center;
        }

        .user-info img {
            width: 30px;
            height: 30px;
            border-radius: 50%;
            margin-right: 10px;
        }

        h1 {
            font-size: 24px;
            margin: 0;
        }

        .page-content {
            display: none;
        }

        .page-content.active {
            display: block;
        }

        /* Form Grid */
        .form-grid {
            display: grid;
            grid-template-columns: repeat(2, 1fr);
            gap: 20px;
        }

        .form-group {
            margin-bottom: 15px;
        }

        .form-label {
            font-weight: bold;
            margin-bottom: 5px;
            color: #328E6E; /* Texte vert pour les labels */
        }

        .form-control,
        .form-select {
            width: 100%;
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 5px;
            font-size: 14px;
            transition: border-color 0.3s ease;
        }

        .form-control:focus,
        .form-select:focus {
            border-color: #328E6E; /* Bordure verte au focus */
            outline: none;
            box-shadow: 0 0 5px rgba(50, 142, 110, 0.5); /* Ombre verte */
        }

        .form-actions {
            display: flex;
            justify-content: flex-end;
            gap: 10px;
        }

        .btn {
            padding: 10px 20px;
            background-color: #328E6E; /* Boutons verts spécifiques */
            color: #fff;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            transition: background-color 0.3s ease;
        }

        .btn:hover {
            background-color: #2b7a5f; /* Vert plus foncé au survol */
        }

        /* Responsive Design */
        @media (max-width: 768px) {
            .form-grid {
                grid-template-columns: 1fr;
            }

            .search-bar input {
                width: 100%;
            }
        }

        /* Table Styles */
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }

        table th,
        table td {
            padding: 10px;
            border: 1px solid #ddd;
            text-align: left;
        }

        table th {
            background-color: #328E6E; /* En-têtes de tableau verts spécifiques */
            color: #fff;
        }

        .edit-btn,
        .delete-btn {
            padding: 5px;
            background-color: transparent;
            border: none;
            cursor: pointer;
        }

        .edit-btn i,
        .delete-btn i {
            color: #328E6E; /* Icônes vertes spécifiques */
        }

        .delete-btn i {
            color: red;
        }

        /* Footer */
        footer {
            margin-top: auto; /* Placer le footer en bas */
            padding: 20px;
            background-color: #E1EEBC; /* Fond légèrement doux */
            text-align: center;
        }

        .buttons {
            display: flex;
            justify-content: center;
            gap: 20px;
        }

        /* Style pour les cartes de statistiques */
        .stats-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
            gap: 20px;
        }

        .stat-card {
            background-color: #f9f9f9;
            border: 1px solid #ddd;
            padding: 20px;
            text-align: center;
            border-radius: 8px;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
        }

        .stat-card i {
            font-size: 3rem;
            color: #28a745; /* Couleur verte pour les icônes */
        }

        /* Style pour les notifications */
        .notifications {
            margin-top: 20px;
        }

        .notification {
            background-color: #fff;
            border: 1px solid #ddd;
            padding: 10px;
            margin-bottom: 10px;
            border-radius: 5px;
        }

        .notification i {
            color: #dc3545; /* Couleur rouge pour les alertes */
            margin-right: 10px;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }

        th {
            background-color: #f2f2f2;
            padding: 12px;
            text-align: left;
        }

        td {
            padding: 10px;
            border-bottom: 1px solid #ddd;
        }

        .btn-edit {
            background-color: #4CAF50;
            color: white;
            padding: 6px 12px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            margin-right: 5px;
        }

        .btn-delete {
            background-color: #f44336;
            color: white;
            padding: 6px 12px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }

        #statusMessage {
            margin: 10px 0;
            padding: 10px;
            border-radius: 4px;
        }

        .image-preview {
            max-width: 150px;
            max-height: 150px;
            margin: 10px 0;
            display: none;
        }

        .upload-area {
            border: 2px dashed #328E6E;
            padding: 20px;
            text-align: center;
            margin: 10px 0;
            border-radius: 5px;
            cursor: pointer;
        }

        .upload-area:hover {
            background-color: #f0f8f5;
        }
    </style>

    <!-- Axios -->
    <script src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jspdf/2.5.1/jspdf.umd.min.js"></script>
</head>
<body>

<div class="container">
    <!-- Sidebar -->
    <div class="sidebar">
        <div class="logo">
            <img src="assets/img/logo.jpg" alt="Logo">
        </div>
        <ul class="menu">
            <li><a href="dashbord.jsp"><i class="fas fa-home"></i> Home</a></li>
            <li><a href="Utilisateur.jsp"><i class="fas fa-users"></i> Utilisateurs</a></li>
            <li><a href="Fournisseur.jsp"><i class="fas fa-users"></i> Fournisseur</a></li>
            <li><a href="Medicament.jsp" class="active"><i class="fas fa-pills"></i> Medicament</a></li>
            <li><a href="bon_de_commande.jsp"><i class="fas fa-shopping-cart"></i> Bon de Commande</a></li>
            <li><a href="#" onclick="logout()"><i class="fas fa-sign-out-alt"></i> Déconnexion</a></li>

        </ul>
    </div>

    <div class="main-content">
        <!-- Bon De Commande List Section -->
        <div id="bonDeCommandeList" class="page-content active">
            <h2>Liste des Bons de Commande</h2>

            <div style="margin-bottom: 15px;">
                <button class="btn btn-primary" onclick="showSection('addcommande')">+ Ajouter</button>
            </div>

            <div id="statusMessageCommande"></div>

            <div class="search-bar">
                <input type="text" id="searchBonCommande" placeholder="Rechercher un bon de commande..." class="form-control" style="width: 300px; display: inline-block;">
                <button class="btn btn-primary" onclick="searchBonsDeCommande()">Rechercher</button>
            </div>

            <table>
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Médicament</th>
                    <th>Fournisseur</th>
                    <th>Date</th>
                    <th>Quantité</th>
                    <th>Prix Unit.</th>
                    <th>Montant HT</th>
                    <th>TTC (+20%)</th>
                    <th>Statut</th>
                    <th>Action</th>
                </tr>
                </thead>
                <tbody id="bonDeCommandeTableBody">
                <!-- Les données seront insérées ici -->
                </tbody>
            </table>
        </div>

        <!-- Add Commande Section -->
        <div id="addcommande" class="page-content">
            <h2>Ajouter un Bon de Commande</h2>
            <form id="addcommande-form" class="form-grid">
                <div class="form-group">
                    <label for="medicamentSelect">Médicament :</label>
                    <select id="medicamentSelect" name="medicament" class="form-select" required></select>
                </div>
                <div class="form-group">
                    <label for="fournisseurSelect">Fournisseur :</label>
                    <select id="fournisseurSelect" name="fournisseur" class="form-select" required></select>
                </div>
                <div class="form-group">
                    <label for="dateCommande">Date de commande :</label>
                    <input type="date" id="dateCommande" name="dateCommande" class="form-control" required />
                </div>
                <div class="form-group">
                    <label for="quantite">Quantité commandée :</label>
                    <input type="number" id="quantite" name="quantite" class="form-control" required />
                </div>
                <div class="form-group">
                    <label for="prixUnitaire">Prix unitaire : </label>
                    <input type="number" id="prixUnitaire" name="prixUnitaire" class="form-control" required />
                </div>
                <div class="form-group">
                    <label for="statutCommande">Statut de commande :</label>
                    <input type="text" id="statutCommande" name="statutCommande" class="form-control" required />
                </div>
                <button type="submit" class="btn btn-success">Enregistrer</button>
                <div id="error-message-commande" class="error-message"></div>
            </form>
        </div>
    </div>
</div>
<script>
    // Global variables
    let currentBonsDeCommande = [];

    document.addEventListener('DOMContentLoaded', function () {
        loadMedicamentsForSelect();
        loadFournisseursForSelect();
        loadBonsDeCommande();
    });

    function loadMedicamentsForSelect() {
        const token = localStorage.getItem('authToken');
        axios.get('http://localhost:8080/api/medicaments', {
            headers: { 'Authorization': 'Bearer ' + token }
        }).then(response => {
            populateSelect('medicamentSelect', response.data, 'id', 'libelle');
        }).catch(err => console.error("Erreur chargement médicaments:", err));
    }

    function loadFournisseursForSelect() {
        const token = localStorage.getItem('authToken');
        axios.get('http://localhost:8080/api/fournisseurs', {
            headers: { 'Authorization': 'Bearer ' + token }
        }).then(response => {
            populateSelect('fournisseurSelect', response.data, 'id', 'nom');
        }).catch(err => console.error("Erreur chargement fournisseurs:", err));
    }

    function populateSelect(selectId, data, valueKey, labelKey) {
        const select = document.getElementById(selectId);
        select.innerHTML = '<option value="">-- Sélectionner --</option>';
        data.forEach(item => {
            const option = document.createElement('option');
            option.value = item[valueKey];
            option.textContent = item[labelKey];
            select.appendChild(option);
        });
    }

    function loadBonsDeCommande() {
        const token = localStorage.getItem('authToken');
        axios.get('http://localhost:8080/api/bon-de-commande', {
            headers: { 'Authorization': 'Bearer ' + token }
        }).then(response => {
            currentBonsDeCommande = response.data;
            displayBonsDeCommande(currentBonsDeCommande);
        }).catch(err => {
            console.error("Erreur lors du chargement des bons de commande:", err);
            document.getElementById('bonDeCommandeTableBody').innerHTML = '<tr><td colspan="8" style="color: red;">Erreur lors du chargement.</td></tr>';
        });
    }

    function displayBonsDeCommande(bons) {
        const tbody = document.getElementById('bonDeCommandeTableBody');
        tbody.innerHTML = '';

        if (bons.length === 0) {
            tbody.innerHTML = '<tr><td colspan="10">Aucun bon de commande trouvé</td></tr>';
            return;
        }

        bons.forEach(bon => {
            const prixUnitaire = bon.prixUnitaire || 0;
            const quantite = bon.quantiteCommandee || 0;
            const montantHT = (prixUnitaire * quantite).toFixed(2);
            const montantTTC = (montantHT * 1.20).toFixed(2);

            const row = document.createElement('tr');
            row.innerHTML = `
            <td>${bon.id}</td>
            <td>${bon.medicament?.libelle || "N/A"}</td>
            <td>${bon.fournisseur?.nom || "N/A"} ${bon.fournisseur?.prenom || ""}</td>
            <td>${new Date(bon.dateCommande).toLocaleDateString()}</td>
            <td>${quantite}</td>
            <td>${prixUnitaire.toFixed(2)} DH</td>
            <td>${montantHT} DH</td>
            <td>${montantTTC} DH</td>
            <td>${bon.statutCommande}</td>
            <td>
                <button class="btn-delete" onclick="deleteBon(${bon.id})">Supprimer</button>
                <button onclick="generatePDF('${encodeURIComponent(JSON.stringify(bon))}')">PDF</button>`;

            tbody.appendChild(row);
        });
    }
    function searchBonsDeCommande() {
        const searchTerm = document.getElementById('searchBonCommande').value.toLowerCase();
        const filtered = currentBonsDeCommande.filter(bon =>
            bon.medicament?.libelle?.toLowerCase().includes(searchTerm) ||
            bon.fournisseur?.nom?.toLowerCase().includes(searchTerm) ||
            bon.statutCommande?.toLowerCase().includes(searchTerm)
        );
        displayBonsDeCommande(filtered);
    }

    function generatePDF(encodedBon) {
        try {
            // Décoder les données JSON reçues
            const bon = JSON.parse(decodeURIComponent(encodedBon));

            // Vérifier que toutes les propriétés nécessaires sont présentes
            if (!bon || !bon.medicament || !bon.fournisseur) {
                throw new Error("Données incomplètes");
            }

            // Initialiser jsPDF
            const { jsPDF } = window.jspdf;
            const doc = new jsPDF();

            // Extraire les informations du bon
            const date = new Date(bon.dateCommande).toLocaleDateString();
            const fournisseur = `${bon.fournisseur.nom} ${bon.fournisseur.prenom}`;
            const medicament = bon.medicament.libelle || 'N/A';
            const prixUnitaire = parseFloat(bon.prixUnitaire) || 0;
            const qte = parseInt(bon.quantiteCommandee) || 0;
            const montantHT = (prixUnitaire * qte).toFixed(2);
            const montantTTC = (montantHT * 1.20).toFixed(2);

            // Générer le contenu du PDF
            doc.setFontSize(16);
            doc.text("Bon de Commande", 20, 20);

            doc.setFontSize(12);
            doc.text(`ID : ${bon.id}`, 20, 30);
            doc.text(`Date : ${date}`, 20, 40);
            doc.text(`Fournisseur : ${fournisseur}`, 20, 50);
            doc.text(`Médicament : ${medicament}`, 20, 60);
            doc.text(`Quantité commandée : ${qte}`, 20, 70);
            doc.text(`Prix unitaire : ${prixUnitaire} DH`, 20, 80);
            doc.text(`Montant HT : ${montantHT} DH`, 20, 90);
            doc.text(`Montant TTC : ${montantTTC} DH`, 20, 100);
            doc.text(`Statut : ${bon.statutCommande}`, 20, 110);

            // Télécharger le PDF
            doc.save(`bon_de_commande_${bon.id}.pdf`);

        } catch (error) {
            console.error("Erreur détaillée :", error);
            alert("Une erreur s'est produite lors de la génération du PDF.");
        }
    }
    function addBonDeCommande(event) {
        event.preventDefault();

        const selectedMedicamentId = document.getElementById('medicamentSelect').value;
        const selectedFournisseurId = document.getElementById('fournisseurSelect').value;

        const bon = {
            medicament: { id: selectedMedicamentId },
            fournisseur: { id: selectedFournisseurId },
            dateCommande: document.getElementById('dateCommande').value,
            quantiteCommandee: parseInt(document.getElementById('quantite').value),
            statutCommande: document.getElementById('statutCommande').value,
            prixUnitaire: parseFloat(document.getElementById('prixUnitaire').value)
        };

        const token = localStorage.getItem('authToken');

        axios.post('http://localhost:8080/api/bon-de-commande', bon, {
            headers: {
                'Authorization': 'Bearer ' + token,
                'Content-Type': 'application/json'
            }
        })
            .then(res => {
                alert("Bon de commande ajouté !");
                document.getElementById('addcommande-form').reset();
                showSection('bonDeCommandeList');
                loadBonsDeCommande();
            })
            .catch(err => {
                console.error("Erreur lors de l'ajout :", err);
                document.getElementById('error-message-commande').textContent = "Erreur lors de l'enregistrement.";
            });
    }

    function deleteBon(id) {
        if (confirm("Êtes-vous sûr de vouloir supprimer ce bon de commande ?")) {
            const token = localStorage.getItem('authToken');
            axios.delete(`http://localhost:8080/api/bon-de-commande/${id}`, {
                headers: {
                    'Authorization': 'Bearer ' + token
                }
            })
                .then(() => {
                    alert("Bon de commande supprimé !");
                    loadBonsDeCommande();
                })
                .catch(err => {
                    console.error("Erreur lors de la suppression :", err);
                    alert("Erreur lors de la suppression");
                });
        }
    }
    document.getElementById('addcommande-form').addEventListener('submit', addBonDeCommande);


    function showSection(sectionId) {
        document.querySelectorAll('.page-content').forEach(section => {
            section.classList.remove('active');
        });
        document.getElementById(sectionId).classList.add('active');
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
                window.location.href = '${pageContext.request.contextPath}/index.jsp';
            })
            .catch(error => {
                console.error("Erreur lors de la déconnexion:", error);
                // Nettoyage quand même en cas d'erreur
                localStorage.removeItem('authToken');
                window.location.href = '${pageContext.request.contextPath}/index.jsp';
            });
    }
</script>

</body>
</html>