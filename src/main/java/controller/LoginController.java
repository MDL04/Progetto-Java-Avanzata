package controller;

import dao.UserDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.User;
import utils.PasswordUtils;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;


import java.io.IOException;
import java.util.Optional;

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

        if(userDAO.selectByUsername(username).isEmpty()){
            messageLbl.setText("Username non trovato!");
            messageLbl.setStyle("-fx-text-fill: red;");
            return;
        }

        if(userDAO.checkLogin(username, hashed)){
            messageLbl.setText("Login riuscito: " + username + "!");
            messageLbl.setStyle("-fx-text-fill: green;");
            Optional<User> userOpt = userDAO.selectByUsername(username);
            if(userOpt.isPresent()) {
                User user = userOpt.get();
                if(!user.isAdmin()) {
                    goToUserDashboard();
                } else {
                    goToAdminDashboard();
                }
            }
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
    private void goToHome(ActionEvent event) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/homepage.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Homepage");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToUserDashboard() {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/userdashboard.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) usernameFld.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Area utente");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToAdminDashboard() {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/admindashboard.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) usernameFld.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Admin Dashboard");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
