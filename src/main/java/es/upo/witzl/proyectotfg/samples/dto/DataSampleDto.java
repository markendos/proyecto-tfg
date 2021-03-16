package es.upo.witzl.proyectotfg.samples.dto;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

public class DataSampleDto {

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

    @Column(length = 4)
    private Float samplingRate;

    private String comments;

}
