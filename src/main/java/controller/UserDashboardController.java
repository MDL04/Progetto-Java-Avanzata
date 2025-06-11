package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import model.User;

import java.io.IOException;

public class UserDashboardController {

    private User currentUser;

    @FXML
    private Label userLabel;

    @FXML
    private Button logoutButton;

    private void initialize() {
        userLabel.setText("Ciao, " + currentUser.getUsername() + "!");
    }

    @FXML
    private void handleGioca(ActionEvent event) {

    }

    @FXML
    private void handleModifica(ActionEvent event) {

    }

    @FXML
    private void goToLeaderboard(ActionEvent event) {}

    @FXML
    private void handleLogout(ActionEvent event) {
        try {
            Stage currentStage = (Stage) logoutButton.getScene().getWindow();
            currentStage.close();

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

    public void setUser(User user) {
        this.currentUser = user;
        initialize();
    }

}
