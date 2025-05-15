<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page isELIgnored="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>Bon de Commande - Pharmacie</title>

    <link rel="stylesheet"
          href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css"
          crossorigin="anonymous"
          referrerpolicy="no-referrer"/>

    <!-- jsPDF -->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jspdf/2.5.1/jspdf.umd.min.js"></script>
    <link rel="stylesheet" href="assets/css/style2.css">


    <!-- Axios -->
    <script src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js"></script>


</head>
<body>

<div class="container">

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

    <!-- Main Content -->
    <div class="main-content">

        <!-- Dashboard -->
        <div class="dashboard">
            <h2>Tableau de bord</h2>
            <div class="kpi-container">
                <div class="kpi-card">
                    <i class="fas fa-pills"></i>
                    <p><strong>Médicaments</strong></p>
                    <p id="nbMedicaments">Chargement...</p>
                </div>
                <div class="kpi-card">
                    <i class="fas fa-users"></i>
                    <p><strong>Fournisseurs</strong></p>
                    <p id="nbFournisseurs">Chargement...</p>
                </div>
                <div class="kpi-card">
                    <i class="fas fa-user"></i>
                    <p><strong>Utilisateurs</strong></p>
                    <p id="nbUtilisateurs">Chargement...</p>
                </div>
                <div class="kpi-card">
                    <i class="fas fa-shopping-cart"></i>
                    <p><strong>Bons de commande</strong></p>
                    <p id="nbBonsDeCommande">Chargement...</p>
                </div>
            </div>
        </div>

        <!-- Notifications -->
        <h3>Notifications</h3>
        <div id="notificationsContainer">
            <div class="notification">Chargement...</div>
        </div>

    </div>

</div>

<script>
    function loadKPIs() {
        const token = localStorage.getItem('authToken');

        axios.get('http://localhost:8080/api/medicaments/count', {
            headers: { 'Authorization': 'Bearer ' + token }
        }).then(response => {
            document.getElementById('nbMedicaments').textContent = response.data;
        });

        axios.get('http://localhost:8080/api/fournisseurs/count', {
            headers: { 'Authorization': 'Bearer ' + token }
        }).then(response => {
            document.getElementById('nbFournisseurs').textContent = response.data;
        });

        axios.get('http://localhost:8080/api/utilisateurs/count', {
            headers: { 'Authorization': 'Bearer ' + token }
        }).then(response => {
            document.getElementById('nbUtilisateurs').textContent = response.data;
        });

        axios.get('http://localhost:8080/api/bon-de-commande/count', {
            headers: { 'Authorization': 'Bearer ' + token }
        }).then(response => {
            document.getElementById('nbBonsDeCommande').textContent = response.data;
        });
    }

    function checkExpirationAndStock() {
        const token = localStorage.getItem('authToken');
        axios.get('http://localhost:8080/api/alerts', {
            headers: { 'Authorization': 'Bearer ' + token }
        })
            .then(response => {
                console.log("Données reçues :", response.data); // ✅ Debug

                const container = document.getElementById('notificationsContainer');
                container.innerHTML = '';

                if (response.data.length === 0) {
                    container.innerHTML = '<div class="notification">Aucune alerte trouvée</div>';
                    return;
                }

                response.data.forEach(medicament => {
                    let messages = [];

                    // ✅ Gestion de la date d'expiration
                    if (medicament.dateExpiration) {
                        // Extraction de la partie date uniquement
                        const dateString = medicament.dateExpiration.split('T')[0];
                        const [year, month, day] = dateString.split('-');

                        const expirationDate = new Date(year, month - 1, day);
                        const today = new Date();

                        const diffDays = Math.ceil((expirationDate - today) / (1000 * 60 * 60 * 24));

                        console.log(`${medicament.libelle} - Diff jours : ${diffDays}`); // ✅ Debug

                        if (diffDays <= 60 && diffDays >= 0) {
                            messages.push(`⚠️ Expiration proche : ${medicament.libelle} (${expirationDate.toLocaleDateString()})`);
                        }
                    }

                    // ✅ Gestion du stock bas
                    if (medicament.qteStock <= medicament.stockMin) {
                        messages.push(`⚠️ Stock bas : ${medicament.libelle} (Stock: ${medicament.qteStock})`);
                    }

                    // ✅ Affichage des messages
                    messages.forEach(msg => addNotification(msg));
                });
            })
            .catch(() => {
                document.getElementById('notificationsContainer').innerHTML =
                    '<div class="notification" style="color: red;">Erreur lors du chargement des notifications</div>';
            });
    }

    function isCloseToExpiration(expirationDate) {
        const today = new Date();
        const expiry = new Date(expirationDate);
        const diffTime = Math.abs(expiry - today);
        const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
        return diffDays <= 7;
    }

    function addNotification(message) {
        const div = document.createElement('div');
        div.className = 'notification';
        div.textContent = message;
        document.getElementById('notificationsContainer').appendChild(div);
    }

    document.addEventListener('DOMContentLoaded', function () {
        loadKPIs();
        checkExpirationAndStock();
    });

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