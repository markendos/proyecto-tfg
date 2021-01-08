package es.upo.witzl.proyectotfg.repository;

import es.upo.witzl.proyectotfg.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete
@Repository
public interface UserRepository extends JpaRepository<User, String> {
    void deleteById(String email);

    User findByEmail(String email);

    User findByUsername(String username);
}
