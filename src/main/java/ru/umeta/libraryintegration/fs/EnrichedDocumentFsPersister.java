package ru.umeta.libraryintegration.fs;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.io.output.FileWriterWithEncoding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import ru.umeta.libraryintegration.inmemory.StringHashRepository;
import ru.umeta.libraryintegration.model.EnrichedDocument;
import ru.umeta.libraryintegration.model.XmlBlob;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;

/**
 * Created by k.kosolapov on 12/2/2015.
 */
@Component
public class EnrichedDocumentFsPersister {

    private static final String SEPARATOR = "|";
    private static final String UTF_8 = "UTF-8";
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    private final BlockingQueue<EnrichedDocument> documentQueue = new LinkedBlockingQueue<>();
    private final BlockingQueue<XmlBlob> xmlQueue = new LinkedBlockingQueue<>();

    private final File documentStorageFile = new File("enrichedDocument.blob");
//    private final File xmlStorageFile = new File("xml.blob");
    private final StringHashRepository stringHashRepository;


    @Autowired
    public EnrichedDocumentFsPersister(StringHashRepository stringHashRepository) {
        this.stringHashRepository = stringHashRepository;
        if (!documentStorageFile.exists()) {
            try {
                documentStorageFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        executorService.execute(() -> {
            try (FileWriterWithEncoding writerWithEncoding = new FileWriterWithEncoding(documentStorageFile, Charset.forName(UTF_8), true)) {
                while (true) {
                    EnrichedDocument document;
                    try {
                        document = documentQueue.take();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        return;
                    }
                    writerWithEncoding.write(String.valueOf(document.getId()));
                    writerWithEncoding.write(SEPARATOR);
                    writerWithEncoding.write(String.valueOf(document.getAuthor().getId()));
                    writerWithEncoding.write(SEPARATOR);
                    writerWithEncoding.write(String.valueOf(document.getTitle().getId()));
                    writerWithEncoding.write(SEPARATOR);
                    writerWithEncoding.write(String.valueOf(document.getIsbn()));
                    writerWithEncoding.write(SEPARATOR);
                    writerWithEncoding.write(String.valueOf(document.getPublishYear()));
//                    writerWithEncoding.write(SEPARATOR);
//                    writerWithEncoding.write(String.valueOf(document.getXmlBlob().getId()));
                    writerWithEncoding.write(SEPARATOR + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
//        executorService.execute(() -> {
//            try (FileWriterWithEncoding writerWithEncoding = new FileWriterWithEncoding(xmlStorageFile, Charset.forName(UTF_8),
//                    true)) {
//                while (true) {
//                    XmlBlob xml;
//                    try {
//                        xml = xmlQueue.take();
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                        return;
//                    }
//                    writerWithEncoding.write(String.valueOf(xml.getId()));
//                    writerWithEncoding.write(SEPARATOR);
//                    writerWithEncoding.write(xml.getXml());
//                    writerWithEncoding.write(SEPARATOR + "\n");
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        });
    }

    public void save(EnrichedDocument document) {
        documentQueue.add(document);
    }

    public long applyToPeristed(Consumer<EnrichedDocument> consumer) {
        long lastId = 0;
        try {
            LineIterator it = FileUtils.lineIterator(documentStorageFile, UTF_8);
            try {
                while (it.hasNext()) {
                    EnrichedDocument document = new EnrichedDocument();
                    String line = it.nextLine();
                    String[] splitStrings = StringUtils.tokenizeToStringArray(line, SEPARATOR);

                    if (splitStrings.length != 5) {
                        continue;
                    }

                    long id = Long.parseLong(splitStrings[0]);
                    document.setId(id);
                    document.setAuthor(stringHashRepository.getById(Long.parseLong(splitStrings[1])));
                    document.setTitle(stringHashRepository.getById(Long.parseLong(splitStrings[2])));
                    if (document.getAuthor() == null || document.getTitle() == null) {
                        continue;
                    }
                    document.setIsbn(splitStrings[3]);
                    document.setPublishYear("null".equals(splitStrings[4]) ? null : Integer.parseInt(splitStrings[4]));
                    lastId = Math.max(id, lastId);
                    consumer.accept(document);
                }
            } finally {
                LineIterator.closeQuietly(it);
            }
        } catch (IOException e) {
            System.out.println("Cannot open the storage file");
        }
        return lastId;
    }
}
