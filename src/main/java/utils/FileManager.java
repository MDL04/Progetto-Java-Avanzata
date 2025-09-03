package utils;

import model.Document;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class FileManager {

    public static List<Document> caricaDocumenti(String language) throws IOException {
        List<Document> docs = new ArrayList<>();
        Path dir = Paths.get("src/main/resources/documents/" + language);
        if (!Files.exists(dir)) return docs;
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir, "*.txt")) {
            for (Path entry : stream) {
                String content = Files.readString(entry);
                String title = entry.getFileName().toString();
                docs.add(new Document(0, language, content, title));
            }
        }
        return docs;
    }

    public static List<String> caricaStopwords(String language) throws IOException {
        List<String> stopwords = new ArrayList<>();
        Path dir = Paths.get("src/main/resources/stopwords/" + language);
        if (!Files.exists(dir)) return stopwords;
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir, "*.txt")) {
            for (Path entry : stream) {
                List<String> lines = Files.readAllLines(entry);
                stopwords.addAll(lines);
            }
        }
        return stopwords;
    }
}