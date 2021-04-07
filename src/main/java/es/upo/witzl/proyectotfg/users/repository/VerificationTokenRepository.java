package es.upo.witzl.proyectotfg.users.repository;

import es.upo.witzl.proyectotfg.users.model.User;
import es.upo.witzl.proyectotfg.users.model.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, String> {

    VerificationToken findByToken(String token);

    VerificationToken findByUser(User user);

    @Modifying
    @Query("delete from VerificationToken t where t.expiryDate <= ?1")
    void deleteAllExpiredSince(Date now);
}
