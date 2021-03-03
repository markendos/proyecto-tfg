package es.upo.witzl.proyectotfg.projects.controller;

import es.upo.witzl.proyectotfg.projects.dto.SubjectDto;
import es.upo.witzl.proyectotfg.projects.model.Project;
import es.upo.witzl.proyectotfg.projects.service.IProjectService;
import es.upo.witzl.proyectotfg.projects.service.ISubjectService;
import es.upo.witzl.proyectotfg.users.model.User;
import es.upo.witzl.proyectotfg.users.security.MyUserPrincipal;
import es.upo.witzl.proyectotfg.users.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.Optional;

@Controller
public class SubjectController {

    @Autowired
    IUserService userService;

    @Autowired
    IProjectService projectService;

    @Autowired
    ISubjectService subjectService;


    @PostMapping(value = "/subject/add", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity registerSubject(@Valid SubjectDto subjectDto, Authentication authentication) {
            MyUserPrincipal principal = (MyUserPrincipal) authentication.getPrincipal();
            Optional<User> userOptional = userService.getUserByEmail(principal.getEmail());

            if(userOptional.isPresent()) {
                User user = userOptional.get();
                Optional<Project> projectOptional = projectService.getProjectById(subjectDto.getProjectId());

                if (projectOptional.isPresent()) {
                    Project project = projectOptional.get();
                    if (project.getUser().equals(user)) {
                        subjectService.registerSubjectToProject(subjectDto, project);

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
