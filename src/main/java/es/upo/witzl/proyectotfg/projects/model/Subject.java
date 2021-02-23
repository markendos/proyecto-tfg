package es.upo.witzl.proyectotfg.projects.model;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
public class Subject {

    @Id
    @Column(name = "project_id")
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "project_id")
    protected Project project;

    @Temporal(TemporalType.TIMESTAMP)
    private Date birthDate;

    @Column(length = 10)
    private String gender;

    @Column(length = 10)
    private float weight;

    @Column(length = 10)
    private float height;

    private boolean smoker;

    @Column(length = 10)
    private float bmi;

    @Column(length = 10)
    private float bfp;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subject subject = (Subject) o;
        return id.equals(subject.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Subject{" +
                "id='" + id + '\'' +
                ", project=" + project +
                ", birthDate=" + birthDate +
                ", gender='" + gender + '\'' +
                ", weight=" + weight +
                ", height=" + height +
                ", smoker=" + smoker +
                ", bmi=" + bmi +
                ", bfp=" + bfp +
                '}';
    }
}
