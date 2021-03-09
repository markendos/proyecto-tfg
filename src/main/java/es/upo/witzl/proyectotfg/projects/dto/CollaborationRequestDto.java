package es.upo.witzl.proyectotfg.projects.dto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.Base64;

public class CollaborationRequestDto {

    private String collaboratorEmail;

    @Positive
    private Long projectId;

    private String requestStatus;

    @Size(max = 255)
    private String requestMessage;

    public String getCollaboratorEmail() {
        return collaboratorEmail;
    }

    public void setCollaboratorEmail(String collaboratorEmail) {
        this.collaboratorEmail = decodeBase64(collaboratorEmail);
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus.trim().toLowerCase();
    }

    public String getRequestMessage() {
        return requestMessage;
    }

    public void setRequestMessage(String requestMessage) {
        this.requestMessage = requestMessage;
    }

    private String decodeBase64(String encoded) {
        byte[] decodedBytes = Base64.getDecoder().decode(encoded);
        return new String(decodedBytes);
    }
}
