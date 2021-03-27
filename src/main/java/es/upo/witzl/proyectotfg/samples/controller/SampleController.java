package es.upo.witzl.proyectotfg.samples.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.upo.witzl.proyectotfg.projects.dto.ProjectDto;
import es.upo.witzl.proyectotfg.projects.model.Project;
import es.upo.witzl.proyectotfg.projects.service.ICollaborationRequestService;
import es.upo.witzl.proyectotfg.projects.service.IProjectService;
import es.upo.witzl.proyectotfg.samples.dto.DataSampleDto;
import es.upo.witzl.proyectotfg.samples.dto.SampleValuesDto;
import es.upo.witzl.proyectotfg.samples.model.DataSample;
import es.upo.witzl.proyectotfg.samples.model.DataValue;
import es.upo.witzl.proyectotfg.samples.model.Sensor;
import es.upo.witzl.proyectotfg.samples.service.ISampleService;
import es.upo.witzl.proyectotfg.samples.service.ISensorService;
import es.upo.witzl.proyectotfg.users.model.User;
import es.upo.witzl.proyectotfg.users.security.MyUserPrincipal;
import es.upo.witzl.proyectotfg.users.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Controller
public class SampleController {

    @Autowired
    IProjectService projectService;

    @Autowired
    IUserService userService;

    @Autowired
    ICollaborationRequestService collaborationRequestService;

    @Autowired
    ISampleService sampleService;

    @Autowired
    ISensorService sensorService;

