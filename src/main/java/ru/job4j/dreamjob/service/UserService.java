package ru.job4j.dreamjob.service;

import net.jcip.annotations.ThreadSafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.job4j.dreamjob.dream.model.User;
import ru.job4j.dreamjob.persistence.UserDBStore;

import java.util.Collection;
import java.util.Optional;

@ThreadSafe
@Service
public class UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class.getName());

    private final UserDBStore store;

    public UserService(UserDBStore store) {
        this.store = store;
    }

    public Optional<User> add(User user) {
        LOGGER.info("UserService.add");
        return store.add(user);
    }

    public User findById(int id) {
        LOGGER.info("UserService.findById");
        return store.findById(id);
    }

    public Collection<User> findAll() {
        LOGGER.info("UserService.findAll");
        return store.findAll();
    }
}
