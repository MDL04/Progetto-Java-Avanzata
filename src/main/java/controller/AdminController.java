package controller;


import dao.DocumentDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Document;
import utils.WDMManager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;




public class AdminController {
    @FXML
    private ListView<String> documentList;

    @FXML
    private ListView<String> stopwordList;

    @FXML
    private Label messageLabel;

    @FXML
    private ComboBox<String> LanguageComboBox;

    private String language;

    @FXML
    private void initialize() {
        try {
            List<Document> allDocs = new ArrayList<>();
            allDocs.addAll(DocumentDAO.selectAllFromItalian());
            allDocs.addAll(DocumentDAO.selectAllFromEnglish());

            for (Document doc : allDocs) {
                documentList.getItems().add(doc.getTitle());
            }

        } catch (SQLException e) {
            e.printStackTrace();
            messageLabel.setText("Errore nel caricamento dei documenti.");
        }

        LanguageComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            language = newValue;
        });

    }

    @FXML
    private void handleUploadDocument() {
        if (language == null || language.isEmpty()) {
            showAlert("Errore", "Seleziona prima una lingua per il documento.");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleziona il documento da caricare");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.txt")
        );
        File file = fileChooser.showOpenDialog(getStage());

        if (file != null) {
            try {
                String content = Files.readString(file.toPath());
                String title = file.getName();
                Document doc = new Document(0, language, content, title);

                if ("Italiano".equals(language)) {
                    DocumentDAO.insertITDocument(doc);
                } else if ("Inglese".equals(language)) {
                    DocumentDAO.insertENDocument(doc);
                }

                documentList.getItems().add(title);
                showAlert("Successo", "Documento caricato correttamente.");

            } catch (IOException | SQLException e) {
                e.printStackTrace();
                showAlert("Errore", "Errore durante il caricamento del documento.");
            }
        }
    }





    @FXML
    public void handleLoadITStopword() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleziona un file di stopwords");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("File di Testo", "*.txt"));

        File file = fileChooser.showOpenDialog(getStage());
        if (file != null) {
            try {
                List<String> words = Files.readAllLines(file.toPath());
                stopwordList.getItems().addAll(words);
                messageLabel.setText("Stopwords caricate da: " + file.getName());
                //WDMManager.delete();
            } catch (IOException e) {
                messageLabel.setText("Errore nella lettura del file di stopwords.");
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void handleLoadENStopword() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleziona un file di stopwords");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("File di Testo", "*.txt"));

        File file = fileChooser.showOpenDialog(getStage());
        if (file != null) {
            try {
                List<String> words = Files.readAllLines(file.toPath());
                stopwordList.getItems().addAll(words);
                messageLabel.setText("Stopwords caricate da: " + file.getName());
                //WDMManager.delete();
            } catch (IOException e) {
                messageLabel.setText("Errore nella lettura del file di stopwords.");
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void handleRemoveSelectedDocument() {
        String selectedLanguage = LanguageComboBox.getSelectionModel().getSelectedItem();
        if (selectedLanguage == null) {
            showAlert("Errore", "Seleziona una lingua prima di eliminare un documento.");
            return;
        }
        String tableName = selectedLanguage.equals("Italiano") ? "it_documents" : "en_documents";
        String langCode = selectedLanguage.equals("Italiano") ? "it" : "en";
        String selected = documentList.getSelectionModel().getSelectedItem();

        if (selected!=null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Conferma");
            alert.setHeaderText(null);
            alert.setContentText("Vuoi davvero eliminare il documento \"" + selected + "\"?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                Optional<Document> optionalDocument = DocumentDAO.selectDocumentByTitle(tableName, langCode, selected);
                if (optionalDocument.isPresent()) {
                    Document document = optionalDocument.get();
                    DocumentDAO.deleteDocument(document);
                    documentList.getItems().remove(selected);
                    messageLabel.setText("Documento " + selected + " rimosso.");
                }else{
                    messageLabel.setText("Documento " + selected + " non trovato nel database.");
                }
                WDMManager.delete();
            }
        }
    }

    @FXML
    public void handleRemoveSelectedStopword() {
        String selected = stopwordList.getSelectionModel().getSelectedItem();
        if (selected != null) {
            stopwordList.getItems().remove(selected);
            messageLabel.setText("Stopword rimossa.");
            WDMManager.delete();
        }
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        try {
            Stage currentStage = (Stage) documentList.getScene().getWindow();
            currentStage.close();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/homepage.fxml"));
            Parent root = loader.load();
            Stage homeStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            homeStage.setScene(new Scene(root));
            homeStage.setTitle("Homepage");
            homeStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Stage getStage() {
        return (Stage) messageLabel.getScene().getWindow();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}