package es.upo.witzl.proyectotfg.projects.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.upo.witzl.proyectotfg.projects.dto.LabelsDto;
import es.upo.witzl.proyectotfg.projects.dto.ProjectDto;
import es.upo.witzl.proyectotfg.projects.model.Label;
import es.upo.witzl.proyectotfg.projects.model.Project;
import es.upo.witzl.proyectotfg.projects.service.ILabelService;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Controller
public class ProjectController {

    @Autowired
    IProjectService projectService;

    @Autowired
    ILabelService labelService;

    @Autowired
    IUserService userService;

    @PostMapping("/user/project/add")
    public ResponseEntity createProject(@Valid ProjectDto projectDto, Authentication authentication) throws JsonProcessingException {
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

    @GetMapping(value = "/user/project/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getUserProjects(Authentication authentication) throws JsonProcessingException {
        MyUserPrincipal principal = (MyUserPrincipal) authentication.getPrincipal();
        Optional<User> userOptional = userService.getUserByEmail(principal.getEmail());
        if(userOptional.isPresent()) {
            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
            User user = userOptional.get();
            List<Project> projectList = projectService.getOwnedProjects(user);
            projectList.addAll(projectService.getCollaboratedProjects(user));
            ObjectMapper mapper = new ObjectMapper();
            mapper.setDateFormat(df);
            HashMap aux = new HashMap();

            if(projectList != null && !projectList.isEmpty()) {
                aux.put("all", projectList);
            }

            return ResponseEntity.ok(mapper.writeValueAsString(aux));
        }

        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/user/project/labels/add")
    public ResponseEntity addOrRemoveLabelsToProject(@Valid LabelsDto labelsDto, Authentication authentication) {
        MyUserPrincipal principal = (MyUserPrincipal) authentication.getPrincipal();
        Optional<User> userOptional = userService.getUserByEmail(principal.getEmail());

        if(userOptional.isPresent()) {
            User user = userOptional.get();
            Optional<Project> projectOptional = projectService.getProjectById(labelsDto.getProjectId());
            if(projectOptional.isPresent()) {
                Project project = projectOptional.get();
                if(user.equals(project.getUser())) {
                    projectService.assignLabels(labelsDto.getNames(), project);
                    return ResponseEntity.ok().build();
                }else {
                    return new ResponseEntity(HttpStatus.FORBIDDEN);
                }
            }
        }

        return ResponseEntity.badRequest().build();
    }

    @GetMapping(value = "/user/project/labels/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getAllLabels(@RequestParam String q) throws JsonProcessingException {
        List<Label> labelList = labelService.getLabels(q);
        ObjectMapper mapper = new ObjectMapper();
        HashMap aux = new HashMap();

        if(labelList != null && !labelList.isEmpty()) {
            aux.put("all", labelList);
        }

        return ResponseEntity.ok(mapper.writeValueAsString(aux));
    }
}
