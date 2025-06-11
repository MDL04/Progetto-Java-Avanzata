package model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Document {
    private long id;
    private String language;
    private String content;
    private String title;

    public Document(long id, String language, String content, String title) {
        this.id = id;
        this.language = language;
        this.content = content;
        this.title = title;
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

    public String getTitle() {return title;}

    public void setId(long id) {
        this.id = id;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setTitle(String title) {this.title = title;}


    public static Document parseDocument(File file, String language, long id, String title){
        Document c = null;
        try{
            String content = Files.readString(file.toPath());
            c = new Document(id, language, content, title);
        }catch(IOException e){
            e.printStackTrace();
        }
        return c;
    }

    @Override
    public String toString() {
        return "[" + language + "] Documento ID: " + id;
    }
}
