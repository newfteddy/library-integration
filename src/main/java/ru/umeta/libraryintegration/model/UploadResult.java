package ru.umeta.libraryintegration.model;

import java.io.Serializable;

/**
 * Created by k.kosolapov on 06.04.2015.
 */
public class UploadResult implements Serializable{
    private static final long serialVersionUID = 8711710207201631499L;
    private int id;
    private String message;

    public UploadResult(int id, String message) {
        this.id = id;
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
