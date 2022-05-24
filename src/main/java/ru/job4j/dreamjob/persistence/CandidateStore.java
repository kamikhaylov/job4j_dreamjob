package ru.job4j.dreamjob.persistence;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.dream.model.Candidate;
import ru.job4j.dreamjob.dream.model.City;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

@ThreadSafe
@Repository
public class CandidateStore {
    private static final Logger LOGGER = Logger.getLogger(CandidateStore.class.getName());

    private final Map<Integer, Candidate> candidates = new ConcurrentHashMap<>();
    private final AtomicInteger atomicInteger = new AtomicInteger(3);

    private CandidateStore() {
        candidates.put(1, new Candidate(1, "Petr", "Junior Java Job",
                LocalDateTime.now(), true, new City(1, "Москва")));
        candidates.put(2, new Candidate(2, "Ivan", "Middle Java Job",
                LocalDateTime.now(), true, new City(1, "Москва")));
        candidates.put(3, new Candidate(3, "Alex", "Senior Java Job",
                LocalDateTime.now(), true, new City(1, "Москва")));
    }

    public Collection<Candidate> findAll() {
        return candidates.values();
    }

    public Candidate findById(int id) {
        return candidates.get(id);
    }

    public boolean add(Candidate candidate) {
        int newId = atomicInteger.incrementAndGet();
        candidate.setId(newId);
        candidate.setCreated(LocalDateTime.now());
        candidates.put(newId, candidate);
        LOGGER.info("CandidateStore.add : " + candidate);
        return true;
    }

    public boolean update(Candidate candidate) {
        candidate.setCreated(candidates.get(candidate.getId()).getCreated());
        candidates.replace(candidate.getId(), candidate);
        LOGGER.info("CandidateStore.update : " + candidate);
        return true;
    }
}
