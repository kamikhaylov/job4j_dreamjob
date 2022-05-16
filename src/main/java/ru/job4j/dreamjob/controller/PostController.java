package ru.job4j.dreamjob.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import ru.job4j.dreamjob.dream.model.post.Post;
import ru.job4j.dreamjob.dream.model.post.PostStore;

import javax.servlet.http.HttpServletRequest;
import java.util.logging.Logger;

@Controller
public class PostController {
    private static final Logger LOGGER = Logger.getLogger(PostController.class.getName());
    private final PostStore store = PostStore.instOf();

    @GetMapping("/posts")
    public String posts(Model model) {
        model.addAttribute("posts", store.findAll());
        return "posts";
    }

    @GetMapping("/formAddPost")
    public String addPost(Model model) {
        model.addAttribute("post", new Post(0, "Заполните поле", "Заполните поле",
                "Заполните поле"));
        return "addPost";
    }

    @PostMapping("/createPost")
    public String createPost(HttpServletRequest req) {
        String name = req.getParameter("name");
        String description = req.getParameter("description");
        LOGGER.info("name : " + name + ", description : " + description);
        store.add(name, description);
        return "redirect:/posts";
    }
}