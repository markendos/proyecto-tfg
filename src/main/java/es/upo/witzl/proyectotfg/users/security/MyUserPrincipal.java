package es.upo.witzl.proyectotfg.users.security;

import es.upo.witzl.proyectotfg.users.model.Privilege;
import es.upo.witzl.proyectotfg.users.model.Role;
import es.upo.witzl.proyectotfg.users.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class MyUserPrincipal implements UserDetails {

    private User user;

    public MyUserPrincipal(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getAuthorities(Arrays.asList(user.getRole()));
    }


    private Collection<? extends GrantedAuthority> getAuthorities(final Collection<Role> roles) {
        List<String> authorities = getPrivileges(roles);
        authorities.addAll(getRoles(roles));
        return getGrantedAuthorities(authorities);
    }

    private List<String> getPrivileges(final Collection<Role> roles) {
        final List<String> privileges = new ArrayList<>();
        final List<Privilege> collection = new ArrayList<>();
        for (final Role role : roles) {
            collection.addAll(role.getPrivileges());
        }
        for (final Privilege item : collection) {
            privileges.add(item.getName());
        }

        return privileges;
    }

    private List<String> getRoles(final Collection<Role> roles) {
        final List<String> roleNames = new ArrayList<>();

        for (final Role role : roles) {
            roleNames.add(role.getName());
        }

        return roleNames;
    }

    private List<GrantedAuthority> getGrantedAuthorities(final List<String> authorities) {
        final List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        for (final String auth : authorities) {
            grantedAuthorities.add(new SimpleGrantedAuthority(auth));
        }
        return grantedAuthorities;
    }
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    public String getEmail() {
        return user.getEmail();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.isEnabled();
    }

    public boolean isAdmin() {
        return user.isAdmin();
    }
}
