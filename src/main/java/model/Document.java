package model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Document {
    private long id;
    private String language;
    private String content;

    public Document(long id, String language, String content) {
        this.id = id;
        this.language = language;
        this.content = content;
    }

    public long getId() {
        return id;
    }

    public String getLanguage() {
        return language;
    }

    public String getContent() {
        return content;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public static Document parseDocument(File file, String language, long id) throws IOException {
        String content = Files.readString(file.toPath());
        return new Document(id, language, content);
    }
}
