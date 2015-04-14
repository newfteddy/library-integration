package ru.umeta.libraryintegration.service;

import ru.umeta.libraryintegration.model.ParseResult;
import ru.umeta.libraryintegration.parser.IXMLParser;
import ru.umeta.libraryintegration.parser.ModsXMLParser;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by k.kosolapov on 14/04/2015.
 */
public class MainService {

    public void parseDirectory(String path) {
        IXMLParser parser = new ModsXMLParser();
        List<File> fileList = getFilesToParse(path);
        boolean isFirstFile = false;
        for (File file : fileList) {
            List<ParseResult> resultList = parser.parse(file);

        }
    }

    private List<File> getFilesToParse(String path) {
        final List<File> result = new ArrayList<>();
        final File folder = new File(path);
        if (folder.listFiles() != null) {
            Collections.addAll(result, folder.listFiles());
        }
        return result;
    }

}
