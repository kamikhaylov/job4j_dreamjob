package ru.job4j.dreamjob.service;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.dreamjob.dream.model.Candidate;
import ru.job4j.dreamjob.persistence.CandidateStore;

import java.util.Collection;

@ThreadSafe
@Service
public class CandidateService {
    private final CandidateStore store;

    public CandidateService(CandidateStore store) {
        this.store = store;
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
