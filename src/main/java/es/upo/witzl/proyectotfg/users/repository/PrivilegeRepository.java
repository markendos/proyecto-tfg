package es.upo.witzl.proyectotfg.users.repository;

import es.upo.witzl.proyectotfg.users.model.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {

    Privilege findByName(String name);
}
