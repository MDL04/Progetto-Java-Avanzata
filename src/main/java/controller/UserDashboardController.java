package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import model.User;

public class UserDashboardController {

    private User currentUser;

    @FXML
    private Label userLabel;

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

    public void setUser(User user) {
        this.currentUser = user;
        initialize();
    }

}
