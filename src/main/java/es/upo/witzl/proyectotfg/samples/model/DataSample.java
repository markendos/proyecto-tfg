package es.upo.witzl.proyectotfg.samples.model;

import es.upo.witzl.proyectotfg.projects.model.Project;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;

@Entity
public class DataSample {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(length = 60)
    private String deviceName;

    @Column(length = 60)
    private String deviceModel;

    @Column(length = 60)
    private String deviceFirmware;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date sampleDate;

    private boolean simulated;

    private float samplingRate;

    private String comments;

    @OneToMany(mappedBy = "dataSample")
    private Collection<DataChannel> dataChannels;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Date getSampleDate() {
        return sampleDate;
    }

    public void setSampleDate(Date sampleDate) {
        this.sampleDate = sampleDate;
    }

    public boolean isSimulated() {
        return simulated;
    }

    public void setSimulated(boolean simulated) {
        this.simulated = simulated;
    }

    public float getSamplingRate() {
        return samplingRate;
    }

    public void setSamplingRate(float samplingRate) {
        this.samplingRate = samplingRate;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Collection<DataChannel> getDataChannels() {
        return dataChannels;
    }

    public void setDataChannels(Collection<DataChannel> dataChannels) {
        this.dataChannels = dataChannels;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataSample that = (DataSample) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "DataSample{" +
                "id=" + id +
                ", deviceName='" + deviceName + '\'' +
                ", deviceModel='" + deviceModel + '\'' +
                ", deviceFirmware='" + deviceFirmware + '\'' +
                ", sampleDate=" + sampleDate +
                ", simulated=" + simulated +
                ", samplingRate=" + samplingRate +
                ", comments='" + comments + '\'' +
                ", dataChannels=" + dataChannels +
                '}';
    }
}
