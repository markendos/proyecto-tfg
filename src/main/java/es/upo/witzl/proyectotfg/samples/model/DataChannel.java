package es.upo.witzl.proyectotfg.samples.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Objects;

@Entity
@IdClass(DataChannelId.class)
public class DataChannel {

    @Id
    @Column(name = "sample_id")
    private Long sampleId;

    @MapsId
    @ManyToOne
    @JoinColumn(name = "sample_id")
    private DataSample dataSample;

    @Id
    @Column(length = 2)
    private String channelName;

    @Column(length = 60)
    private String channelLabel;

    @Column(columnDefinition = "integer default 10", nullable = false)
    private Integer resolution;

    private Boolean digital;

    private Boolean input;

    @ManyToOne
    @JoinColumn(name = "sensor_id", nullable = false)
    private Sensor sensor;

    @OneToOne(mappedBy = "dataChannel")
    @PrimaryKeyJoinColumn
    private ChannelStatistics statistics;

    public Long getSampleId() {
        return sampleId;
    }

    public void setSampleId(Long sampleId) {
        this.sampleId = sampleId;
    }

    @JsonIgnore
    public DataSample getDataSample() {
        return dataSample;
    }

    public void setDataSample(DataSample dataSample) {
        this.dataSample = dataSample;
        this.setSampleId(dataSample.getId());
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getChannelLabel() {
        return channelLabel;
    }

    public void setChannelLabel(String channelLabel) {
        this.channelLabel = channelLabel;
    }

    public Integer getResolution() {
        return resolution;
    }

    public void setResolution(Integer resolution) {
        this.resolution = resolution;
    }

    public Boolean isDigital() {
        return digital;
    }

    public void setDigital(Boolean digital) {
        this.digital = digital;
    }

    public Boolean isInput() {
        return input;
    }

    public void setInput(Boolean input) {
        this.input = input;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }

    public ChannelStatistics getStatistics() {
        return statistics;
    }

    public void setStatistics(ChannelStatistics statistics) {
        this.statistics = statistics;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataChannel that = (DataChannel) o;
        return dataSample.equals(that.dataSample) && channelName.equals(that.channelName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dataSample, channelName);
    }

    @Override
    public String toString() {
        return "DataChannel{" +
                ", channelName='" + channelName + '\'' +
                ", channelLabel='" + channelLabel + '\'' +
                ", resolution=" + resolution +
                ", digital=" + digital +
                ", input=" + input +
                ", sensor=" + sensor +
                '}';
    }
}
