package es.upo.witzl.proyectotfg.users.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import es.upo.witzl.proyectotfg.projects.model.CollaborationRequest;
import es.upo.witzl.proyectotfg.projects.model.Project;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity // This tells Hibernate to make a table out of this class
public class User{

    @Id
    @Column(length = 50)
    private String email;

    @Column(unique = true, nullable = false, length = 50)
    private String username;

    @Column(length = 60, nullable = false)
    private String password;

    private boolean enabled;

    @ManyToOne
    @JoinColumn(name="role_id", nullable=false)
    private Role role;

    @OneToOne(mappedBy = "user")
    @PrimaryKeyJoinColumn
    private VerificationToken verificationTokentoken;

    @OneToOne(mappedBy = "user")
    @PrimaryKeyJoinColumn
    private PasswordResetToken resetToken;

    @OneToMany(mappedBy = "user")
    private Collection<Project> ownedProjects;

    @OneToMany(mappedBy = "collaborator")
    private Collection<CollaborationRequest> collaborationRequests;

    public User() {
        super();
        this.enabled = false;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    @JsonIgnore
    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @JsonIgnore
    public VerificationToken getVerificationTokentoken() {
        return verificationTokentoken;
    }

    public void setVerificationTokentoken(VerificationToken verificationTokentoken) {
        this.verificationTokentoken = verificationTokentoken;
    }

    @JsonIgnore
    public PasswordResetToken getResetToken() {
        return resetToken;
    }

    public void setResetToken(PasswordResetToken resetToken) {
        this.resetToken = resetToken;
    }

    @JsonIgnore
    public Collection<Project> getOwnedProjects() {
        return ownedProjects;
    }

    public void setOwnedProjects(Collection<Project> ownedProjects) {
        this.ownedProjects = ownedProjects;
    }

    @JsonIgnore
    public Collection<CollaborationRequest> getCollaborationRequests() {
        return collaborationRequests;
    }

    public void setCollaborationRequests(Collection<CollaborationRequest> collaborationRequests) {
        this.collaborationRequests = collaborationRequests;
    }

    @JsonIgnore
    public Collection<Project> getCollaboratedProjects() {
        Collection<CollaborationRequest> collaborationRequests = getCollaborationRequests();
        Collection<Project> approved = new ArrayList<>();
        for(CollaborationRequest cr : collaborationRequests) {
            if(cr.getRequestStatus().equals("approved")) {
                approved.add(cr.getProject());
            }
        }
        return approved;
    }

    @JsonIgnore
    public Collection<Project> getRequestedProjects() {
        Collection<CollaborationRequest> collaborationRequests = getCollaborationRequests();
        Collection<Project> requested = new ArrayList<>();
        for(CollaborationRequest cr : collaborationRequests) {
            requested.add(cr.getProject());
        }
        return requested;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + ((getEmail() == null) ? 0 : getEmail().hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final User user = (User) obj;
        if (!getEmail().equals(user.getEmail())) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", enabled=" + enabled +
                ", role=" + role +
                ", verificationTokentoken=" + verificationTokentoken +
                ", resetToken=" + resetToken +
                '}';
    }

    public boolean isAdmin(){
        return role.getName().equals("ROLE_ADMIN");
    }
}
