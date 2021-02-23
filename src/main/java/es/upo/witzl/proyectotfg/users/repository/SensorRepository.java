package es.upo.witzl.proyectotfg.users.repository;

import es.upo.witzl.proyectotfg.users.model.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SensorRepository extends JpaRepository<Sensor, Long> {

    Optional<Sensor> findByName(String name);

    Optional<Sensor> findByAlias(String alias);
}
