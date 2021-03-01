package es.upo.witzl.proyectotfg.projects.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class CollaborationRequestKey implements Serializable {

    @Column(name = "collaborator_id")
    private Long collaboratorId;

    @Column(name = "project_id")
    private Long projectId;

    public Long getCollaboratorId() {
        return collaboratorId;
    }

    public void setCollaboratorId(Long collaboratorId) {
        this.collaboratorId = collaboratorId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CollaborationRequestKey that = (CollaborationRequestKey) o;
        return collaboratorId.equals(that.collaboratorId) && projectId.equals(that.projectId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(collaboratorId, projectId);
    }
}
