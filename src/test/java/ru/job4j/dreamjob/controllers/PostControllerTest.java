package ru.job4j.dreamjob.controllers;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;
import ru.job4j.dreamjob.dream.model.City;
import ru.job4j.dreamjob.dream.model.Post;
import ru.job4j.dreamjob.service.CityService;
import ru.job4j.dreamjob.service.PostService;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PostControllerTest {
    @Mock
    private Model model;
    @Mock
    private PostService postService;
    @Mock
    private CityService cityService;
    @Mock
    private HttpSession httpSession;

    @BeforeEach
    public void before() {
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    public void after() {
        Mockito.reset(model, postService, cityService, httpSession);
    }

    @Test
    public void whenPosts() {
        List<Post> posts = Arrays.asList(
                new Post(1, "Junior", "Java Dev",
                        LocalDateTime.of(2022, 1, 1, 10, 0), true,
                        new City(1, "Москва")),
                new Post(2, "Middle", "Java Dev",
                        LocalDateTime.of(2022, 1, 1, 10, 0), true,
                        new City(1, "Москва"))
        );
        when(postService.findAll()).thenReturn(posts);
        PostController postController = new PostController(
                postService,
                cityService
        );
        String page = postController.posts(model, httpSession);

        verify(model).addAttribute("posts", posts);
        Assertions.assertEquals(page, "posts");
    }

    @Test
    public void whenAddPost() {
        Post post = new Post(0, "Заполните поле", "Заполните поле",
                null);
        PostController postController = new PostController(
                postService,
                cityService
        );
        String page = postController.addPost(model, httpSession);

        verify(model).addAttribute("post", post);
        verify(model).addAttribute("cities", cityService.getAllCities());
        Assertions.assertEquals(page, "addPost");
    }

    @Test
    public void whenCreatePost() {
        Post input = new Post(1, "Junior", "Java Dev",
                LocalDateTime.of(2022, 1, 1, 10, 0), true,
                new City(1, "Москва"));
        PostController postController = new PostController(
                postService,
                cityService
        );
        String page = postController.createPost(input);

        verify(postService).add(input);
        Assertions.assertEquals(page, "redirect:/posts");
    }

    @Test
    public void whenFormUpdatePost() {
        int id = 0;
        PostController postController = new PostController(
                postService,
                cityService
        );
        String page = postController.formUpdatePost(model, id, httpSession);

        verify(model).addAttribute("post", postService.findById(id));
        verify(model).addAttribute("cities", cityService.getAllCities());
        Assertions.assertEquals(page, "updatePost");
    }

    @Test
    public void whenUpdatePost() {
        Post input = new Post(1, "Junior", "Java Dev",
                LocalDateTime.of(2022, 1, 1, 10, 0), true,
                new City(1, "Москва"));
        PostController postController = new PostController(
                postService,
                cityService
        );
        String page = postController.updatePost(input);

        verify(postService).update(input);
        Assertions.assertEquals(page, "redirect:/posts");
    }
}