package es.upo.witzl.proyectotfg.samples.service;

import es.upo.witzl.proyectotfg.projects.model.Project;
import es.upo.witzl.proyectotfg.projects.repository.ProjectRepository;
import es.upo.witzl.proyectotfg.samples.model.DataChannel;
import es.upo.witzl.proyectotfg.samples.model.DataSample;
import es.upo.witzl.proyectotfg.samples.model.DataValue;
import es.upo.witzl.proyectotfg.samples.model.Sensor;
import es.upo.witzl.proyectotfg.samples.repository.DataChannelRepository;
import es.upo.witzl.proyectotfg.samples.repository.DataSampleRepository;
import es.upo.witzl.proyectotfg.samples.repository.DataValueRepository;
import es.upo.witzl.proyectotfg.samples.repository.SensorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SampleService implements ISampleService {

    @Autowired
    DataSampleRepository dataSampleRepository;

    @Autowired
    DataChannelRepository dataChannelRepository;

    @Autowired
    DataValueRepository dataValueRepository;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    SensorRepository sensorRepository;

    @Override
    public Optional<DataSample> getSampleById(Long id) {
        return dataSampleRepository.findById(id);
    }

    @Override
    public Collection<Collection<Integer>> getSampleValues(DataSample sample) {
        List allValues = new ArrayList();

        for(DataChannel dc : sample.getDataChannels()) {
            String id = sample.getId() + "@" + dc.getChannelName();
            Optional<DataValue> dataValue = dataValueRepository.findById(id);
            List<Integer> channelValues = (List) dataValue.get().getValues();
           allValues.add(channelValues);
        }

        return allValues;
    }

    @Override
    public void test() {
        DataSample ds = new DataSample();
        ds.setSampleDate(new Date());
        Project project = projectRepository.getOne(Long.parseLong("7"));
        ds.setProject(project);
        ds = dataSampleRepository.save(ds);
        DataChannel dc = new DataChannel();
        dc.setChannelName("A0");
        List<Integer> l = new ArrayList<>();
        for(int i = 0; i < 1000; i++) {
            l.add(i);
        }
        DataValue dv = new DataValue();
        dv.setId(ds.getId().toString() + "@" + dc.getChannelName());
        dv.setValues(l);
        dc.setDataSample(ds);
        ds.setSize(dv.getValues().size());
        Sensor sensor = sensorRepository.getOne(Long.parseLong("10"));
        dc.setSensor(sensor);
        //dataSampleRepository.save(ds);
        dataChannelRepository.save(dc);
        dataValueRepository.save(dv);
    }
}
