package es.upo.witzl.proyectotfg.users.dto;

import es.upo.witzl.proyectotfg.users.validation.PasswordMatches;
import es.upo.witzl.proyectotfg.users.validation.ValidEmail;
import es.upo.witzl.proyectotfg.users.validation.ValidPassword;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@PasswordMatches
public class UserDto {

    @ValidEmail
    @NotBlank
    @Size(min = 1, max = 50, message = "{Size.userDto.email}")
    private String email;

    @NotBlank
    @Size(min = 1, max = 50, message = "{Size.userDto.username}")
    private String username;

    @ValidPassword
    private String password;

    @NotBlank
    @Size(min = 1)
    private String matchingPassword;

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    private Integer role;

    public Integer getRole() {
        return role;
    }

    public void setRole(final Integer role) {
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public String getMatchingPassword() {
        return matchingPassword;
    }

    public void setMatchingPassword(final String matchingPassword) {
        this.matchingPassword = matchingPassword;
    }
}
