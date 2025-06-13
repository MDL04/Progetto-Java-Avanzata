package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HomepageController {

    /**
     * Permette di andare alla sezione Login
     * @param event
     */
    @FXML
    private void goToLogin(ActionEvent event) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

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
     * Permette di andare alla sezione Leaderboard
     * @param event
     */
    @FXML
    private void goToLeaderboard(ActionEvent event) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/leaderboard.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            stage.setScene(new Scene(root));

            stage.setMinWidth(600);
            stage.setMinHeight(400);

            stage.setTitle("Classifica");
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Permette di andare alla sezione Info
     * @param event
     */
    @FXML
    private void goToInfo(ActionEvent event) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/info.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            stage.setScene(new Scene(root));

            stage.setMinWidth(600);
            stage.setMinHeight(400);

            stage.setTitle("Info");
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
