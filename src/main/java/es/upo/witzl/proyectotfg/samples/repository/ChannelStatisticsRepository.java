package es.upo.witzl.proyectotfg.samples.repository;

import es.upo.witzl.proyectotfg.samples.model.ChannelStatistics;
import es.upo.witzl.proyectotfg.samples.model.DataChannelId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChannelStatisticsRepository extends JpaRepository<ChannelStatistics, DataChannelId> {
}
