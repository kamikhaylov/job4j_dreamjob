package ru.job4j.dreamjob.service;

import ru.job4j.dreamjob.dream.model.Post;
import ru.job4j.dreamjob.persistence.PostStore;

import java.util.Collection;

public class PostService {
    private static final PostService INST = new PostService();
    private final PostStore store = PostStore.instOf();

    private PostService() {
    }

    public static PostService instOf() {
        return INST;
    }

    public Collection<Post> findAll() {
        return store.findAll();
    }

    public boolean add(Post post) {
        return store.add(post);
    }

    public Post findById(int id) {
        return store.findById(id);
    }

    public boolean update(Post post) {
        return store.update(post);
    }
}
