package es.upo.witzl.proyectotfg.projects.service;

import es.upo.witzl.proyectotfg.projects.model.Label;

import java.util.List;

public interface ILabelService {

    List<Label> getLabels(String searchStr);

    Label getByName(String name);

    boolean existsByName(String name);

    Label createNewLabel(String name);
}
