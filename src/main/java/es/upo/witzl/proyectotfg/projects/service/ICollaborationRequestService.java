package es.upo.witzl.proyectotfg.projects.service;

import es.upo.witzl.proyectotfg.projects.dto.CollaborationRequestDto;
import es.upo.witzl.proyectotfg.projects.model.CollaborationRequest;
import es.upo.witzl.proyectotfg.projects.model.Project;
import es.upo.witzl.proyectotfg.users.model.User;

import java.util.List;

public interface ICollaborationRequestService {

    CollaborationRequest requestCollaboration(CollaborationRequestDto crDto, User user, Project project);

    List<CollaborationRequest> getPendingRequests(User user);
}
