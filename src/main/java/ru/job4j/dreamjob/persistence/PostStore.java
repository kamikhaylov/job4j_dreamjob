package ru.job4j.dreamjob.persistence;

import ru.job4j.dreamjob.dream.model.Post;

import java.util.Calendar;
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
        posts.put(1, new Post(1, "Junior", "Junior Java Job", Calendar.getInstance().getTime()));
        posts.put(2, new Post(2, "Middle", "Middle Java Job", Calendar.getInstance().getTime()));
        posts.put(3, new Post(3, "Senior", "Senior Java Job", Calendar.getInstance().getTime()));
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
        int newId = atomicInteger.incrementAndGet();
        Calendar calendar = Calendar.getInstance();
        posts.put(newId, new Post(newId, name, description, calendar.getTime()));
        LOGGER.info("PostStore.add : newId : " + newId + ", name : " + name
                + ", description : " + description + ", create : " + calendar.getTime());
        return true;
    }

    public boolean update(Post post) {
        post.setCreated(posts.get(post.getId()).getCreated());
        posts.replace(post.getId(), post);
        LOGGER.info("PostStore.update : " + post);
        return true;
    }
}