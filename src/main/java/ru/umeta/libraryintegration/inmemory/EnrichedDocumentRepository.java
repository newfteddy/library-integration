package ru.umeta.libraryintegration.inmemory;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import gnu.trove.map.hash.TIntLongHashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import ru.umeta.libraryintegration.dao.EnrichedDocumentDao;
import ru.umeta.libraryintegration.model.Document;
import ru.umeta.libraryintegration.model.EnrichedDocument;
import ru.umeta.libraryintegration.model.StringHash;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * The repository consists of large amount of hashmaps to get fast access to near duplicates.
 * The maps have the following structure:
 * year -> t1 -> t2 -> a1
 *                 ||    \\ -> a2
 *                 \\ -> t3 -> a1
 *                 ||    \\ -> a2
 *                 \\ -> t4 -> a1
 *                       \\ -> a2
 * ...
 *
 *
 *
 * Created by Kirill Kosolapov (https://github.com/c-tash) on 12.11.2015.
 */
@Primary
@Repository
public class EnrichedDocumentRepository implements IEnrichedDocumentRepository {

    Multimap<String, Long> isbnMap = ArrayListMultimap.create();

    //no year maps
    Multimap<Integer, Long> t1t2a1Map = ArrayListMultimap.create();
    Multimap<Integer, Long> t1t2a2Map = ArrayListMultimap.create();
    Multimap<Integer, Long> t1t3a1Map = ArrayListMultimap.create();
    Multimap<Integer, Long> t1t3a2Map = ArrayListMultimap.create();
    Multimap<Integer, Long> t1t4a1Map = ArrayListMultimap.create();
    Multimap<Integer, Long> t1t4a2Map = ArrayListMultimap.create();
    Multimap<Integer, Long> t2t3a1Map = ArrayListMultimap.create();
    Multimap<Integer, Long> t2t3a2Map = ArrayListMultimap.create();
    Multimap<Integer, Long> t2t4a1Map = ArrayListMultimap.create();
    Multimap<Integer, Long> t2t4a2Map = ArrayListMultimap.create();
    Multimap<Integer, Long> t3t4a1Map = ArrayListMultimap.create();
    Multimap<Integer, Long> t3t4a2Map = ArrayListMultimap.create();

    //year maps
    Multimap<Integer, Long> yt1t2a1Map = ArrayListMultimap.create();
    Multimap<Integer, Long> yt1t2a2Map = ArrayListMultimap.create();
    Multimap<Integer, Long> yt1t3a1Map = ArrayListMultimap.create();
    Multimap<Integer, Long> yt1t3a2Map = ArrayListMultimap.create();
    Multimap<Integer, Long> yt1t4a1Map = ArrayListMultimap.create();
    Multimap<Integer, Long> yt1t4a2Map = ArrayListMultimap.create();
    Multimap<Integer, Long> yt2t3a1Map = ArrayListMultimap.create();
    Multimap<Integer, Long> yt2t3a2Map = ArrayListMultimap.create();
    Multimap<Integer, Long> yt2t4a1Map = ArrayListMultimap.create();
    Multimap<Integer, Long> yt2t4a2Map = ArrayListMultimap.create();
    Multimap<Integer, Long> yt3t4a1Map = ArrayListMultimap.create();
    Multimap<Integer, Long> yt3t4a2Map = ArrayListMultimap.create();


    private final EnrichedDocumentDao enrichedDocumentDao;

    @Autowired
    public EnrichedDocumentRepository(EnrichedDocumentDao enrichedDocumentDao) {
        this.enrichedDocumentDao = enrichedDocumentDao;
    }

    @Override
    public List<EnrichedDocument> getNearDuplicates(Document document) {
        StringHash author = document.getAuthor();
        StringHash title = document.getTitle();

        Byte a1 = author.getHashPart1();
        Byte a2 = author.getHashPart2();

        Byte t1 = title.getHashPart1();
        Byte t2 = title.getHashPart2();
        Byte t3 = title.getHashPart3();
        Byte t4 = title.getHashPart4();

        Integer yt1t2a1Hash = getHashWithoutYear(t1, t2, a1);
        Integer yt1t2a2Hash = getHashWithoutYear(t1, t2, a2);
        Integer yt1t3a1Hash = getHashWithoutYear(t1, t3, a1);
        Integer yt1t3a2Hash = getHashWithoutYear(t1, t3, a2);
        Integer yt1t4a1Hash = getHashWithoutYear(t1, t4, a1);
        Integer yt1t4a2Hash = getHashWithoutYear(t1, t4, a2);

        Integer yt2t3a1Hash = getHashWithoutYear(t2, t3, a1);
        Integer yt2t3a2Hash = getHashWithoutYear(t2, t3, a2);
        Integer yt2t4a1Hash = getHashWithoutYear(t2, t4, a1);
        Integer yt2t4a2Hash = getHashWithoutYear(t2, t4, a2);

        Integer yt3t4a1Hash = getHashWithoutYear(t3, t4, a1);
        Integer yt3t4a2Hash = getHashWithoutYear(t3, t4, a2);

        List<Long> result = new ArrayList<>();

        result.addAll(t1t2a1Map.get(yt1t2a1Hash));
        result.addAll(t1t2a2Map.get(yt1t2a2Hash));
        result.addAll(t1t3a1Map.get(yt1t3a1Hash));
        result.addAll(t1t3a2Map.get(yt1t3a2Hash));
        result.addAll(t1t4a1Map.get(yt1t4a1Hash));
        result.addAll(t1t4a2Map.get(yt1t4a2Hash));
        result.addAll(t2t3a1Map.get(yt2t3a1Hash));
        result.addAll(t2t3a2Map.get(yt2t3a2Hash));
        result.addAll(t2t4a1Map.get(yt2t4a1Hash));
        result.addAll(t2t4a2Map.get(yt2t4a2Hash));
        result.addAll(t3t4a1Map.get(yt3t4a1Hash));
        result.addAll(t3t4a2Map.get(yt3t4a2Hash));
        return result.stream().distinct()
                .map(enrichedDocumentDao::getEnrichedDocument)
                .collect(Collectors.toList());
    }

    private Integer getHashWithoutYear(byte hash1, byte hash2, byte hash3) {
        //shift is of the size of a byte
        int shift = 8;
        int result = hash1;
        result = result << shift + hash2;
        result = result << shift + hash3;
        return result;
    }

    @Override
    public List<EnrichedDocument> getNearDuplicatesWithIsbn(Document document) {
        return isbnMap.get(document.getIsbn()).stream()
                .map(enrichedDocumentDao::getEnrichedDocument)
                .collect(Collectors.toList());
    }

    @Override
    public List<EnrichedDocument> getNearDuplicatesWithNullIsbn(Document document) {
        List<EnrichedDocument> nearDuplicates = getNearDuplicates(document);
        return nearDuplicates.stream().filter(
                d -> d.getIsbn() == null)
                .collect(Collectors.toList());
    }

    @Override
    public List<EnrichedDocument> getNearDuplicatesWithPublishYear(Document document) {
        StringHash author = document.getAuthor();
        StringHash title = document.getTitle();
        Integer year = document.getPublishYear();

        Byte a1 = author.getHashPart1();
        Byte a2 = author.getHashPart2();

        Byte t1 = title.getHashPart1();
        Byte t2 = title.getHashPart2();
        Byte t3 = title.getHashPart3();
        Byte t4 = title.getHashPart4();

        Integer yt1t2a1Hash = getHashWithYear(year, t1, t2, a1);
        Integer yt1t2a2Hash = getHashWithYear(year, t1, t2, a2);
        Integer yt1t3a1Hash = getHashWithYear(year, t1, t3, a1);
        Integer yt1t3a2Hash = getHashWithYear(year, t1, t3, a2);
        Integer yt1t4a1Hash = getHashWithYear(year, t1, t4, a1);
        Integer yt1t4a2Hash = getHashWithYear(year, t1, t4, a2);

        Integer yt2t3a1Hash = getHashWithYear(year, t2, t3, a1);
        Integer yt2t3a2Hash = getHashWithYear(year, t2, t3, a2);
        Integer yt2t4a1Hash = getHashWithYear(year, t2, t4, a1);
        Integer yt2t4a2Hash = getHashWithYear(year, t2, t4, a2);

        Integer yt3t4a1Hash = getHashWithYear(year, t3, t4, a1);
        Integer yt3t4a2Hash = getHashWithYear(year, t3, t4, a2);

        List<Long> result = new ArrayList<>();

        result.addAll(yt1t2a1Map.get(yt1t2a1Hash));
        result.addAll(yt1t2a2Map.get(yt1t2a2Hash));
        result.addAll(yt1t3a1Map.get(yt1t3a1Hash));
        result.addAll(yt1t3a2Map.get(yt1t3a2Hash));
        result.addAll(yt1t4a1Map.get(yt1t4a1Hash));
        result.addAll(yt1t4a2Map.get(yt1t4a2Hash));
        result.addAll(yt2t3a1Map.get(yt2t3a1Hash));
        result.addAll(yt2t3a2Map.get(yt2t3a2Hash));
        result.addAll(yt2t4a1Map.get(yt2t4a1Hash));
        result.addAll(yt2t4a2Map.get(yt2t4a2Hash));
        result.addAll(yt3t4a1Map.get(yt3t4a1Hash));
        result.addAll(yt3t4a2Map.get(yt3t4a2Hash));
        return result.stream().distinct()
                .map(enrichedDocumentDao::getEnrichedDocument)
                .collect(Collectors.toList());
    }

    private Integer getHashWithYear(int year, byte hash1, byte hash2, byte hash3) {
        int shift = 8;
        int result = year;
        result = result << shift + hash1;
        result = result << shift + hash2;
        result = result << shift + hash3;
        return result;
    }

    @Override
    public List<EnrichedDocument> getNearDuplicatesWithIsbnAndPublishYear(Document document) {
        List<EnrichedDocument> nearDuplicates = getNearDuplicatesWithIsbn(document);
        return nearDuplicates.stream().filter((
                d -> d.getPublishYear() == document.getPublishYear()))
                .collect(Collectors.toList());
    }

    @Override
    public Number save(EnrichedDocument enrichedDocument) {
        //save enrichedDocument to the database
        Long id = enrichedDocumentDao.save(enrichedDocument);

        String isbn = enrichedDocument.getIsbn();
        if (isbn != null ) {
            isbnMap.put(isbn, enrichedDocument.getId());
        }

        StringHash author = enrichedDocument.getAuthor();
        StringHash title = enrichedDocument.getTitle();
        Integer year = enrichedDocument.getPublishYear();

        Byte a1 = author.getHashPart1();
        Byte a2 = author.getHashPart2();

        Byte t1 = title.getHashPart1();
        Byte t2 = title.getHashPart2();
        Byte t3 = title.getHashPart3();
        Byte t4 = title.getHashPart4();

        Integer t1t2a1Hash = getHashWithoutYear(t1, t2, a1);
        Integer t1t2a2Hash = getHashWithoutYear(t1, t2, a2);
        Integer t1t3a1Hash = getHashWithoutYear(t1, t3, a1);
        Integer t1t3a2Hash = getHashWithoutYear(t1, t3, a2);
        Integer t1t4a1Hash = getHashWithoutYear(t1, t4, a1);
        Integer t1t4a2Hash = getHashWithoutYear(t1, t4, a2);

        Integer t2t3a1Hash = getHashWithoutYear(t2, t3, a1);
        Integer t2t3a2Hash = getHashWithoutYear(t2, t3, a2);
        Integer t2t4a1Hash = getHashWithoutYear(t2, t4, a1);
        Integer t2t4a2Hash = getHashWithoutYear(t2, t4, a2);

        Integer t3t4a1Hash = getHashWithoutYear(t3, t4, a1);
        Integer t3t4a2Hash = getHashWithoutYear(t3, t4, a2);

        Integer yt1t2a1Hash = getHashWithYear(year, t1, t2, a1);
        Integer yt1t2a2Hash = getHashWithYear(year, t1, t2, a2);
        Integer yt1t3a1Hash = getHashWithYear(year, t1, t3, a1);
        Integer yt1t3a2Hash = getHashWithYear(year, t1, t3, a2);
        Integer yt1t4a1Hash = getHashWithYear(year, t1, t4, a1);
        Integer yt1t4a2Hash = getHashWithYear(year, t1, t4, a2);

        Integer yt2t3a1Hash = getHashWithYear(year, t2, t3, a1);
        Integer yt2t3a2Hash = getHashWithYear(year, t2, t3, a2);
        Integer yt2t4a1Hash = getHashWithYear(year, t2, t4, a1);
        Integer yt2t4a2Hash = getHashWithYear(year, t2, t4, a2);

        Integer yt3t4a1Hash = getHashWithYear(year, t3, t4, a1);
        Integer yt3t4a2Hash = getHashWithYear(year, t3, t4, a2);

        t1t2a1Map.put(t1t2a1Hash, id);
        t1t2a2Map.put(t1t2a2Hash, id);
        t1t3a1Map.put(t1t3a1Hash, id);
        t1t3a2Map.put(t1t3a2Hash, id);
        t1t4a1Map.put(t1t4a1Hash, id);
        t1t4a2Map.put(t1t4a2Hash, id);
        t2t3a1Map.put(t2t3a1Hash, id);
        t2t3a2Map.put(t2t3a2Hash, id);
        t2t4a1Map.put(t2t4a1Hash, id);
        t2t4a2Map.put(t2t4a2Hash, id);
        t3t4a1Map.put(t3t4a1Hash, id);
        t3t4a2Map.put(t3t4a2Hash, id);

        yt1t2a1Map.put(yt1t2a1Hash, id);
        yt1t2a2Map.put(yt1t2a2Hash, id);
        yt1t3a1Map.put(yt1t3a1Hash, id);
        yt1t3a2Map.put(yt1t3a2Hash, id);
        yt1t4a1Map.put(yt1t4a1Hash, id);
        yt1t4a2Map.put(yt1t4a2Hash, id);
        yt2t3a1Map.put(yt2t3a1Hash, id);
        yt2t3a2Map.put(yt2t3a2Hash, id);
        yt2t4a1Map.put(yt2t4a1Hash, id);
        yt2t4a2Map.put(yt2t4a2Hash, id);
        yt3t4a1Map.put(yt3t4a1Hash, id);
        yt3t4a2Map.put(yt3t4a2Hash, id);

        return id;
    }

    @Override
    public void getPersistedData() {
        enrichedDocumentDao.getEnrichedDocumentForIdRange(0, 10000);
    }
}
