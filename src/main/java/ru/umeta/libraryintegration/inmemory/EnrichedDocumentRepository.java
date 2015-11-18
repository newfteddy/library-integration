package ru.umeta.libraryintegration.inmemory;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.springframework.stereotype.Repository;
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
@Repository
public class EnrichedDocumentRepository {

    private AtomicLong identity = new AtomicLong(0);

    Multimap<String, EnrichedDocument> isbnMap = ArrayListMultimap.create();

    //no year maps
    Multimap<Integer, EnrichedDocument> t1t2a1Map = ArrayListMultimap.create();
    Multimap<Integer, EnrichedDocument> t1t2a2Map = ArrayListMultimap.create();
    Multimap<Integer, EnrichedDocument> t1t3a1Map = ArrayListMultimap.create();
    Multimap<Integer, EnrichedDocument> t1t3a2Map = ArrayListMultimap.create();
    Multimap<Integer, EnrichedDocument> t1t4a1Map = ArrayListMultimap.create();
    Multimap<Integer, EnrichedDocument> t1t4a2Map = ArrayListMultimap.create();
    Multimap<Integer, EnrichedDocument> t2t3a1Map = ArrayListMultimap.create();
    Multimap<Integer, EnrichedDocument> t2t3a2Map = ArrayListMultimap.create();
    Multimap<Integer, EnrichedDocument> t2t4a1Map = ArrayListMultimap.create();
    Multimap<Integer, EnrichedDocument> t2t4a2Map = ArrayListMultimap.create();
    Multimap<Integer, EnrichedDocument> t3t4a1Map = ArrayListMultimap.create();
    Multimap<Integer, EnrichedDocument> t3t4a2Map = ArrayListMultimap.create();

    //year maps
    Multimap<Integer, EnrichedDocument> yt1t2a1Map = ArrayListMultimap.create();
    Multimap<Integer, EnrichedDocument> yt1t2a2Map = ArrayListMultimap.create();
    Multimap<Integer, EnrichedDocument> yt1t3a1Map = ArrayListMultimap.create();
    Multimap<Integer, EnrichedDocument> yt1t3a2Map = ArrayListMultimap.create();
    Multimap<Integer, EnrichedDocument> yt1t4a1Map = ArrayListMultimap.create();
    Multimap<Integer, EnrichedDocument> yt1t4a2Map = ArrayListMultimap.create();
    Multimap<Integer, EnrichedDocument> yt2t3a1Map = ArrayListMultimap.create();
    Multimap<Integer, EnrichedDocument> yt2t3a2Map = ArrayListMultimap.create();
    Multimap<Integer, EnrichedDocument> yt2t4a1Map = ArrayListMultimap.create();
    Multimap<Integer, EnrichedDocument> yt2t4a2Map = ArrayListMultimap.create();
    Multimap<Integer, EnrichedDocument> yt3t4a1Map = ArrayListMultimap.create();
    Multimap<Integer, EnrichedDocument> yt3t4a2Map = ArrayListMultimap.create();


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

        List<EnrichedDocument> result = new ArrayList<>();

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
        return result.stream().distinct().collect(Collectors.toList());
    }

    private Integer getHashWithoutYear(byte hash1, byte hash2, byte hash3) {
        int prime = 31;
        int result = hash1;
        result = result*prime + hash2;
        result = result*prime + hash3;
        return result;
    }

    public List<EnrichedDocument> getNearDuplicatesWithIsbn(Document document) {
        return (List<EnrichedDocument>) isbnMap.get(document.getIsbn());
    }

    public List<EnrichedDocument> getNearDuplicatesWithNullIsbn(Document document) {
        List<EnrichedDocument> nearDuplicates = getNearDuplicates(document);
        return nearDuplicates.stream().filter(
                d -> d.getIsbn() == null)
                .collect(Collectors.toList());
    }

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

        List<EnrichedDocument> result = new ArrayList<>();

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
        return result.stream().distinct().collect(Collectors.toList());
    }

    private Integer getHashWithYear(int year, byte hash1, byte hash2, byte hash3) {
        int prime = 31;
        int result = year;
        result = result*prime + hash1;
        result = result*prime + hash2;
        result = result*prime + hash3;
        return result;
    }

    public List<EnrichedDocument> getNearDuplicatesWithIsbnAndPublishYear(Document document) {
        List<EnrichedDocument> nearDuplicates = getNearDuplicatesWithIsbn(document);
        return nearDuplicates.stream().filter((
                d -> d.getPublishYear() == document.getPublishYear()))
                .collect(Collectors.toList());
    }

    public Number save(EnrichedDocument enrichedDocument) {
        long id = identity.getAndIncrement();
        enrichedDocument.setId(id);
        String isbn = enrichedDocument.getIsbn();
        if (isbn != null ) {
            isbnMap.put(isbn, enrichedDocument);
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

        t1t2a1Map.put(t1t2a1Hash, enrichedDocument);
        t1t2a2Map.put(t1t2a2Hash, enrichedDocument);
        t1t3a1Map.put(t1t3a1Hash, enrichedDocument);
        t1t3a2Map.put(t1t3a2Hash, enrichedDocument);
        t1t4a1Map.put(t1t4a1Hash, enrichedDocument);
        t1t4a2Map.put(t1t4a2Hash, enrichedDocument);
        t2t3a1Map.put(t2t3a1Hash, enrichedDocument);
        t2t3a2Map.put(t2t3a2Hash, enrichedDocument);
        t2t4a1Map.put(t2t4a1Hash, enrichedDocument);
        t2t4a2Map.put(t2t4a2Hash, enrichedDocument);
        t3t4a1Map.put(t3t4a1Hash, enrichedDocument);
        t3t4a2Map.put(t3t4a2Hash, enrichedDocument);

        yt1t2a1Map.put(yt1t2a1Hash, enrichedDocument);
        yt1t2a2Map.put(yt1t2a2Hash, enrichedDocument);
        yt1t3a1Map.put(yt1t3a1Hash, enrichedDocument);
        yt1t3a2Map.put(yt1t3a2Hash, enrichedDocument);
        yt1t4a1Map.put(yt1t4a1Hash, enrichedDocument);
        yt1t4a2Map.put(yt1t4a2Hash, enrichedDocument);
        yt2t3a1Map.put(yt2t3a1Hash, enrichedDocument);
        yt2t3a2Map.put(yt2t3a2Hash, enrichedDocument);
        yt2t4a1Map.put(yt2t4a1Hash, enrichedDocument);
        yt2t4a2Map.put(yt2t4a2Hash, enrichedDocument);
        yt3t4a1Map.put(yt3t4a1Hash, enrichedDocument);
        yt3t4a2Map.put(yt3t4a2Hash, enrichedDocument);

        return id;
    }
}
