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
import java.util.Optional;
/**
 * Controller per la schermata di login dell'utente.
 * Gestisce la view login
 */
public class LoginController {

    @FXML
    private TextField usernameFld;

    @FXML
    private PasswordField passwordFld;

    @FXML
    private Label messageLbl;

    private final UserDAO userDAO = new UserDAO();

    /**
     * Metodo per gestire il login
     * Verifica se le credenziali inserite sono corrette.
     * Può portare alla user_dashboard o all'admin_dashboard
     */
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

    /**
     * Consente di passare alla register view
     */
    @FXML
    private void goToRegister() {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/register.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) usernameFld.getScene().getWindow();
            stage.setScene(new Scene(root));

            stage.setMinWidth(600);
            stage.setMinHeight(400);

            stage.setTitle("Registrazione");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Consente di tornare alla homepage
     * @param event
     */
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

    /**
     * Consente di passare alla user_dashboard
     */
    @FXML
    private void goToUserDashboard() {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/user_dashboard.fxml"));
            Parent root = loader.load();
            UserDashboardController userDashboardController = loader.getController();
            Optional<User> userOpt = userDAO.selectByUsername(usernameFld.getText());
            if(userOpt.isPresent()) {
                userDashboardController.setUser(userOpt.get());
            }
            Stage stage = (Stage) usernameFld.getScene().getWindow();
            stage.setScene(new Scene(root));

            stage.setMinWidth(600);
            stage.setMinHeight(400);

            stage.setTitle("Area utente");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Consente di passare all'admin_dashboard
     */
    @FXML
    private void goToAdminDashboard() {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/admin_dashboard.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) usernameFld.getScene().getWindow();
            stage.setScene(new Scene(root));

            stage.setMinWidth(600);
            stage.setMinHeight(400);

            stage.setTitle("Area admin");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
