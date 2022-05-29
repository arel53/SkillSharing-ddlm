package es.uji.ei1027.skillSharing.controller;

import es.uji.ei1027.skillSharing.Mail;
import es.uji.ei1027.skillSharing.dao.ColaboracionDao;
import es.uji.ei1027.skillSharing.dao.DemandaDao;
import es.uji.ei1027.skillSharing.dao.EstudianteDao;
import es.uji.ei1027.skillSharing.dao.OfertaDao;
import es.uji.ei1027.skillSharing.modelo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.*;

import javax.mail.Session;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;



//TODO Se tiene que hacer un validados de colaboración
class ValoracionValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }

    @Override
    public void validate(Object obj, Errors errors) {
        Colaboracion colaboracion = (Colaboracion) obj;
        if (colaboracion.getComentario().equals("")){
            errors.rejectValue("comentario", "empty_comment", "Se debe introducir un comentario");
        }
        if ( colaboracion.getRate() > 5 || colaboracion.getRate() <= 0){
            errors.rejectValue("rate", "bad_rate_value", "Se debe introducir un valor para " +
                    "la puntuación entre el 0 y el 5 (incluidos)");
        }
    }
}

@Controller
@RequestMapping("/colaboracion")
public class ColaboracionController {

    private ColaboracionDao colaboracionDao;
    private OfertaDao ofertaDao;
    private DemandaDao demandaDao;
    private EstudianteDao estudianteDao;

    @Autowired
    public  void setEstudianteDao(EstudianteDao estudianteDao){this.estudianteDao=estudianteDao;}
    @Autowired
    public void setColaboracionDao(ColaboracionDao colaboracionDao){this.colaboracionDao=colaboracionDao;}

    @Autowired
    public void setOfertaDao(OfertaDao ofertaDao) {this.ofertaDao = ofertaDao;}

    @Autowired
    public void setDemandaDao(DemandaDao demandaDao) {this.demandaDao = demandaDao;}


    @RequestMapping(value = "/confirmColabOferta/{idOferta}")
    public String goConfirmOferta(HttpSession session, Model model, @PathVariable String idOferta){
        if (session.getAttribute("user") == null){
            session.setAttribute("nextUrl","colaboracion/confirmColabOferta/" + idOferta);
            return "redirect:../../login";
        }
        Oferta of = (Oferta)ofertaDao.getOferta(idOferta);
        Estudiante es = (Estudiante) estudianteDao.getEstudiante(of.getEstudiante());


        Usuario user = (Usuario) session.getAttribute("user");
        if(user.getNif().equals(of.getEstudiante())){
            return "redirect:../mismaPersona";
        }

        model.addAttribute("estudiante", es);
        model.addAttribute("oferta", of);
        return "colaboracion/confirmColabOferta";
    }

    @RequestMapping(value = "/confirmColabDemanda/{idDemanda}")
    public String goConfirmDemanda(HttpSession session, Model model, @PathVariable String idDemanda){
        if (session.getAttribute("user") == null){
            session.setAttribute("nextUrl","colaboracion/confirmColabDemanda/" + idDemanda);
            return "redirect:../../login";
        }
        Usuario user = (Usuario) session.getAttribute("user");
        Demanda demanda = demandaDao.getDemanda(idDemanda);
        Estudiante es = estudianteDao.getEstudiante(demanda.getEstudiante());
        if(user.getNif().equals(demanda.getEstudiante())){
            return "redirect:../mismaPersona";
        }
        model.addAttribute("estudiante", es);
        model.addAttribute("demanda", demanda);
        return "colaboracion/confirmColabDemanda";
    }

    @RequestMapping(value = "/mismaPersona")
    public String noColaborarMismaPersona(HttpSession session){
        if (session.getAttribute("user") == null){
            session.setAttribute("nextUrl","colaboracion/mismaPersona");
            return "redirect:../login";
        }
        return "colaboracion/noColaborarMismaPersona";

    }

