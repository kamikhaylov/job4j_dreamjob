package ru.job4j.dreamjob.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.job4j.dreamjob.dream.model.Post;
import ru.job4j.dreamjob.service.PostService;

import javax.servlet.http.HttpServletRequest;
import java.util.logging.Logger;

@Controller
public class PostController {
    private static final Logger LOGGER = Logger.getLogger(PostController.class.getName());
    private final PostService postService = PostService.instOf();

    @GetMapping("/posts")
    public String posts(Model model) {
        model.addAttribute("posts", postService.findAll());
        return "posts";
    }

    @GetMapping("/formAddPost")
    public String addPost(Model model) {
        model.addAttribute("post", new Post(0, "Заполните поле", "Заполните поле",
                null));
        return "addPost";
    }

    @PostMapping("/createPost")
    public String createPost(HttpServletRequest req) {
        String name = req.getParameter("name");
        String description = req.getParameter("description");
        LOGGER.info("PostController.createPost : name : " + name
                + ", description : " + description);
        postService.add(name, description);
        return "redirect:/posts";
    }

    @GetMapping("/formUpdatePost/{postId}")
    public String formUpdatePost(Model model, @PathVariable("postId") int id) {
        model.addAttribute("post", postService.findById(id));
        return "updatePost";
    }

    @PostMapping("/updatePost")
    public String updatePost(@ModelAttribute Post post) {
        postService.update(post);
        return "redirect:/posts";
    }
}