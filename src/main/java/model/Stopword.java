package model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Stopword {
    private String language;
    private String content;
    private long id;
    private String title;

    /**
     * Crea un'istanza di quest'oggetto
     * @param language
     * @param content
     * @param id
     * @param title
     */
    public Stopword(String language, String content, long id, String title) {
        this.language = language;
        this.content = content;
        this.id = id;
        this.title = title;
    }

    /**@return language*/
    public String getLanguage() {return language;}

    /**@return content*/
    public String getContent() {return content;}

    /**@return id*/
    public long getId() {return id;}

    /**@return title*/
    public String getTitle() {return title;}

    /**@param language*/
    public void setLanguage(String language) {this.language = language;}

    /**@param content*/
    public void setContent(String content) {this.content = content;}

    /**@param id*/
    public void setId(long id) {this.id = id;}

    /**@param title*/
    public void setTitle(String title) {this.title = title;}

    /**
     * Permette di trasformare un file in un oggetto Stopword
     * @param file
     * @param language
     * @param id
     * @param title
     * @return
     */
    public static Stopword parseStopword(File file, String language, long id, String title){
        Stopword s = null;
        try{
            String content = Files.readString(file.toPath());
            s = new Stopword(language, content, id, title);
        }catch(IOException e){
            e.printStackTrace();
        }
        return s;
    }
}
