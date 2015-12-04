package ru.umeta.libraryintegration.fs;

import com.google.common.base.Charsets;
import org.apache.commons.io.output.FileWriterWithEncoding;
import org.apache.commons.codec.binary.Hex;
import org.springframework.stereotype.Component;
import ru.umeta.libraryintegration.model.StringHash;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by k.kosolapov on 12/2/2015.
 */
@Component
public class StringHashFsSaver {

    private static final String SEPARATOR = "|";
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    private final BlockingQueue<StringHash> queue = new LinkedBlockingQueue<>();

    private final File storageFile = new File("//Storage//stringHash.blob");

    public StringHashFsSaver() {
        executorService.execute(() -> {
            try (FileWriterWithEncoding writerWithEncoding = new FileWriterWithEncoding(storageFile, Charsets.UTF_8)) {
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

}
