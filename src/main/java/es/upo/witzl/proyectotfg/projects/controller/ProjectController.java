package es.upo.witzl.proyectotfg.projects.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.upo.witzl.proyectotfg.projects.dto.ProjectDto;
import es.upo.witzl.proyectotfg.projects.model.Project;
import es.upo.witzl.proyectotfg.projects.service.ICollaborationRequestService;
import es.upo.witzl.proyectotfg.projects.service.IProjectService;
import es.upo.witzl.proyectotfg.projects.service.ISubjectService;
import es.upo.witzl.proyectotfg.samples.service.ISampleService;
import es.upo.witzl.proyectotfg.users.model.User;
import es.upo.witzl.proyectotfg.users.security.MyUserPrincipal;
import es.upo.witzl.proyectotfg.users.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Controller
public class ProjectController {

    @Autowired
    IProjectService projectService;

    @Autowired
    IUserService userService;

    @Autowired
    ICollaborationRequestService collaborationRequestService;

    @Autowired
    ISampleService sampleService;

    @Autowired
    ISubjectService subjectService;

    @PostMapping(value = "/project/add", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createProject(@Valid ProjectDto projectDto, final Authentication authentication)
            throws JsonProcessingException {
        if(projectDto.getId() == null) {
            MyUserPrincipal principal = (MyUserPrincipal) authentication.getPrincipal();
            Optional<User> userOptional = userService.getUserByEmail(principal.getEmail());

            if(userOptional.isPresent()) {
                User user = userOptional.get();
                Project newProject = projectService.registerNewProject(projectDto, user);
                ObjectMapper mapper = new ObjectMapper();
                HashMap aux = new HashMap();

                if(newProject != null) {
                    aux.put("new", newProject);
                }
                return ResponseEntity.ok(mapper.writeValueAsString(aux));
            }
        }

        return ResponseEntity.badRequest().build();
    }

    @GetMapping(value = "/project/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getUserProjects(final Authentication authentication) throws JsonProcessingException {
        MyUserPrincipal principal = (MyUserPrincipal) authentication.getPrincipal();
        Optional<User> userOptional = userService.getUserByEmail(principal.getEmail());

        if(userOptional.isPresent()) {
            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
            User user = userOptional.get();
            List<Project> ownedProjects = projectService.getOwnedProjects(user);
            ObjectMapper mapper = new ObjectMapper();
            mapper.setDateFormat(df);
            HashMap aux = new HashMap();

            if(ownedProjects != null) {
                aux.put("owned", ownedProjects);
            }

            return ResponseEntity.ok(mapper.writeValueAsString(aux));
        }

        return ResponseEntity.badRequest().build();
    }

    @GetMapping(value = "/project/collaborations", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getCollaboratedProjects(final Authentication authentication) throws JsonProcessingException {
        MyUserPrincipal principal = (MyUserPrincipal) authentication.getPrincipal();
        Optional<User> userOptional = userService.getUserByEmail(principal.getEmail());

        if(userOptional.isPresent()) {
            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
            User user = userOptional.get();
            List<Project> authorizedProjects = projectService.getProjectsApproved(user);
            ObjectMapper mapper = new ObjectMapper();
            mapper.setDateFormat(df);
            HashMap aux = new HashMap();

            if(authorizedProjects != null) {
                aux.put("auth", authorizedProjects);
            }

            return ResponseEntity.ok(mapper.writeValueAsString(aux));
        }

        return ResponseEntity.badRequest().build();
    }

    @GetMapping(value = "/project/foreign", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getForeignProjects(final Authentication authentication) throws JsonProcessingException {
        MyUserPrincipal principal = (MyUserPrincipal) authentication.getPrincipal();
        Optional<User> userOptional = userService.getUserByEmail(principal.getEmail());

        if(userOptional.isPresent()) {
            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
            User user = userOptional.get();
            List<Project> foreignProjects = projectService.getForeignProjects(user);
            List<Project> projectsRequested = projectService.getProjectsRequested(user);
            ObjectMapper mapper = new ObjectMapper();
            mapper.setDateFormat(df);
            HashMap aux = new HashMap();

            if(foreignProjects != null) {
                aux.put("all", foreignProjects);
            }

            if(projectsRequested != null) {
                aux.put("requested", projectsRequested);
            }

            return ResponseEntity.ok(mapper.writeValueAsString(aux));
        }

        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping(value = "/project/delete/{projectId}")
    public ResponseEntity deleteProject(@PathVariable final String projectId, final Authentication authentication) {
        MyUserPrincipal principal = (MyUserPrincipal) authentication.getPrincipal();
        Optional<User> userOptional = userService.getUserByEmail(principal.getEmail());

        if(userOptional.isPresent() && projectId != null) {
            User user = userOptional.get();
            Optional<Project> projectOptional = projectService.getProjectById(Long.parseLong(projectId));

            if (projectOptional.isPresent()) {
                Project project = projectOptional.get();
                if(project.getUser().equals(user)) {
                    if(project.hasCollaborationRequests()) {
                        collaborationRequestService.deleteProjectCollaborations(project);
                    }
                    if(project.hasDataSamples()) {
                        sampleService.deleteSamples(project.getDataSamples());
                    }
                    if(project.getSubject() != null) {
                        subjectService.deleteSubject(project.getSubject());
                    }
                    projectService.deleteProject(project);

                    return ResponseEntity.ok().build();
                } else {
                    return ResponseEntity.badRequest().build();
                }
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/project/{projectId}")
    public ModelAndView viewProject(final ModelMap model, @PathVariable String projectId,
                                    final Authentication authentication) {

        MyUserPrincipal principal = (MyUserPrincipal) authentication.getPrincipal();
        Optional<User> userOptional = userService.getUserByEmail(principal.getEmail());
        String redirect = "redirect:/login";

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Optional<Project> projectOpt = projectService.getProjectById(Long.parseLong(projectId));

            if(projectOpt.isPresent()) {
                Project project = projectOpt.get();
                boolean isCollaborator = collaborationRequestService.isCollaborator(project, user);
                if(project.getUser().equals(user) || isCollaborator) {
                    redirect =  "project";
                    model.addAttribute("project", project);
                    model.addAttribute("role", isCollaborator ? "collaborator" : "owner");
                    return new ModelAndView(redirect, model);
                }
            }
        }
        return new ModelAndView(redirect, model);
    }
}
