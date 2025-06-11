package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.User;

import java.io.IOException;
import java.util.Optional;

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
        // Mostra dialogo di conferma
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Conferma Logout");
        alert.setHeaderText("Stai per effettuare il logout");
        alert.setContentText("Sei sicuro di voler uscire? I dati non salvati andranno persi.");

        // Personalizza i pulsanti
        ButtonType confirmButton = new ButtonType("Conferma", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Annulla", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(confirmButton, cancelButton);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == confirmButton) {
            try {
                Stage currentStage = (Stage) logoutButton.getScene().getWindow();
                currentStage.close();

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/homepage.fxml"));
                Parent root = loader.load();
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Homepage");
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setUser(User user) {
        this.currentUser = user;
        initialize();
    }

}
