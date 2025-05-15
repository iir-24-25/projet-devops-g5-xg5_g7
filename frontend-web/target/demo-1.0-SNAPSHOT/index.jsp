<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="fr">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Connexion - Pharmacie</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
</head>
<body>
<div class="container">
  <!-- Sidebar -->
  <div class="sidebar">
    <div class="logo">
      <img src="${pageContext.request.contextPath}/assets/img/logo.jpg" alt="Logo">
    </div>
  </div>

  <!-- Auth Section -->
  <div class="auth-section">
    <div class="auth-box auth-box-signin">
      <h2>Connexion</h2>
      <form id="signin-form" class="auth-form">
        <div class="form-group">
          <label for="signin-email">Email :</label>
          <input type="text" id="signin-email" name="email" required>
        </div>
        <div class="form-group">
          <label for="signin-password">Mot de passe :</label>
          <input type="password" id="signin-password" name="password" required>
        </div>
        <button type="submit" class="btn btn-primary">Se connecter</button>
        <div id="signin-error" class="error-message"></div>
      </form>
      <p>Pas encore inscrit ? <a href="register.jsp">Créer un compte</a></p>
    </div>
  </div>
</div>

<script src="${pageContext.request.contextPath}/assets/js/auth.js"></script>
</body>
</html>