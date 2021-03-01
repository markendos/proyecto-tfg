package es.upo.witzl.proyectotfg.projects.repository;

import es.upo.witzl.proyectotfg.projects.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
}
