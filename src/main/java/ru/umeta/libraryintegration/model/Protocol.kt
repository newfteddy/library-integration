package ru.umeta.libraryintegration.model

import org.hibernate.annotations.NaturalId

import javax.persistence.*
import java.io.Serializable

/**
 * Created by ctash on 29.04.2015.
 */
@Entity
@Table(name = "protocol")
class Protocol : Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue
    var id: Long? = null

    @NaturalId(mutable = true)
    @Column(name = "name", unique = true)
    var name: String? = null

    @OneToMany(mappedBy = "protocol")
    var document: Collection<Document>? = null

    companion object {

        private val serialVersionUID = 4908088548280013641L
    }
}
