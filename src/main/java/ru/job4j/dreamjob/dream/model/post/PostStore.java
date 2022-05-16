package ru.job4j.dreamjob.dream.model.post;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class PostStore {
    private static final Logger LOGGER = Logger.getLogger(PostStore.class.getName());
    private static final PostStore INST = new PostStore();
    private int indexId = 3;

    private final Map<Integer, Post> posts = new ConcurrentHashMap<>();

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
        indexId++;
        posts.put(indexId, new Post(indexId, name, description, createDate()));
        LOGGER.info("indexId : " + indexId + ", name : " + name + ", description : " + description);
        return true;
    }

    public boolean update(Post post) {
        posts.put(post.getId(), new Post(post.getId(), post.getName(), post.getDescription(),
                posts.get(post.getId()).getCreated()));
        return true;
    }

    private String createDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        LOGGER.info("createDate : " + dateFormat.format(calendar.getTime()));
        return dateFormat.format(calendar.getTime());
    }
}