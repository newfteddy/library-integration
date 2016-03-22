package ru.umeta.libraryintegration.service;

import org.apache.commons.io.output.FileWriterWithEncoding;
import org.eclipse.collections.impl.factory.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.umeta.libraryintegration.inmemory.InMemoryRepository;
import ru.umeta.libraryintegration.model.EnrichedDocumentLite;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Created by ctash on 18.03.2016.
 */
@Component public class JavaDuplicateService {

    @Autowired InMemoryRepository repository;

    @Autowired StringHashService stringService;

    public void parse() throws IOException {
        simpleParse();
    }

    public void simpleParse() {
        Section section = new Section();
        long i = 1;
        try (Writer writer = new FileWriterWithEncoding("result", "UTF-8");
                BufferedReader br = new BufferedReader(new FileReader(new File("duplicates.blob")))) {
            for (String line = br.readLine();line != null; line = br.readLine()) {
                if (line.contains("SE")) {
                    if (!section.list.isEmpty()) {
                        EnrichedDocumentLite headDoc = section.getList().get(0);
                        int idHead = headDoc.getId();
                        if (repository.getDocMarked()[idHead]) {
                            continue;
                        } else {
                            repository.getDocMarked()[idHead] = true;
                        }
                        section.head = headDoc;
                        String authorHead = repository.getString(headDoc.getAuthorId());
                        Set<String> aTokensHead = stringService.getSimHashTokens(authorHead);
                        String titleHead = repository.getString(headDoc.getTitleId());
                        Set<String> tTokensHead = stringService.getSimHashTokens(titleHead);
                        EnrichedDocumentLite[] results = new EnrichedDocumentLite[section.getList().size()];
                        AtomicInteger count = new AtomicInteger(1);
                        section.getList().parallelStream().forEach(docIt -> {
                            int id = docIt.getId();
                            if (repository.getDocMarked()[id]) {
                                return;
                            }

                            if (docIt == headDoc) {
                                return;
                            }

                            String aStringIt = repository.getString(docIt.getAuthorId());
                            Set<String> aTokensIt = stringService.getSimHashTokens(aStringIt);
                            double authorTokensRatio = aTokensHead.size() * 1.0 / aTokensIt.size();
                            if (authorTokensRatio > 1) {
                                authorTokensRatio = 1.0 / authorTokensRatio;
                            }

                            if (authorTokensRatio >= 0.4) {

                                String tStringIt = repository.getString(docIt.getTitleId());
                                Set<String> tTokensIt = stringService.getSimHashTokens(tStringIt);
                                double titleTokensRatio = tTokensHead.size() * 1.0 / tTokensIt.size();
                                if (titleTokensRatio > 1) {
                                    titleTokensRatio = 1.0 / titleTokensRatio;
                                }

                                if (authorTokensRatio + titleTokensRatio >= 0.7 * 2) {
                                    double ratio = stringService.distance(aTokensHead, aTokensIt) + stringService.distance(tTokensHead, tTokensIt);
                                    if (ratio >= 1.4) {
                                        EnrichedDocumentLite clone = docIt.clone();
                                        results[count.getAndIncrement()] = clone;
                                        clone.setRatio(ratio / 2);
                                        repository.getDocMarked()[id] = true;
                                    }
                                }
                            }
                        });

                        section.list = Arrays.stream(results).parallel()
                                .filter(it -> it != null)
                                .collect(Collectors.toList());
                        try {
                            writer.write("SE\n");
                            writer.write(section.head.getId() + "\n");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        section.list.forEach(itResult -> {
                            try {
                                writer.write(itResult.getId() + "|" + itResult.getRatio() + "\n");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });

                        section = new Section();
                    }
                } else {
                    int id = Integer.parseInt(line);
                    if (id < 25000000) {
                        section.list.add(repository.getDocStorage()[id]);
                    }
                }
                i++;
                if (i % 1000000 == 0) {
                    System.out.println("Collecting " + i / 1000000 + "M\n");
                    writer.flush();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    class Section {
        EnrichedDocumentLite head;

        List<EnrichedDocumentLite> list = Lists.mutable.<EnrichedDocumentLite>empty();

        public List<EnrichedDocumentLite> getList() {
            return list;
        }

        public void setList(List<EnrichedDocumentLite> list) {
            this.list = list;
        }

        public EnrichedDocumentLite getHead() {
            return head;
        }

        public void setHead(EnrichedDocumentLite head) {
            this.head = head;
        }
    }

}
