package quiz;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum QuestionType {
    ABSOLUTE_FREQUENCY("Frequenza Assoluta",
            Arrays.asList(
                    "Quante volte compare la parola '%s' nel documento '%s'?",
                    "La parola '%s' compare nel documento '%s':",
                    "La parola '%s', nel documento '%s' ha una frequenza:",
                    "La parola '%s' compare nel documento '%s' più o meno di '%d' volte? ")),

    COMPARISON("Confronto",
            Arrays.asList(
                    "Quale parola è più frequente tra '%s', '%s', '%s', '%s' nel documento '%s'?",
                    "Qual è la parola più frequente nel documento '%s'?",
                    "Quale parola è meno frequente tra '%s', '%s', '%s', '%s' nel documento '%s'?",
                    "Qual è la parola meno frequente nel documento '%s'?")),

    DOCUMENT_SPECIFIC("Esclusione",
            Arrays.asList(
                    "Quale di queste parole non compare nel testo del documento '%s'?",
                    "Quale parola non è presente nel testo dei vari documenti?",
                    "La parola '%s' compare nei testi?",
                    "Quale parola tra '%s' e '%s' è presente nel testo del documento?")),

    EXCLUSION("Documento specifico",
            Arrays.asList(
                    "In quale documento compare più spesso la parola '%s'?",
                    "In che documento è presente la parola '%s'?",
                    "Nella totalità dei documenti, qual è il termine più ricorrente?",
                    "In che documento non compare la parola '%s'?"));

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
