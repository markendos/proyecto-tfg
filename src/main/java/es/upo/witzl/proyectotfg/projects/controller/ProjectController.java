package es.upo.witzl.proyectotfg.projects.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.upo.witzl.proyectotfg.projects.dto.ProjectDto;
import es.upo.witzl.proyectotfg.projects.model.Project;
import es.upo.witzl.proyectotfg.projects.service.IProjectService;
import es.upo.witzl.proyectotfg.users.model.User;
import es.upo.witzl.proyectotfg.users.security.MyUserPrincipal;
import es.upo.witzl.proyectotfg.users.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

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

    @PostMapping(value = "/project/add", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createProject(@Valid ProjectDto projectDto, Authentication authentication)
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
    public ResponseEntity getUserProjects(Authentication authentication) throws JsonProcessingException {
        MyUserPrincipal principal = (MyUserPrincipal) authentication.getPrincipal();
        Optional<User> userOptional = userService.getUserByEmail(principal.getEmail());

        if(userOptional.isPresent()) {
            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
            User user = userOptional.get();
            List<Project> ownedProjects = projectService.getOwnedProjects(user);
            List<Project> authorizedProjects = projectService.getCollaboratedProjects(user);
            ObjectMapper mapper = new ObjectMapper();
            mapper.setDateFormat(df);
            HashMap aux = new HashMap();

            if(ownedProjects != null) {
                aux.put("owned", ownedProjects);
            }

            if(authorizedProjects != null) {
                aux.put("auth", authorizedProjects);
            }

            return ResponseEntity.ok(mapper.writeValueAsString(aux));
        }

        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping(value = "/project/delete/{projectId}")
    public ResponseEntity deleteProject(@PathVariable String projectId, Authentication authentication) {
        MyUserPrincipal principal = (MyUserPrincipal) authentication.getPrincipal();
        Optional<User> userOptional = userService.getUserByEmail(principal.getEmail());

        if(userOptional.isPresent() && projectId != null) {
            User user = userOptional.get();
            Optional<Project> projectOptional = projectService.getProjectById(Long.parseLong(projectId));

            if (projectOptional.isPresent()) {
                Project project = projectOptional.get();
                if(project.getUser().equals(user)) {
                    projectService.deleteProject(project);

                    return ResponseEntity.ok().build();
                } else {
                    ResponseEntity.status(HttpStatus.FORBIDDEN);
                }
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.badRequest().build();
    }
}
