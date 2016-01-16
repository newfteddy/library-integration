package ru.umeta.libraryintegration.parser

import gov.loc.mods.v3.*
import org.apache.xmlbeans.XmlException
import org.apache.xmlbeans.impl.values.XmlObjectBase
import ru.umeta.libraryintegration.json.ModsParseResult
import ru.umeta.libraryintegration.json.ParseResult
import org.apache.xmlbeans.XmlObject
import java.io.File
import java.io.IOException
import java.lang.reflect.Array
import java.util.ArrayList
import java.util.Arrays

/**
 * Created by k.kosolapov on 06.04.2015.
 */
object ModsXMLParser : IXMLParser {

    private val ISBN = "isbn"
    private val NULL: String? = null
    private val SPACE = " "
    private val ERROR_PARSING_XML_FILE = "Error parsing XML file"

    override fun parse(file: File): List<ParseResult> {
        val resultList = ArrayList<ParseResult>()
        try {
            val modsCollectionDocument = ModsCollectionDocument.Factory.parse(file)
            val modsCollection = modsCollectionDocument.modsCollection.modsArray
            for (mods in modsCollection) {
                val title = parseTitle(mods)
                val isbn = parseIdentifier(mods)
                val name = parseName(mods)
                val publishYear = parsePublishYear(mods)
                resultList.add(ModsParseResult(title, isbn, name, publishYear, mods))
            }

        } catch (e: XmlException) {
            System.err.println(ERROR_PARSING_XML_FILE)
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return resultList
    }

    //    public EnrichedDocument enrich(ModsDefinition definition, EnrichedDocument enrichedDocument) {
    //        try {
    //            ModsDefinition enrichedDefinition = ModsDefinition.Factory.parse(enrichedDocument.getXml());
    //            //abstract
    //            AbstractDefinition[] newAbstractArray = mergeSimpleProperty(definition.getAbstractArray(), enrichedDefinition.getAbstractArray());
    //            enrichedDefinition.setAbstractArray(newAbstractArray);
    //            //classification
    //            List<ClassificationDefinition> enrichedClassificationArray = mergeComplexProperty(definition.getClassificationArray(), enrichedDefinition.getClassificationArray());
    //            enrichedDefinition.setClassificationArray(enrichedClassificationArray.toArray(new ClassificationDefinition[enrichedClassificationArray.size()]));
    //
    //            //genre
    //            List<GenreDefinition> enrichedGenreArray = mergeComplexProperty(definition.getGenreArray(), enrichedDefinition.getGenreArray());
    //            enrichedDefinition.setGenreArray(enrichedGenreArray.toArray(new GenreDefinition[enrichedGenreArray.size()]));
    //
    //            //identifier
    //            List<IdentifierDefinition> enrichedIdentifierArray = mergeComplexProperty(definition.getIdentifierArray(), enrichedDefinition.getIdentifierArray());
    //            enrichedDefinition.setIdentifierArray(enrichedIdentifierArray.toArray(new IdentifierDefinition[enrichedIdentifierArray.size()]));
    //
    //            //language
    //            List<LanguageDefinition> enrichedLanguageArray = mergeComplexProperty(definition.getLanguageArray(), enrichedDefinition.getLanguageArray());
    //            enrichedDefinition.setLanguageArray(enrichedLanguageArray.toArray(new LanguageDefinition[enrichedLanguageArray.size()]));
    //
    //            //location
    //            List<LocationDefinition> enrichedLocationArray = mergeComplexProperty(definition.getLocationArray(), enrichedDefinition.getLocationArray());
    //            enrichedDefinition.setLocationArray(enrichedLocationArray.toArray(new LocationDefinition[enrichedLocationArray.size()]));
    //
    //            //note
    //            NoteDefinition[] newNoteArray = mergeSimpleProperty(definition.getNoteArray(), enrichedDefinition.getNoteArray());
    //            enrichedDefinition.setNoteArray(newNoteArray);
    //
    //            //originInfo
    //            OriginInfoDefinition[] newOriginInfoArray = mergeSimpleProperty(definition.getOriginInfoArray(), enrichedDefinition.getOriginInfoArray());
    //            enrichedDefinition.setOriginInfoArray(newOriginInfoArray);
    //
    //            //part
    //            PartDefinition[] newPartArray = mergeSimpleProperty(definition.getPartArray(), enrichedDefinition.getPartArray());
    //            enrichedDefinition.setPartArray(newPartArray);
    //
    //            //physicalDescription
    //            PhysicalDescriptionDefinition[] newPhysicalDescriptionArray = mergeSimpleProperty(definition.getPhysicalDescriptionArray(), enrichedDefinition.getPhysicalDescriptionArray());
    //            enrichedDefinition.setPhysicalDescriptionArray(newPhysicalDescriptionArray);
    //
    //            //recordInfo
    //            RecordInfoDefinition[] newRecordInfoArray = mergeSimpleProperty(definition.getRecordInfoArray(), enrichedDefinition.getRecordInfoArray());
    //            enrichedDefinition.setRecordInfoArray(newRecordInfoArray);
    //
    //            //relatedItem
    //            List<RelatedItemDefinition> enrichedRelatedItemArray = mergeComplexProperty(definition.getRelatedItemArray(), enrichedDefinition.getRelatedItemArray());
    //            enrichedDefinition.setRelatedItemArray(enrichedRelatedItemArray.toArray(new RelatedItemDefinition[enrichedRelatedItemArray.size()]));
    //
    //            //subject
    //            List<SubjectDefinition> enrichedSubjectArray = mergeComplexProperty(definition.getSubjectArray(), enrichedDefinition.getSubjectArray());
    //            enrichedDefinition.setSubjectArray(enrichedSubjectArray.toArray(new SubjectDefinition[enrichedSubjectArray.size()]));
    //
    //            //tableOfContents
    //            TableOfContentsDefinition[] newTableOfContentsArray = mergeSimpleProperty(definition.getTableOfContentsArray(), enrichedDefinition.getTableOfContentsArray());
    //            enrichedDefinition.setTableOfContentsArray(newTableOfContentsArray);
    //
    //            //targetAudience
    //            TargetAudienceDefinition[] newTargetAudienceArray = mergeSimpleProperty(definition.getTargetAudienceArray(), enrichedDefinition.getTargetAudienceArray());
    //            enrichedDefinition.setTargetAudienceArray(newTargetAudienceArray);
    //
    //            //typeOfResource
    //            TypeOfResourceDefinition[] newTypeOfResourceArray = mergeSimpleProperty(definition.getTypeOfResourceArray(), enrichedDefinition.getTypeOfResourceArray());
    //            enrichedDefinition.setTypeOfResourceArray(newTypeOfResourceArray);
    //
    //            enrichedDocument.setXml(enrichedDefinition.xmlText());
    //
    //            return enrichedDocument;
    //        } catch (XmlException e) {
    //            e.printStackTrace();
    //            return null;
    //        }
    //    }

    private fun <T : XmlObject> mergeSimpleProperty(propertyArray: Array<T>, enrichedPropertyArray: Array<T>): Array<T> {
        if (enrichedPropertyArray.size == 0 && propertyArray.size > 0) {
            return propertyArray
        }
        return enrichedPropertyArray
    }

    private fun <T : XmlObject> mergeComplexProperty(propertyArray: Array, enrichedPropertyArray: Array):
            List<T> {
        val enrichedPropertyList = ArrayList(Arrays.asList(enrichedPropertyArray))
        var size = enrichedPropertyList.size
        for (property in propertyArray) {
            var i = 0
            for (enrichedProperty in enrichedPropertyList) {
                val enrichedPropertyBase = enrichedProperty as XmlObjectBase
                if (property.stringValue == enrichedPropertyBase.stringValue) {
                    break
                }
                i++
            }
            if (i == size) {
                enrichedPropertyList.add(property)
                size++
            }
        }
        return enrichedPropertyList
    }

    fun parseTitle(document: ModsDefinition?): String {
        val titleBuilder = StringBuilder("")
        if (document != null) {
            val titleInfoArray = document.titleInfoArray
            if (titleInfoArray != null) {
                for (i in titleInfoArray.indices) {
                    val titleInfo = titleInfoArray[i]
                    if (titleInfo != null) {
                        val titleArray = titleInfo.titleArray
                        if (titleArray != null) {
                            for (j in titleArray.indices) {
                                titleBuilder.append(titleArray[j].stringValue)
                                titleBuilder.append(SPACE)
                            }
                        }

                        val subTitleArray = titleInfo.subTitleArray
                        if (subTitleArray != null) {
                            for (j in subTitleArray.indices) {
                                titleBuilder.append(subTitleArray[j].stringValue)
                                titleBuilder.append(SPACE)
                            }
                        }
                    }
                }
            }
        }

        return titleBuilder.toString()
    }

    fun parseIdentifier(document: ModsDefinition): String {
        val identifierArray = document.identifierArray

        var isbn: String = NULL
        for (identifier in identifierArray) {
            if (identifier != null && ISBN.compareTo(identifier.type) == 0) {
                isbn = identifier.stringValue
                break
            }
        }

        return isbn
    }

    fun parseName(document: ModsDefinition?): String {
        val nameBuilder = StringBuilder("")
        if (document != null) {
            val nameArray = document.nameArray
            if (nameArray != null) {
                for (name in nameArray) {
                    val namePartArray = name.namePartArray
                    if (namePartArray != null) {
                        for (namePart in namePartArray) {
                            nameBuilder.append(namePart.stringValue)
                            nameBuilder.append(SPACE)
                        }
                    }
                }
            }
        }

        return nameBuilder.toString()
    }

    fun parsePublishYear(document: ModsDefinition?): Int? {
        if (document != null) {
            val originInfoArray = document.originInfoArray
            if (originInfoArray != null && originInfoArray.size > 0) {
                val dateIssuedArray = originInfoArray[0].dateIssuedArray
                if (dateIssuedArray != null && dateIssuedArray.size > 0) {
                    try {
                        return Integer.valueOf(dateIssuedArray[0].stringValue)
                    } catch (e: NumberFormatException) {
                        return null
                    }

                }
            }
        }
        return null
    }
}
