package com.MeloTech.label;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("users/{userId}/projects/{projectId}/labels")
public class LabelController {
    private final LabelService labelService;

    @Autowired

    public LabelController(LabelService labelService) {
        this.labelService = labelService;
    }

        //    create
    @PostMapping("")
    public Label createLabel(@RequestBody Label label) {
        return labelService.createLabel(label);
    }

    //    getall
    @GetMapping("")
    public List<Label> getAll() {
        return labelService.getAllLabels();
    }

    //    getbyid
    @GetMapping("/{id}")
    public Optional<Label> getLabelById(@PathVariable String id) {
        return labelService.getLabelById(id);
    }

    //    update
    @PutMapping("/{id}")
    public Label updateLabel(@PathVariable String id, @RequestBody Label labelDetails) {
        return labelService.updateLabel(id, labelDetails);
    }

    //    delete
    @DeleteMapping("/{id}")
    public void deleteLabel(@PathVariable String id) {
        labelService.deleteTask(id);
    }
}
