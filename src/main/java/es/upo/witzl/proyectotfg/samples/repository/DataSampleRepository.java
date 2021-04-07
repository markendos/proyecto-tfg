package es.upo.witzl.proyectotfg.samples.repository;

import es.upo.witzl.proyectotfg.samples.model.DataSample;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DataSampleRepository extends JpaRepository<DataSample, Long> {
}
