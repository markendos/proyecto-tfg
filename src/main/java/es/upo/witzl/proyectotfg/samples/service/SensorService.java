package es.upo.witzl.proyectotfg.samples.service;

import es.upo.witzl.proyectotfg.samples.dto.SensorDto;
import es.upo.witzl.proyectotfg.samples.error.SensorAlreadyExistsException;
import es.upo.witzl.proyectotfg.samples.model.Sensor;
import es.upo.witzl.proyectotfg.samples.repository.SensorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SensorService implements ISensorService {

    @Autowired
    private SensorRepository sensorRepository;

    @Override
    public Sensor registerNewSensor(final SensorDto sensorDto) {
        if(nameExists(sensorDto.getName()) || aliasExists(sensorDto.getAlias())) {
            throw new SensorAlreadyExistsException("There is a sensor registered with that name and/or alias");
        }
        final Sensor sensor = new Sensor();

        sensor.setName(sensorDto.getName());
        sensor.setAlias(sensorDto.getAlias().toUpperCase());
        sensor.setDescription(sensorDto.getDescription());

        return sensorRepository.save(sensor);
    }

    @Override
    public Sensor updateSensor(Sensor sensor, SensorDto sensorDto) {
        Optional<Sensor> sensorOptional = sensorRepository.findByAlias(sensorDto.getAlias());
        Sensor s;
        if(sensorOptional.isPresent()) {
            s = sensorOptional.get();
            if(!s.equals(sensor)) {
                throw new SensorAlreadyExistsException("There is a sensor registered with that name and/or alias");
            }
        }
        sensorOptional = sensorRepository.findByName(sensorDto.getName());
        if(sensorOptional.isPresent()) {
            s = sensorOptional.get();
            if(!s.equals(sensor)) {
                throw new SensorAlreadyExistsException("There is a sensor registered with that name and/or alias");
            }
        }
        sensor.setAlias(sensorDto.getAlias());
        sensor.setName(sensorDto.getName());
        sensor.setDescription(sensorDto.getDescription());

        return sensorRepository.save(sensor);
    }


    @Override
    public Optional<Sensor> getSensorById(Long id) {
        return sensorRepository.findById(id);
    }

    @Override
    public Optional<Sensor> getSensorByName(final String name) {
        return sensorRepository.findByName(name);
    }

    @Override
    public Optional<Sensor> getSensorByAlias(final String alias) {
        return sensorRepository.findByAlias(alias);
    }

    @Override
    public List<Sensor> getSensors() {
        return sensorRepository.findAll();
    }

    @Override
    public void saveSensor(final Sensor sensor) {
        sensorRepository.save(sensor);
    }

    @Override
    public boolean nameExists(final String name) {
        return sensorRepository.findByName(name).isPresent();
    }

    @Override
    public boolean aliasExists(final String alias) {
        return sensorRepository.findByAlias(alias).isPresent();
    }
}
