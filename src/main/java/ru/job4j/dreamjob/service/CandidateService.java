package ru.job4j.dreamjob.service;

import ru.job4j.dreamjob.dream.model.Candidate;
import ru.job4j.dreamjob.persistence.CandidateStore;

import java.util.Collection;

public class CandidateService {
    private static final CandidateService INST = new CandidateService();
    private final CandidateStore store = CandidateStore.instOf();

    private CandidateService() {
    }

    public static CandidateService instOf() {
        return INST;
    }

    public Collection<Candidate> findAll() {
        return store.findAll();
    }

    public boolean add(Candidate candidate) {
        return store.add(candidate);
    }

    public Candidate findById(int id) {
        return store.findById(id);
    }

    public boolean update(Candidate candidate) {
        return store.update(candidate);
    }
}
