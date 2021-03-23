package es.upo.witzl.proyectotfg.samples.service;

import es.upo.witzl.proyectotfg.projects.model.Project;
import es.upo.witzl.proyectotfg.projects.repository.ProjectRepository;
import es.upo.witzl.proyectotfg.samples.dto.DataSampleDto;
import es.upo.witzl.proyectotfg.samples.dto.SampleValuesDto;
import es.upo.witzl.proyectotfg.samples.model.*;
import es.upo.witzl.proyectotfg.samples.repository.*;
import org.apache.commons.math3.distribution.ConstantRealDistribution;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.stat.StatUtils;
import org.apache.commons.math3.stat.descriptive.moment.Kurtosis;
import org.apache.commons.math3.stat.descriptive.moment.Skewness;
import org.apache.commons.math3.stat.inference.KolmogorovSmirnovTest;
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

    @Autowired
    ChannelStatisticsRepository statisticsRepository;

    @Override
    public DataSample registerNewSample(DataSampleDto sampleDto, Project project) {
        /*Create the data sample*/
        DataSample ds = new DataSample();
        ds.setProject(project);
        ds.setSampleDate(new Date());
        ds.setSize(sampleDto.getSize());
        ds.setSamplingRate(sampleDto.getSamplingRate());
        ds.setComments(sampleDto.getComments());
        ds.setDeviceName(sampleDto.getDeviceName());
        ds.setDeviceModel(sampleDto.getDeviceModel());
        ds.setDeviceFirmware(sampleDto.getDeviceFirmware());
        ds.setStatsGenerated(false);
        ds = dataSampleRepository.save(ds);

        /*Create the data channels associated to the sample*/
        List<String> channels = sampleDto.getChannelNames();
        List<String> labels = sampleDto.getChannelLabels();
        List<Integer> resolutions = sampleDto.getChannelResolutions();
        List<Long> sensorIds = sampleDto.getSensors();
        List<DataChannel> sampleChannels = new ArrayList<>();

        for (int i = 0; i < sampleDto.getChannelNames().size(); i++) {
            DataChannel dc = new DataChannel();
            dc.setDataSample(ds);
            dc.setSampleId(ds.getId());
            dc.setChannelName(channels.get(i));
            if(labels != null && !labels.isEmpty()) {
                dc.setChannelLabel(labels.get(i));
            }
            dc.setResolution(resolutions.get(i));
            dc.setDigital(false);
            dc.setInput(false);
            Sensor sensor = getSensor(sensorIds.get(i));
            dc.setSensor(sensor);
            dataChannelRepository.save(dc);
            sampleChannels.add(dc);
        }
        ds.setDataChannels(sampleChannels);
        return dataSampleRepository.save(ds);
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
            if(dataValue.isPresent()) {
                List<Integer> channelValues = (List) dataValue.get().getValues();
                if(channelValues != null && !channelValues.isEmpty()) {
                    allValues.add(channelValues);
                }
            }
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

            /* ----------------------  Distribution tests ---------------------- */
            double alpha = 0.05;
            NormalDistribution normal = new NormalDistribution();
            KolmogorovSmirnovTest KSTest = new KolmogorovSmirnovTest();
            double result = KSTest.kolmogorovSmirnovTest(normal, normalized);
            if(result > alpha) {
                cs.setDistribution("normal");
            } else {
                UniformRealDistribution urd = new UniformRealDistribution();
                result = KSTest.kolmogorovSmirnovTest(urd, normalized);
                if(result > alpha) {
                    cs.setDistribution("uniform");
                } else {
                    ConstantRealDistribution crd = new ConstantRealDistribution(1);
                    result = KSTest.kolmogorovSmirnovTest(crd, normalized);
                    if(result > alpha) {
                        cs.setDistribution("constant");
                    } else {
                        cs.setDistribution("undefined");
                    }
                }
            }
            /* ----------------------  Distribution tests ---------------------- */

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

    @Override
    public void addValueToSample(SampleValuesDto sdto, DataSample sample) {
        DataValue dv = new DataValue();
        dv.setId(sdto.getSampleId() + "@" + sdto.getChannelName());
        dv.setValues(sdto.getValues());
        dataValueRepository.save(dv);
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

    private Sensor getSensor(Long sensorId) {
        Optional<Sensor> sensorOpt = sensorRepository.findById(sensorId);
        if(sensorOpt.isPresent()){
            return sensorOpt.get();
        }else {
            return sensorRepository.findByAlias("RAW").get();
        }
    }
}
