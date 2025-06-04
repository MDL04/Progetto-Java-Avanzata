package controller;

import actors.User;
import dao.UserDAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import utils.PasswordUtils;

public class RegisterController {

    @FXML
    private TextField usernameFld, emailFld;

    @FXML
    private PasswordField passwordFld, confirmPasswordFld;

    @FXML
    private Label messageLbl;

    private final UserDAO userDAO = new UserDAO();

    @FXML
    private void handleRegister() {
        String username = usernameFld.getText();
        String email = emailFld.getText();
        String password = passwordFld.getText();
        String confirmPassword = confirmPasswordFld.getText();

        if(!password.equals(confirmPassword)) {
            messageLbl.setText("Le password non coincindono");
            return;
        }

        String hashedPassword = PasswordUtils.hashPassword(password);

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(hashedPassword);
        user.setAdmin(false);
        user.setUrlAvatar("default_avatar.png");

        userDAO.insert(user);
        messageLbl.setText("Registrazione completata");
    }
}
