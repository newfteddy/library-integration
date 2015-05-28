package ru.umeta.libraryintegration.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.umeta.libraryintegration.json.ParseResult;
import ru.umeta.libraryintegration.json.UploadResult;
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

    @Autowired
    private StringHashService stringHashService;

    @Autowired
    private DocumentService documentService;

    public MainService(IXMLParser parser) {
        this.parser = parser;
    }

    public UploadResult parseDirectory(String path) {

        List<File> fileList = getFilesToParse(path);
        UploadResult result = new UploadResult(0, 0);
        for (File file : fileList) {
            long startTime = System.nanoTime();
            List<ParseResult> resultList = parser.parse(file);
            long parseTime = System.nanoTime();
            System.out.println("The documents bulk parsed in " + (double)(parseTime - startTime) / 1000000000.0);
            UploadResult uploadResult = documentService.processDocumentList(resultList, null);
            result.setParsedDocs(result.getParsedDocs() + uploadResult.getParsedDocs());
            result.setNewEnriched(result.getNewEnriched() + uploadResult.getNewEnriched());
            long endTime = System.nanoTime();
            System.out.println("The documents bulk is added in " + (double)(endTime - startTime) / 1000000000.0);
        }
        return result;
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
