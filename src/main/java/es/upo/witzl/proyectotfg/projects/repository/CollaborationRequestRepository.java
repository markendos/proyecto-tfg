package es.upo.witzl.proyectotfg.projects.repository;

import es.upo.witzl.proyectotfg.projects.model.CollaborationRequest;
import es.upo.witzl.proyectotfg.projects.model.CollaborationRequestKey;
import es.upo.witzl.proyectotfg.projects.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface CollaborationRequestRepository extends JpaRepository<CollaborationRequest, CollaborationRequestKey> {

    List<CollaborationRequest> findByProjectInAndRequestStatusLike(Collection<Project> projects, String status);

    boolean existsByIdAndRequestStatusLike(CollaborationRequestKey crk, String status);
}
