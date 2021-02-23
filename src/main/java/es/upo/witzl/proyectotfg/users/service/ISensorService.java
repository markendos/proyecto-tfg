package es.upo.witzl.proyectotfg.users.service;

import es.upo.witzl.proyectotfg.users.dto.SensorDto;
import es.upo.witzl.proyectotfg.users.error.SensorAlreadyExistsException;
import es.upo.witzl.proyectotfg.users.model.Sensor;

import java.util.List;
import java.util.Optional;

public interface ISensorService {

    Sensor registerNewSensor(SensorDto sensorDto) throws SensorAlreadyExistsException;

    Optional<Sensor> getSensorById(Long id);

    Optional<Sensor> getSensorByName(String name);

    Optional<Sensor> getSensorByAlias(String alias);

    List<Sensor> getSensors();

    void saveSensor(Sensor sensor);

    boolean nameExists(String name);

    boolean aliasExists(String alias);
}
