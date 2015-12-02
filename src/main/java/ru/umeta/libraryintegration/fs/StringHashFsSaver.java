package ru.umeta.libraryintegration.fs;

import ru.umeta.libraryintegration.model.StringHash;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by k.kosolapov on 12/2/2015.
 */
public class StringHashFsSaver {

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    private final BlockingQueue<StringHash> queue = new LinkedBlockingQueue<>();

    Str

    public void save(StringHash stringHash) {
        queue.add(stringHash);
    }

}
