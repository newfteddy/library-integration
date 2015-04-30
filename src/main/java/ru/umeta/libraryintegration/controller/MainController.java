package ru.umeta.libraryintegration.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.umeta.libraryintegration.json.UploadResult;
import ru.umeta.libraryintegration.service.MainService;

/**
 * Created by k.kosolapov on 06.04.2015.
 */
@RestController
@RequestMapping("/rest")
public class MainController {

    @Autowired
    private MainService mainService;

    @RequestMapping("/upload")
    public UploadResult upload(@RequestParam(value = "path", defaultValue = "default path") String path) {
        int parsedDocs = mainService.parseDirectory(path);
        return new UploadResult(parsedDocs, "success");
    }

}
