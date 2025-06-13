package controller;

import dao.UserDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.User;
import utils.PasswordUtils;

import java.io.IOException;

/**
 * Controller per la schermata di registrazione dell'utente. Gestisce la registrazione di un nuovo utente,
 * la validazione dei campi di input e la navigazione tra le schermate.
 */


public class RegisterController {

    @FXML
    private TextField usernameFld, emailFld;

    @FXML
    private PasswordField passwordFld, confirmPasswordFld;

    @FXML
    private Label messageLbl;

    private final UserDAO userDAO = new UserDAO();

    /**
     * Gestisce la registrazione di un nuovo utente e verifica
     * che tutti i dati inseriti siano validi
     */
    @FXML
    private void handleRegister() {
        String username = usernameFld.getText();
        String email = emailFld.getText();
        String password = passwordFld.getText();
        String confirmPassword = confirmPasswordFld.getText();

        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showError("Completa tutti i campi!");
            return;
        }

        if (!email.contains("@") || !email.substring(email.indexOf("@")).contains(".")) {
            showError("Email non valida: dominio non riconosciuto!");
            return;
        }

        if(password.length() < 7){
            showError("La password deve contenere almeno 7 caratteri!");
            return;
        }

        if(!password.equals(confirmPassword)) {
            showError("Le password non coincidono!");
            return;
        }

        try {
            if (userDAO.selectByUsername(username).isPresent()) {
                showError("Username non disponibile!");
                return;
            }

            if (userDAO.selectByEmail(email).isPresent()) {
                showError("Email non disponibile! Potresti già avere un account.");
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        String hashedPassword = PasswordUtils.hashPassword(password);

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(hashedPassword);
        user.setAdmin(false);
        user.setUrlAvatar("/images/avatar/default_avatar.png");

        if(userDAO.insert(user)){
            showSuccess("Registrazione completata!");
            goToLogin();
        }else{
            showError("Errore durante la registrazione!");
        }
    }

    /**
     * Permette di andare alla sezione Login
     */
    @FXML
    void goToLogin(){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) usernameFld.getScene().getWindow();
            stage.setScene(new Scene(root));

            stage.setMinWidth(600);
            stage.setMinHeight(400);

            stage.setTitle("Login");
            stage.show();
        } catch (IOException e) {
                throw new RuntimeException(e);
        }
    }

    /**
     * Permette di andare alla sezione home
     * @param event
     */
    @FXML
    private void goToHome(ActionEvent event) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/homepage.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));

            stage.setMinWidth(600);
            stage.setMinHeight(400);

            stage.setTitle("Homepage");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gestisce la visualizzazione di errori
     * @param message
     */
    private void showError(String message) {
        messageLbl.setText(message);
        messageLbl.setStyle("-fx-text-fill: red;");
    }

    /**
     * Gestisce la visualizzazione di successo
     * @param message
     */
    private void showSuccess(String message) {
        messageLbl.setText(message);
        messageLbl.setStyle("-fx-text-fill: green;");
    }

}