    @PostMapping(value = "/sample/add", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity addSample(@Valid DataSampleDto sampleDto, final Authentication authentication)
            throws JsonProcessingException {
        if(sampleDto.getProjectId() != null) {
            MyUserPrincipal principal = (MyUserPrincipal) authentication.getPrincipal();
            Optional<User> userOptional = userService.getUserByEmail(principal.getEmail());
            if(userOptional.isPresent()) {
                User user = userOptional.get();
                Optional<Project> projectOpt = projectService.getProjectById(sampleDto.getProjectId());
                if(projectOpt.isPresent()) {
                    Project project = projectOpt.get();
                    boolean isCollaborator = collaborationRequestService.isCollaborator(project, user);
                    if(project.getUser().equals(user) || isCollaborator) {
                        DataSample newSample = sampleService.registerNewSample(sampleDto, project);
                        ObjectMapper mapper = new ObjectMapper();
                        HashMap aux = new HashMap();

                        if(newSample != null) {
                            aux.put("new", newSample);
                        }
                        return ResponseEntity.ok(mapper.writeValueAsString(aux));
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

        return ResponseEntity.badRequest().build();
    }

    @PostMapping(value = "/sample/saveValues", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity saveValues(@Valid @RequestBody SampleValuesDto sampleValuesDto, final Authentication authentication)
            throws JsonProcessingException {
        Long sampleId = sampleValuesDto.getSampleId();
        if(sampleId != null) {
            MyUserPrincipal principal = (MyUserPrincipal) authentication.getPrincipal();
            Optional<User> userOptional = userService.getUserByEmail(principal.getEmail());
            if(userOptional.isPresent()) {
                User user = userOptional.get();
                Optional<DataSample> dataSampleOpt = sampleService.getSampleById(sampleId);
                if(dataSampleOpt.isPresent()) {
                    DataSample dataSample = dataSampleOpt.get();
                    boolean isCollaborator = collaborationRequestService.isCollaborator(dataSample.getProject(), user);
                    if(dataSample.getProject().getUser().equals(user) || isCollaborator) {
                        sampleService.addValueToSample(sampleValuesDto, dataSample);
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

        return ResponseEntity.badRequest().build();
    }

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
                    List<Sensor> sensors = sensorService.getSensors();
                    model.addAttribute("project", project);
                    model.addAttribute("sensors", sensors);
                    model.addAttribute("role", isCollaborator ? "collaborator" : "owner");
                    return new ModelAndView(redirect, model);
                }
            }
        }
        return new ModelAndView(redirect, model);
    }

    @GetMapping(value = "/samples/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getProjectSamples(@RequestParam String projectId, final Authentication authentication)
            throws JsonProcessingException {
        MyUserPrincipal principal = (MyUserPrincipal) authentication.getPrincipal();
        Optional<User> userOptional = userService.getUserByEmail(principal.getEmail());

        if(userOptional.isPresent()) {
            User user = userOptional.get();
            Optional<Project> projectOpt = projectService.getProjectById(Long.parseLong(projectId));
            if(projectOpt.isPresent()) {
                Project project = projectOpt.get();
                boolean isCollaborator = collaborationRequestService.isCollaborator(project, user);
                if(user.equals(project.getUser()) || isCollaborator) {
                    List<DataSample> samples = (List)project.getDataSamples();
                    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                    ObjectMapper mapper = new ObjectMapper();
                    mapper.setDateFormat(df);
                    HashMap aux = new HashMap();

                    if(samples != null) {
                        aux.put("all", samples);
                    }
                    return ResponseEntity.ok(mapper.writeValueAsString(aux));
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

    @GetMapping(value = "/sample/{sampleId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity viewSample(@PathVariable String sampleId, final Authentication authentication)
            throws JsonProcessingException {

        MyUserPrincipal principal = (MyUserPrincipal) authentication.getPrincipal();
        Optional<User> userOptional = userService.getUserByEmail(principal.getEmail());

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Optional<DataSample> sampleOpt = sampleService.getSampleById(Long.parseLong(sampleId));

            if(sampleOpt.isPresent()) {
                DataSample sample = sampleOpt.get();
                boolean isCollaborator = collaborationRequestService.isCollaborator(sample.getProject(), user);
                if(sample.getProject().getUser().equals(user) || isCollaborator) {
                    List<List<Integer>> values = (List) sampleService.getSampleValues(sample);
                    ObjectMapper mapper = new ObjectMapper();
                    HashMap aux = new HashMap();

                    if(values != null && !values.isEmpty()) {
                        aux.put("all", values);
                    }
                    return ResponseEntity.ok(mapper.writeValueAsString(aux));
                } else {
                    return ResponseEntity.badRequest().build();
                }
            } else {
                return ResponseEntity.notFound().build();
            }
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping(value = "/sample/delete/{sampleId}")
    public ResponseEntity deleteSample(@PathVariable final String sampleId, final Authentication authentication) {
        MyUserPrincipal principal = (MyUserPrincipal) authentication.getPrincipal();
        Optional<User> userOptional = userService.getUserByEmail(principal.getEmail());

        if(userOptional.isPresent() && sampleId != null) {
            User user = userOptional.get();
            Optional<DataSample> sampleOpt = sampleService.getSampleById(Long.parseLong(sampleId));

            if (sampleOpt.isPresent()) {
                DataSample sample = sampleOpt.get();
                if(sample.getProject().getUser().equals(user)) {
                    sampleService.deleteSample(sample);

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

    @GetMapping(value = "/sample/statistics/{sampleId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity generateStatistics(@PathVariable String sampleId, final Authentication authentication)
            throws JsonProcessingException {

        MyUserPrincipal principal = (MyUserPrincipal) authentication.getPrincipal();
        Optional<User> userOptional = userService.getUserByEmail(principal.getEmail());

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            Optional<DataSample> sampleOpt = sampleService.getSampleById(Long.parseLong(sampleId));

            if(sampleOpt.isPresent()) {
                DataSample sample = sampleOpt.get();
                boolean isCollaborator = collaborationRequestService.isCollaborator(sample.getProject(), user);
                if(sample.getProject().getUser().equals(user) || isCollaborator) {
                    sample = sampleService.getStatistics(sample);
                    ObjectMapper mapper = new ObjectMapper();
                    HashMap aux = new HashMap();
                    aux.put("sample", sample);

                    return ResponseEntity.ok(mapper.writeValueAsString(aux));
                } else {
                    return ResponseEntity.badRequest().build();
                }
            } else {
                return ResponseEntity.notFound().build();
            }
        }
        return ResponseEntity.notFound().build();
    }
}
