package es.upo.witzl.proyectotfg.samples.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.List;

public class DataSampleDto {

    @Positive
    private Long projectId;

    @Size(max = 60)
    private String deviceName;

    @Size(max = 60)
    private String deviceModel;

    @Size(max = 60)
    private String deviceFirmware;

    @Min(1)
    @Max(1000)
    private Float samplingRate;

    @Size(max = 255)
    private String comments;

    private List<String> channelNames;

    private List<String> channelLabels;

    private List<Integer> channelResolutions;

    private List<Long> sensors;

    private Integer size;

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public String getDeviceFirmware() {
        return deviceFirmware;
    }

    public void setDeviceFirmware(String deviceFirmware) {
        this.deviceFirmware = deviceFirmware;
    }

    public Float getSamplingRate() {
        return samplingRate;
    }

    public void setSamplingRate(Float samplingRate) {
        this.samplingRate = samplingRate;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public List<String> getChannelNames() {
        return channelNames;
    }

    public void setChannelNames(List<String> channelNames) {
        this.channelNames = channelNames;
    }

    public List<String> getChannelLabels() {
        return channelLabels;
    }

    public void setChannelLabels(List<String> channelLabels) {
        this.channelLabels = channelLabels;
    }

    public List<Integer> getChannelResolutions() {
        return channelResolutions;
    }

    public void setChannelResolutions(List<Integer> channelResolutions) {
        this.channelResolutions = channelResolutions;
    }

    public List<Long> getSensors() {
        return sensors;
    }

    public void setSensors(List<Long> sensors) {
        this.sensors = sensors;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}
