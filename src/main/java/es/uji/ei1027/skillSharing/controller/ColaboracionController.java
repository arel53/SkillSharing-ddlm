package es.uji.ei1027.skillSharing.controller;

import es.uji.ei1027.skillSharing.dao.ColaboracionDao;
import es.uji.ei1027.skillSharing.dao.DemandaDao;
import es.uji.ei1027.skillSharing.dao.OfertaDao;
import es.uji.ei1027.skillSharing.modelo.Colaboracion;
import es.uji.ei1027.skillSharing.modelo.Demanda;
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
@RequestMapping("/colaboracion")
public class ColaboracionController {

    private ColaboracionDao colaboracionDao;
    private OfertaDao ofertaDao;
    private DemandaDao demandaDao;

    @Autowired
    public void setColaboracionDao(ColaboracionDao colaboracionDao){this.colaboracionDao=colaboracionDao;}

    @Autowired
    public void setOfertaDao(OfertaDao ofertaDao) {this.ofertaDao = ofertaDao;}

    @Autowired
    public void setDemandaDao(DemandaDao demandaDao) {this.demandaDao = demandaDao;}

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
        int idOferta = ofertaDao.devuelveUltimoId();
        colaboracion.setIdOferta(idOferta); colaboracion.setIdDemanda(demanda.getIdDemanda());colaboracion.setHoras(demanda.getHoras());
        colaboracion.setIniFecha(demanda.getIniFecha()); colaboracion.setFinFecha(demanda.getFinFecha());
        colaboracionDao.addColaboracion(colaboracion);
        return "redirect:../listMisColaboraciones";
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
        return "redirect:../../list";
    }

    @RequestMapping("/listColaboracionesUser")
    public String listColaboraciones(Model model){
        model.addAttribute("colaboraciones",colaboracionDao.getColaboraciones());
        return "colaboracion/listUser";
    }

    @RequestMapping("/listMisColaboraciones")
    public String listMisColaboraciones(Model model, HttpSession session){
        if (session.getAttribute("user") == null){
            session.setAttribute("nextUrl","/usuario/list");
            return "login";
        }
        model.addAttribute("colaboraciones",colaboracionDao.getColaboraciones());
        return "colaboracion/listMisColaboraciones";
    }



}
