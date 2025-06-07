package controller;

import dao.UserDAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import utils.PasswordUtils;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


import java.io.IOException;

public class LoginController {

    @FXML
    private TextField usernameFld;

    @FXML
    private PasswordField passwordFld;

    @FXML
    private Label messageLbl;

    private final UserDAO userDAO = new UserDAO();

    @FXML
    private void handleLogin() {
        String username = usernameFld.getText();
        String password = passwordFld.getText();
        String hashed = PasswordUtils.hashPassword(password);

        if(userDAO.checkLogin(username, hashed)){
            messageLbl.setText("Login riuscito: " + username + "!");
            messageLbl.setStyle("-fx-text-fill: green;");
        } else {
            messageLbl.setText("Credenziali errate!");
            messageLbl.setStyle("-fx-text-fill: red;");
        }
    }

    @FXML
    private void goToRegister() {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/register.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) usernameFld.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Registrazione");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToHome() {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/homepage.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) usernameFld.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Homepage");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
