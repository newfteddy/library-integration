package ru.umeta.libraryintegration.json;

import java.io.Serializable;

/**
 * Created by k.kosolapov on 06.04.2015.
 */
public class UploadResult implements Serializable {

    private static final long serialVersionUID = 8711710207201631499L;
    private int parsedDocs;
    private int newEnriched;

    public UploadResult(int parsedDocs, int newEnriched) {
        this.parsedDocs = parsedDocs;
        this.newEnriched = newEnriched;
    }

    public int getParsedDocs() {
        return parsedDocs;
    }

    public void setParsedDocs(int parsedDocs) {
        this.parsedDocs = parsedDocs;
    }

    public int getNewEnriched() {
        return newEnriched;
    }

    public void setNewEnriched(int newEnriched) {
        this.newEnriched = newEnriched;
    }
}