    @RequestMapping(value = "/addColaboracionOferta/{idOferta}")
    public String addColaboracionOferta(HttpSession session,@PathVariable String idOferta, Model model){
        if (session.getAttribute("user") == null){
            session.setAttribute("nextUrl","/addColaboracionOferta/" + idOferta);
            return "redirect:../../login";
        }
        Usuario user = (Usuario) session.getAttribute("user");
        Oferta oferta = ofertaDao.getOferta(idOferta);
        Colaboracion colaboracion = new Colaboracion();
        Demanda demanda = new Demanda();
        demanda.setEstudiante(user.getNif());
        demanda.setHoras(oferta.getHoras()); demanda.setIniFecha(oferta.getIniFecha());
        demanda.setFinFecha(oferta.getFinFecha()); demanda.setSkill(oferta.getSkill());
        demanda.setNombreSkill(oferta.getNombreSkill()); demanda.setNivelSkill(oferta.getNivelSkill());demanda.setDescripcion("Creada automática por el sistema");
        demandaDao.addDemanda(demanda);
        int idDemanda = demandaDao.devuelveUltimoId();
        demandaDao.endDemanda(idDemanda +"");
        colaboracion.setIdOferta(oferta.getIdOferta()); colaboracion.setIdDemanda(idDemanda);colaboracion.setHoras(oferta.getHoras());
        colaboracion.setIniFecha(oferta.getIniFecha()); colaboracion.setFinFecha(oferta.getFinFecha());
        colaboracionDao.addColaboracion(colaboracion);

        Estudiante e = estudianteDao.getEstudiante(user.getNif());
        Estudiante ofertante = estudianteDao.getEstudiante(oferta.getEstudiante());
        Mail.connect();
        Session s = Mail.connect();

        Mail.send(s,"¡Hey! ¿Como va? Hay alguien que ha solicitado una colaboración contigo",
            "Conectate a la aplicación para ver con quien vas a tener el gusto de colaborar. ", e.getEmail());

        Mail.close(s);

        session.setAttribute("nombre", ofertante.getNombre() + " " + ofertante.getApellido());

        return "redirect:../listMisColaboraciones";
    }


    @RequestMapping(value = "/addColaboracionDemanda/{idDemanda}")
    public String addColaboracionDemanda(HttpSession session,@PathVariable String idDemanda){
        if (session.getAttribute("user") == null){
            session.setAttribute("nextUrl","/addColaboracionDemanda/"+ idDemanda);
            return "redirect:../../login";
        }
        Usuario user = (Usuario) session.getAttribute("user");
        Demanda demanda = demandaDao.getDemanda(idDemanda);
        Colaboracion colaboracion = new Colaboracion();
        Oferta oferta = new Oferta();
        oferta.setEstudiante(user.getNif());oferta.setHoras(demanda.getHoras());
        oferta.setIniFecha(demanda.getIniFecha());
        oferta.setFinFecha(demanda.getFinFecha()); oferta.setSkill(demanda.getSkill());
        oferta.setNombreSkill(demanda.getNombreSkill()); oferta.setNivelSkill(demanda.getNivelSkill());
        oferta.setDescripcion("Creada automática por el sistema");
        ofertaDao.addOferta(oferta);
        int idOferta = ofertaDao.devuelveUltimoId();
        ofertaDao.endOferta(idOferta + "");
        colaboracion.setIdOferta(idOferta); colaboracion.setIdDemanda(demanda.getIdDemanda());colaboracion.setHoras(demanda.getHoras());
        colaboracion.setIniFecha(demanda.getIniFecha()); colaboracion.setFinFecha(demanda.getFinFecha());
        colaboracionDao.addColaboracion(colaboracion);

        Estudiante e = estudianteDao.getEstudiante(user.getNif());
        Estudiante demandante = estudianteDao.getEstudiante(demanda.getEstudiante());
        Mail.connect();
        Session s = Mail.connect();

        Mail.send(s,"¡Hey! ¿Como va? Hay alguien que ha solicitado una colaboración contigo",
                "Conectate a la aplicación para ver con quien vas a tener el gusto de colaborar. ", e.getEmail());

        Mail.close(s);

        session.setAttribute("nombre", demandante.getNombre() + " " + demandante.getApellido());
        return "redirect:../listMisColaboraciones";
    }

