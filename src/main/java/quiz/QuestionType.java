package quiz;

import java.util.List;

/**
 * Questa enumerazione rappresenta i diversi tipi di domanda che possono essere generati.
 * Ogni tipo di domanda ha una lista di modelli predefiniti.
 */


public enum QuestionType {
    ABSOLUTE_FREQUENCY(List.of(
            "Quante volte compare la parola '%s' nel documento '%s'?",
            "Qual è la frequenza della parola '%s' nel documento '%s'?",
            "In quante occorrenze si trova la parola '%s' nel documento '%s'?",
            "Quante volte è ripetuta la parola '%s' nel documento '%s'?",
            "Conta le occorrenze della parola '%s' nel documento '%s'"
    )),

    COMPARISON(List.of(
            "Quante volte compare la parola più frequente nel documento '%s'?",
            "Qual è la frequenza massima di una parola nel documento '%s'?",
            "Qual è la differenza di frequenza tra '%s' e '%s' nel documento '%s'?",
            "Quante volte in più compare '%s' rispetto a '%s' nel documento '%s'?",
            "Qual è la somma delle frequenze di '%s' e '%s' nel documento '%s'?"
    )),

    DOCUMENT_SPECIFIC(List.of(
            "In quanti documenti compare la parola '%s'?",
            "Quante volte compare la parola '%s' nel documento dove è più frequente?",
            "Qual è la frequenza totale della parola '%s' in tutti i documenti?",
            "In quanti documenti la parola '%s' compare almeno 2 volte?",
            "Qual è la frequenza media (arrotondata) della parola '%s' nei documenti dove compare?"
    )),

    EXCLUSION(List.of(
            "Quante volte compare la parola '%s' in tutti i documenti tranne '%s'?",
            "Qual è la frequenza della parola più comune in tutti i documenti?",
            "Quante parole diverse compaiono almeno 3 volte nel documento '%s'?",
            "Qual è il numero di parole uniche nel documento '%s'?",
            "Quante volte compare complessivamente la parola meno frequente in tutti i documenti?"
    ));

    private final List<String> questionTemplates;

    QuestionType(List<String> questionTemplates) {
        this.questionTemplates = questionTemplates;
    }

    public List<String> getQuestionList() {
        return questionTemplates;
    }
}