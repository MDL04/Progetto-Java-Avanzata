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

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;

    @FXML
    private Label messageLabel;

    public ModifyUserController() {
        this.userDAO = new UserDAO();
        this.availableAvatars = new ArrayList<>();
    }

    @FXML
    private void initialize() {
        loadAvailableAvatars();
        setupAvatarComboBox();
    }

    private void loadAvailableAvatars() {
        // Lista degli avatar disponibili nella cartella resources/images/avatar/
        availableAvatars.add("default_avatar.png");
        availableAvatars.add("avatar1.png");
        availableAvatars.add("avatar2.png");
        availableAvatars.add("avatar3.png");
        availableAvatars.add("avatar4.png");
        availableAvatars.add("avatar5.png");
        // Aggiungi altri avatar se necessario

        // Popola la ComboBox
        avatarComboBox.getItems().clear();
        for (String avatar : availableAvatars) {
            avatarComboBox.getItems().add(avatar.replace(".png", ""));
        }
    }

    private void setupAvatarComboBox() {
        // Quando viene selezionato un avatar, aggiorna l'anteprima
        avatarComboBox.setOnAction(e -> updateAvatarPreview());
    }

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

    private void loadUserData() {
        if (currentUser != null) {
            usernameField.setText(currentUser.getUsername());
            emailField.setText(currentUser.getEmail());

            // Carica l'avatar attuale
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

    @FXML
    private void handleSave(ActionEvent event) {
        if (!validateInput()) {
            return;
        }

        try {
            // Aggiorna i dati del profilo
            boolean profileUpdated = updateProfile();

            // Aggiorna la password se specificata
            boolean passwordUpdated = updatePasswordIfNeeded();

            if (profileUpdated && passwordUpdated) {
                showMessage("Dati aggiornati con successo!", Alert.AlertType.INFORMATION);

                // Torna alla dashboard
                goToDashboard(event);
            } else {
                showMessage("Errore durante l'aggiornamento dei dati.", Alert.AlertType.ERROR);
            }

        } catch (Exception e) {
            showMessage("Errore imprevisto: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    private boolean updateProfile() {
        // Aggiorna i dati dell'utente
        currentUser.setUsername(usernameField.getText().trim());
        currentUser.setEmail(emailField.getText().trim());

        // Costruisci l'URL dell'avatar basato sulla selezione
        String selectedAvatar = avatarComboBox.getValue();
        if (selectedAvatar != null) {
            String avatarUrl = "/images/avatar/" + selectedAvatar + ".png";
            currentUser.setUrlAvatar(avatarUrl);
        }

        return userDAO.updateUserProfile(currentUser);
    }

    // Resto del codice rimane uguale...
    private boolean validateInput() {
        // Validazione username
        String username = usernameField.getText().trim();
        if (username.isEmpty()) {
            showMessage("Il nome utente non può essere vuoto.", Alert.AlertType.WARNING);
            return false;
        }

        // Controlla se il nome utente è disponibile (solo se è stato cambiato)
        if (!username.equals(currentUser.getUsername()) &&
                !isUsernameAvailable(username)) {
            showMessage("Username già in uso.", Alert.AlertType.WARNING);
            return false;
        }

        // Validazione email
        String email = emailField.getText().trim();
        if (email.isEmpty()) {
            showMessage("L'email non può essere vuota.", Alert.AlertType.WARNING);
            return false;
        }

        if (!isValidEmail(email)) {
            showMessage("Dominio email non riconosciuto", Alert.AlertType.WARNING);
            return false;
        }

        // Controlla se l'email è disponibile (solo se è stata cambiata)
        if (!email.equals(currentUser.getEmail()) &&
                !isEmailAvailable(email)) {
            showMessage("Email già in uso.", Alert.AlertType.WARNING);
            return false;
        }

        // Validazione password (solo se vengono inserite)
        String currentPassword = currentPasswordField.getText();
        String newPassword = newPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (!currentPassword.isEmpty() || !newPassword.isEmpty() || !confirmPassword.isEmpty()) {
            if (currentPassword.isEmpty()) {
                showMessage("Inserisci la password attuale per cambiarla.", Alert.AlertType.WARNING);
                return false;
            }

            // Verifica password attuale
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

        return true; // Non c'è bisogno di aggiornare la password
    }

    private boolean isValidEmail(String email) {
        return email.contains("@") && email.substring(email.indexOf("@")).contains(".");
    }

    private void showMessage(String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle("Modifica Dati");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        // Chiedi conferma prima di annullare
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Conferma");
        alert.setHeaderText("Annullare le modifiche?");
        alert.setContentText("Tutte le modifiche non salvate andranno perse.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            goToDashboard(event);
        }
    }

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

    private boolean isUsernameAvailable(String username) {
        return !userDAO.selectByUsername(username).isPresent();
    }

    private boolean isEmailAvailable(String email) {
        return !userDAO.selectByEmail(email).isPresent();
    }
}