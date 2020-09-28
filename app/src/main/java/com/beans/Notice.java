package com.beans;

import java.io.Serializable;
import java.util.ArrayList;

public class Notice implements Serializable {
    private String id;
    private String title;
    private String message;
    private String noticeDeptId;

    public Notice() {
        super();
    }

    public Notice(String id, String title, String message, String noticeDeptId) {
        super();
        this.id = id;
        this.title = title;
        this.message = message;
        this.noticeDeptId = noticeDeptId;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public String getNoticeDeptId() {
        return noticeDeptId;
    }
    public void setNoticeDeptId(String noticeDeptId) {
        this.noticeDeptId = noticeDeptId;
    }

}
