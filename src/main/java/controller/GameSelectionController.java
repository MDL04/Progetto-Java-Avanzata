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
 * Controller per la selezione del gioco.
 * Gestisce la view game_selection
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
     * Metodo che inizializza la scena.
     * Aggiunge gli elementi nelle ComboBox per la lingua e la difficoltà e setta il listener per la chiusura della finestra.
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
     * Gestisce l'azione per avviare il quiz dopo che l'utente ha selezionato lingua e difficoltà.
     * Se i valori non sono selezionati, mostra un messaggio di errore.
     * Carica la scena della fase di lettura con i parametri selezionati.
     */
    @FXML
    public void handleStartQuiz() {
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
     * Mostra una finestra di conferma quando l'utente tenta di uscire dal gioco.
     * Permette all'utente di scegliere se uscire senza salvare o annullare l'uscita.
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
                Stage stage = (Stage) languageComboBox.getScene().getWindow();
                stage.close();
            } else {
                alert.close();
            }
        }
    }

    /**
     * Imposta l'utente corrente.
     * Questo metodo viene chiamato per assegnare un oggetto User al controller.
     *
     * @param currentUser
     */
    public void setUser(User currentUser) {
        this.currentUser = currentUser;
    }
}
