package es.upo.witzl.proyectotfg.users.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.upo.witzl.proyectotfg.users.dto.SensorDto;
import es.upo.witzl.proyectotfg.users.model.Sensor;
import es.upo.witzl.proyectotfg.users.service.ISensorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Controller
public class SensorController {

    @Autowired
    ISensorService sensorService;

    @PostMapping("/admin/sensor/add")
    public ResponseEntity createOrUpdateDataSensor(@Valid SensorDto sensorDto) {
        String message = "";
        if(sensorDto.getId() == null) {
            sensorService.registerNewSensor(sensorDto);
            message = "created";
        } else {
            Optional<Sensor> sensorOptional = sensorService.getSensorById(sensorDto.getId());
            if(sensorOptional.isPresent()) {
                Sensor sensor = sensorOptional.get();
                sensor.setName(sensorDto.getName());
                sensor.setAlias(sensorDto.getAlias());
                sensor.setDescription(sensorDto.getDescription());
                sensorService.saveSensor(sensor);
                message = "updated";
            } else {
                return ResponseEntity.notFound().build();
            }
        }

        return ResponseEntity.ok(message);
    }

    @GetMapping("/console/sensors")
    public ModelAndView getDataSensors(final ModelMap model) throws JsonProcessingException {
        List<Sensor> sensorList = sensorService.getSensors();
        ObjectMapper mapper = new ObjectMapper();
        HashMap aux = new HashMap();

        if(sensorList != null && !sensorList.isEmpty()) {
            aux.put("all", sensorList);
        }
        model.addAttribute("sensors", mapper.writeValueAsString(aux));

        return new ModelAndView("/console", model);
    }

    @GetMapping(value = "/admin/sensor/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getAllSensors() throws JsonProcessingException {
        List<Sensor> sensorList = sensorService.getSensors();
        ObjectMapper mapper = new ObjectMapper();
        HashMap aux = new HashMap();

        if(sensorList != null && !sensorList.isEmpty()) {
            aux.put("all", sensorList);
        }

        return ResponseEntity.ok(mapper.writeValueAsString(aux));
    }


}
