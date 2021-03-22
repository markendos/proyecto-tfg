package es.upo.witzl.proyectotfg.samples.dto;

import javax.validation.constraints.Positive;
import java.util.List;

public class SampleValuesDto {

    @Positive
    private Long sampleId;

    private String channelName;

    private List<Integer> values;

    public Long getSampleId() {
        return sampleId;
    }

    public void setSampleId(Long sampleId) {
        this.sampleId = sampleId;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public List<Integer> getValues() {
        return values;
    }

    public void setValues(List<Integer> values) {
        this.values = values;
    }
}
