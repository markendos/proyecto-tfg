package es.upo.witzl.proyectotfg.dto;

import es.upo.witzl.proyectotfg.validation.ValidEmail;

import javax.validation.constraints.NotBlank;
import java.util.Base64;

public class UserRoleDto {
    @NotBlank
    private String user;

    @NotBlank
    private String role;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        byte[] decodedBytes = Base64.getDecoder().decode(user);
        String decodedString = new String(decodedBytes);
        this.user = decodedString;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
