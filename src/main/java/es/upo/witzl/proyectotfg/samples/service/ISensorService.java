package es.upo.witzl.proyectotfg.samples.service;

import es.upo.witzl.proyectotfg.samples.dto.SensorDto;
import es.upo.witzl.proyectotfg.samples.error.SensorAlreadyExistsException;
import es.upo.witzl.proyectotfg.samples.model.Sensor;

import java.util.List;
import java.util.Optional;

public interface ISensorService {

    Sensor registerNewSensor(SensorDto sensorDto) throws SensorAlreadyExistsException;

    Sensor updateSensor(Sensor sensor, SensorDto sensorDto);

    Optional<Sensor> getSensorById(Long id);

    Optional<Sensor> getSensorByName(String name);

    Optional<Sensor> getSensorByAlias(String alias);

    List<Sensor> getSensors();

    void saveSensor(Sensor sensor);

    boolean nameExists(String name);

    boolean aliasExists(String alias);
}
