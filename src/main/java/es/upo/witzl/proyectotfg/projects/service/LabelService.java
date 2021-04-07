package es.upo.witzl.proyectotfg.projects.service;

import es.upo.witzl.proyectotfg.projects.model.Label;
import es.upo.witzl.proyectotfg.projects.repository.LabelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LabelService implements ILabelService{

    @Autowired
    private LabelRepository labelRepository;

    @Override
    public List<Label> getLabels(String searchStr) {
        if(searchStr.isEmpty()) {
            return labelRepository.findAll();
        }else {
            return labelRepository.findByNameContaining(searchStr);
        }
    }

    @Override
    public Label getByName(String name) {
        return labelRepository.findByName(name.toLowerCase());
    }

    @Override
    public boolean existsByName(String name) {
        return labelRepository.findByName(name) != null;
    }

    @Override
    public Label createNewLabel(String name) {
        Label label = new Label();
        label.setName(name);

        return labelRepository.save(label);
    }
}
