package ru.job4j.dreamjob.controllers;

import net.jcip.annotations.ThreadSafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ru.job4j.dreamjob.dream.model.Candidate;
import ru.job4j.dreamjob.service.CandidateService;
import ru.job4j.dreamjob.service.CityService;
import ru.job4j.dreamjob.util.DreamJobSession;

import javax.servlet.http.HttpSession;
import java.io.IOException;

@ThreadSafe
@Controller
public class CandidateController {
    private static final Logger LOGGER =
            LoggerFactory.getLogger(CandidateController.class.getName());
    private final CandidateService candidateService;
    private final CityService cityService;

    public CandidateController(CandidateService candidateService, CityService cityService) {
        this.candidateService = candidateService;
        this.cityService = cityService;
    }

    @GetMapping("/candidates")
    public String candidates(Model model, HttpSession session) {
        LOGGER.info("CandidateController.candidates");
        DreamJobSession.check(model, session);
        model.addAttribute("candidates", candidateService.findAll());
        return "candidates";
    }

    @GetMapping("/formAddCandidate")
    public String addCandidate(Model model, HttpSession session) {
        LOGGER.info("CandidateController.addCandidate");
        DreamJobSession.check(model, session);
        model.addAttribute("candidate", new Candidate(0, "Заполните поле", "Заполните поле",
                null));
        model.addAttribute("cities", cityService.getAllCities());
        return "addCandidate";
    }

    @PostMapping("/createCandidate")
    public String createCandidate(@ModelAttribute Candidate candidate,
                                  @RequestParam("file") MultipartFile file) throws IOException {
        LOGGER.info("CandidateController.createCandidate : name : " + candidate.getName()
                + ", description : " + candidate.getDescription()
                + ", city.id : " + candidate.getCity().getId()
                + ", file.name : "  + file.getName());
        candidate.setPhoto(file.getBytes());
        candidate.setCity(cityService.findById(candidate.getCity().getId()));
        candidateService.add(candidate);
        return "redirect:/candidates";
    }

    @GetMapping("/formUpdateCandidate/{candidateId}")
    public String formUpdateCandidate(Model model, @PathVariable("candidateId") int id,
                                      HttpSession session) {
        LOGGER.info("CandidateController.formUpdateCandidate");
        DreamJobSession.check(model, session);
        model.addAttribute("candidate", candidateService.findById(id));
        model.addAttribute("cities", cityService.getAllCities());
        return "updateCandidate";
    }

    @PostMapping("/updateCandidate")
    public String updateCandidate(@ModelAttribute Candidate candidate,
                                  @RequestParam() MultipartFile file) throws IOException {
        LOGGER.info("CandidateController.updateCandidate");
        candidate.setCity(cityService.findById(candidate.getCity().getId()));
        candidate.setPhoto(file.getBytes());
        candidateService.update(candidate);
        return "redirect:/candidates";
    }

    @GetMapping("/photoCandidate/{candidateId}")
    public ResponseEntity<Resource> download(@PathVariable("candidateId") Integer candidateId) {
        Candidate candidate = candidateService.findById(candidateId);
        return ResponseEntity.ok()
                .headers(new HttpHeaders())
                .contentLength(candidate.getPhoto().length)
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(new ByteArrayResource(candidate.getPhoto()));
    }
}
