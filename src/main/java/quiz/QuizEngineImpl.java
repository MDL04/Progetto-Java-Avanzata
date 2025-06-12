package quiz;

import model.WordDocumentMatrix;

import java.util.*;

public class QuizEngineImpl implements QuizEngine {
    private final QuestionFactory factory;
    private final Random rand = new Random();

    public QuizEngineImpl(WordDocumentMatrix matrix) {
        this.factory = new QuestionFactory(matrix);
    }

    @Override
    public List<Question> generaQuiz(int n) {
        List<Question> quiz = new ArrayList<>();
        QuestionType[] types = QuestionType.values();
        int id = 1;
        int maxTentativi = n * 3; // Evita loop infiniti
        int tentativi = 0;

        while (quiz.size() < n && tentativi < maxTentativi) {
            QuestionType type = types[rand.nextInt(types.length)];
            try {
                Question q = factory.generateQuestion(id, type);
                quiz.add(q);
                id++;
            } catch (Exception e) {
                System.err.println("Errore nella generazione della domanda tipo " + type + ": " + e.getMessage());
            }
            tentativi++;
        }

        if (quiz.size() < n) {
            System.err.println("Attenzione: generate solo " + quiz.size() + " domande su " + n + " richieste");
        }

        return quiz;
    }

    public List<Question> generaQuizBilanciato(int n) {
        List<Question> quiz = new ArrayList<>();
        QuestionType[] types = QuestionType.values();
        int domandePerTipo = n / types.length;
        int rimanenti = n % types.length;
        int id = 1;

        // Genera domande bilanciate per tipo
        for (QuestionType type : types) {
            int numDomande = domandePerTipo + (rimanenti > 0 ? 1 : 0);
            if (rimanenti > 0) rimanenti--;

            for (int i = 0; i < numDomande; i++) {
                try {
                    Question q = factory.generateQuestion(id, type);
                    quiz.add(q);
                    id++;
                } catch (Exception e) {
                    System.err.println("Errore nella generazione della domanda tipo " + type + ": " + e.getMessage());
                }
            }
        }

        // Mescola le domande
        Collections.shuffle(quiz);
        return quiz;
    }

    public Map<QuestionType, List<Question>> generaQuizPerTipo(int domandePerTipo) {
        Map<QuestionType, List<Question>> quizPerTipo = new HashMap<>();

        for (QuestionType type : QuestionType.values()) {
            List<Question> domande = new ArrayList<>();
            int id = 1;

            for (int i = 0; i < domandePerTipo; i++) {
                try {
                    Question q = factory.generateQuestion(id, type);
                    domande.add(q);
                    id++;
                } catch (Exception e) {
                    System.err.println("Errore nella generazione della domanda tipo " + type + ": " + e.getMessage());
                }
            }

            quizPerTipo.put(type, domande);
        }

        return quizPerTipo;
    }
}