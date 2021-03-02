package es.upo.witzl.proyectotfg.projects.dto;

import javax.validation.constraints.Positive;
import java.util.List;

public class LabelsDto {

    @Positive
    private Long projectId;

    private List<String> names;

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public List<String> getNames() {
        return names;
    }

    public void setNames(List<String> names) {
        this.names = names;
    }
}
