package es.upo.witzl.proyectotfg.projects.model;

import es.upo.witzl.proyectotfg.users.model.User;

import javax.persistence.*;

@Entity
public class CollaborationRequest {

    @EmbeddedId
    CollaborationRequestKey id;

    @ManyToOne
    @MapsId("collaboratorId")
    @JoinColumn(name = "collaborator_id")
    private User collaborator;

    @ManyToOne
    @MapsId("projectId")
    @JoinColumn(name = "project_id")
    private Project project;

    @Column(length = 10, nullable = false)
    private String requestStatus;

    private String requestMessage;

    public CollaborationRequestKey getId() {
        return id;
    }

    public void setId(CollaborationRequestKey id) {
        this.id = id;
    }

    public User getCollaborator() {
        return collaborator;
    }

    public void setCollaborator(User collaborator) {
        this.collaborator = collaborator;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public String getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }

    public String getRequestMessage() {
        return requestMessage;
    }

    public void setRequestMessage(String requestMessage) {
        this.requestMessage = requestMessage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CollaborationRequest that = (CollaborationRequest) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "CollaborationRequest{" +
                "id=" + id +
                ", collaborator=" + collaborator +
                ", project=" + project +
                ", requestStatus='" + requestStatus + '\'' +
                ", requestMessage='" + requestMessage + '\'' +
                '}';
    }
}
