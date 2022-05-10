package es.uji.ei1027.skillSharing.controller;

import com.fasterxml.jackson.databind.ser.std.UUIDSerializer;
import es.uji.ei1027.skillSharing.dao.EstudianteDao;
import es.uji.ei1027.skillSharing.dao.UsuarioDao;
import es.uji.ei1027.skillSharing.modelo.Estudiante;
import es.uji.ei1027.skillSharing.modelo.Usuario;
import org.apache.catalina.User;
import org.jasypt.util.password.BasicPasswordEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/estudiante")
public class EstudianteController {

    private EstudianteDao estudianteDao;
    private UsuarioDao usuarioDao;
    @Autowired
    public void setUsuarioDao(UsuarioDao usuarioDao){this.usuarioDao=usuarioDao;}

    @Autowired
    public void setEstudianteDao(EstudianteDao estudianteDao){this.estudianteDao=estudianteDao;}

    @RequestMapping("/perfil")
    public String listEstudiante(HttpSession session, Model model){

        if (session.getAttribute("user") == null){
            session.setAttribute("nextUrl","/estudiante/perfil");
            return "login";
        }
        Usuario user = (Usuario)session.getAttribute("user");

        return "redirect:get/"+ user.getNif();
    }

    @RequestMapping(value = "/get/{nif}", method = RequestMethod.GET)
    public String getEstudiante(HttpSession session, Model model, @PathVariable String nif){
        if (session.getAttribute("user") == null){
            session.setAttribute("nextUrl","/estudiante/perfil");
            return "redirect:/login";
        }
        Usuario user = (Usuario) session.getAttribute("user");
        if (!user.getNif().equals(nif)){
            return "redirect:/forbiden";
        }
        Estudiante estudiante = estudianteDao.getEstudiante(nif);
        model.addAttribute("estudiante", estudiante);
        return "estudiante/perfil";
    }



    @RequestMapping("/list")
    public String listDemandas(Model model){
        model.addAttribute("estudiantes",estudianteDao.getEstudiantes());
        return "estudiante/list";
    }

    @RequestMapping(value = "/update/{nif}", method = RequestMethod.GET)
    public String editEstudiante(HttpSession session,Model model, @PathVariable String nif){

        if (session.getAttribute("user") == null){
            session.setAttribute("nextUrl","/estudiante/update/"+nif);
            return "redirect:/login";
        }
        Usuario user = (Usuario)session.getAttribute("user");
        if (!user.getNif().equals(nif)){
            return "redirect:/forbiden";
        }
        Estudiante es = estudianteDao.getEstudiante(nif);
        model.addAttribute("estudiante", es);
        return "estudiante/update";
    }

    @RequestMapping(value = "/passwdUser/{nif}", method = RequestMethod.GET)
    public String goPasswdUser(HttpSession session ,Model model, @PathVariable String nif){
        if (session.getAttribute("user") == null){
            session.setAttribute("nextUrl","/estudiante/update/"+nif);
            return "redirect:/login";
        }
        Usuario user = (Usuario) session.getAttribute("user");
        if (!user.getNif().equals(nif)){
            return "redirect:/forbiden";
            }
        model.addAttribute("usuario", user);
        return "estudiante/passwdUser";

        }
    @RequestMapping(value="/passwdUser", method = RequestMethod.POST)
    public String processUpdateSubmitUser(HttpSession session, @ModelAttribute("usuario") Usuario usuario, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return "estudiante/passwd";
        }
        BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();
        usuario.setPassword(passwordEncryptor.encryptPassword(usuario.getPassword()));
        usuarioDao.updateUsuario(usuario);
        return "redirect:update/"+usuario.getNif();
    }


    @RequestMapping(value="/update", method = RequestMethod.POST)
    public String processUpdateSubmit(
            @ModelAttribute("estudiante") Estudiante estudiante,
            BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return "estudiante/update";
        }
        estudianteDao.updateEstudiante(estudiante);
        return "redirect:perfil";
    }
}
