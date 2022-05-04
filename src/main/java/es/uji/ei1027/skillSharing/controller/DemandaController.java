package es.uji.ei1027.skillSharing.controller;

import es.uji.ei1027.skillSharing.dao.DemandaDao;
import es.uji.ei1027.skillSharing.dao.SkillDao;
import es.uji.ei1027.skillSharing.modelo.Demanda;
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
@RequestMapping("/demanda")
public class DemandaController {

    private DemandaDao demandaDao;
    private SkillDao skillDao;

    @Autowired
    public void setDemandaDao(DemandaDao demandaDao){this.demandaDao=demandaDao;}
    @Autowired
    public void setSkillDao(SkillDao skillDao){this.skillDao=skillDao;}

    @RequestMapping(value = "/add")
    public String addDemanda(Model model){
        model.addAttribute("demanda", new Demanda());
        model.addAttribute("skills",skillDao.getSkillsActivas());
        return "demanda/add";
    }

    @RequestMapping(value="/add", method= RequestMethod.POST)
    public String processAddSubmit(@ModelAttribute("demanda") Demanda demanda,
                                  BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "demanda/add";
        demandaDao.addDemanda(demanda);
        return "redirect:list";
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
        return "redirect:list";
    }

    @RequestMapping(value = "/delete/{idDemanda}")
    public String  processDeleteDemanda(@PathVariable String idDemanda){
        demandaDao.endDemanda(idDemanda);
        return "redirect:../../list";
    }

    // TODO Se tiene que utilizar una vista especifica para listar las demandas de usuario,
    //  así se diferencian de las de alguien no registrado

    @RequestMapping("/list")
    public String listDemandas(Model model){
        model.addAttribute("demandas",demandaDao.getDemandas());
        model.addAttribute("skills", skillDao.getSkillsTodas());
        return "demanda/listOfertasUser";
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

    // TODO Falta listDemandas de SKP, hay que pensar entre todos que listar y como
}