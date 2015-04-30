package ru.umeta.libraryintegration.json;

import java.io.Serializable;

/**
 * Created by k.kosolapov on 06.04.2015.
 */
public class UploadResult implements Serializable {

    private static final long serialVersionUID = 8711710207201631499L;
    private int parsedDocs;
    private String message;

    public UploadResult(int parsedDocs, String message) {
        this.parsedDocs = parsedDocs;
        this.message = message;
    }

    public int getParsedDocs() {
        return parsedDocs;
    }

    public void setParsedDocs(int parsedDocs) {
        this.parsedDocs = parsedDocs;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
