package es.upo.witzl.proyectotfg.users.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ActiveUserStore {

    private static ActiveUserStore INSTANCE;
    private List<LoggedUser> users;

    public ActiveUserStore() {
        this.users = new ArrayList<LoggedUser>();
    }

    public static ActiveUserStore getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new ActiveUserStore();
        }

        return INSTANCE;
    }

    public List<LoggedUser> getUsers() {
        return users;
    }

    public void setUsers(List<LoggedUser> users) {
        this.users = users;
    }

    public void addUser(LoggedUser user) {
        this.users.add(user);
    }

    public void addUsers(Collection<LoggedUser> newUsers) {
        this.users.addAll(newUsers);
    }
}
