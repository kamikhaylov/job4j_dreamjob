package ru.job4j.dreamjob.persistence;

import ru.job4j.dreamjob.dream.model.Post;
import ru.job4j.dreamjob.dream.util.DateUtil;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

public class PostStore {
    private static final Logger LOGGER = Logger.getLogger(PostStore.class.getName());
    private static final PostStore INST = new PostStore();

    private final Map<Integer, Post> posts = new ConcurrentHashMap<>();
    private final AtomicInteger atomicInteger = new AtomicInteger(3);

    private PostStore() {
        posts.put(1, new Post(1, "Junior", "Junior Java Job", "2022-01-01"));
        posts.put(2, new Post(2, "Middle", "Middle Java Job", "2022-01-02"));
        posts.put(3, new Post(3, "Senior", "Senior Java Job", "2022-01-03"));
    }

    public static PostStore instOf() {
        return INST;
    }

    public Collection<Post> findAll() {
        return posts.values();
    }

    public Post findById(int id) {
        return posts.get(id);
    }

    public boolean add(String name, String description) {
        int indexId = atomicInteger.incrementAndGet();
        posts.put(indexId, new Post(indexId, name, description, DateUtil.createDate()));
        LOGGER.info("indexId : " + indexId + ", name : " + name + ", description : " + description);
        return true;
    }

    public boolean update(Post post) {
        posts.put(post.getId(), new Post(post.getId(), post.getName(), post.getDescription(),
                posts.get(post.getId()).getCreated()));
        return true;
    }
}