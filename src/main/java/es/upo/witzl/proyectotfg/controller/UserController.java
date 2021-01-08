package es.upo.witzl.proyectotfg.controller;

import es.upo.witzl.proyectotfg.security.ActiveUserStore;
import es.upo.witzl.proyectotfg.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UserController {

    @Autowired
    ActiveUserStore activeUserStore;

    @Autowired
    IUserService userService;

    @GetMapping("/loggedUsers")
    public ModelAndView getLoggedUsers(final ModelMap model) {
        model.addAttribute("users", activeUserStore.getUsers());
        return new ModelAndView("users", model);
    }
}
