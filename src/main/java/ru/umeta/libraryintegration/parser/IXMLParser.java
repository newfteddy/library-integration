package ru.umeta.libraryintegration.parser;

import java.io.File;
import java.util.List;

/**
 * Created by k.kosolapov on 06.04.2015.
 */
public interface IXMLParser {

    List<IParseResult> parse(File file);

}
