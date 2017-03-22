package ru.umeta.libraryintegration.parser;

import gov.loc.mods.v3.*;
import jdk.nashorn.internal.runtime.regexp.joni.Regex;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.impl.values.XmlObjectBase;
import org.springframework.stereotype.Component;
import ru.umeta.libraryintegration.json.ModsParseResult;
import ru.umeta.libraryintegration.json.ParseResult;
import ru.umeta.libraryintegration.model.EnrichedDocument;
import org.apache.xmlbeans.XmlObject;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.*;



/**
 * Created by k.kosolapov on 06.04.2015.
 */
@Component
public class ModsXMLParser implements IXMLParser {

    private static final String ISBN = "isbn";
    private static final String NULL = null;
    private static final String SPACE = " ";
    private static final String ERROR_PARSING_XML_FILE = "Error parsing XML file";

    @Override
    public List<ParseResult> parse(File file) {
        List<ParseResult> resultList = new ArrayList<>();
        try {
            ModsCollectionDocument modsCollectionDocument = ModsCollectionDocument.Factory.parse(file);
            ModsDefinition[] modsCollection = modsCollectionDocument.getModsCollection().getModsArray();
            for (ModsDefinition mods : modsCollection) {
                final String title = parseTitle(mods);
                final String isbn = parseIdentifier(mods);
                final String name = parseName(mods);
                final Integer publishYear = parsePublishYear(mods);
                resultList.add(new ModsParseResult(title, isbn, name, publishYear, mods));
            }

        } catch (XmlException e) {
            System.err.println(ERROR_PARSING_XML_FILE);
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultList;
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

    private <T extends XmlObject> T[] mergeSimpleProperty(T[] propertyArray, T[] enrichedPropertyArray) {
        if (enrichedPropertyArray.length == 0 && propertyArray.length > 0) {
            return propertyArray;
        }
        return enrichedPropertyArray;
    }

    private <T extends XmlObject> List<T> mergeComplexProperty(T[] propertyArray, T[] enrichedPropertyArray) {
        List<T> enrichedPropertyList = new ArrayList<>(Arrays.asList(enrichedPropertyArray));
        int size = enrichedPropertyList.size();
        for (T property : propertyArray) {
        XmlObjectBase propertyBase = (XmlObjectBase) property;
        int i = 0;
        for (T enrichedProperty : enrichedPropertyList) {
        XmlObjectBase enrichedPropertyBase = (XmlObjectBase) enrichedProperty;
        if (propertyBase.getStringValue().equals(enrichedPropertyBase.getStringValue())) {
            break;
        }
        i++;
    }
        if (i == size) {
            enrichedPropertyList.add(property);
            size++;
        }
    }
        return enrichedPropertyList;
    }

    /*correction*/

    public String StringPreProcess(String line){

        String tmp = "";
        String result = "";
        String delims  = "[ ;:,.]+";
        String tokens [] = line.split(delims);

        for (int i = 0;i < tokens.length;i++){
            tokens[i] = tokens[i].toLowerCase();
            tmp = tokens[i];
            tmp = tmp.replaceAll("\\D+","");

            if (tmp.equals("") == false){

                int num = Integer.parseInt(tmp);
                if (num > 1500 && num <2020){
                    tokens[i] = Integer.toString(num);
                }
                else {
                    NumToWord converter = new NumToWord();
                    String number = converter.ConvertNumToWord(num);
                    tokens[i] = number;
                }
            }
            result = result.concat(tokens[i] + " ");
        }
        //System.out.print(result + "\n");
        return result;
    }
    /*--correction*/


    public String parseTitle(ModsDefinition document) {
        StringBuilder titleBuilder = new StringBuilder("");
        if (document != null ) {
            final TitleInfoDefinition[] titleInfoArray = document.getTitleInfoArray();
            if (titleInfoArray != null) {
                for (int i = 0; i < titleInfoArray.length; i++) {
                    final TitleInfoDefinition titleInfo = titleInfoArray[i];
                    if (titleInfo != null) {
                        final StringPlusLanguage[] titleArray = titleInfo.getTitleArray();
                        if (titleArray != null) {
                            for (int j = 0; j < titleArray.length; j++) {
                                /*correction*/
                                String tmp = StringPreProcess(titleArray[j].getStringValue());
                                /*--correction*/
                                titleBuilder.append(tmp);
                                titleBuilder.append(SPACE);
                            }
                        }

                        final StringPlusLanguage[] subTitleArray = titleInfo.getSubTitleArray();
                        if (subTitleArray != null) {
                            for (int j = 0; j < subTitleArray.length; j++) {
                                titleBuilder.append(subTitleArray[j].getStringValue());
                                titleBuilder.append(SPACE);
                            }
                        }
                    }
                }
            }
        }

        return titleBuilder.toString();
    }


    public String parseIdentifier(ModsDefinition document) {
        IdentifierDefinition[] identifierArray = document.getIdentifierArray();

        String isbn = NULL;
        for (IdentifierDefinition identifier : identifierArray) {
        if (identifier != null && ISBN.equals(identifier.getType())) {
            isbn = identifier.getStringValue();
            break;
        }
    }

        return isbn;
    }

    public String parseName(ModsDefinition document) {
        StringBuilder nameBuilder = new StringBuilder("");
        if (document != null) {
            final NameDefinition[] nameArray = document.getNameArray();
            if (nameArray != null) {
                for (NameDefinition name : nameArray) {
                    final NamePartDefinition[] namePartArray = name.getNamePartArray();
                    if (namePartArray != null) {
                        for (NamePartDefinition namePart : namePartArray) {
                            nameBuilder.append(namePart.getStringValue());
                            nameBuilder.append(SPACE);
                        }

                    }

                }
            }

        }

        return nameBuilder.toString();
    }

    public Integer parsePublishYear(ModsDefinition document) {
        if (document != null) {
            final OriginInfoDefinition[] originInfoArray = document.getOriginInfoArray();
            if (originInfoArray != null && originInfoArray.length > 0) {
                DateDefinition[] dateIssuedArray = originInfoArray[0].getDateIssuedArray();
                if (dateIssuedArray != null && dateIssuedArray.length > 0) {
                    try {
                        return Integer.valueOf(dateIssuedArray[0].getStringValue());
                    } catch (NumberFormatException e) {
                        return null;
                    }
                }
            }
        }
        return null;
    }

}
