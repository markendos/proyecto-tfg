package es.upo.witzl.proyectotfg.projects.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

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

    @Temporal(TemporalType.DATE)
    private Date birthDate;

    @Column(length = 1)
    private String gender;

    @Column(length = 6)
    private Float weight;

    @Column(length = 6)
    private Float height;

    @Column(nullable = true)
    private Boolean smoker;

    @Column(length = 10)
    private Float bmi;

    @Column(length = 10)
    private Float bfp;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @JsonIgnore
    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Float getWeight() {
        return weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }

    public Float getHeight() {
        return height;
    }

    public void setHeight(Float height) {
        this.height = height;
    }

    public Boolean isSmoker() {
        return smoker;
    }

    public void setSmoker(Boolean smoker) {
        this.smoker = smoker;
    }

    public Float getBmi() {
        return bmi;
    }

    public void setBmi(Float bmi) {
        this.bmi = bmi;
    }

    public Float getBfp() {
        return bfp;
    }

    public void setBfp(Float bfp) {
        this.bfp = bfp;
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
