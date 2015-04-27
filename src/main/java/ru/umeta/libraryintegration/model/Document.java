package ru.umeta.libraryintegration.model;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by k.kosolapov on 27.04.2015.
 */
@Entity
@Table(name = "Document")
public class Document implements Serializable{

    private static final long serialVersionUID = 3933411330113087708L;

    @Id
    @Column(name = "id")
    @GeneratedValue
    private Long id;



}
