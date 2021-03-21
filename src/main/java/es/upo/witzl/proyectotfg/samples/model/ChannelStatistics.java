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
    @JoinColumn(name = "sample_id")
    @JoinColumn(name = "channel_name")
    private DataChannel dataChannel;

    private Double mean;

    private Double stdDev;

    private Double kurtosis;

    private Double skewness;

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

    public Double getKurtosis() {
        return kurtosis;
    }

    public void setKurtosis(Double kurtosis) {
        this.kurtosis = kurtosis;
    }

    public Double getSkewness() {
        return skewness;
    }

    public void setSkewness(Double skewness) {
        this.skewness = skewness;
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
