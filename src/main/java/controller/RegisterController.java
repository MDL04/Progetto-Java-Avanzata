package controller;

import dao.UserDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.User;
import utils.PasswordUtils;

import java.io.IOException;

public class RegisterController {

    @FXML
    private TextField usernameFld, emailFld;

    @FXML
    private PasswordField passwordFld, confirmPasswordFld;

    @FXML
    private Label messageLbl;

    private final UserDAO userDAO = new UserDAO();

    @FXML
    private void handleRegister() {
        String username = usernameFld.getText();
        String email = emailFld.getText();
        String password = passwordFld.getText();
        String confirmPassword = confirmPasswordFld.getText();

        //Controllo campi vuoti
        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showError("Completa tutti i campi!");
            return;
        }

        // Controllo email
        if (!email.contains("@") || !email.substring(email.indexOf("@")).contains(".")) {
            showError("Email non valida: dominio non riconosciuto!");
            return;
        }

        // Controllo password
        if(password.length() < 7){
            showError("La password deve contenere almeno 7 caratteri!");
            return;
        }

        // Controllo che conferma password sia uguale a password
        if(!password.equals(confirmPassword)) {
            showError("Le password non coincidono!");
            return;
        }

        // Se il database è raggiungibile controlla se esista già un utente con quella mail o username
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
        user.setUrlAvatar("default_avatar.png");

        if(userDAO.insert(user)){
            showSuccess("Registrazione completata!");
            goToLogin();
        }else{
            showError("Errore durante la registrazione!");
        }
    }

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

    private void showError(String message) {
        messageLbl.setText(message);
        messageLbl.setStyle("-fx-text-fill: red;");
    }

    private void showSuccess(String message) {
        messageLbl.setText(message);
        messageLbl.setStyle("-fx-text-fill: green;");
    }

}
