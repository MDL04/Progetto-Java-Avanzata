package controller;

import dao.UserDAO;
import javafx.animation.PauseTransition;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Duration;
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

    @FXML
    private Button loginBtn;

    private final UserDAO userDAO = new UserDAO();

    @FXML
    private void initialize() {
        loginBtn.disableProperty().bind(
                Bindings.createBooleanBinding(
                        () -> usernameFld.getText().isBlank() || passwordFld.getText().isBlank(),
                        usernameFld.textProperty(), passwordFld.textProperty()
                )
        );
        ChangeListener<String> clearMsg = (obs, o, n) -> {
            messageLbl.setText("");
            messageLbl.setStyle("");
        };
        usernameFld.textProperty().addListener(clearMsg);
        passwordFld.textProperty().addListener(clearMsg);
    }

    @FXML
    private void handleLogin() {
        String username = usernameFld.getText();
        String password = passwordFld.getText();
        String hashed   = PasswordUtils.hashPassword(password);

        Optional<User> userOpt = userDAO.selectByUsername(username);
        if (userOpt.isEmpty()) {
            showError("Username non trovato!");
            return;
        }

        if (!userDAO.checkLogin(username, hashed)) {
            showError("Credenziali non valide");
            return;
        }

        showInfo("Login effettuato con successo: " + username + "!");

        User user = userOpt.get();
        PauseTransition navigationDelay = new PauseTransition(Duration.seconds(2.2));
        navigationDelay.setOnFinished(e -> {
            if (!user.isAdmin()) goToUserDashboard();
            else goToAdminDashboard();
        });
        navigationDelay.play();
    }

    private void showError(String msg) {
        messageLbl.setText(msg);
        messageLbl.setStyle("-fx-text-fill: red;");
    }

    private void showInfo(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.show();
        PauseTransition delay = new PauseTransition(Duration.seconds(2));
        delay.setOnFinished(e -> a.close());
        delay.play();
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
