package es.uji.ei1027.skillSharing.controller;

import es.uji.ei1027.skillSharing.dao.ColaboracionDao;
import es.uji.ei1027.skillSharing.dao.EstudianteDao;
import es.uji.ei1027.skillSharing.dao.UsuarioDao;
import es.uji.ei1027.skillSharing.modelo.Estudiante;
import es.uji.ei1027.skillSharing.modelo.Usuario;
import org.jasypt.util.password.BasicPasswordEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;


class EstudianteValidador implements Validator{
    @Override
    public boolean supports(Class<?> cls) { return Estudiante.class.isAssignableFrom(cls);
    }

    @Override
    public void validate(Object obj, Errors errors) {
        Estudiante e = (Estudiante) obj;
        if (e.getNombre().equals(""))
            errors.rejectValue("nombre", "empty_nombre", "El campo Nombre no puede estar vacio");
        if (e.getApellido().equals(""))
            errors.rejectValue("apellido", "empty_apellido", "El campo Apellido no puede estar vacio");
        if (e.getGrado().equals(""))
            errors.rejectValue("grado", "empty_grado", "El campo Grado no puede estar vacio");
        if (e.getDireccion().equals(""))
            errors.rejectValue("direccion", "empty_direccion", "El campo Dirección no puede estar vacio");
    }
}

@Controller
@RequestMapping("/estudiante")
public class EstudianteController {

    private EstudianteDao estudianteDao;
    private UsuarioDao usuarioDao;
    private ColaboracionDao colaboracionDao;
    @Autowired
    public void setUsuarioDao(UsuarioDao usuarioDao){this.usuarioDao=usuarioDao;}

    @Autowired
    public void setEstudianteDao(EstudianteDao estudianteDao){this.estudianteDao=estudianteDao;}

    @Autowired
    public void setcolaboracionDao(ColaboracionDao colaboracionDao){this.colaboracionDao=colaboracionDao;}

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
        model.addAttribute("horasOfrecidas", colaboracionDao.getHorasOfrecidasEstudiante(nif));
        model.addAttribute("horasRecibidas", colaboracionDao.getHorasRecibidasEstudiante(nif));
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
        session.setAttribute("old_estudiante", es);
        return "estudiante/update";
    }

    @RequestMapping(value="/update", method = RequestMethod.POST)
    public String processUpdateSubmit( HttpSession session,
            @ModelAttribute("estudiante") Estudiante estudiante,
            BindingResult bindingResult){
        Usuario user = (Usuario) session.getAttribute("user");
        Estudiante oldEstudiante = (Estudiante) session.getAttribute("old_estudiante");
        if (user.isSkp()){
            return "redirect:/forbiden";
        }

        if (!estudiante.getNif().equals(user.getNif())){
            return "redirect:/forbiden";
        }

        if (!estudiante.getEmail().equals(oldEstudiante.getEmail())){
            return "redirect:/forbiden";
        }

        if (estudiante.getEdad() != oldEstudiante.getEdad()){
            return "redirect:/forbiden";
        }

        if (!estudiante.getSexo().equals(oldEstudiante.getSexo())){
            return "redirect:/forbiden";
        }

        if (estudiante.getHoras() != oldEstudiante.getHoras()){
            return "redirect:/forbiden";
        }

        EstudianteValidador estudianteValidator = new EstudianteValidador();
        estudianteValidator.validate(estudiante, bindingResult);
        if (bindingResult.hasErrors()) {
            return "estudiante/update";
        }

        estudianteDao.updateEstudiante(estudiante);
        return "redirect:perfil";
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
}
