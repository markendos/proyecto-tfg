package es.upo.witzl.proyectotfg.users.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.upo.witzl.proyectotfg.users.dto.UserRoleDto;
import es.upo.witzl.proyectotfg.users.model.Role;
import es.upo.witzl.proyectotfg.users.model.User;
import es.upo.witzl.proyectotfg.users.security.ActiveUserStore;
import es.upo.witzl.proyectotfg.users.security.LoggedUser;
import es.upo.witzl.proyectotfg.users.security.MyUserPrincipal;
import es.upo.witzl.proyectotfg.users.security.SessionUtils;
import es.upo.witzl.proyectotfg.users.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RestController
public class UserController {

    @Autowired
    IUserService userService;

    @Autowired
    SessionUtils sessionUtils;

    @GetMapping(value = "/admin/user/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getAllUsers() throws JsonProcessingException {
        List<User> allUsers = userService.getUsers();
        List<LoggedUser> active = ActiveUserStore.getInstance().getUsers();

        ObjectMapper mapper = new ObjectMapper();
        HashMap aux = new HashMap();

        if(allUsers != null) {
            aux.put("all", allUsers);
        }
        if(active != null) {
            aux.put("active", active);
        }

        return ResponseEntity.ok(mapper.writeValueAsString(aux));
    }

    @PutMapping(value = "/admin/user/role", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity changeRole(@Valid @RequestBody UserRoleDto userAndRole, Authentication authentication) {
        Optional<User> userOptional = userService.getUserByEmail(userAndRole.getUser());
        MyUserPrincipal principal = (MyUserPrincipal) authentication.getPrincipal();

        if(userOptional.isPresent()) {
           User user = userOptional.get();

           if(user.getEmail().equals(principal.getEmail())) {
               return ResponseEntity.badRequest().build();
           } else {
               String roleStr = "ROLE_" + userAndRole.getRole().toUpperCase();
               Role role = userService.getRole(roleStr);
               user.setRole(role);
               userService.saveUser(user);
               sessionUtils.expireUserSessions(user.getEmail());
               return ResponseEntity.ok().build();
           }
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }
}
