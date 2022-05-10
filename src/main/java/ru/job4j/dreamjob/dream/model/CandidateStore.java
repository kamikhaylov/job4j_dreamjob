package ru.job4j.dreamjob.dream.model;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CandidateStore {
    private static final CandidateStore INST = new CandidateStore();

    private final Map<Integer, Candidate> candidates = new ConcurrentHashMap<>();

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
}
