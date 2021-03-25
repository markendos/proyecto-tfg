package es.upo.witzl.proyectotfg.projects.service;

import es.upo.witzl.proyectotfg.projects.dto.CollaborationRequestDto;
import es.upo.witzl.proyectotfg.projects.model.CollaborationRequest;
import es.upo.witzl.proyectotfg.projects.model.CollaborationRequestKey;
import es.upo.witzl.proyectotfg.projects.model.Project;
import es.upo.witzl.proyectotfg.projects.repository.CollaborationRequestRepository;
import es.upo.witzl.proyectotfg.users.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CollaborationRequestService implements ICollaborationRequestService{

    @Autowired
    private CollaborationRequestRepository collaborationRequestRepository;

    @Override
    public Optional<CollaborationRequest> getCollaborationRequestById(String collaboratorEmail, Long projectId) {
        CollaborationRequestKey crk = new CollaborationRequestKey();
        crk.setCollaboratorId(collaboratorEmail);
        crk.setProjectId(projectId);
        return collaborationRequestRepository.findById(crk);
    }

    @Override
    public CollaborationRequest requestCollaboration(CollaborationRequestDto crDto, User user, Project project) {
        CollaborationRequest cr = new CollaborationRequest(user, project);
        cr.setCollaborator(user);
        cr.setProject(project);
        cr.setRequestMessage(crDto.getRequestMessage());
        cr.setRequestStatus("created");
        return collaborationRequestRepository.save(cr);
    }

    @Override
    public List<CollaborationRequest> getPendingRequests(User user) {
        return collaborationRequestRepository.findByProjectInAndRequestStatusLike(user.getOwnedProjects(),"created");
    }

    @Override
    public CollaborationRequest updateCollaborationRequest(CollaborationRequest cr) {
        return collaborationRequestRepository.save(cr);
    }

    @Override
    public boolean isCollaborator(Project project, User user) {
        CollaborationRequest cr = new CollaborationRequest(user, project);
        return collaborationRequestRepository.existsByIdAndRequestStatusLike(cr.getId(), "approved");
    }

    @Override
    public void deleteProjectCollaborations(Project project) {
        for(CollaborationRequest cr : project.getCollaborationRequests()) {
            collaborationRequestRepository.delete(cr);
        }
    }
}
