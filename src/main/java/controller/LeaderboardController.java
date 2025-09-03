package controller;

import dao.UserDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.User;

import java.io.IOException;
import java.util.List;

/**
 * Controller per la schermata della classifica.
 * Gestisce la leaderboard view
 */

public class LeaderboardController {

    @FXML
    private TableView leaderboardTable;

    @FXML
    private TableColumn usernameColumn;

    @FXML
    private TableColumn easyColumn;

    @FXML
    private TableColumn mediumColumn;

    @FXML
    private TableColumn hardColumn;

    @FXML
    private Button homeButton;

    @FXML
    private Button dashboardButton;

    boolean isLoggedin = false;
    private User currentUser;

    private final UserDAO userDAO = new UserDAO();

    /**
     * Metodo che viene chiamato quando la scena viene inizializzata.
     */
    @FXML
    private void initialize() {
        setupColumns();
        hardColumn.setSortType(TableColumn.SortType.DESCENDING);
        leaderboardTable.getSortOrder().add(hardColumn);
        updateLeaderboard();
    }

    /**
     * Metodo per aggiornare la visibilità dei pulsanti in base allo stato di login dell'utente.
     * Mostra il pulsante "Torna alla home" se l'utente non è loggato, altrimenti mostra il pulsante "Dashboard".
     */
    private void updateButtonVisibility() {
        if (isLoggedin && currentUser != null) {
            homeButton.setVisible(false);
            homeButton.setManaged(false);
            dashboardButton.setVisible(true);
        } else {
            homeButton.setVisible(true);
            dashboardButton.setVisible(false);
        }
    }

    /**
     * Consente di tornare alla sezione home
     * @param event
     */
    @FXML
    private void goToHome(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/homepage.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            stage.setScene(new Scene(root));

            stage.setMinHeight(400);
            stage.setMinWidth(600);

            stage.setTitle("Homepage");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Consente di tornare alla sezione user_dashboard
     * @param event
     */
    @FXML
    private void goToUserDashboard(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/user_dashboard.fxml"));
            Parent root = loader.load();

            UserDashboardController dashboardController = loader.getController();
            if (currentUser != null) {
                dashboardController.setUser(currentUser);
            }

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            stage.setScene(new Scene(root));

            stage.setMinHeight(400);
            stage.setMinWidth(600);

            stage.setTitle("Dashboard Utente");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Imposta le colonne della tabella della classifica.
     */
    private void setupColumns() {
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        easyColumn.setCellValueFactory(new PropertyValueFactory<>("bestScoreEasy"));
        mediumColumn.setCellValueFactory(new PropertyValueFactory<>("bestScoreMedium"));
        hardColumn.setCellValueFactory(new PropertyValueFactory<>("bestScoreHard"));
    }

    /**
     * Aggiorna la classifica con i dati presenti nel database.
     */
    private void updateLeaderboard() {
        ObservableList<User> data = FXCollections.observableArrayList();
        List<User> users = userDAO.selectAll();
        List<User> filteredUsers = users.stream()
                .filter(user -> user.getBestScoreEasy() > 0 || user.getBestScoreMedium() > 0 || user.getBestScoreHard() > 0)
                .toList();
        data.addAll(filteredUsers);
        leaderboardTable.setItems(data);
    }

    /**
     * Imposta l'utente corrente nel controller.
     * @param user
     */
    public void setUser(User user) {
        this.currentUser = user;
        updateButtonVisibility();
        updateLeaderboard();
    }
}
