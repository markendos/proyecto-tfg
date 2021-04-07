package es.upo.witzl.proyectotfg.samples.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class DataChannelId implements Serializable {
    @Column(name = "sample_id", nullable = false)
    private Long sampleId;

    @Column(length = 2, nullable = false)
    private String channelName;

    public DataChannelId() {
    }

    public DataChannelId(Long sampleId, String channelName) {
        this.sampleId = sampleId;
        this.channelName = channelName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataChannelId that = (DataChannelId) o;
        return sampleId.equals(that.sampleId) && channelName.equals(that.channelName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sampleId, channelName);
    }
}
