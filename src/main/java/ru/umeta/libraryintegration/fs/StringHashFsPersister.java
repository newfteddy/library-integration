package ru.umeta.libraryintegration.fs;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.io.output.FileWriterWithEncoding;
import org.apache.commons.codec.binary.Hex;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import ru.umeta.libraryintegration.model.StringHash;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by k.kosolapov on 12/2/2015.
 */
@Component
public class StringHashFsPersister {

    private static final String SEPARATOR = "|";
    private static final String UTF_8 = "UTF-8";
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    private final BlockingQueue<StringHash> queue = new LinkedBlockingQueue<>();

    private final File storageFile = new File("stringHash.blob");



    public StringHashFsPersister() {
        //storageFile.mkdirs();
        if (!storageFile.exists()) {
            try {
                storageFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        executorService.execute(() -> {
            try (FileWriterWithEncoding writerWithEncoding = new FileWriterWithEncoding(storageFile, Charset.forName(UTF_8), true)) {
                while (true) {
                    StringHash stringHash;
                    try {
                        stringHash = queue.take();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        return;
                    }
                    writerWithEncoding.write(Hex.encodeHexString(new byte[] {
                            stringHash.getHashPart1(),
                            stringHash.getHashPart2(),
                            stringHash.getHashPart3(),
                            stringHash.getHashPart4()
                    }));
                    writerWithEncoding.write(SEPARATOR);
                    writerWithEncoding.write(String.valueOf(stringHash.getId()));
                    writerWithEncoding.write(SEPARATOR);
                    writerWithEncoding.write(stringHash.getValue());
                    writerWithEncoding.write(SEPARATOR + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void save(StringHash stringHash) {
        queue.add(stringHash);
    }

    public long fillMaps(Map<String, StringHash> map, Map<Long, StringHash> idMap) {
        long lastId = 0;
        try {
            LineIterator it = FileUtils.lineIterator(storageFile, UTF_8);
            try {
                while (it.hasNext()) {
                    try {
                        StringHash stringHash = new StringHash();
                        String line = it.nextLine();
                        String[] splitStrings = StringUtils.tokenizeToStringArray(line, SEPARATOR);

                        byte[] bytes = Hex.decodeHex(splitStrings[0].toCharArray());
                        if (bytes.length < 4) {
                            continue;
                        } else {
                            stringHash.setHashPart1(bytes[0]);
                            stringHash.setHashPart2(bytes[1]);
                            stringHash.setHashPart3(bytes[2]);
                            stringHash.setHashPart4(bytes[3]);
                        }
                        long id = Long.parseLong(splitStrings[1]);
                        stringHash.setId(id);
                        lastId = Math.max(id, lastId);
                        String value = splitStrings[2];
                        stringHash.setValue(value);

                        map.put(value, stringHash);
                        idMap.put(id, stringHash);

                    } catch (DecoderException e) {
                        e.printStackTrace();
                    }
                }

                }
            finally {
                LineIterator.closeQuietly(it);
            }
        } catch (IOException e) {
            System.out.println("Cannot open the storage file");
        }
        return lastId;
    }
}
