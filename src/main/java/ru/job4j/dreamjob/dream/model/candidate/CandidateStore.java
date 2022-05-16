package ru.job4j.dreamjob.dream.model.candidate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

public class CandidateStore {
    private static final Logger LOGGER = Logger.getLogger(CandidateStore.class.getName());
    private static final CandidateStore INST = new CandidateStore();

    private final Map<Integer, Candidate> candidates = new ConcurrentHashMap<>();
    private final AtomicInteger atomicInteger = new AtomicInteger(3);

    private CandidateStore() {
        candidates.put(1, new Candidate(1, "Petr", "Junior Java Job", "2022-01-01"));
        candidates.put(2, new Candidate(2, "Ivan", "Middle Java Job", "2022-01-02"));
        candidates.put(3, new Candidate(3, "Alex", "Senior Java Job", "2022-01-03"));
    }

    public static CandidateStore instOf() {
        return INST;
    }

    public Collection<Candidate> findAll() {
        return candidates.values();
    }

    public Candidate findById(int id) {
        return candidates.get(id);
    }

    public boolean add(String name, String description) {
        int indexId = atomicInteger.incrementAndGet();
        candidates.put(indexId, new Candidate(indexId, name, description, createDate()));
        LOGGER.info("indexId : " + indexId + ", name : " + name + ", description : " + description);
        return true;
    }

    public boolean update(Candidate candidate) {
        candidates.put(candidate.getId(), new Candidate(candidate.getId(), candidate.getName(),
                candidate.getDescription(), candidates.get(candidate.getId()).getCreated()));
        return true;
    }

    private String createDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        String date = dateFormat.format(calendar.getTime());
        LOGGER.info("createDate : " + date);
        return date;
    }
}
