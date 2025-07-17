package controller;

import dao.UserDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import model.User;
import utils.PasswordUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Controller per la schermata di modifica dell'utente.
 * Questa classe gestisce la modifica dei dati dell'utente, inclusi nome utente, email, avatar e password
 */


public class ModifyUserController {

    private User currentUser;
    private UserDAO userDAO;
    private List<String> availableAvatars;

    @FXML
    private TextField usernameField;

    @FXML
    private TextField emailField;

    @FXML
    private ComboBox<String> avatarComboBox;

    @FXML
    private ImageView avatarPreview;

    @FXML
    private PasswordField currentPasswordField;

    @FXML
    private PasswordField newPasswordField;

    @FXML
    private PasswordField confirmPasswordField;

    /**
     *  Costruisce un'istanza di ModifyUserController
     */
    public ModifyUserController() {
        this.userDAO = new UserDAO();
        this.availableAvatars = new ArrayList<>();
    }

    /**
     * Inizializza la vista, caricando gli avatar disponibili
     */
    @FXML
    private void initialize() {
        loadAvailableAvatars();
        setupAvatarComboBox();
    }

    /**
     * Popola la combobox di della lista degli avatar disponibili predefiniti
     */
    private void loadAvailableAvatars() {
        availableAvatars.add("default_avatar.png");
        availableAvatars.add("avatar1.png");
        availableAvatars.add("avatar2.png");
        availableAvatars.add("avatar3.png");
        availableAvatars.add("avatar4.png");
        availableAvatars.add("avatar5.png");

        avatarComboBox.getItems().clear();
        for (String avatar : availableAvatars) {
            avatarComboBox.getItems().add(avatar.replace(".png", ""));
        }
    }

    /**
     * Aggiorna l'anteprima dell'avatar quando viene selezionato
     */
    private void setupAvatarComboBox() {
        avatarComboBox.setOnAction(e -> updateAvatarPreview());
    }

    /**
     * Aggiorna l'anteprima dell'avatar quando viene selezionato
     */
    private void updateAvatarPreview() {
        String selected = avatarComboBox.getValue();
        if (selected != null) {
            String imagePath = "/images/avatar/" + selected + ".png";
            try {
                InputStream imageStream = getClass().getResourceAsStream(imagePath);
                if (imageStream != null) {
                    Image image = new Image(imageStream);
                    avatarPreview.setImage(image);
                } else {
                    // Carica immagine di default se non trova l'avatar
                    loadDefaultAvatar();
                }
            } catch (Exception e) {
                System.err.println("Errore nel caricare l'avatar: " + imagePath);
                loadDefaultAvatar();
            }
        }
    }

    /**
     * Imposta l'avatar di default
     */
    private void loadDefaultAvatar() {
        try {
            InputStream defaultStream = getClass().getResourceAsStream("/images/avatar/default.png");
            if (defaultStream != null) {
                Image defaultImage = new Image(defaultStream);
                avatarPreview.setImage(defaultImage);
            }
        } catch (Exception e) {
            System.err.println("Errore nel caricare l'avatar di default");
        }
    }

    public void setUser(User user) {
        this.currentUser = user;
        loadUserData();
    }

    /**
     * Setta i dati dell'utente nella vista
     */
    private void loadUserData() {
        if (currentUser != null) {
            usernameField.setText(currentUser.getUsername());
            emailField.setText(currentUser.getEmail());
            String currentAvatar = currentUser.getUrlAvatar();
            if (currentAvatar != null && !currentAvatar.isEmpty()) {
                // Estrai il nome del file dall'URL
                String avatarName = currentAvatar.substring(currentAvatar.lastIndexOf("/") + 1);
                avatarName = avatarName.replace(".png", "");
                avatarComboBox.setValue(avatarName);
            } else {
                avatarComboBox.setValue("default");
            }

            updateAvatarPreview();
        }
    }

