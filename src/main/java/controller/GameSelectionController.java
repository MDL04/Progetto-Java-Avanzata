package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.Optional;

public class GameSelectionController {

    @FXML
    private ComboBox<String> languageComboBox;

    @FXML
    private ComboBox<String> difficultyComboBox;

    @FXML
    private Label resultLabel;

    public void initialize() {
        languageComboBox.getItems().addAll("it", "en");
        difficultyComboBox.getItems().addAll("Facile", "Media", "Difficile");
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
            readingController.initializeReadingPhase(linguaCodice, difficoltà);

            Stage stage = (Stage) languageComboBox.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Fase di Lettura - " + difficoltà);
            stage.show();

        } catch (Exception e) {
            System.err.println("Errore nel caricamento della fase di lettura: " + e.getMessage());
            e.printStackTrace();
            resultLabel.setText("Errore nel caricamento: " + e.getMessage());
        }
    }

    @FXML
    public void handleExitClick() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Conferma uscita");
        alert.setHeaderText("Sei sicuro di voler uscire?");
        alert.setContentText("Scegli un'opzione:");

        ButtonType salva = new ButtonType("Salva e Esci");
        ButtonType esciSenzaSalvare = new ButtonType("Esci senza salvare");
        ButtonType annulla = new ButtonType("Annulla", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(salva, esciSenzaSalvare, annulla);

        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent()){
            if(result.get() == salva){
                //Salvo nel database. Per farlo si può creare un metodo in gamesessionDAO. Devo quittare. -Franzab
            }
        }
    }

}