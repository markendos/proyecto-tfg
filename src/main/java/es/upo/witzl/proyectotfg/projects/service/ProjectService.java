package es.upo.witzl.proyectotfg.projects.service;

import es.upo.witzl.proyectotfg.projects.dto.ProjectDto;
import es.upo.witzl.proyectotfg.projects.model.Label;
import es.upo.witzl.proyectotfg.projects.model.Project;
import es.upo.witzl.proyectotfg.projects.repository.ProjectRepository;
import es.upo.witzl.proyectotfg.users.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProjectService implements IProjectService{

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ILabelService labelService;

    @Override
    public Project registerNewProject(ProjectDto projectDto, User user) {
        final Project project = new Project();

        project.setName(projectDto.getName());
        project.setDescription(projectDto.getDescription());
        project.setStartDate(new Date());
        project.setUser(user);
        projectRepository.save(project);
        return project;
    }

    @Override
    public Optional<Project> getProjectById(Long id) {
        return projectRepository.findById(id);
    }

    @Override
    public List<Project> getOwnedProjects(User user) {
        return (List<Project>)user.getOwnedProjects();
    }

    @Override
    public List<Project> getProjectsRequested(User user) {
        return (List) user.getRequestedProjects();
    }

    @Override
    public List<Project> getProjectsApproved(User user) {
        return (List) user.getCollaboratedProjects();
    }


    @Override
    public List<Project> getForeignProjects(User user) {
        return projectRepository.findByUserNotLike(user);
    }

    @Override
    public void saveProject(Project project) {

    }

    @Override
    public void assignLabels(Collection<String> labels, Project project) {
        Collection<Label> projectLabels = new ArrayList<>();
        if(labels != null) {
            for (String l : labels) {
                String labelStr = l.trim().toLowerCase();
                if (!labelStr.isEmpty()) {
                    Label label;
                    if (labelService.existsByName(labelStr)) {
                        label = labelService.getByName(labelStr);
                    } else {
                        label = labelService.createNewLabel(labelStr);
                    }
                    projectLabels.add(label);
                }
            }
        }
        project.setLabels(projectLabels);
        projectRepository.save(project);
    }

    @Override
    public void deleteProject(Project project) {
        projectRepository.delete(project);
    }
}
