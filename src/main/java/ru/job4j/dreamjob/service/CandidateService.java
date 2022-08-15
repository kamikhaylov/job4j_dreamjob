package ru.job4j.dreamjob.service;

import net.jcip.annotations.ThreadSafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.job4j.dreamjob.dream.model.Candidate;
import ru.job4j.dreamjob.persistence.CandidateDbStore;

import java.util.Collection;
import java.util.List;

@ThreadSafe
@Service
public class CandidateService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CandidateService.class.getName());

    private final CandidateDbStore store;
    private final CityService cityService;

    public CandidateService(CandidateDbStore store, CityService cityService) {
        this.store = store;
        this.cityService = cityService;
    }

    public Collection<Candidate> findAll() {
        LOGGER.info("CandidateService.findAll");
        List<Candidate> candidates = store.findAll();
        candidates.forEach(
                candidate -> candidate.setCity(
                        cityService.findById(candidate.getCity().getId())
                )
        );
        return candidates;
    }

    public boolean add(Candidate candidate) {
        LOGGER.info("CandidateService.add");
        return store.add(candidate);
    }

    public Candidate findById(int id) {
        return store.findById(id);
    }

    public boolean update(Candidate candidate) {
        return store.update(candidate);
    }
}
