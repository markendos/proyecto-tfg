package es.upo.witzl.proyectotfg.samples.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class ChannelStatistics {

    @EmbeddedId
    private DataChannelId id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "channel_name")
    @JoinColumn(name = "sample_id")
    private DataChannel dataChannel;

    private Double min;

    private Double max;

    private Double mean;

    private Double stdDev;

    private Double median;

    @Column(length = 60)
    private String distribution;

    @JsonIgnore
    public DataChannelId getId() {
        return id;
    }

    public void setId(DataChannelId id) {
        this.id = id;
    }

    @JsonIgnore
    public DataChannel getDataChannel() {
        return dataChannel;
    }

    public void setDataChannel(DataChannel dataChannel) {
        this.dataChannel = dataChannel;
        this.setId(new DataChannelId(dataChannel.getSampleId(), dataChannel.getChannelName()));
    }

    public Double getMean() {
        return mean;
    }

    public void setMean(Double mean) {
        this.mean = mean;
    }

    public Double getStdDev() {
        return stdDev;
    }

    public void setStdDev(Double stdDev) {
        this.stdDev = stdDev;
    }

    public Double getMedian() {
        return median;
    }

    public void setMedian(Double median) {
        this.median = median;
    }

    public Double getMin() {
        return min;
    }

    public void setMin(Double kurtosis) {
        this.min = kurtosis;
    }

    public Double getMax() {
        return max;
    }

    public void setMax(Double skewness) {
        this.max = skewness;
    }

    public String getDistribution() {
        return distribution;
    }

    public void setDistribution(String distribution) {
        this.distribution = distribution;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChannelStatistics that = (ChannelStatistics) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
