package es.upo.witzl.proyectotfg.projects.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.upo.witzl.proyectotfg.projects.dto.LabelsDto;
import es.upo.witzl.proyectotfg.projects.model.Label;
import es.upo.witzl.proyectotfg.projects.model.Project;
import es.upo.witzl.proyectotfg.projects.service.ICollaborationRequestService;
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
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RestController
public class LabelController {

    @Autowired
    IProjectService projectService;

    @Autowired
    ILabelService labelService;

    @Autowired
    IUserService userService;

    @Autowired
    ICollaborationRequestService collaborationRequestService;

    @PostMapping("/labels/add")
    public ResponseEntity addOrRemoveLabelsToProject(@Valid LabelsDto labelsDto, Authentication authentication) {
        MyUserPrincipal principal = (MyUserPrincipal) authentication.getPrincipal();
        Optional<User> userOptional = userService.getUserByEmail(principal.getEmail());

        if(userOptional.isPresent()) {
            User user = userOptional.get();
            Optional<Project> projectOptional = projectService.getProjectById(labelsDto.getProjectId());
            if(projectOptional.isPresent()) {
                Project project = projectOptional.get();
                if(user.equals(project.getUser()) || collaborationRequestService.isCollaborator(project, user)) {
                    projectService.assignLabels(labelsDto.getNames(), project);
                    return ResponseEntity.ok().build();
                }else {
                    return new ResponseEntity(HttpStatus.FORBIDDEN);
                }
            }
        }

        return ResponseEntity.badRequest().build();
    }

    @GetMapping(value = "/labels/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getAllLabels(@RequestParam String q) throws JsonProcessingException {
        List<Label> allLabels = labelService.getLabels(q);
        ObjectMapper mapper = new ObjectMapper();
        HashMap aux = new HashMap();

        if(allLabels != null) {
            aux.put("all", allLabels);
        }

        return ResponseEntity.ok(mapper.writeValueAsString(aux));
    }

    @GetMapping(value = "/labels/{projectId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getProjectLabels(@PathVariable String projectId) throws JsonProcessingException {
        List<Label> myLabels = null;

        if(projectId != null) {
            Optional<Project> projectOptional = projectService.getProjectById(Long.parseLong(projectId));

            if (projectOptional.isPresent()) {
                Project project = projectOptional.get();
                myLabels = (List) project.getLabels();
            }
        }
        ObjectMapper mapper = new ObjectMapper();
        HashMap aux = new HashMap();

        if(myLabels != null && !myLabels.isEmpty()) {
            aux.put("tagged", myLabels);
        }

        return ResponseEntity.ok(mapper.writeValueAsString(aux));
    }
}
