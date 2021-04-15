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
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.LocaleContextResolver;
import org.unbescape.html.HtmlEscape;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;

@Controller
public class RequestController {

    @Autowired
    ICollaborationRequestService collaborationRequestService;

    @Autowired
    IProjectService projectService;

    @Autowired
    IUserService userService;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private MessageSource messages;

    @Autowired
    private Environment env;

    @Autowired
    private LocaleContextResolver localeResolver;

    @PostMapping(value = "/collaboration/request", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity requestCollaboration(final HttpServletRequest request,
                                               @Valid final CollaborationRequestDto crDto,
                                               final Authentication authentication) {
        Locale locale = localeResolver.resolveLocale(request);
        MyUserPrincipal principal = (MyUserPrincipal) authentication.getPrincipal();
        Optional<User> userOptional = userService.getUserByEmail(principal.getEmail());

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Optional<Project> projectOptional = projectService.getProjectById(crDto.getProjectId());

            if (projectOptional.isPresent()) {
                Project project = projectOptional.get();
                if (!project.getUser().equals(user)) {
                    CollaborationRequest newCr = collaborationRequestService.requestCollaboration(crDto, user, project);
                    mailSender.send(constructCollaborationRequestEmail(getAppUrl(request), locale, newCr));
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
    public ResponseEntity getPendingCollaborationRequests(final Authentication authentication)
            throws JsonProcessingException {
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

    @PutMapping(value = "/collaboration/update", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateCollaborationRequest(final HttpServletRequest request,
                                                     @Valid @RequestBody final CollaborationRequestDto crDto,
                                                     final Authentication authentication) {
        Locale locale = localeResolver.resolveLocale(request);

        if (crDto.getRequestStatus().equals("approved") || crDto.getRequestStatus().equals("denied")) {
            MyUserPrincipal principal = (MyUserPrincipal) authentication.getPrincipal();
            Optional<User> userOptional = userService.getUserByEmail(principal.getEmail());

            if (userOptional.isPresent()) {
                User owner = userOptional.get();
                Optional<Project> projectOptional = projectService.getProjectById(crDto.getProjectId());

                if (projectOptional.isPresent()) {
                    Project project = projectOptional.get();

                    if (project.getUser().equals(owner)) {
                        Optional<CollaborationRequest> crOptional = collaborationRequestService
                                .getCollaborationRequestById(crDto.getCollaboratorEmail(), crDto.getProjectId());

                        if (crOptional.isPresent()) {
                            CollaborationRequest cr = crOptional.get();
                            cr.setRequestStatus(crDto.getRequestStatus());
                            cr = collaborationRequestService.updateCollaborationRequest(cr);
                            mailSender.send(constructCollaborationResponseEmail(getAppUrl(request), locale, cr));
                            return ResponseEntity.ok().build();
                        } else {
                            return ResponseEntity.notFound().build();
                        }
                    } else {
                        ResponseEntity.status(HttpStatus.FORBIDDEN);
                    }
                } else {
                    return ResponseEntity.notFound().build();
                }
            } else {
                return ResponseEntity.notFound().build();
            }
        }
            return ResponseEntity.badRequest().build();
    }

    // ============== NON-API ============

    private SimpleMailMessage constructCollaborationRequestEmail(final String contextPath, final Locale locale,
                                                                 final CollaborationRequest cr) {
        final String url = contextPath + "/home";
        final String message1 = HtmlEscape.unescapeHtml(messages.getMessage("message.collaborationRequest.email.message1",
                Arrays.asList(cr.getCollaborator().getUsername()).toArray(), locale));
        String message2 = "";

        if(!cr.getRequestMessage().isEmpty()) {
            message2= HtmlEscape.unescapeHtml(messages.getMessage("message.collaborationRequest.email.message2",
                    null, locale)) + "\r\n" + cr.getRequestMessage();
        }
        final String message3 = HtmlEscape.unescapeHtml(messages.getMessage("message.collaborationRequest.email.message3",
                null, locale));
        final String subject = HtmlEscape.unescapeHtml(messages.getMessage("message.collaborationRequest.email.subject",
                null, locale));
        final String body = message1 + message2 + "\r\n\n" + message3 + "\r\n" + url;
        return constructEmail(subject, body, cr.getProject().getUser());
    }

    private SimpleMailMessage constructCollaborationResponseEmail(final String contextPath, final Locale locale,
                                                                 final CollaborationRequest cr) {
        final Project project = cr.getProject();
        final String url = contextPath + "/home";
        String status = "";
        if(cr.getRequestStatus().equals("approved")) {
            status = messages.getMessage("message.request.status.approved", null, locale);
        } else if (cr.getRequestStatus().equals("denied")) {
            status = messages.getMessage("message.request.status.denied", null, locale);
        }
        final String message1 = HtmlEscape.unescapeHtml(messages.getMessage("message.collaborationResponse.email.message1",
                Arrays.asList(project.getName(), project.getId(), status).toArray(), locale));
        final String message2 = HtmlEscape.unescapeHtml(messages.getMessage("message.collaborationResponse.email.message2",
                null, locale));
        final String subject = HtmlEscape.unescapeHtml(messages.getMessage("message.collaborationResponse.email.subject",
                null, locale));
        final String body = message1 + "\r\n\n" + message2 + "\r\n" + url;
        return constructEmail(subject, body, cr.getCollaborator());
    }

    private SimpleMailMessage constructEmail(String subject, String body, User user) {
        final SimpleMailMessage email = new SimpleMailMessage();
        email.setSubject(subject);
        email.setText(body);
        email.setTo(user.getEmail());
        email.setFrom(env.getProperty("support.email"));
        return email;
    }

    private String getAppUrl(HttpServletRequest request) {
        return env.getProperty("web.protocol") + "://" + request.getServerName() + ":" + request.getServerPort() +
                request.getContextPath();
    }
}
