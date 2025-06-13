package controller;

import dao.DocumentDAO;
import dao.StopwordDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Document;
import model.Stopword;
import utils.WDMManager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.ArrayList;
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
        List<Document> allDocs = new ArrayList<>();
        allDocs.addAll(DocumentDAO.selectAllDocuments());
        List<Stopword> allSW = new ArrayList<>();
        allSW.addAll(StopwordDAO.selectAllStopwords());

        for (Document doc : allDocs)
            documentList.getItems().add(doc.getTitle());

        for (Stopword sw : allSW)
            stopwordList.getItems().add(sw.getTitle());

        // ⚠️ Inizializza WDM appena avvii la schermata
        WDMManager.getInstance();
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
                String content = Files.readString(file.toPath());
                String title = file.getName();
                Document doc = new Document(0, language, content, title);
                DocumentDAO.insertDocument(doc);
                documentList.getItems().add(title);

                // ✅ Ricarica WDM
                WDMManager.delete();
                WDMManager.getInstance();

                showInfoAlert("Successo", "Documento caricato correttamente.");
            } catch (IOException | SQLException e) {
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
                String words = Files.readString(file.toPath());
                String title = file.getName();
                Stopword stopword = new Stopword(language, words, 0, title);
                StopwordDAO.insertStopword(stopword);
                stopwordList.getItems().add(title);

                // ✅ Ricarica WDM
                WDMManager.delete();
                WDMManager.getInstance();
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
                Optional<Document> optionalDocument = DocumentDAO.selectDocumentByTitle(selected);
                if (optionalDocument.isPresent()) {
                    DocumentDAO.deleteDocument(optionalDocument.get());
                    documentList.getItems().remove(selected);

                    // ✅ Ricarica WDM
                    WDMManager.delete();
                    WDMManager.getInstance();
                } else {
                    showInfoAlert("Errore", "Documento \"" + selected + "\" non trovato nel database.");
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
                Optional<Stopword> optionalStopword = StopwordDAO.selectSWByTitle(selected);
                if (optionalStopword.isPresent()) {
                    StopwordDAO.deleteStopword(optionalStopword.get());
                    stopwordList.getItems().remove(selected);

                    // ✅ Ricarica WDM
                    WDMManager.delete();
                    WDMManager.getInstance();
                } else {
                    showInfoAlert("Errore", "Stopwords \"" + selected + "\" non trovato nel database.");
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
