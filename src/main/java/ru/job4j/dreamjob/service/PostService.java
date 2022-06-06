package ru.job4j.dreamjob.service;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.dreamjob.dream.model.Post;
import ru.job4j.dreamjob.persistence.PostDBStore;

import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

@ThreadSafe
@Service
public class PostService {
    private static final Logger LOGGER = Logger.getLogger(PostService.class.getName());
    private final PostDBStore store;
    private final CityService cityService;

    public PostService(PostDBStore store, CityService cityService) {
        this.store = store;
        this.cityService = cityService;
    }

    public Collection<Post> findAll() {
        LOGGER.info("PostService.findAll");
        List<Post> posts = store.findAll();
        posts.forEach(
                post -> post.setCity(
                        cityService.findById(post.getCity().getId())
                )
        );
        return posts;
    }

    public boolean add(Post post) {
        LOGGER.info("PostService.add");
        return store.add(post);
    }

    public Post findById(int id) {
        return store.findById(id);
    }

    public boolean update(Post post) {
        return store.update(post);
    }
}
