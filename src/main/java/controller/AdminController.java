package controller;


import dao.DocumentDAO;
import javafx.fxml.FXML;
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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;




public class AdminController {
    @FXML
    private ListView<String> documentList;

    @FXML
    private ListView<String> stopwordList;

    @FXML
    private void initialize() {
        List<Document> allDocs = new ArrayList<>();
        allDocs.addAll(DocumentDAO.selectAll());

        for (Document doc : allDocs) {
            documentList.getItems().add(doc.getTitle());
        }

    }

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
                showAlert("Successo", "Documento caricato correttamente.");

            } catch (IOException | SQLException e) {
                e.printStackTrace();
                showAlert("Errore", "Errore durante il caricamento del documento.");
            }
        }
    }


    @FXML
    public void handleLoadStopword() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleziona un file di stopwords");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("File di Testo", "*.txt"));

        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            try {
                List<String> words = Files.readAllLines(file.toPath());
                stopwordList.getItems().addAll(words);
                //WDMManager.delete();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    @FXML
    public void handleRemoveSelectedDocument() {
        String selected = documentList.getSelectionModel().getSelectedItem();

        if (selected!=null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Conferma");
            alert.setHeaderText(null);
            alert.setContentText("Vuoi davvero eliminare il documento \"" + selected + "\"?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                Optional<Document> optionalDocument = DocumentDAO.selectDocumentByTitle(selected);
                if (optionalDocument.isPresent()) {
                    Document document = optionalDocument.get();
                    DocumentDAO.deleteDocument(document);
                    documentList.getItems().remove(selected);
                }else{
                    showAlert("Errore", "Documento " + selected + " non trovato nel database.");
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
            WDMManager.delete();
        }
    }


    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}