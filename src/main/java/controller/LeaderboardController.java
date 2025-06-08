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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.User;

import java.io.IOException;
import java.util.List;

public class LeaderboardController {

    @FXML
    TableView leaderboardTable;

    @FXML
    TableColumn usernameColumn;

    @FXML
    TableColumn easyColumn;

    @FXML
    TableColumn mediumColumn;

    @FXML
    TableColumn hardColumn;

    private ObservableList<User> leaderboardData = FXCollections.observableArrayList();
    private final UserDAO userDAO = new UserDAO();

    @FXML
    private void initialize() {
        setupColumns();

        hardColumn.setSortType(TableColumn.SortType.DESCENDING);
        leaderboardTable.getSortOrder().add(hardColumn);


        //updateLeaderboard();
    }

    @FXML
    private void goToHome(ActionEvent event) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/homepage.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Homepage");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setupColumns() {
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));

        easyColumn.setCellValueFactory(new PropertyValueFactory<>("easyScore"));
        mediumColumn.setCellValueFactory(new PropertyValueFactory<>("mediumScore"));
        hardColumn.setCellValueFactory(new PropertyValueFactory<>("hardScore"));
    }


//    private void updateLeaderboard() {
//        ObservableList<User> data = FXCollections.observableArrayList();
//
//        List<User> users = userDAO.selectAll();
//        List<User> filteredUsers = users.stream().filter(user -> user.getBestScoreEasy() > 0 && user.getBestScoreNormal() > 0 && user.getBestScoreHard() > 0).toList();
//
//        data.addAll(filteredUsers);
//
//        leaderboardTable.setItems(data);
//    }

}
