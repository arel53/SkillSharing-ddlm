package es.uji.ei1027.skillSharing.controller;

import es.uji.ei1027.skillSharing.dao.UsuarioDao;
import es.uji.ei1027.skillSharing.modelo.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
public class IndexController {
    private UsuarioDao usuarioDao;
    @Autowired
    public void setUsuarioDao(UsuarioDao usuarioDao){this.usuarioDao=usuarioDao;}

    @RequestMapping("/")
    public String home(HttpSession session){
        Usuario user = (Usuario)session.getAttribute("user");
        if (user != null){
            if (user.isSkp()){
                return "redirect:/inicio/skpIndex";
            }else{
                return "redirect:/inicio/userIndex";
            }
        }
        return "/inicio/index";
    }
    @RequestMapping("/disableduser")
    public String goDisabled(HttpSession session) {
        return "disabled_user";
   }
   @RequestMapping("/forbiden")
    public String goForbiden(HttpSession session) {
        return "forbiden";
    }
    @RequestMapping("/inicio/skpIndex")
    public String goInicioSkpIndex(HttpSession session){
        if (session.getAttribute("user") == null){
            session.setAttribute("nextUrl","/inicio/skpIndex");
            //System.out.println("noesd");
            return "redirect:/login";
        }
        Usuario user = (Usuario)session.getAttribute("user");
        if (!user.isSkp()){
            return "redirect:/forbiden";
        }
        return "/inicio/skpIndex";
    }

    @RequestMapping("/inicio/userIndex")
    public String goInicioUserIndex(HttpSession session){
        if (session.getAttribute("user") == null){
            session.setAttribute("nextUrl","/inicio/userIndex");
            return "redirect:/login";
        }
        Usuario user = (Usuario)session.getAttribute("user");
        if (user.isSkp()){
            return "redirect:/forbiden";
        }
        return "/inicio/userIndex";
    }
}
