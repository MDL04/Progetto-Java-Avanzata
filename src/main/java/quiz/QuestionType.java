package quiz;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum QuestionType {
    ABSOLUTE_FREQUENCY("Frequenza Assoluta",
            Arrays.asList(
                    "Quante volte compare la parola '%s' nel documento %s?",
                    "Qual è la frequenza assoluta della parola '%s' in %s?",
                    "Quante occorrenze ha la parola '%s' nel testo %s?")),

    COMPARISON("Confronto",
            Arrays.asList(
                    "Quale tra le seguenti parole è la più frequente %s?",
                    "Tra queste parole, quale appare più spesso %s?")),

    DOCUMENT_SPECIFIC("Documento Specifico",
            Arrays.asList(
                    "In quale documento compare più spesso la parola '%s'?",
                    "Dove appare più frequentemente la parola '%s'?")),

    EXCLUSION("Esclusione",
            Arrays.asList(
                    "Quale di queste parole NON compare mai %s?",
                    "Quale tra queste parole è assente %s?"));

    private final String type;
    private final List<String> questionList;

    QuestionType(String type, List<String> questionList) {
        this.type = type;
        this.questionList = questionList;
    }

    public String getType() {
        return type;
    }

    public List<String> getQuestionList() {
        return questionList;
    }

}