    @RequestMapping(value = "/colaborar/{idOferta}/{idDemanda}")
    public String addColaboracionDemanda(HttpSession session,@PathVariable String idOferta,@PathVariable String idDemanda ){

        if (session.getAttribute("user") == null){
            session.setAttribute("nextUrl","/addColaboracionDemanda/"+ idDemanda);
            return "redirect:../../../login";
        }

        Oferta oferta = ofertaDao.getOferta(idOferta);
        Demanda demanda = demandaDao.getDemanda(idDemanda);
        Colaboracion colaboracion = new Colaboracion();

        colaboracion.setIdOferta(Integer.parseInt(idOferta)); colaboracion.setIdDemanda(Integer.parseInt(idDemanda));colaboracion.setHoras(demanda.getHoras());
        colaboracion.setIniFecha(demanda.getIniFecha()); colaboracion.setFinFecha(demanda.getFinFecha());
        colaboracionDao.addColaboracion(colaboracion);

        return "redirect:../../listMisColaboraciones";

    }

    @RequestMapping(value = "/update/{idColaboracion}", method = RequestMethod.GET)
    public String editColaboracion(Model model, HttpSession session,@PathVariable String idColaboracion){
        if (session.getAttribute("user") == null){
            session.setAttribute("nextUrl","/colaboracion/update/" + idColaboracion);
            return "redirect:../../login";

        }
        model.addAttribute("colaboracion",colaboracionDao.getColaboracion(idColaboracion));
        return "colaboracion/update";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String processUpdateSubmit(HttpSession session,
            @ModelAttribute("colaboracion") Colaboracion colaboracion,
            BindingResult bindingResult){

        if (session.getAttribute("user") == null){
            session.setAttribute("nextUrl","/colaboracion/delete/");
            return "redirect:../../login";

        }
        if (bindingResult.hasErrors())
            return "colaboracion/update";
        colaboracionDao.updateColaboracion(colaboracion);
        return "redirect:list";
    }

    @RequestMapping(value = "/delete/{idColaboracion}/")
    public String  processDeleteColaboracion(@PathVariable String idColaboracion, HttpSession session){
        if (session.getAttribute("user") == null){
            session.setAttribute("nextUrl","/colaboracion/delete/" + idColaboracion);
            return "redirect:../../../login";

        }

        Usuario user = (Usuario) session.getAttribute("user");
        System.out.println(idColaboracion);
        colaboracionDao.endColaboracion(idColaboracion);
        session.setAttribute("eliminado", true);
        if (user.isSkp()){
            return "redirect:../../listSKP";
        }
        else
            return "redirect:../../listMisColaboraciones";
    }


    @RequestMapping(value = "/deleteEnCurso/{idColaboracion}/")
    public String  processDeleteColaboracionEnCurso(@PathVariable String idColaboracion, HttpSession session){
        if (session.getAttribute("user") == null){
            session.setAttribute("nextUrl","/colaboracion/delete/" + idColaboracion);
            return "redirect:../../../login";

        }

        Usuario user = (Usuario) session.getAttribute("user");
        System.out.println(idColaboracion);
        colaboracionDao.endColaboracion(idColaboracion);
        session.setAttribute("eliminado", true);
        return "redirect:../../enCurso";

    }

    @RequestMapping("/listSKP")
    public String listColaboraciones(Model model, HttpSession session,@SessionAttribute(name = "eliminado", required = false) String eliminado){
        if (session.getAttribute("user") == null){
            session.setAttribute("nextUrl","/colaboracion/listSKP");
            return "redirect:../login";
        }
        Usuario user = (Usuario) session.getAttribute("user");
        if (!user.isSkp())
            return "redirect:/forbiden";
        model.addAttribute("colaboraciones",colaboracionDao.getColaboraciones());
        model.addAttribute("eliminado", eliminado);
        session.removeAttribute("eliminado");

        return "colaboracion/listSKP";
    }

    // Solo puede valorar el demandante
    @RequestMapping("/listMisColaboraciones")
    public String listMisColaboraciones(Model model, HttpSession session, @SessionAttribute(name= "nombre", required=false) String nombre,
                                        @SessionAttribute(name = "eliminado", required = false) String eliminado){
        if (session.getAttribute("user") == null){
            session.setAttribute("nextUrl","/colaboracion/listMisCoraciones");
            return "redirect:../login";
        }
        Usuario user = (Usuario) session.getAttribute("user");
        model.addAttribute("misColaboraciones",colaboracionDao.getColaboracionesEstudianteActivas(user.getNif()));
        model.addAttribute("fechaActual", LocalDate.now());
        model.addAttribute("userNif", user.getNif());
        model.addAttribute("nombre", nombre);
        session.removeAttribute("nombre");
        model.addAttribute("eliminado", eliminado);
        session.removeAttribute("eliminado");
        return "colaboracion/listMisColaboraciones";
    }

    @RequestMapping("/listMisColaboracionesValoradas")
    public String listMisColaboracionesValoradas(Model model, HttpSession session,
                                                 @SessionAttribute(name = "valorada", required = false) String valorada){
        if (session.getAttribute("user") == null){
            session.setAttribute("nextUrl","/colaboracion/listMisCoracionesValoradas");
            return "redirect:../login";
        }
        Usuario user = (Usuario) session.getAttribute("user");
        model.addAttribute("misColaboraciones",colaboracionDao.getColaboracionesEstudianteNoActivas(user.getNif()));
        model.addAttribute("valorada", valorada);
        session.removeAttribute("valorada");
        return "colaboracion/listMisColaboracionesValoradas";
    }

    @RequestMapping(value = "/valorar/{idColaboracion}", method = RequestMethod.GET)
    public String valorar(HttpSession session, Model model, @PathVariable("idColaboracion") String idColaboracion){
        if (session.getAttribute("user") == null){
            session.setAttribute("nextUrl","/colaboracion/valorar/" + idColaboracion);
            return "redirect:../../login";
        }
        model.addAttribute("colaboracion", colaboracionDao.getColaboracion(idColaboracion));
        return "colaboracion/valorar";
    }

    @RequestMapping(value = "processValoracion", method = RequestMethod.POST)
    public String processValoracion(HttpSession session, @ModelAttribute("colaboracion") Colaboracion colaboracion, BindingResult bindingResult){
        if (session.getAttribute("user") == null){
            session.setAttribute("nextUrl","/colaboracion/processValoracion");
            return "redirect:../login";
        }
        ValoracionValidator valoracionValidator = new ValoracionValidator();
        valoracionValidator.validate(colaboracion, bindingResult);
        if (bindingResult.hasErrors())
            return "colaboracion/valorar";
        colaboracionDao.updateColaboracion(colaboracion);
        colaboracionDao.endColaboracion(colaboracion.getIdColaboracion() + "");
        session.setAttribute("valorada", true);
        return "redirect:listMisColaboracionesValoradas";
    }


    @RequestMapping(value = "porValorar")
    public String porValorar(HttpSession session, Model model, @SessionAttribute(name="nombre", required = false) String nombre, @SessionAttribute(name="eliminado", required = false) String eliminado){
        if (session.getAttribute("user") == null){
            session.setAttribute("nextUrl","/colaboracion/porValorar");
            return "redirect:../login";
        }
        Usuario user = (Usuario) session.getAttribute("user");
        model.addAttribute("misColaboraciones", colaboracionDao.getColaboracionesEstudianteActivas(user.getNif()));
        model.addAttribute("fechaActual", LocalDate.now());
        model.addAttribute("userNif", user.getNif());
        model.addAttribute("nombre", nombre);
        session.removeAttribute("nombre");
        model.addAttribute("eliminado", eliminado);
        session.removeAttribute("eliminado");
        return "colaboracion/colaboracionesPorValorar";
    }

    @RequestMapping(value = "enCurso")
    public String enCurso(HttpSession session, Model model, @SessionAttribute(name="nombre", required = false) String nombre, @SessionAttribute(name="eliminado", required = false) String eliminado){
        if (session.getAttribute("user") == null){
            session.setAttribute("nextUrl","/colaboracion/enCurso");
            return "redirect:../login";
        }
        Usuario user = (Usuario) session.getAttribute("user");
        model.addAttribute("misColaboraciones", colaboracionDao.getColaboracionesEstudianteActivas(user.getNif()));
        model.addAttribute("fechaActual", LocalDate.now());
        model.addAttribute("userNif", user.getNif());
        model.addAttribute("nombre", nombre);
        session.removeAttribute("nombre");
        model.addAttribute("eliminado", eliminado);
        session.removeAttribute("eliminado");
        return "colaboracion/colaboracionesEnCurso";
    }

}
