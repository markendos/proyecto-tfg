package es.upo.witzl.proyectotfg.projects.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.upo.witzl.proyectotfg.projects.dto.CollaborationRequestDto;
import es.upo.witzl.proyectotfg.projects.model.CollaborationRequest;
import es.upo.witzl.proyectotfg.projects.model.Project;
import es.upo.witzl.proyectotfg.projects.service.ICollaborationRequestService;
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

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Controller
public class RequestController {

    @Autowired
    ICollaborationRequestService collaborationRequestService;

    @Autowired
    IProjectService projectService;

    @Autowired
    IUserService userService;

    @PostMapping(value = "/collaboration/request", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity requestCollaboration(@Valid CollaborationRequestDto crDto, Authentication authentication) {
        MyUserPrincipal principal = (MyUserPrincipal) authentication.getPrincipal();
        Optional<User> userOptional = userService.getUserByEmail(principal.getEmail());

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Optional<Project> projectOptional = projectService.getProjectById(crDto.getProjectId());

            if (projectOptional.isPresent()) {
                Project project = projectOptional.get();
                if (!project.getUser().equals(user)) {
                    collaborationRequestService.requestCollaboration(crDto, user, project);

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

    @GetMapping(value = "/collaboration/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getPendingCollaborationRequests(Authentication authentication) throws JsonProcessingException {
        MyUserPrincipal principal = (MyUserPrincipal) authentication.getPrincipal();
        Optional<User> userOptional = userService.getUserByEmail(principal.getEmail());

        if(userOptional.isPresent()) {
            User user = userOptional.get();
            List<CollaborationRequest> collaborationRequests = collaborationRequestService.getPendingRequests(user);
            ObjectMapper mapper = new ObjectMapper();
            HashMap aux = new HashMap();

            if(collaborationRequests != null) {
                aux.put("all", collaborationRequests);
            }

            return ResponseEntity.ok(mapper.writeValueAsString(aux));
        }

        return ResponseEntity.badRequest().build();
    }
}
