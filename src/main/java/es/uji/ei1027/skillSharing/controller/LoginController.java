package es.uji.ei1027.skillSharing.controller;

import javax.servlet.http.HttpSession;
import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
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
        Usuario usuario = (Usuario) obj;
        if (usuario.getUsername().equals("")){
            //System.out.println("baduser");
            errors.rejectValue("username", "user_empty", "Debes intruducir un nombre de usuario");
        }
        if (usuario.getPassword().equals("")) {
            //System.out.println("empt pass");
            errors.rejectValue("password", "pd_empty","Debes intruducir una contraseña");
        }
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

    @RequestMapping("/loginColaboraDemanda/{idDemanda}")
    public String loginColaboraDemanda(@PathVariable("idDemanda") String idDemanda){
        return "redirect:../colaboracion/confirmColabDemanda/"+ idDemanda;
    }

    @RequestMapping("/loginColaboraOferta/{idOferta}")
    public String loginColaboraOferta(@PathVariable("idOferta") String idOferta){
        return "redirect:../colaboracion/confirmColabOferta/"+ idOferta;
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
            return "redirect:/disableduser";
        }
        session.setAttribute("user", user);
        String nextUrl = (String) session.getAttribute("nextUrl");
        if (nextUrl == null){
            if(user.isSkp()) {
                return "redirect:/inicio/skpIndex";
            }else{
                return "redirect:/inicio/userIndex";
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
