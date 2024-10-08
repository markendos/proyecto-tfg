package es.upo.witzl.proyectotfg.projects.dto;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.Date;

public class SubjectDto {

    @Positive
    private Long projectId;

    @Past
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date birthDate;

    @NotBlank
    @Size(min = 1, max = 1)
    private String gender;

    @Positive
    private Float weight;

    @Positive
    private Float height;

    private Boolean smoker;

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
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

    @Override
    public String toString() {
        return "SubjectDto{" +
                "projectId=" + projectId +
                ", birthDate=" + birthDate +
                ", gender='" + gender + '\'' +
                ", weight=" + weight +
                ", height=" + height +
                ", smoker=" + smoker +
                '}';
    }
}
