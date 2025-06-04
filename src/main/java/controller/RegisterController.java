package controller;

import actors.User;
import dao.UserDAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
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

        if(!password.equals(confirmPassword)) {
            messageLbl.setText("Le password non coincindono!");
            messageLbl.setStyle("-fx-text-fill: red;");
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
            messageLbl.setText("Registrazione completata!");
            messageLbl.setStyle("-fx-text-fill: green;");
        }else{
            messageLbl.setText("Errore nella registrazione!");
            messageLbl.setStyle("-fx-text-fill: red;");
        }
    }

    @FXML
    private void goToLogin(){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) usernameFld.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Login");
            stage.show();
        } catch (IOException e) {
                throw new RuntimeException(e);
        }
    }
}
