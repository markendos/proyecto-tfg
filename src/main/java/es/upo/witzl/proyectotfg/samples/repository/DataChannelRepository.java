package es.upo.witzl.proyectotfg.samples.repository;

import es.upo.witzl.proyectotfg.samples.model.DataChannel;
import es.upo.witzl.proyectotfg.samples.model.DataChannelId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DataChannelRepository extends JpaRepository<DataChannel, DataChannelId> {
}
