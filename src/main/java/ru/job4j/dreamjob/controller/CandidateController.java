package ru.job4j.dreamjob.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.job4j.dreamjob.dream.model.candidate.Candidate;
import ru.job4j.dreamjob.dream.model.candidate.CandidateStore;

import javax.servlet.http.HttpServletRequest;
import java.util.logging.Logger;

@Controller
public class CandidateController {
    private static final Logger LOGGER = Logger.getLogger(CandidateController.class.getName());
    private final CandidateStore store = CandidateStore.instOf();

    @GetMapping("/candidates")
    public String candidates(Model model) {
        model.addAttribute("candidates", store.findAll());
        return "candidates";
    }

    @GetMapping("/formAddCandidate")
    public String addCandidate(Model model) {
        model.addAttribute("candidate", new Candidate(0, "Заполните поле", "Заполните поле",
                "Заполните поле"));
        return "addCandidate";
    }

    @PostMapping("/createCandidate")
    public String createPost(HttpServletRequest req) {
        String name = req.getParameter("name");
        String description = req.getParameter("description");
        LOGGER.info("name : " + name + ", description : " + description);
        store.add(name, description);
        return "redirect:/candidates";
    }

    @GetMapping("/formUpdateCandidate/{candidateId}")
    public String formUpdatePost(Model model, @PathVariable("candidateId") int id) {
        model.addAttribute("candidate", store.findById(id));
        return "updateCandidate";
    }

    @PostMapping("/updateCandidate")
    public String updateCandidate(@ModelAttribute Candidate candidate) {
        store.update(candidate);
        return "redirect:/candidates";
    }
}
