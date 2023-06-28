package com.example.demo.controller;

import com.example.demo.model.Tutorial;
import com.example.demo.service.TutorialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/tutorials")
public class TutorialController {

    @Autowired
    TutorialService tutorialService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Flux<Tutorial> getAllTutorials(@RequestParam(required = false) String title) {
        if (title == null)
            return tutorialService.findAll();
        else
            return tutorialService.findByTitleContaining(title);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Tutorial> getTutorialById(@PathVariable("id") int id) {
        return tutorialService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Tutorial> createTutorial(@RequestBody Tutorial tutorial) {
        return tutorialService.save(new Tutorial(tutorial.getTitle(), tutorial.getDescription(), false));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Tutorial> updateTutorial(@PathVariable("id") int id, @RequestBody Tutorial tutorial) {
        return tutorialService.update(id, tutorial);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteTutorial(@PathVariable("id") int id) {
        return tutorialService.deleteById(id);
    }

    @DeleteMapping()
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteAllTutorials() {
        return tutorialService.deleteAll();
    }

    @GetMapping("/published")
    @ResponseStatus(HttpStatus.OK)
    public Flux<Tutorial> findByPublished() {
        return tutorialService.findByPublished(true);
    }
}