    /**
     * Salva i dati modificati dell'utente
     * @param event evento di salvataggio
     */
    @FXML
    private void handleSave(ActionEvent event) {
        if (!validateInput()) {
            return;
        }

        try {
            boolean profileUpdated = updateProfile();

            boolean passwordUpdated = updatePasswordIfNeeded();

            if (profileUpdated && passwordUpdated) {
                showMessage("Dati aggiornati con successo!", Alert.AlertType.INFORMATION);

                goToDashboard(event);
            } else {
                showMessage("Errore durante l'aggiornamento dei dati.", Alert.AlertType.ERROR);
            }

        } catch (Exception e) {
            showMessage("Errore imprevisto: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    /**
     * Aggiorna i dati dell'utente
     * @return true se l'aggiornamento è stato fatto con successo
     */
    private boolean updateProfile() {
        currentUser.setUsername(usernameField.getText().trim());
        currentUser.setEmail(emailField.getText().trim());

        String selectedAvatar = avatarComboBox.getValue();
        if (selectedAvatar != null) {
            String avatarUrl = "/images/avatar/" + selectedAvatar + ".png";
            currentUser.setUrlAvatar(avatarUrl);
        }

        return userDAO.updateUserProfile(currentUser);
    }

    /**
     * Valida i dati dell'utente
     * @return true se sono validi
     */
    private boolean validateInput() {
        String username = usernameField.getText().trim();
        if (username.isEmpty()) {
            showMessage("Il nome utente non può essere vuoto.", Alert.AlertType.WARNING);
            return false;
        }

        if (!username.equals(currentUser.getUsername()) &&
                !isUsernameAvailable(username)) {
            showMessage("Username già in uso.", Alert.AlertType.WARNING);
            return false;
        }

        String email = emailField.getText().trim();
        if (email.isEmpty()) {
            showMessage("L'email non può essere vuota.", Alert.AlertType.WARNING);
            return false;
        }

        if (!isValidEmail(email)) {
            showMessage("Dominio email non riconosciuto", Alert.AlertType.WARNING);
            return false;
        }

        if (!email.equals(currentUser.getEmail()) &&
                !isEmailAvailable(email)) {
            showMessage("Email già in uso.", Alert.AlertType.WARNING);
            return false;
        }

        String currentPassword = currentPasswordField.getText();
        String newPassword = newPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (!currentPassword.isEmpty() || !newPassword.isEmpty() || !confirmPassword.isEmpty()) {
            if (currentPassword.isEmpty()) {
                showMessage("Inserisci la password attuale per cambiarla.", Alert.AlertType.WARNING);
                return false;
            }

            if (!userDAO.checkLogin(currentUser.getUsername(), PasswordUtils.hashPassword(currentPassword))) {
                showMessage("Password attuale non corretta.", Alert.AlertType.WARNING);
                return false;
            }

            if (newPassword.isEmpty()) {
                showMessage("Inserisci la nuova password.", Alert.AlertType.WARNING);
                return false;
            }

            if (newPassword.length() < 7) {
                showMessage("La nuova password deve essere di almeno 6 caratteri.", Alert.AlertType.WARNING);
                return false;
            }

            if (!newPassword.equals(confirmPassword)) {
                showMessage("Le password non corrispondono.", Alert.AlertType.WARNING);
                return false;
            }
        }

        return true;
    }

    /**
     * Permette di cambiare la password se richiesta
     * @return True se la password è stata aggiornata con successo
     */
    private boolean updatePasswordIfNeeded() {
        String newPassword = newPasswordField.getText();

        if (!newPassword.isEmpty()) {
            String hashedPassword = PasswordUtils.hashPassword(newPassword);
            boolean updated = userDAO.updatePassword(currentUser.getId(), hashedPassword);

            if (updated) {
                currentUser.setPassword(hashedPassword);
            }

            return updated;
        }

        return true;
    }

    /**
     * Verifica la validità dell'email
     * @param email
     * @return True se l'email è valida
     */
    private boolean isValidEmail(String email) {
        return email.contains("@") && email.substring(email.indexOf("@")).contains(".");
    }

    /**
     * Si occupa di mostrare un messaggio di errore o successo
     * @param message
     * @param type
     */
    private void showMessage(String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle("Modifica Dati");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Permette di annullare le modifiche
     * @param event
     */
    @FXML
    private void handleCancel(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Conferma");
        alert.setHeaderText("Annullare le modifiche?");
        alert.setContentText("Tutte le modifiche non salvate andranno perse.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            goToDashboard(event);
        }
    }

    /**
     * Permette di ritornare alla user-dashboard
     * @param event
     */
    private void goToDashboard(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/user_dashboard.fxml"));
            Parent root = loader.load();

            UserDashboardController controller = loader.getController();
            if (currentUser != null) {
                controller.setUser(currentUser);
            }

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));

            stage.setMinWidth(600);
            stage.setMinHeight(400);

            stage.setTitle("Area utente");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Verifica se un nome utente è disponibile
     * @param username
     * @return True se è disponibile
     */
    private boolean isUsernameAvailable(String username) {
        return !userDAO.selectByUsername(username).isPresent();
    }

    /**
     * Verifica se un'email è disponibile
     * @param email
     * @return True se è disponibile
     */
    private boolean isEmailAvailable(String email) {
        return !userDAO.selectByEmail(email).isPresent();
    }
}