package ru.umeta.libraryintegration.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.umeta.libraryintegration.model.UploadResult;
import ru.umeta.libraryintegration.parser.IParseResult;
import ru.umeta.libraryintegration.parser.IXMLParser;
import ru.umeta.libraryintegration.parser.ModsXMLParser;

import java.io.File;
import java.util.List;

/**
 * Created by k.kosolapov on 06.04.2015.
 */
@RestController
@RequestMapping("/rest")
public class MainController {

    @RequestMapping("/upload")
    public UploadResult upload(@RequestParam(value="path", defaultValue = "default path") String path) {
        IXMLParser parser = new ModsXMLParser();
        List<File> fileList = getFilesToParse(path);
        for (File file : fileList) {
            List<IParseResult> resultList = parser.parse(file);
        }
    }
}
