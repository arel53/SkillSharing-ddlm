package es.uji.ei1027.skillSharing.controller;

import es.uji.ei1027.skillSharing.modelo.Usuario;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller

public class IndexController {
    @RequestMapping("/")
    public String redirectIndex(HttpSession session, Model model){
    if (session.getAttribute("user") == null)
    {
        session.setAttribute("nextUrl","/usuario/list");
        model.addAttribute("usuario", new Usuario());
        return "login";
    }

    return "usuario/list";
}

}
