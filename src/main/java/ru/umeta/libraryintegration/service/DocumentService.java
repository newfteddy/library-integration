package ru.umeta.libraryintegration.service;

import org.springframework.beans.factory.annotation.Autowired;
import ru.umeta.libraryintegration.json.ModsParseResult;
import ru.umeta.libraryintegration.json.ParseResult;
import ru.umeta.libraryintegration.model.Document;
import ru.umeta.libraryintegration.model.StringHash;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.BitSet;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by ctash on 28.04.2015.
 */
public class DocumentService {

    @Autowired
    private StringHashService stringHashService;

    public void processDocumentList(List<ParseResult> resultList) {
        for (ParseResult parseResult : checkNotNull(resultList)) {
            if (parseResult instanceof ModsParseResult) {
                final Document document = new Document();
                ModsParseResult modsParseResult = (ModsParseResult) parseResult;
                final StringHash authorHash = stringHashService.getStringHash(modsParseResult.getAuthor());
                document.setAuthor(authorHash);
            }
        }
    }
}
