package es.uji.ei1027.skillSharing.controller;

import es.uji.ei1027.skillSharing.dao.DemandaDao;
import es.uji.ei1027.skillSharing.dao.OfertaDao;
import es.uji.ei1027.skillSharing.dao.SkillDao;
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
import java.util.List;


@Controller
@RequestMapping("/demanda")
public class DemandaController {

    private DemandaDao demandaDao;
    private SkillDao skillDao;
    private OfertaDao ofertaDao;

    @Autowired
    public void setDemandaDao(DemandaDao demandaDao){this.demandaDao=demandaDao;}
    @Autowired
    public void setSkillDao(SkillDao skillDao){this.skillDao=skillDao;}
    @Autowired
    public void setOfertaDao(OfertaDao ofertaDao) {this.ofertaDao = ofertaDao;}

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String addDemanda(HttpSession session, Model model){
        if (session.getAttribute("user") == null){
            session.setAttribute("nextUrl","/demanda/add");
            return "redirect:/login";
        }

        model.addAttribute("demanda", new Demanda());
        model.addAttribute("skills",skillDao.getSkillsActivas());
        return "demanda/add";
    }

    @RequestMapping(value="/add", method= RequestMethod.POST)
    public String processAddSubmit(HttpSession session,@ModelAttribute("demanda") Demanda demanda,
                                  BindingResult bindingResult) {
        if (session.getAttribute("user") == null){
            session.setAttribute("nextUrl","/demanda/add");
            return "redirect:/login";
        }
        if (bindingResult.hasErrors())
            return "demanda/add";
        Usuario user  = (Usuario) session.getAttribute("user");
        demanda.setEstudiante(user.getNif());
        List<Oferta> ofertaAsociadasSkill = ofertaDao.getOfertasAsociadasASkill(demanda.getSkill());
        demandaDao.addDemanda(demanda);
        if (ofertaAsociadasSkill.isEmpty())
            return "redirect:listMisDemandas";
        else{
            return "redirect:/oferta/listOfertasUser/"+ demanda.getSkill();
        }
    }

    @RequestMapping(value = "/update/{idDemanda}", method = RequestMethod.GET)
    public String editDemanda(Model model, @PathVariable String idDemanda){
        model.addAttribute("demanda",demandaDao.getDemanda(idDemanda));
        model.addAttribute("skills",skillDao.getSkillsActivas());
        return "demanda/update";
    }

    @RequestMapping(value="/update", method = RequestMethod.POST)
    public String processUpdateSubmit(
            @ModelAttribute("demanda") Demanda demanda,
            BindingResult bindingResult){
        if (bindingResult.hasErrors())
            return "demanda/update";
        demandaDao.updateDemanda(demanda);
        return "redirect:listMisDemandas";
    }

    @RequestMapping(value = "/delete/{idDemanda}")
    public String  processDeleteOferta(HttpSession session,@PathVariable String idDemanda){
        if (session.getAttribute("user") == null){
            session.setAttribute("nextUrl","/usuario/list");
            return "login";
        }
        Usuario user = (Usuario)session.getAttribute("user");
        if (!user.isSkp()){
            Demanda demanda = (Demanda)demandaDao.getDemanda(idDemanda);
            if (user.getNif().equals(demanda.getEstudiante())){
                demandaDao.endDemanda(idDemanda);
                return "redirect:../../listMisDemandas";
            }else{
                return "redirect:/forbiden";
            }
        }else{
            demandaDao.endDemanda(idDemanda);
            return "redirect:../listSKP";
        }
    }






    @RequestMapping("/list")
    public String listDemandas(Model model){
        model.addAttribute("demandas",demandaDao.getDemandas());
        return "demanda/list";
    }

    @RequestMapping("/listSKP")
    public String listDemandasSKP(Model model){
        model.addAttribute("demandas",demandaDao.getDemandas());
        return "demanda/listSKP";
    }

    @RequestMapping("/listDemandasUser")
    public String listDemandasUser(Model model, HttpSession session){
        if (session.getAttribute("user") == null){
            session.setAttribute("nextUrl","/usuario/list");
            return "login";
        }
        Usuario user = (Usuario) session.getAttribute("user");
        model.addAttribute("misDemandas",demandaDao.getTodasDemandasMenosMias(user.getNif()));
        return "demanda/listDemandasUser";
    }

    @RequestMapping("/listDemandasUser/{idSkill}")
    public String listDemandasUser(Model model, HttpSession session,@PathVariable String idSkill){
        if (session.getAttribute("user") == null){
            session.setAttribute("nextUrl","/usuario/list");
            return "login";
        }
        Usuario user = (Usuario) session.getAttribute("user");
        model.addAttribute("misDemandas",demandaDao.getDemandasAsociadasASkill(Integer.parseInt(idSkill)));
        return "demanda/listDemandasEnlazadas";
    }

    @RequestMapping("/listMisDemandas")
    public String listMisDemandas(Model model, HttpSession session){
        if (session.getAttribute("user") == null){
            session.setAttribute("nextUrl","/usuario/list");
            return "login";
        }
        Usuario user = (Usuario) session.getAttribute("user");
        model.addAttribute("misDemandas",demandaDao.getDemandasEstudiante(user.getNif()));
        return "demanda/listMisDemandas";
    }

    // TODO Falta listDemandas de SKP, hay que pensar entre todos que listar y como 259x2x48
}