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

/**
 * Questa classe gestisce l'interfaccia utente per la selezione della lingua e della difficoltà del quiz.
 * È responsabile per la configurazione e l'avvio del quiz, nonché per la gestione dell'uscita dall'applicazione.
 */

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

    /**
     * Inizializza la view della selezione delle impostazioni di gioco
     */
    @FXML
    public void initialize() {
        languageComboBox.getItems().addAll("it", "en");
        difficultyComboBox.getItems().addAll("Facile", "Media", "Difficile");

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

    /**
     * Gestisce l'evento di avvio del quiz, configurando la lingua e la difficoltà selezionata dall'utente.
     */
    @FXML
    public void handleStartQuiz(){
        String linguaCodice = languageComboBox.getValue();
        String difficoltà = difficultyComboBox.getValue();

        if (linguaCodice == null || difficoltà == null) {
            resultLabel.setText("Seleziona lingua e difficoltà");
            return;
        }

        try {
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

    /**
     * Controlla la possibilità di uscire dal gioco durante la seleziona delle impostazioni di partita
     */
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

    /**
     * Imposta l'utente corrente che parteciperà al quiz
     *
     * @param currentUser*/
    public void setUser(User currentUser) {
        this.currentUser = currentUser;
    }

}