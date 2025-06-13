package model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Document {
    private long id;
    private String language;
    private String content;
    private String title;

    /**
     * Crea un'istanza di quest'oggetto
     * @param id
     * @param language
     * @param content
     * @param title
     */
    public Document(long id, String language, String content, String title) {
        this.id = id;
        this.language = language;
        this.content = content;
        this.title = title;
    }

    /**@return id*/
    public long getId() {
        return id;
    }

    /**@return linguaggio*/
    public String getLanguage() {
        return language;
    }

    /**@return contenuto*/
    public String getContent() {
        return content;
    }

    /**@return titolo*/
    public String getTitle() {return title;}

    /**@param id*/
    public void setId(long id) {
        this.id = id;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setContent(String content) {
        this.content = content;
    }

    /**@param title*/
    public void setTitle(String title) {this.title = title;}

    /**
     * Permette di trasformare un file in un oggetto Document
     * @param file
     * @param language
     * @param id
     * @param title
     * @return parsed document
     */
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
