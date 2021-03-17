package es.upo.witzl.proyectotfg.samples.service;

import es.upo.witzl.proyectotfg.samples.model.DataSample;

import java.util.Collection;
import java.util.Optional;

public interface ISampleService {

    Optional<DataSample> getSampleById(Long id);

    Collection<Collection<Integer>> getSampleValues(DataSample sample);

    void test();

}
