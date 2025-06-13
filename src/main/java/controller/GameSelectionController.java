package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.User;

import java.util.Optional;

public class GameSelectionController {

    @FXML
    private ComboBox<String> languageComboBox;

    @FXML
    private ComboBox<String> difficultyComboBox;

    @FXML
    private VBox vBox;

    @FXML
    private Label resultLabel;

    private User currentUser;

    @FXML
    public void initialize() {
        languageComboBox.getItems().addAll("it", "en");
        difficultyComboBox.getItems().addAll("Facile", "Media", "Difficile");

        // Listener per quando la scena è disponibile
        vBox.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.windowProperty().addListener((obsWin, oldWin, newWin) -> {
                    if (newWin != null) {
                        Stage stage = (Stage) newWin;
                        stage.setOnCloseRequest(event -> {
                            event.consume();
                            handleExitClick();
                        });
                    }
                });
            }
        });
    }

    @FXML
    public void handleStartQuiz(){
        String linguaCodice = languageComboBox.getValue();
        String difficoltà = difficultyComboBox.getValue();

        if (linguaCodice == null || difficoltà == null) {
            resultLabel.setText("Seleziona lingua e difficoltà");
            return;
        }

        try {
            // ✅ VAI ALLA FASE DI LETTURA
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/reading_phase.fxml"));
            Parent root = loader.load();

            ReadingPhaseController readingController = loader.getController();
            readingController.setUser(currentUser);
            readingController.initialize(linguaCodice, difficoltà);

            Stage stage = (Stage) languageComboBox.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Fase di Lettura - " + difficoltà);
            stage.show();

        } catch (Exception e) {
            System.err.println("Errore nel caricamento della fase di lettura: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void handleExitClick() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Conferma uscita");
        alert.setHeaderText("Sei sicuro di voler uscire?");
        alert.setContentText("Se esci non potrai riprendere la tua partita.");

        ButtonType esciSenzaSalvare = new ButtonType("Esci senza salvare");
        ButtonType annulla = new ButtonType("Annulla", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(esciSenzaSalvare, annulla);

        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent()){
            if (result.get() == esciSenzaSalvare) {
                // Chiudi l'applicazione senza salvare
                Stage stage = (Stage) languageComboBox.getScene().getWindow();
                stage.close();
            } else {
                // Annulla l'uscita
                alert.close();
            }
        }
    }

    public void setUser(User currentUser) {
        this.currentUser = currentUser;
    }

}