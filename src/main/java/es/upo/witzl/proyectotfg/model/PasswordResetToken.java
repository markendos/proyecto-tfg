package es.upo.witzl.proyectotfg.model;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;

@Entity
public class PasswordResetToken extends Token{

    public PasswordResetToken() {
        super();
    }

    public PasswordResetToken(final String token) {
        super(token);
    }

    public PasswordResetToken(final String token, final User user) {
        super(token, user);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        return super.equals(obj);
    }
}
