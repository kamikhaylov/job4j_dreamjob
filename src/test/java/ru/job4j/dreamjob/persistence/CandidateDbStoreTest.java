package ru.job4j.dreamjob.persistence;

import org.apache.commons.dbcp2.BasicDataSource;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.job4j.dreamjob.Main;
import ru.job4j.dreamjob.dream.model.Candidate;
import ru.job4j.dreamjob.dream.model.City;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

public class CandidateDbStoreTest {
    private static final String IMG_PATH = "./src/test/resources/image/image.png";
    private static final String IMG_UPDATE_PATH = "./src/test/resources/image/imageUpdate.png";

    private static BasicDataSource pool;

    @BeforeClass
    public static void before() {
        pool = new Main().loadPool();
    }

    @AfterMethod
    public void after() throws SQLException {
        try (PreparedStatement st = pool.getConnection().prepareStatement(
                "DELETE FROM candidate;"
                        + "ALTER TABLE candidate ALTER COLUMN id RESTART WITH 1;")) {
            st.execute();
        }
    }

    @DataProvider(name = "candidateProvider")
    private Object[][] dataCandidate() {
        return new Object[][] {
                {
                        new Candidate(1, "Petr", "desc", LocalDateTime.now(), true,
                                new City(1, "Москва"))
                },
                {
                        new Candidate(2, "Ivan", "Dev", LocalDateTime.now(), false,
                                new City(2, "Спб"))
                },
                {
                        new Candidate(2, "Ivan", "Dev", LocalDateTime.now(), false,
                                new City(2, "Спб"))
                }
        };
    }

    @Test(dataProvider = "candidateProvider")
    public void whenCreateCandidate(Candidate candidate) {
        CandidateDbStore store = new CandidateDbStore(pool);

        boolean addResult = store.add(candidate);
        Candidate candidateInDb = store.findById(candidate.getId());
        List<Candidate> candidates = store.findAll();

        assertTrue(addResult);
        assertNotNull(candidateInDb);
        assertEquals(candidates.size(), 1);
        assertEquals(candidateInDb.getId(), candidate.getId());
        assertEquals(candidateInDb.getName(), candidate.getName());
        assertEquals(candidateInDb.getDescription(), candidate.getDescription());
        assertEquals(candidateInDb.getCreated(), candidate.getCreated());
        assertEquals(candidateInDb.getCity().getId(), candidate.getCity().getId());
    }

    @Test()
    public void whenUpdateCandidate() {
        CandidateDbStore store = new CandidateDbStore(pool);
        Candidate candidate = new Candidate(1, "Petr", "desc",
                LocalDateTime.now(), true, new City(1, "Москва"));
        boolean addResult = store.add(candidate);
        Candidate updatedCandidate = new Candidate(candidate.getId(), "Ivan", "Java",
                LocalDateTime.now(), true, new City(2, "Спб"));

        boolean updateResult = store.update(updatedCandidate);
        Candidate candidateInDb = store.findById(candidate.getId());
        List<Candidate> candidates = store.findAll();

        assertTrue(addResult);
        assertTrue(updateResult);
        assertNotNull(candidateInDb);
        assertEquals(candidates.size(), 1);
        assertEquals(candidateInDb.getId(), candidate.getId());
        assertEquals(candidateInDb.getId(), updatedCandidate.getId());
        assertEquals(candidateInDb.getName(), updatedCandidate.getName());
        assertEquals(candidateInDb.getDescription(), updatedCandidate.getDescription());
        assertEquals(candidateInDb.getCreated(), updatedCandidate.getCreated());
        assertEquals(candidateInDb.getCity().getId(), updatedCandidate.getCity().getId());
    }

    @Test
    public void whenAddPhoto() throws IOException {
        CandidateDbStore store = new CandidateDbStore(pool);
        Candidate candidate = new Candidate(1, "Petr", "desc", LocalDateTime.now(), true,
                new City(1, "Москва"), createPhoto(IMG_PATH));

        boolean addResult = store.add(candidate);
        Candidate candidateInDb = store.findById(candidate.getId());
        List<Candidate> candidates = store.findAll();

        assertTrue(addResult);
        assertNotNull(candidateInDb);
        assertNotNull(candidateInDb.getPhoto());
        assertEquals(candidates.size(), 1);
        assertEquals(candidateInDb.getPhoto(), candidate.getPhoto());
    }

    @Test
    public void whenUpdatePhoto() throws IOException {
        CandidateDbStore store = new CandidateDbStore(pool);
        Candidate candidate = new Candidate(1, "Petr", "desc",
                LocalDateTime.now(), true, new City(1, "Москва"), createPhoto(IMG_PATH));
        boolean addResult = store.add(candidate);
        Candidate updatedCandidate = new Candidate(candidate.getId(), "Petr", "desc",
                LocalDateTime.now(), true, new City(1, "Москва"), createPhoto(IMG_UPDATE_PATH));

        boolean updateResult = store.update(updatedCandidate);
        Candidate candidateInDb = store.findById(candidate.getId());
        List<Candidate> candidates = store.findAll();

        assertTrue(addResult);
        assertTrue(updateResult);
        assertNotNull(candidateInDb);
        assertEquals(candidates.size(), 1);
        assertEquals(candidateInDb.getId(), candidate.getId());
        assertEquals(candidateInDb.getId(), updatedCandidate.getId());
        assertEquals(candidateInDb.getPhoto(), updatedCandidate.getPhoto());
    }

    private byte[] createPhoto(String path) throws IOException {
        return Files.readAllBytes(Paths.get(path));
    }
}