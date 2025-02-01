package com.MeloTech.label;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class LabelService {
    private final LabelRepository labelRepository;

    @Autowired
    LabelService(LabelRepository labelRepository) {
        this.labelRepository = labelRepository;
    }

    public Label createLabel(Label label) {

        Optional<Label> existingLabel = labelRepository.findByName(label.getName());

        if (existingLabel.isPresent()) {
            throw new IllegalArgumentException("Label name already exists");
        }

        return labelRepository.save(label);
    }

    public List<Label> getAllLabels() {
        return labelRepository.findAll();
    }

    public Optional<Label> getLabelById(String id) {
        return labelRepository.findById(id);
    }

    public Label updateLabel(String id, Label labelDetails) {
        return labelRepository.findById(id).map(label -> {
            Optional<Label> existingLabel = labelRepository.findByName(labelDetails.getName());

            if (existingLabel.isPresent() && !existingLabel.get().getId().equals(id)) {
                throw new IllegalArgumentException("Label name already exists");
            }
            label.setName(labelDetails.getName());
            label.setColor(labelDetails.getColor());
            return labelRepository.save(label);
        }).orElseThrow(() -> new RuntimeException("Label Not Found"));
    }
    public void deleteTask(String id){
        labelRepository.deleteById(id);
    }
}
