package es.upo.witzl.proyectotfg.projects.service;

import es.upo.witzl.proyectotfg.projects.dto.CollaborationRequestDto;
import es.upo.witzl.proyectotfg.projects.model.CollaborationRequest;
import es.upo.witzl.proyectotfg.projects.model.Project;
import es.upo.witzl.proyectotfg.projects.repository.CollaborationRequestRepository;
import es.upo.witzl.proyectotfg.users.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CollaborationRequestService implements ICollaborationRequestService{

    @Autowired
    private CollaborationRequestRepository collaborationRequestRepository;

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
}
