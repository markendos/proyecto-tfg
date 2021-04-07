package es.upo.witzl.proyectotfg.projects.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.List;

public class ProjectDto {

    @Positive
    private Long id;

    @NotBlank
    @Size(min = 1, max = 60)
    private String name;

    @NotBlank
    @Size(min= 1, max = 255)
    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
