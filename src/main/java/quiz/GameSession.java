package quiz;

import java.util.*;

public class GameSession {
    private final int idSessione;
    private long idUser;
    private final List<Question> domande;
    private final Map<Integer, String> risposteUtente;
    private int punteggio;

    public GameSession(int idSessione, QuestionFactory factory, int numeroDomande) {
        this.idSessione = idSessione;
        this.domande = new ArrayList<>();
        this.risposteUtente = new HashMap<>();
        this.punteggio = 0;

        generaDomande(factory, numeroDomande);
    }

    public int getIdSessione() {
        return idSessione;
    }

    public List<Question> getDomande() {
        return domande;
    }

    public Map<Integer, String> getRisposteUtente() {
        return risposteUtente;
    }

    public int getPunteggio() {
        return punteggio;
    }

    private void generaDomande(QuestionFactory factory, int numeroDomande) {
        List<QuestionType> tipi = List.of(QuestionType.values());
        Random r = new Random();

        for (int i = 0; i < numeroDomande; i++) {
            QuestionType tipo = tipi.get(r.nextInt(tipi.size()));
            try {
                Question q = factory.generateQuestion(i + 1, tipo);
                domande.add(q);
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Errore nella generazione della domanda: " + e.getMessage());
                // Riprova con un tipo diverso
                i--;
            }
        }
    }

    public void registraRisposta(int idDomanda, String risposta) {
        risposteUtente.put(idDomanda, risposta);
    }

    public int valutaRisposte() {
        punteggio = 0;
        for (Question q : domande) {
            String risposta = risposteUtente.getOrDefault(q.getId(), "");
            if (q.getCorrectAnswer().equals(risposta)) {
                punteggio++;
                q.setCorrect(true);
            } else {
                q.setCorrect(false);
            }
            q.setUserAnswer(risposta);
        }
        return punteggio;
    }

    public double getPercentualeCorrette() {
        return domande.isEmpty() ? 0.0 : (double) punteggio / domande.size() * 100;
    }

    public List<Question> getDomandeErrate() {
        return domande.stream()
                .filter(q -> !q.isCorrect())
                .toList();
    }

    public List<Question> getDomandeCorrette() {
        return domande.stream()
                .filter(Question::isCorrect)
                .toList();
    }
}