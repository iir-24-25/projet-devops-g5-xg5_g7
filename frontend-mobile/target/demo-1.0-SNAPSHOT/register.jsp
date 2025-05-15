<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="fr">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Inscription - Pharmacie</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
</head>
<body>
<div class="container">
  <div class="sidebar">
    <div class="logo">
      <img src="${pageContext.request.contextPath}/assets/img/logo.jpg" alt="Logo">
    </div>
  </div>

  <div class="auth-section">
    <div class="auth-box auth-box-signup">
      <h2>Inscription</h2>
      <form id="signup-form" class="auth-form">
        <div class="form-group">
          <label for="signup-firstName">Prénom :</label>
          <input type="text" id="signup-firstName" name="firstName" required>
        </div>
        <div class="form-group">
          <label for="signup-lastName">Nom :</label>
          <input type="text" id="signup-lastName" name="lastName" required>
        </div>
        <div class="form-group">
          <label for="signup-email">Email :</label>
          <input type="email" id="signup-email" name="email" required>
        </div>
        <div class="form-group">
          <label for="signup-telephone">Numéro de téléphone :</label>
          <input type="text" id="signup-telephone" name="telephone" required>
        </div>
        <div class="form-group">
          <label for="signup-adresse">Adresse :</label>
          <input type="text" id="signup-adresse" name="adresse" required>
        </div>
        <div class="form-group">
          <label for="signup-password">Mot de passe :</label>
          <input type="password" id="signup-password" name="password" required>
        </div>
        <div class="form-group">
          <label for="signup-confirmPassword">Confirmation :</label>
          <input type="password" id="signup-confirmPassword" name="confirmPassword" required>
        </div>
        <button type="submit" class="btn btn-primary">S'inscrire</button>
        <div id="register-error" class="error-message"></div>
      </form>
      <p>Déjà inscrit ? <a href="index.jsp">Se connecter</a></p>
    </div>
  </div>
</div>

<script src="${pageContext.request.contextPath}/assets/js/auth.js"></script>
</body>
</html>