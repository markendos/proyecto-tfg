package es.upo.witzl.proyectotfg.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Collection;
import java.util.Iterator;

@Entity // This tells Hibernate to make a table out of this class
public class User {

    @Id
    @Column(length = 50)
    private String email;

    @Column(unique = true, nullable = false, length = 50)
    private String username;

    @Column(length = 60)
    private String password;

    private boolean enabled;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(columnDefinition = "varchar(50)", name = "user_email", referencedColumnName = "email"), inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Collection<Role> roles;

    @OneToOne(mappedBy = "user")
    @PrimaryKeyJoinColumn
    private VerificationToken verificationTokentoken;

    @OneToOne(mappedBy = "user")
    @PrimaryKeyJoinColumn
    private PasswordResetToken resetToken;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Collection<Role> getRoles() {
        return roles;
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = roles;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public VerificationToken getVerificationTokentoken() {
        return verificationTokentoken;
    }

    public void setVerificationTokentoken(VerificationToken verificationTokentoken) {
        this.verificationTokentoken = verificationTokentoken;
    }

    public PasswordResetToken getResetToken() {
        return resetToken;
    }

    public void setResetToken(PasswordResetToken resetToken) {
        this.resetToken = resetToken;
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
        final StringBuilder builder = new StringBuilder();
        builder.append("User [email=")
                .append(email)
                .append(", username=").append(username)
                .append(", enabled=").append(enabled)
                .append(", roles=").append(roles)
                .append("]");
        return builder.toString();
    }

    public boolean isAdmin() {
        Iterator<Role> it = roles.iterator();
        boolean exit = false;

        while (it.hasNext() && !exit) {
            Role r = it.next();
            if (r.getName().equals("ROLE_ADMIN")) {
                exit = true;
            }
        }
        return exit;
    }
}
