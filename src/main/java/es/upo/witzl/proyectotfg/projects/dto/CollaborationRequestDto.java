package es.upo.witzl.proyectotfg.projects.dto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

public class CollaborationRequestDto {

    @Positive
    private Long projectId;

    @Size(max = 255)
    private String requestMessage;

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getRequestMessage() {
        return requestMessage;
    }

    public void setRequestMessage(String requestMessage) {
        this.requestMessage = requestMessage;
    }

    @Override
    public String toString() {
        return "CollaborationRequestDto{" +
                "projectId=" + projectId +
                ", requestMessage='" + requestMessage + '\'' +
                '}';
    }
}
