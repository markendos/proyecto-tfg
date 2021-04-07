package es.upo.witzl.proyectotfg.users.dto;


import es.upo.witzl.proyectotfg.users.validation.ValidPassword;

public class PasswordDto {

    private String oldPassword;

    @ValidPassword
    private String newPassword;

    private  String token;

    private String username;

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
