package es.uji.ei1027.skillSharing.controller;

import es.uji.ei1027.skillSharing.dao.EstudianteDao;
import es.uji.ei1027.skillSharing.modelo.Estudiante;
import es.uji.ei1027.skillSharing.modelo.Oferta;
import es.uji.ei1027.skillSharing.modelo.Usuario;
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

    @Autowired
    public void setEstudianteDao(EstudianteDao estudianteDao){this.estudianteDao=estudianteDao;}

    @RequestMapping("/getEstudiante")
    public String listEstudiante(HttpSession session, Model model){

        if (session.getAttribute("user") == null){
            session.setAttribute("nextUrl","/estudiante/perfil");
            return "login";
        }
        Usuario user = (Usuario)session.getAttribute("user");

        model.addAttribute("estudiante",estudianteDao.getEstudiante(user.getNif()));
        return "redirect:/get/"+ user.getNif();
    }

    @RequestMapping(value = "/get/{nif}", method = RequestMethod.GET)
    public String getEstudiante(HttpSession session, Model model, @PathVariable String nif){
        if (session.getAttribute("user") == null){
            session.setAttribute("nextUrl","/estudiante/perfil");
            return "login";
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
        Estudiante e = estudianteDao.getEstudiante(nif);
        model.addAttribute("estudiante", e);
        if (session.getAttribute("user") == null){
            session.setAttribute("nextUrl","/estudiante/update");
            return "login";
        }
        return "/update";
    }


    @RequestMapping(value="/update", method = RequestMethod.POST)
    public String processUpdateSubmit(
            @ModelAttribute("estudiante") Estudiante estudiante,
            BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return "estudiante/update";
        }
        estudianteDao.updateEstudiante(estudiante);
        return "redirect:/getEstudiante";
    }
}
