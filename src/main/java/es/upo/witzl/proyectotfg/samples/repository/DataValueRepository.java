package es.upo.witzl.proyectotfg.samples.repository;

import es.upo.witzl.proyectotfg.samples.model.DataValue;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DataValueRepository extends MongoRepository<DataValue, String> {
}
