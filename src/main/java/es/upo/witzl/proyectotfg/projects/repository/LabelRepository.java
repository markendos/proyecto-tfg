package es.upo.witzl.proyectotfg.projects.repository;

import es.upo.witzl.proyectotfg.projects.model.Label;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LabelRepository extends JpaRepository<Label, String> {

    Label findByName(String name);

    List<Label> findByNameContaining(String name);
}
