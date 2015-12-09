package ru.umeta.libraryintegration.model;

/**
 * Created by k.kosolapov on 12/9/2015.
 */
public class XmlBlob {
    private Long id;
    private String xml;

    public XmlBlob(Long id, String xml) {
        this.id = id;
        this.xml = xml;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getXml() {
        return xml;
    }

    public void setXml(String xml) {
        this.xml = xml;
    }
}
