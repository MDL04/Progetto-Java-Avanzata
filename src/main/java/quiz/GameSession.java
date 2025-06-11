package quiz;

import java.util.*;

public class GameSession {

    private final int idSessione;
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
            Question q = switch (tipo) {
                case ABSOLUTE_FREQUENCY -> factory.generateAbsoluteFrequencyQuestion(i);
                case COMPARISON -> factory.generateComparisonQuestion(i);
                case DOCUMENT_SPECIFIC -> factory.generateDocumentSpecificQuestion(i);
                case EXCLUSION -> factory.generateExclusionQuestion(i);
            };
            domande.add(q);
        }
    }
    public void registraRisposta(int idDomanda, String risposta){
        risposteUtente.put(idDomanda, risposta);
    }

    public int valutaRisposte(){
        punteggio = 0;
        for (Question q : domande) {
            String risposta = risposteUtente.getOrDefault(q.getId(), "");
            if(q.getCorrectAnswer().equalsIgnoreCase(risposta)){
                punteggio++;
            }
        }
        return punteggio;
    }
}
