package es.upo.witzl.proyectotfg.projects.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import es.upo.witzl.proyectotfg.samples.model.DataSample;
import es.upo.witzl.proyectotfg.users.model.User;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;
import java.util.Objects;

@Entity
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(length = 60, nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;

    @ManyToMany
    @JoinTable(name = "project_label", joinColumns = @JoinColumn(name = "project_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "label_id", referencedColumnName = "name"))
    private Collection<Label> labels;

    @OneToOne(mappedBy = "project")
    @PrimaryKeyJoinColumn
    private Subject subject;

    @OneToMany(mappedBy = "project")
    private Collection<CollaborationRequest> collaborationRequests;

    @OneToMany(mappedBy = "project")
    private Collection<DataSample> dataSamples;

    @Transient
    private Integer numSamples;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Collection<Label> getLabels() {
        return labels;
    }

    public void setLabels(Collection<Label> labels) {
        this.labels = labels;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    @JsonIgnore
    public Collection<CollaborationRequest> getCollaborationRequests() {
        return collaborationRequests;
    }

    public void setCollaborationRequests(Collection<CollaborationRequest> collaborationRequests) {
        this.collaborationRequests = collaborationRequests;
    }

    @JsonIgnore
    public Collection<DataSample> getDataSamples() {
        return dataSamples;
    }

    public void setDataSamples(Collection<DataSample> dataSamples) {
        this.dataSamples = dataSamples;
    }

    public boolean isOwner(String user) {
        return user.equals(this.getUser().getUsername());
    }

    public Integer getNumSamples() {
        return dataSamples == null ? 0 : dataSamples.size();
    }

    public boolean hasCollaborationRequests() {
        return this.getCollaborationRequests() != null && !this.getCollaborationRequests().isEmpty();
    }

    public boolean hasDataSamples() {
        return this.getDataSamples() != null && !this.getDataSamples().isEmpty();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Project project = (Project) o;
        return id.equals(project.id) && user.equals(project.user) && name.equals(project.name) && description.equals(project.description) && startDate.equals(project.startDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user);
    }

    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", user=" + user +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", startDate=" + startDate +
                ", labels=" + labels +
                ", subject=" + subject +
                '}';
    }
}
