package es.upo.witzl.proyectotfg.projects.repository;

import es.upo.witzl.proyectotfg.projects.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {
}
