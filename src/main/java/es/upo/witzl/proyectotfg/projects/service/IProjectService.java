package es.upo.witzl.proyectotfg.projects.service;

import es.upo.witzl.proyectotfg.projects.dto.CollaborationRequestDto;
import es.upo.witzl.proyectotfg.projects.dto.ProjectDto;
import es.upo.witzl.proyectotfg.projects.model.CollaborationRequest;
import es.upo.witzl.proyectotfg.projects.model.Project;
import es.upo.witzl.proyectotfg.users.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface IProjectService {

    Project registerNewProject(ProjectDto projectDto, User user);

    Optional<Project> getProjectById(Long id);

    List<Project> getOwnedProjects(User user);

    List<Project> getProjectsRequested(User user);

    List<Project> getProjectsApproved(User user);

    List<Project> getForeignProjects(User user);

    void assignLabels(Collection<String> labels, Project project);

    void deleteProject(Project project);
}
