package ru.umeta.libraryintegration.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.umeta.libraryintegration.json.UploadResult;
import ru.umeta.libraryintegration.service.MainService;

/**
 * The Main Rest Controller
 * Created by Kirill Kosolapov (https://github.com/c-tash) on 06.04.2015.
 */
@RestController
@RequestMapping("/rest")
public class MainRestController {

    @Autowired
    private MainService mainService;

    @RequestMapping("/upload")
    public UploadResult upload(@RequestParam(value = "path", defaultValue = "default path") String path)
            throws InterruptedException {
        return mainService.parseDirectory(path);
    }

    @RequestMapping("/balance")
    public UploadResult balance(@RequestParam(value = "path", defaultValue = "default path") String path,
                                @RequestParam(value = "level", defaultValue = "1") int level)
            throws InterruptedException {
        return mainService.parseDirectoryBalance(path, level);
    }

}
