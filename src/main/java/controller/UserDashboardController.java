package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.stage.Stage;
import model.User;
import utils.WDMManager;
import javafx.scene.paint.Color;


import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

public class UserDashboardController {

    private User currentUser;

    @FXML
    private Label userLabel;

    @FXML
    private Button logoutButton;

    @FXML
    private ImageView userAvatarImageView;

    @FXML private Label bestEasyLabel;
    @FXML private Label bestMediumLabel;
    @FXML private Label bestHardLabel;
    @FXML private Label gamesEasyLabel;
    @FXML private Label gamesMediumLabel;
    @FXML private Label gamesHardLabel;
    @FXML private Label avgEasyLabel;
    @FXML private Label avgMediumLabel;
    @FXML private Label avgHardLabel;


    @FXML
    private void initialize() {
        WDMManager.getInstance();
    }

    @FXML
    private void handleGioca(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/game_selection.fxml"));
            Parent root = loader.load();

            GameSelectionController gameSelectionController = loader.getController();
            gameSelectionController.setUser(currentUser);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Gioco");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleModifica(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/modify.fxml"));
            Parent root = loader.load();

            ModifyUserController modifyController = loader.getController();
            modifyController.setUser(currentUser);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            stage.setScene(new Scene(root));

            stage.setMinHeight(400);
            stage.setMinWidth(600);

            stage.setTitle("Modifica dati personali");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToLeaderboard(ActionEvent event) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/leaderboard.fxml"));
            Parent root = loader.load();
            LeaderboardController leaderboardController = loader.getController();
            leaderboardController.isLoggedin = true;
            leaderboardController.setUser(currentUser);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            stage.setScene(new Scene(root));

            stage.setMinWidth(600);
            stage.setMinHeight(400);

            stage.setTitle("Classifica");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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

                stage.setMinWidth(600);
                stage.setMinHeight(400);

                stage.setTitle("Homepage");
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    public void setUser(User user) {
        this.currentUser = user;
        if (currentUser != null) {
            userLabel.setText("Ciao, " + currentUser.getUsername() + "!");
            loadUserAvatar();

            LinearGradient gradient = new LinearGradient(0, 0, 1, 0, true, null,
                    new Stop(0, Color.web("#0077cc")),  // Blu
                    new Stop(1, Color.web("#f6a500"))); // Arancione

            // Applicazione del gradiente al testo della label
            userLabel.setTextFill(gradient);

            // Miglior punteggio
            bestEasyLabel.setText(String.valueOf(user.getBestScoreEasy()));
            bestMediumLabel.setText(String.valueOf(user.getBestScoreMedium()));
            bestHardLabel.setText(String.valueOf(user.getBestScoreHard()));

            // Partite giocate
            gamesEasyLabel.setText(String.valueOf(user.getPartiteEasy()));
            gamesMediumLabel.setText(String.valueOf(user.getPartiteMedium()));
            gamesHardLabel.setText(String.valueOf(user.getPartiteHard()));

            // Punteggio medio
            avgEasyLabel.setText(user.getPartiteEasy() > 0 ? String.valueOf(user.getScoreEasy() / user.getPartiteEasy()) : "0");
            avgMediumLabel.setText(user.getPartiteMedium() > 0 ? String.valueOf(user.getScoreMedium() / user.getPartiteMedium()) : "0");
            avgHardLabel.setText(user.getPartiteHard() > 0 ? String.valueOf(user.getScoreHard() / user.getPartiteHard()) : "0");
        }
    }

    private void loadUserAvatar() {
        if (currentUser != null && currentUser.getUrlAvatar() != null) {
            String avatarPath = currentUser.getUrlAvatar();

            try {
                // Prova a caricare l'avatar dell'utente
                InputStream imageStream = getClass().getResourceAsStream(avatarPath);
                if (imageStream != null) {
                    Image userImage = new Image(imageStream);
                    userAvatarImageView.setImage(userImage);
                } else {
                    // Se non trova l'avatar, carica quello di default
                    loadDefaultAvatar();
                }
            } catch (Exception e) {
                System.err.println("Errore nel caricare l'avatar utente: " + avatarPath);
                loadDefaultAvatar();
            }
        } else {
            // Se l'utente non ha un avatar impostato, carica quello di default
            loadDefaultAvatar();
        }
    }

    private void loadDefaultAvatar() {
        try {
            InputStream defaultStream = getClass().getResourceAsStream("/images/avatar/default.png");
            if (defaultStream != null) {
                Image defaultImage = new Image(defaultStream);
                userAvatarImageView.setImage(defaultImage);
            } else {
                System.err.println("Impossibile caricare l'avatar di default");
            }
        } catch (Exception e) {
            System.err.println("Errore nel caricare l'avatar di default: " + e.getMessage());
        }
    }
}