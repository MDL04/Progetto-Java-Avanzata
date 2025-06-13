package quiz;

import java.util.*;

/**
 * Questa classe rappresenta un tentativo di quiz dell'utente, gestendo le domande
 * le risposte e il punteggio.
 */

public class QuizAttempt {
    private final List<Question> domande;
    private final Map<Integer, String> risposteUtente;
    private int punteggio;
    private final String lingua;


    /**
     * Crea un'istanza di quest'oggetto
     * @param factory
     * @param numeroDomande
     * @param lingua
     */
    public QuizAttempt(QuestionFactory factory, int numeroDomande, String lingua) {
        this.domande = new ArrayList<>();
        this.risposteUtente = new HashMap<>();
        this.punteggio = 0;
        this.lingua = lingua;
        generaDomande(factory, numeroDomande);
    }

    /**@return domande del quiz*/
    public List<Question> getDomande() {
        return domande;
    }

    /**@return risposte utente*/
    public Map<Integer, String> getRisposteUtente() {
        return risposteUtente;
    }


    /**@return punteggio*/
    public int getPunteggio() {
        return punteggio;
    }

    /**
     * Genera un tot di omande in base al tipo e alla lingua
     * @param factory
     * @param numeroDomande
     */
    private void generaDomande(QuestionFactory factory, int numeroDomande) {
        List<QuestionType> tipi = List.of(QuestionType.values());
        Random r = new Random();

        for (int i = 0; i < numeroDomande; i++) {
            QuestionType tipo = tipi.get(r.nextInt(tipi.size()));
            try {
                Question q = factory.generateQuestion(i + 1, tipo, lingua);
                domande.add(q);
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Errore nella generazione della domanda: " + e.getMessage());
                i--;
            }
        }
    }

    /**
     * Registra la risposta dell'utente per una domanda specifica
     *
     * @param idDomanda
     * @param risposta
     */
    public void registraRisposta(int idDomanda, String risposta) {
        risposteUtente.put(idDomanda, risposta);
    }

    /**
     * Ritorna il punteggio conseguito dall'utente
     * @return
     */
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

    /**
     * Ritorna la percentuale di risposte corrette
     * @return
     */
    public double getPercentualeCorrette() {
        return domande.isEmpty() ? 0.0 : (double) punteggio / domande.size() * 100;
    }

    /**
     * Ritorna una lista di domande errate
     * @return
     */
    public List<Question> getDomandeErrate() {
        return domande.stream()
                .filter(q -> !q.isCorrect())
                .toList();
    }

    /**
     * Ritorna una lista di domande corrette
     * @return
     */
    public List<Question> getDomandeCorrette() {
        return domande.stream()
                .filter(Question::isCorrect)
                .toList();
    }
}