package es.upo.witzl.proyectotfg.samples.controller;

import es.upo.witzl.proyectotfg.projects.model.Project;
import es.upo.witzl.proyectotfg.projects.service.ICollaborationRequestService;
import es.upo.witzl.proyectotfg.projects.service.IProjectService;
import es.upo.witzl.proyectotfg.users.model.User;
import es.upo.witzl.proyectotfg.users.security.MyUserPrincipal;
import es.upo.witzl.proyectotfg.users.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
public class SampleController {

    @Autowired
    IProjectService projectService;

    @Autowired
    IUserService userService;

    @Autowired
    ICollaborationRequestService collaborationRequestService;

    @GetMapping("/registerSample/{projectId}")
    public ModelAndView registerSample(final ModelMap model, @PathVariable String projectId,
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
                    redirect =  "/registerSample";
                    model.addAttribute("project", project);
                    model.addAttribute("role", isCollaborator ? "collaborator" : "owner");
                    return new ModelAndView(redirect, model);
                }
            }
        }
        return new ModelAndView(redirect, model);
    }
}
