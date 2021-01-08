package es.upo.witzl.proyectotfg.security;

import java.util.ArrayList;
import java.util.List;

public class ActiveUserStore {

    public List<LoggedUser> users;

    public ActiveUserStore() {
        users = new ArrayList<>();
    }

    public List<LoggedUser> getUsers() {
        return users;
    }

    public void setUsers(List<LoggedUser> users) {
        this.users = users;
    }
}
