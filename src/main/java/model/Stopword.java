package model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Stopword {
    private String language;
    private String content;
    private long id;
    private String title;


    public Stopword(String language, String content, long id, String title) {
        this.language = language;
        this.content = content;
        this.id = id;
        this.title = title;
    }

    public String getLanguage() {return language;}

    public String getContent() {return content;}

    public long getId() {return id;}

    public String getTitle() {return title;}

    public void setLanguage(String language) {this.language = language;}

    public void setContent(String content) {this.content = content;}

    public void setId(long id) {this.id = id;}

    public void setTitle(String title) {this.title = title;}


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
