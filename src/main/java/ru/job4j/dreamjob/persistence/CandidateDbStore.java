package ru.job4j.dreamjob.persistence;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.dream.model.Candidate;
import ru.job4j.dreamjob.dream.model.City;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository 
public class CandidateDbStore {
    private static final Logger LOGGER = LoggerFactory.getLogger(CandidateDbStore.class.getName());
    private final BasicDataSource pool;

    public CandidateDbStore(BasicDataSource pool) {
        this.pool = pool;
    }

    public List<Candidate> findAll() {
        LOGGER.info("CandidateDbStore.findAll");

        List<Candidate> candidates = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement("SELECT * FROM candidate")
        ) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    candidates.add(new Candidate(
                            it.getInt("id"),
                            it.getString("name"),
                            it.getString("description"),
                            it.getTimestamp("created").toLocalDateTime(),
                            it.getBoolean("visible"),
                            new City(it.getInt("city_id"), null)));
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        LOGGER.info("CandidateDbStore.findAll.result : " + candidates.toString());
        return candidates;
    }

    public boolean add(Candidate candidate) {
        LOGGER.info("CandidateDbStore.add");

        boolean result = false;
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement(
                     "INSERT INTO candidate(name, description, created, visible, city_id, photo) "
                             + "VALUES (?, ?, ?, ?, ?, ?)",
                     PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, candidate.getName());
            ps.setString(2, candidate.getDescription());
            ps.setTimestamp(3, getCreated(candidate));
            ps.setBoolean(4, candidate.isVisible());
            ps.setInt(5, candidate.getCity().getId());
            ps.setString(6, encodeBase64(candidate.getPhoto()));
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    candidate.setId(id.getInt(1));
                }
            }
            result = true;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        LOGGER.info("CandidateDbStore.add.result : " + result);
        return result;
    }

    public boolean update(Candidate candidate) {
        LOGGER.info("CandidateDbStore.update : " + candidate.toString());

        boolean result = false;
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(
                     "UPDATE candidate SET name = ?, description = ?, created = ?, visible = ?, "
                             + "city_id = ?, photo = ? WHERE id = ?")
        ) {
            ps.setString(1, candidate.getName());
            ps.setString(2, candidate.getDescription());
            ps.setTimestamp(3, getCreated(candidate));
            ps.setBoolean(4, candidate.isVisible());
            ps.setInt(5, candidate.getCity().getId());
            ps.setString(6, encodeBase64(candidate.getPhoto()));
            ps.setInt(7, candidate.getId());
            ps.executeUpdate();
            result = true;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        LOGGER.info("CandidateDbStore.add.update : " + result);
        return result;
    }

    public Candidate findById(int id) {
        LOGGER.info("CandidateDbStore.findById.id : " + id);

        Candidate candidate = null;
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =
                     cn.prepareStatement("SELECT * FROM candidate WHERE id = ?")
        ) {
            ps.setInt(1, id);
            try (ResultSet it = ps.executeQuery()) {
                if (it.next()) {
                    candidate = new Candidate(
                            it.getInt("id"),
                            it.getString("name"),
                            it.getString("description"),
                            it.getTimestamp("created").toLocalDateTime(),
                            it.getBoolean("visible"),
                            new City(it.getInt("city_id"), null),
                            decodeBase64(it.getString("photo")));
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }

        LOGGER.info("CandidateDbStore.findById.candidate : " + candidate);
        return candidate;
    }

    private String encodeBase64(byte[] photo) {
        Base64 base64 = new Base64();
        return Objects.nonNull(photo) ? base64.encodeToString(photo) : "";
    }

    private byte[] decodeBase64(String photo) {
        Base64 base64 = new Base64();
        return StringUtils.isNotEmpty(photo) ? base64.decode(photo) : null;
    }

    private Timestamp getCreated(Candidate candidate) {
        return Timestamp.valueOf(
                Objects.nonNull(candidate.getCreated())
                        ? candidate.getCreated() : LocalDateTime.now());
    }
}
