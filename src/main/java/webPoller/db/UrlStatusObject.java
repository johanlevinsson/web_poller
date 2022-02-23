package webPoller.db;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
public class UrlStatusObject {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String url;
    private int code;
    private String creationTime;
    private String name;

    public UrlStatusObject() {
    }

    public UrlStatusObject(String url, int code, String name) {
        this.url = url;
        this.code = code;
        this.creationTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
