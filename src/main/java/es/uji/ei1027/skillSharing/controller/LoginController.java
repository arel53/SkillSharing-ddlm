package es.uji.ei1027.skillSharing.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import es.uji.ei1027.skillSharing.dao.UsuarioDao;
import es.uji.ei1027.skillSharing.modelo.Usuario;

class UserValidator implements Validator {
    @Override
    public boolean supports(Class<?> cls) {
        return Usuario.class.isAssignableFrom(cls);
    }
    @Override
    public void validate(Object obj, Errors errors) {
        Usuario userd = (Usuario) obj;
        if (userd.getUsername().equals(""))
            errors.rejectValue("usuario", "user_empty",
                    "Cal introduir un usuari");
        if (userd.getPassword().equals(""))
            errors.rejectValue("usuario", "pd_empty",
                    "Cal introduir una contrasenya");
    }
}

@Controller
public class LoginController {
    @Autowired
    private UsuarioDao usuarioDao;

    @RequestMapping("/login")
    public String login(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "login";
    }
    @RequestMapping(value="/login", method= RequestMethod.POST)
    public String checkLogin(@ModelAttribute("usuario") Usuario usuario,
                             BindingResult bindingResult, HttpSession session) {
        UserValidator userValidator = new UserValidator();
        userValidator.validate(usuario, bindingResult);
        if (bindingResult.hasErrors()) {
            return "login";
        }
        Usuario user;
        user = usuarioDao.loadUser(usuario);
        if (user == null) {
            bindingResult.rejectValue("password", "badpw", "Contrasenya incorrecta");
            return "login";
        }
        if (!user.isActive()){
            session.setAttribute("disabled", user.getDescripcion());
            return "disabled_user";
        }
        session.setAttribute("user", user);
        String nextUrl = (String) session.getAttribute("nextUrl");
        if (nextUrl == null){
            if(user.isSkp()) {
                return "inicio/skpIndex";
            }else{
                return "inicio/userIndex";
            }
        }

        session.removeAttribute("nextUrl");
        // Torna a la pàgina principal
        return "redirect:"+nextUrl;
    }
    @RequestMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
