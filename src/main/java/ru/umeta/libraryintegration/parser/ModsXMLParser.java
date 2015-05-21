package ru.umeta.libraryintegration.parser;

import gov.loc.mods.v3.*;
import org.apache.xmlbeans.XmlException;
import ru.umeta.libraryintegration.json.ModsParseResult;
import ru.umeta.libraryintegration.json.ParseResult;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by k.kosolapov on 06.04.2015.
 */
public class ModsXMLParser implements IXMLParser {

    private static final String ISBN = "isbn";
    private static final String NULL = null;
    private static final String SPACE = " ";
    private static final String ERROR_PARSING_XML_FILE = "Error parsing XML file";

    @Override
    public List<ParseResult> parse(File file) {
        List<ParseResult> resultList = new ArrayList<>();
        try {
            final ModsCollectionDocument modsCollectionDocument = ModsCollectionDocument.Factory.parse(file);
            final ModsDefinition[] modsCollection = modsCollectionDocument.getModsCollection().getModsArray();
            for (ModsDefinition mods : modsCollection) {
                final String title = parseTitle(mods);
                final String isbn = parseIdentifier(mods);
                final String name = parseName(mods);
                final String xml = mods.xmlText();

            }

        } catch (XmlException e) {
            System.err.println(ERROR_PARSING_XML_FILE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultList;
    }

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
                                titleBuilder.append(titleArray[j].getStringValue());
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
        final IdentifierDefinition[] identifierArray = document.getIdentifierArray();
        final IdentifierDefinition identifier = identifierArray.length > 0 ? identifierArray[0] : null;
        final String isbn;
        if (identifier != null) {
            isbn = ISBN.compareTo(identifier.getType()) == 0 ? identifier.getStringValue() : NULL;
        } else {
            isbn = NULL;
        }
        return isbn;
    }

    public String parseName(ModsDefinition document) {
        final StringBuilder nameBuilder = new StringBuilder("");
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


}
