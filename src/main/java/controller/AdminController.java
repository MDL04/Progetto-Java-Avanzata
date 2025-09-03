package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Document;
import utils.FileManager;
import utils.WDMManager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Gestisce le view relative alle funzionalità disponibili
 * all'user admin
 */

public class AdminController {
    @FXML
    private ListView<String> documentList;

    @FXML
    private ListView<String> stopwordList;

    /**
     * Inizializza la view e mostra i documenti già presenti nel database (se presenti)
     */
    @FXML
    private void initialize() {
        documentList.getItems().clear();
        stopwordList.getItems().clear();
        try {
            // Carica documenti IT
            for (Document doc : FileManager.caricaDocumenti("it")) {
                documentList.getItems().add(doc.getTitle());
            }
            // Carica documenti EN
            for (Document doc : FileManager.caricaDocumenti("en")) {
                documentList.getItems().add(doc.getTitle());
            }
            // Carica stopwords IT
            for (String sw : FileManager.caricaStopwords("it")) {
                stopwordList.getItems().add(sw);
            }
            // Carica stopwords EN
            for (String sw : FileManager.caricaStopwords("en")) {
                stopwordList.getItems().add(sw);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Permette di caricare del database un documento nelle lingue disponibili
     */
    @FXML
    private void handleUploadDocument() {
        List<String> choices = Arrays.asList("Italiano", "Inglese");
        ChoiceDialog<String> dialog = new ChoiceDialog<>("Italiano", choices);
        dialog.setTitle("Seleziona lingua");
        dialog.setHeaderText("Scegli la lingua del documento");
        dialog.setContentText("Lingua:");

        Optional<String> result = dialog.showAndWait();
        if (result.isEmpty()) {
            return;
        }
        String selectedLang = result.get();
        String language = selectedLang.equals("Italiano") ? "it" : "en";

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleziona il documento da caricare");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.txt")
        );
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            try {
                Path targetDir = Path.of("src/main/resources/documents/" + language);
                Files.createDirectories(targetDir);
                Path targetFile = targetDir.resolve(file.getName());
                Files.copy(file.toPath(), targetFile, StandardCopyOption.REPLACE_EXISTING);

                documentList.getItems().add(file.getName());

                WDMManager.delete();
                WDMManager.getInstance();

                showInfoAlert("Successo", "Documento caricato correttamente.");
            } catch (IOException e) {
                e.printStackTrace();
                showInfoAlert("Errore", "Errore durante il caricamento del documento.");
            }
        }
    }

    /**
     * Permette di caricare del database un documento di stopowords nelle lingue disponibili
     */
    @FXML
    public void handleLoadStopwords() {
        List<String> choices = Arrays.asList("Italiano", "Inglese");
        ChoiceDialog<String> dialog = new ChoiceDialog<>("Italiano", choices);
        dialog.setTitle("Seleziona lingua");
        dialog.setHeaderText("Scegli la lingua delle stopwords");
        dialog.setContentText("Lingua:");

        Optional<String> result = dialog.showAndWait();
        if (result.isEmpty()) {
            return;
        }
        String selectedLang = result.get();
        String language = selectedLang.equals("Italiano") ? "it" : "en";

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleziona un file di stopwords");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("File di Testo", "*.txt"));

        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            try {
                Path targetDir = Path.of("src/main/resources/stopwords/" + language);
                Files.createDirectories(targetDir);
                Path targetFile = targetDir.resolve(file.getName());
                Files.copy(file.toPath(), targetFile, StandardCopyOption.REPLACE_EXISTING);

                stopwordList.getItems().add(file.getName());

                WDMManager.delete();
                WDMManager.getInstance();

                showInfoAlert("Successo", "Stopwords caricate correttamente.");
            } catch (IOException e) {
                e.printStackTrace();
                showInfoAlert("Errore", "Errore durante il caricamento delle stopwords.");
            }
        }
    }

    /**
     * Permette di eliminare dal database il documento selezionato
     */
    @FXML
    public void handleRemoveSelectedDocument() {
        String selected = documentList.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Conferma");
            alert.setContentText("Vuoi davvero eliminare il documento \"" + selected + "\"?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    String language = null;
                    Path filePath = Path.of("src/main/resources/documents/it", selected);
                    if (Files.exists(filePath)) {
                        language = "it";
                    } else {
                        filePath = Path.of("src/main/resources/documents/en", selected);
                        if (Files.exists(filePath)) {
                            language = "en";
                        }
                    }
                    if (language == null) {
                        showInfoAlert("Errore", "Documento non trovato.");
                        return;
                    }

                    Files.deleteIfExists(filePath);
                    documentList.getItems().remove(selected);

                    WDMManager.delete();
                    WDMManager.getInstance();

                    showInfoAlert("Successo", "Documento eliminato correttamente.");
                } catch (IOException e) {
                    e.printStackTrace();
                    showInfoAlert("Errore", "Errore durante l'eliminazione del documento.");
                }
            }
        }
    }


    /**
     * Permette di eliminare dal database il documento di stopwords selezionato
     */
    @FXML
    public void handleRemoveSelectedStopword() {
        String selected = stopwordList.getSelectionModel().getSelectedItem();
        if (selected != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Conferma");
            alert.setContentText("Vuoi davvero eliminare le stopwords \"" + selected + "\"?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    String language = null;
                    Path filePath = Path.of("src/main/resources/stopwords/it", selected);
                    if (Files.exists(filePath)) {
                        language = "it";
                    } else {
                        filePath = Path.of("src/main/resources/stopwords/en", selected);
                        if (Files.exists(filePath)) {
                            language = "en";
                        }
                    }
                    if (language == null) {
                        showInfoAlert("Errore", "File di stopwords non trovato.");
                        return;
                    }

                    Files.deleteIfExists(filePath);
                    stopwordList.getItems().remove(selected);

                    WDMManager.delete();
                    WDMManager.getInstance();

                    showInfoAlert("Successo", "Stopwords eliminate correttamente.");
                } catch (IOException e) {
                    e.printStackTrace();
                    showInfoAlert("Errore", "Errore durante l'eliminazione delle stopwords.");
                }
            }
        }
    }

    /**
     * Permette di tornare alla sezione hompepage
     * @param event
     */
    @FXML
    private void handleLogout(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Conferma Logout");
        alert.setHeaderText("Stai per effettuare il logout");
        alert.setContentText("Sei sicuro di voler uscire? I dati non salvati andranno persi.");

        ButtonType confirmButton = new ButtonType("Conferma", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Annulla", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(confirmButton, cancelButton);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == confirmButton) {
            try {
                Stage currentStage = (Stage) documentList.getScene().getWindow();
                currentStage.close();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/homepage.fxml"));
                Parent root = loader.load();
                Stage homeStage = new Stage();

                homeStage.setScene(new Scene(root));

                homeStage.setMinHeight(400);
                homeStage.setMinWidth(600);

                homeStage.setTitle("Homepage");
                homeStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Si occupa di mostrare messaggi informativi
     *
     * @param title
     * @param message
     */
    private void showInfoAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
