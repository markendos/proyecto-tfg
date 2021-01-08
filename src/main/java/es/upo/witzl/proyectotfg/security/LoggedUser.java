package es.upo.witzl.proyectotfg.security;

import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;
import java.util.List;
import java.util.Objects;

@Component
public class LoggedUser implements HttpSessionBindingListener {
    private String email;
    private String username;
    private boolean admin;
    private String ip;
    private ActiveUserStore activeUserStore;

    public LoggedUser(String email, String username, boolean admin, String ip, ActiveUserStore activeUserStore) {
        this.email = email;
        this.username = username;
        this.admin = admin;
        this.activeUserStore = activeUserStore;
        this.ip = ip;
    }

    public LoggedUser() {
    }

    @Override
    public void valueBound(HttpSessionBindingEvent event) {
        List<LoggedUser> users = activeUserStore.getUsers();
        LoggedUser user = (LoggedUser) event.getValue();
        if (!users.contains(user.getUsername())) {
            users.add(user);
        }
    }

    @Override
    public void valueUnbound(HttpSessionBindingEvent event) {
        List<LoggedUser> users = activeUserStore.getUsers();
        LoggedUser user = (LoggedUser) event.getValue();
        users.remove(user);
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

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        admin = admin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoggedUser that = (LoggedUser) o;
        return email.equals(that.email) && username.equals(that.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, username);
    }

    @Override
    public String toString() {
        return "LoggedUser{" +
                "email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", admin=" + admin + '\'' +
                ", ip=" + ip +
                '}';
    }
}
