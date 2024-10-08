package es.upo.witzl.proyectotfg.samples.service;

import es.upo.witzl.proyectotfg.projects.model.Project;
import es.upo.witzl.proyectotfg.samples.dto.DataSampleDto;
import es.upo.witzl.proyectotfg.samples.dto.SampleValuesDto;
import es.upo.witzl.proyectotfg.samples.model.DataSample;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ISampleService {

    DataSample registerNewSample(DataSampleDto sampleDto, Project project);

    Optional<DataSample> getSampleById(Long id);

    List<List<Integer>> getSampleValues(DataSample sample);

    DataSample getStatistics(DataSample sample);

    void deleteSample(DataSample sample);

    void deleteSamples(Collection<DataSample> samples);

    void addValueToSample(SampleValuesDto sdto, DataSample sample);

}
