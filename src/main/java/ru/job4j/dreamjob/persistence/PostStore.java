package ru.job4j.dreamjob.persistence;

import net.jcip.annotations.ThreadSafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.dream.model.City;
import ru.job4j.dreamjob.dream.model.Post;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@ThreadSafe
@Repository
public class PostStore {
    private static final Logger LOGGER = LoggerFactory.getLogger(PostStore.class.getName());

    private final Map<Integer, Post> posts = new ConcurrentHashMap<>();
    private final AtomicInteger atomicInteger = new AtomicInteger(3);

    private PostStore() {
        posts.put(1, new Post(1, "Junior", "Junior Java Job", LocalDateTime.now(),
                true, new City(1, "Москва")));
        posts.put(2, new Post(2, "Middle", "Middle Java Job", LocalDateTime.now(),
                true, new City(1, "Москва")));
        posts.put(3, new Post(3, "Senior", "Senior Java Job", LocalDateTime.now(),
                true, new City(1, "Москва")));
    }

    public Collection<Post> findAll() {
        return posts.values();
    }

    public Post findById(int id) {
        return posts.get(id);
    }

    public boolean add(Post post) {
        int newId = atomicInteger.incrementAndGet();
        post.setId(newId);
        post.setCreated(LocalDateTime.now());
        posts.put(newId, post);
        LOGGER.info("PostStore.add : newId : " + post);
        return true;
    }

    public boolean update(Post post) {
        post.setCreated(posts.get(post.getId()).getCreated());
        posts.replace(post.getId(), post);
        LOGGER.info("PostStore.update : " + post);
        return true;
    }
}