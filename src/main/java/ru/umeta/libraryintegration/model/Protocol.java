package ru.umeta.libraryintegration.model;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by ctash on 29.04.2015.
 */
@Entity
@Table(name = "Protocol")
public class Protocol{

    @Id
    @Column(name = "id")
    @GeneratedValue
    private Long id;

    @Column(name = "name")
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
