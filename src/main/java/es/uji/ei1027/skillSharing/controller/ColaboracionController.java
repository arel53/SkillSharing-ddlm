package es.uji.ei1027.skillSharing.controller;

import es.uji.ei1027.skillSharing.dao.ColaboracionDao;
import es.uji.ei1027.skillSharing.dao.DemandaDao;
import es.uji.ei1027.skillSharing.dao.EstudianteDao;
import es.uji.ei1027.skillSharing.dao.OfertaDao;
import es.uji.ei1027.skillSharing.modelo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;



//TODO Se tiene que hacer un validados de colaboración

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
            return "redirect:../../login";
        }
        return "colaboracion/noColaborarMismaPersona";

    }

    @RequestMapping(value = "/addColaboracionOferta/{idOferta}")
    public String addColaboracionOferta(HttpSession session,@PathVariable String idOferta){
        if (session.getAttribute("user") == null){
            session.setAttribute("nextUrl","/addColaboracionOferta/" + idOferta);
            return "login";
        }
        Usuario user = (Usuario) session.getAttribute("user");
        Oferta oferta = ofertaDao.getOferta(idOferta);
        Colaboracion colaboracion = new Colaboracion();
        Demanda demanda = new Demanda();
        demanda.setEstudiante(user.getNif()); demanda.setHoras(oferta.getHoras()); demanda.setIniFecha(oferta.getIniFecha());
        demanda.setFinFecha(oferta.getFinFecha()); demanda.setSkill(oferta.getSkill());
        demanda.setNombreSkill(oferta.getNombreSkill()); demanda.setNivelSkill(oferta.getNivelSkill());demanda.setDescripcion("Creada automática por el sistema");
        demandaDao.addDemanda(demanda);
        demandaDao.endDemanda(demanda.getIdDemanda()+"");
        int idDemanda = demandaDao.devuelveUltimoId();
        colaboracion.setIdOferta(oferta.getIdOferta()); colaboracion.setIdDemanda(idDemanda);colaboracion.setHoras(oferta.getHoras());
        colaboracion.setIniFecha(oferta.getIniFecha()); colaboracion.setFinFecha(oferta.getFinFecha());
        colaboracionDao.addColaboracion(colaboracion);
        return "redirect:../listMisColaboraciones";
    }


    @RequestMapping(value = "/addColaboracionDemanda/{idDemanda}")
    public String addColaboracionDemanda(HttpSession session,@PathVariable String idDemanda){
        if (session.getAttribute("user") == null){
            session.setAttribute("nextUrl","/addColaboracionDemanda/"+ idDemanda);
            return "login";
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
        ofertaDao.endOferta(oferta.getIdOferta()+"");
        int idOferta = ofertaDao.devuelveUltimoId();
        colaboracion.setIdOferta(idOferta); colaboracion.setIdDemanda(demanda.getIdDemanda());colaboracion.setHoras(demanda.getHoras());
        colaboracion.setIniFecha(demanda.getIniFecha()); colaboracion.setFinFecha(demanda.getFinFecha());
        colaboracionDao.addColaboracion(colaboracion);
        return "redirect:../listMisColaboraciones";
    }

    @RequestMapping(value = "/colaborar/{idOferta}/{idDemanda}")
    public String addColaboracionDemanda(HttpSession session,@PathVariable String idOferta,@PathVariable String idDemanda ){

        if (session.getAttribute("user") == null){
            session.setAttribute("nextUrl","/addColaboracionDemanda/"+ idDemanda);
            return "login";
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
    public String editColaboracion(Model model, @PathVariable String idColaboracion){
        model.addAttribute("colaboracion",colaboracionDao.getColaboracion(idColaboracion));
        return "colaboracion/update";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String processUpdateSubmit(
            @ModelAttribute("colaboracion") Colaboracion colaboracion,
            BindingResult bindingResult){
        if (bindingResult.hasErrors())
            return "colaboracion/update";
        colaboracionDao.updateColaboracion(colaboracion);
        return "redirect:list";
    }

    @RequestMapping(value = "/delete/{idColaboracion}")
    public String  processDeleteDemanda(@PathVariable String idColaboracion){
        colaboracionDao.endColaboracion(idColaboracion);
        return "redirect:../../listSKP";
    }

    @RequestMapping("/listSKP")
    public String listColaboraciones(Model model){
        model.addAttribute("colaboraciones",colaboracionDao.getColaboraciones());
        return "colaboracion/listSKP";
    }

    // Solo puede valorar el demandante
    @RequestMapping("/listMisColaboraciones")
    public String listMisColaboraciones(Model model, HttpSession session){
        if (session.getAttribute("user") == null){
            session.setAttribute("nextUrl","/colaboracion/listMisCoraciones");
            return "login";
        }
        Usuario user = (Usuario) session.getAttribute("user");
        model.addAttribute("misColaboraciones",colaboracionDao.getColaboracionesEstudianteActivas(user.getNif()));
        model.addAttribute("fechaActual", LocalDate.now());
        model.addAttribute("userNif", user.getNif());
        return "colaboracion/listMisColaboraciones";
    }

    @RequestMapping("/listMisColaboracionesValoradas")
    public String listMisColaboracionesValoradas(Model model, HttpSession session){
        if (session.getAttribute("user") == null){
            session.setAttribute("nextUrl","/colaboracion/listMisCoraciones");
            return "login";
        }
        Usuario user = (Usuario) session.getAttribute("user");
        model.addAttribute("misColaboraciones",colaboracionDao.getColaboracionesEstudianteNoActivas(user.getNif()));
        return "colaboracion/listMisColaboracionesValoradas";
    }

    @RequestMapping(value = "/valorar/{idColaboracion}", method = RequestMethod.GET)
    public String valorar(HttpSession session, Model model, @PathVariable("idColaboracion") String idColaboracion){
        if (session.getAttribute("user") == null){
            session.setAttribute("nextUrl","/colaboracion/listMisCoraciones");
            return "redirect:../../login";
        }
        model.addAttribute("colaboracion", colaboracionDao.getColaboracion(idColaboracion));
        return "colaboracion/valorar";
    }

    @RequestMapping(value = "processValoracion", method = RequestMethod.POST)
    public String processValoracion(HttpSession session, @ModelAttribute("colaboracion") Colaboracion colaboracion, BindingResult bindingResult){
        if (session.getAttribute("user") == null){
            session.setAttribute("nextUrl","/colaboracion/listMisCoraciones");
            return "redirect:../../login";
        }
        if (bindingResult.hasErrors())
            return "colaboracion/valorar";
        System.out.println(colaboracion);
        colaboracionDao.updateColaboracion(colaboracion);
        colaboracionDao.endColaboracion(colaboracion.getIdColaboracion() + "");
        return "redirect:listMisColaboraciones";
    }

}
