package es.upo.witzl.proyectotfg.samples.service;

import es.upo.witzl.proyectotfg.projects.model.Project;
import es.upo.witzl.proyectotfg.projects.repository.ProjectRepository;
import es.upo.witzl.proyectotfg.samples.dto.DataSampleDto;
import es.upo.witzl.proyectotfg.samples.model.*;
import es.upo.witzl.proyectotfg.samples.repository.*;
import org.apache.commons.math3.stat.StatUtils;
import org.apache.commons.math3.stat.descriptive.moment.Kurtosis;
import org.apache.commons.math3.stat.descriptive.moment.Skewness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.crypto.Data;
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

    @Autowired
    ChannelStatisticsRepository statisticsRepository;

    @Override
    public DataSample registerNewSample(DataSampleDto sampleDto, Project project) {
        DataSample ds = new DataSample();

        return ds;
    }

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
        Random r  = new Random();
        for(int i = 0; i < 1000; i++) {
            l.add(r.nextInt());
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

    @Override
    public DataSample getStatistics(DataSample sample) {
        for(DataChannel dc : sample.getDataChannels()) {
            String id = sample.getId() + "@" + dc.getChannelName();
            Optional<DataValue> dataValue = dataValueRepository.findById(id);
            List<Integer> channelValues = (List) dataValue.get().getValues();
            ChannelStatistics cs = new ChannelStatistics();
            cs.setDataChannel(dc);
            double[] values = channelValues.stream().mapToDouble(i -> i).toArray();
            double[] normalized = normalizeZeroOne(values);
            cs.setMean(StatUtils.mean(normalized));
            cs.setStdDev(Math.sqrt(StatUtils.variance(normalized)));
            Kurtosis k = new Kurtosis();
            cs.setKurtosis(k.evaluate(normalized));
            Skewness s = new Skewness();
            cs.setSkewness(s.evaluate(normalized));
            statisticsRepository.save(cs);
            dc.setStatistics(cs);
            dataChannelRepository.save(dc);
        }
        sample.setStatsGenerated(true);
        return dataSampleRepository.save(sample);
    }

    @Override
    public void deleteSample(DataSample sample) {
        for(DataChannel dc : sample.getDataChannels()) {
            String id = sample.getId() + "@" + dc.getChannelName();
            dataValueRepository.deleteById(id);
            if(dc.getStatistics() != null) {
                statisticsRepository.delete(dc.getStatistics());
            }
            dataChannelRepository.delete(dc);
        }
        dataSampleRepository.delete(sample);
    }

    private double[] normalizeZeroOne(double[] values) {
        double[] normalized = new double[values.length];
        double min = StatUtils.min(values);
        double max = StatUtils.max(values);
        for(int i = 0; i < values.length; i++) {
            normalized[i] = (values[i] - min)/(max - min);
        }
        return normalized;
    }
}
