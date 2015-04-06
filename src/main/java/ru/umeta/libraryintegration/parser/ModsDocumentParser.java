package ru.umeta.libraryintegration.parser;

import gov.loc.mods.v3.*;

/**
 * Created by k.kosolapov on 05.02.2015.
 */
public class ModsDocumentParser {

    private static final String ISBN = "isbn";
    private static final String NULL = "null";
    private static final String SPACE = " ";

    public static String parseTitle(ModsDocument document) {
        StringBuilder titleBuilder = new StringBuilder("");
        if (document != null && document.getMods() != null) {
            final TitleInfoDefinition[] titleInfoArray = document.getMods().getTitleInfoArray();
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

    public static String parseIdentifier(ModsDocument document) {
        final IdentifierDefinition[] identifierArray = document.getMods().getIdentifierArray();
        final IdentifierDefinition identifier = identifierArray.length > 0 ? identifierArray[0] : null;
        final String isbn;
        if (identifier != null) {
            isbn = ISBN.compareTo(identifier.getType()) == 0 ? identifier.getStringValue() : NULL;
        } else {
            isbn = NULL;
        }
        return isbn;
    }

    public static String parseName(ModsDocument document) {
        final StringBuilder nameBuilder = new StringBuilder("");
        if (document != null && document.getMods() != null) {
            final NameDefinition[] nameArray = document.getMods().getNameArray();
            if (nameArray != null) {
                for (int i = 0; i < nameArray.length; i++) {
                    final NamePartDefinition[] namePartArray = nameArray[i].getNamePartArray();
                    if (namePartArray != null) {
                        for (int j = 0; j < namePartArray.length; j++) {
                            nameBuilder.append(namePartArray[j].getStringValue());
                            nameBuilder.append(SPACE);
                        }
                    }
                }
            }
        }
        
        return nameBuilder.toString();
    }
}
