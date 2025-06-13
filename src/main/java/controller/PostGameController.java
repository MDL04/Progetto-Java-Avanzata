package controller;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import quiz.Question;
import quiz.QuizAttempt;

/**
 * Controller per la schermata post-game, che visualizza i risultati del quiz
 *  * e fornisce informazioni sui punteggi e sul tempo trascorso.
 */


public class PostGameController{

    @FXML
    private TextField difficulty;

    @FXML
    private TextField timeSpent;

    @FXML
    private TextField userScore;

    @FXML
    private ListView listView;

    /**
     * Inizializza i dati della dashboard post-game e mostrando il punto dell'utente, la difficoltà e il tempo speso.
     * @param session
     * @param difficolta
     * @param tempo
     */
    public void initData(QuizAttempt session, String difficolta, String tempo) {
        userScore.setText("Punteggio: " + session.valutaRisposte() + "/" + session.getDomande().size());
        difficulty.setText("Difficoltà: " + difficolta);
        timeSpent.setText("Tempo utilizzato: " + tempo);
    }

    /**
     * Aggiorna la ListView con i parametri risultanti dalla sessione appena conclusa
     * @param session
     */
    public void updateListView(QuizAttempt session) {
        for(Question question : session.getDomande()){
            StringBuilder sb = new StringBuilder();
            sb.append("Domanda ").append(question.getId()).append(": ").append(question.getQuestionText()).append("\n");
            sb.append("Risposta corretta: ").append(question.getCorrectAnswer()).append("\n");
            if (session.getRisposteUtente().containsKey(question.getId())) {
                sb.append("La tua risposta: ").append(session.getRisposteUtente().get(question.getId())).append("\n");
            } else {
                sb.append("Non hai risposto a questa domanda.\n");
            }
            listView.getItems().add(sb.toString());
        }
    }
}
