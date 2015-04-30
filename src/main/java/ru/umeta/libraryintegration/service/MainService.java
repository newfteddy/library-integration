package ru.umeta.libraryintegration.service;

import org.springframework.stereotype.Service;
import ru.umeta.libraryintegration.json.ParseResult;
import ru.umeta.libraryintegration.parser.IXMLParser;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by k.kosolapov on 14/04/2015.
 */
@Service
public class MainService {

    private final IXMLParser parser;

    public MainService(IXMLParser parser) {
        this.parser = parser;
    }

    public int parseDirectory(String path) {

        List<File> fileList = getFilesToParse(path);
        boolean isFirstFile = false;
        int parsedDocs = 0;
        for (File file : fileList) {
            List<ParseResult> resultList = parser.parse(file);
            parsedDocs += resultList.size();

        }
        return parsedDocs;
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
