package es.uji.ei1027.skillSharing.controller;

import es.uji.ei1027.skillSharing.Mail;
import es.uji.ei1027.skillSharing.dao.DemandaDao;
import es.uji.ei1027.skillSharing.dao.OfertaDao;
import es.uji.ei1027.skillSharing.dao.SkillDao;
import es.uji.ei1027.skillSharing.modelo.Demanda;
import es.uji.ei1027.skillSharing.modelo.Oferta;
import es.uji.ei1027.skillSharing.modelo.Skill;
import es.uji.ei1027.skillSharing.modelo.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.*;

import javax.mail.Session;
import javax.servlet.http.HttpSession;
import java.util.List;


class DemandaValidator implements Validator {
    @Override
    public boolean supports(Class<?> cls) {
        return Demanda.class.isAssignableFrom(cls);
    }
    @Override
    public void validate(Object obj, Errors errors) {
        Demanda dem = (Demanda) obj;
        if (dem.getIniFecha() == null)
            errors.rejectValue("iniFecha", "empty_fechai", "Debes introducir una fecha de inicio");
        if (dem.getFinFecha() == null)
            errors.rejectValue("finFecha", "empty_fechaf", "Debes introducir una fecha de final");
        if (dem.getDescripcion().equals(""))
            errors.rejectValue("descripcion","empty_descr", "Debes introducir una descripción");
        if (dem.getHoras() == 0 )
            errors.rejectValue("horas","horas0","El número de horas debe ser superior a 0");
        if (dem.getHoras() < 0 )
            errors.rejectValue("horas","horas_neg","El número de horas debe ser superior a 0");

    }
}

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

    @RequestMapping(value = "/add", method = RequestMethod.GET) //alumno
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
        DemandaValidator demandaValidator = new DemandaValidator();
        demandaValidator.validate(demanda, bindingResult);
        if (bindingResult.hasErrors())
            return "demanda/add";
        Usuario user  = (Usuario) session.getAttribute("user");
        demanda.setEstudiante(user.getNif());
        List<Oferta> ofertaAsociadasSkill = ofertaDao.getOfertasAsociadasASkill(demanda.getSkill());
        demandaDao.addDemanda(demanda);
        Skill skillInfo = skillDao.getSkill(demanda.getSkill() + "");
        session.setAttribute("nombre", skillInfo.getNombre() + " " + skillInfo.getNivel());
        if (ofertaAsociadasSkill.isEmpty())
            return "redirect:listMisDemandas";
        else{
            return "redirect:/oferta/listOfertasUser/"+ demanda.getSkill()+"/"+demandaDao.devuelveUltimoId();
        }
    }

    @RequestMapping(value = "/update/{idDemanda}", method = RequestMethod.GET) //alumno
    public String editDemanda(HttpSession session, Model model, @PathVariable String idDemanda){
        if (session.getAttribute("user") == null){
            session.setAttribute("nextUrl","/oferta/update/"+idDemanda);
            return "redirect:/login";
        }
        model.addAttribute("demanda",demandaDao.getDemanda(idDemanda));
        model.addAttribute("skills",skillDao.getSkillsActivas());
        return "demanda/update";
    }

    @RequestMapping(value="/update", method = RequestMethod.POST)
    public String processUpdateSubmit(Model model, HttpSession session,@ModelAttribute("demanda") Demanda demanda,
            BindingResult bindingResult){
        if (session.getAttribute("user") == null){
            session.setAttribute("nextUrl","/oferta/update/"+demanda.getIdDemanda());
            return "redirect:/login";
        }
        model.addAttribute("skills", skillDao.getSkillsActivas());

        DemandaValidator demandaValidator = new DemandaValidator();
        demandaValidator.validate(demanda, bindingResult);
        if (bindingResult.hasErrors())
            return "demanda/update";
        Usuario user = (Usuario) session.getAttribute("user");
        session.setAttribute("editado", true);
        if (!user.getNif().equals(demanda.getEstudiante())){
            return "redirect:/forbiden";
        }
        demandaDao.updateDemanda(demanda);
        return "redirect:listMisDemandas";
    }

    @RequestMapping(value = "/delete/{idDemanda}")
    public String  processDeleteOferta(HttpSession session,@PathVariable String idDemanda){
        if (session.getAttribute("user") == null){
            session.setAttribute("nextUrl","/demanda/delete/"+ idDemanda);
            return "redirect:../../login";
        }
        Usuario user = (Usuario)session.getAttribute("user");
        session.setAttribute("eliminado", true);
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
    public String listDemandas(Model model,HttpSession session){
        if (session.getAttribute("user") == null){
            session.setAttribute("nextUrl","/demanda/list");
            return "redirect:/login";
        }
        Usuario user = (Usuario)session.getAttribute("user");
        if (!user.isSkp()){
            return "redirect:/forbiden";
        }
        model.addAttribute("demandas",demandaDao.getDemandas());
        model.addAttribute("demanda",new Demanda());
        model.addAttribute("skills",skillDao.getSkillsActivas());
        model.addAttribute("list","inicioDemandas");
        return "demanda/list";
    }

    @RequestMapping("/listSKP")
    public String listDemandasSKP(Model model, HttpSession session, @SessionAttribute(name = "eliminado", required = false) String eliminado){
        if (session.getAttribute("user") == null){
            session.setAttribute("nextUrl","/demanda/listSKP");
            return "redirect:/login";
        }

        model.addAttribute("demandas",demandaDao.getDemandas());
        model.addAttribute("demanda",new Demanda());
        model.addAttribute("skills",skillDao.getSkillsActivas());
        model.addAttribute("list","skpDemandas");
        model.addAttribute("eliminado", eliminado);
        session.removeAttribute("eliminado");
        return "demanda/listSKP";
    }

    @RequestMapping("/listDemandasUser/{idSkill}/{idOferta}")
    public String listDemandasUser(Model model, HttpSession session,@PathVariable String idSkill,@PathVariable String idOferta){
        if (session.getAttribute("user") == null){
            session.setAttribute("nextUrl","/demanda/listDemandasUser");
            return "redirect:/login";
        }
        Usuario user = (Usuario) session.getAttribute("user");
        model.addAttribute("misDemandas",demandaDao.getDemandasAsociadasASkill(Integer.parseInt(idSkill)));
        model.addAttribute("oferta",ofertaDao.getOferta(idOferta));
        List<String> emails = demandaDao.getDemandasEstudiantesEnviarCorreo(Integer.parseInt(idSkill));
        Session s = Mail.connect();

        for (String email: emails){
            Mail.send(s,"¡Hey! ¿Como va? Hay alguien que oferta tu habilidad demandada",
                    "Conectate a la aplicación para ver quien te podría ayudar y gana una nueva experiencia. ", email);
        }

        Mail.close(s);

        return "demanda/listDemandasEnlazadas";
    }

    @RequestMapping("/listDemandasUser")
    public String listDemandasUser(Model model, HttpSession session){
        if (session.getAttribute("user") == null){
            session.setAttribute("nextUrl","/demanda/listDemandasUser");
            return "redirect:/login";
        }
        Usuario user = (Usuario) session.getAttribute("user");
        model.addAttribute("demandas",demandaDao.getTodasDemandasMenosMias(user.getNif()));
        model.addAttribute("demanda",new Demanda());
        model.addAttribute("skills",skillDao.getSkillsActivas());
        model.addAttribute("list","demandasUser");
        return "demanda/listDemandasUser";
    }


    @RequestMapping("/listMisDemandas")
    public String listMisDemandas(Model model, HttpSession session, @SessionAttribute(name = "nombre", required = false)
            String nombre, @SessionAttribute(name = "editado", required = false) String editado, @SessionAttribute(name = "eliminado", required = false) String eliminado){
        if (session.getAttribute("user") == null){
            session.setAttribute("nextUrl","/demanda/listMisDemandas");
            return "redirect:/login";
        }
        Usuario user = (Usuario) session.getAttribute("user");
        model.addAttribute("demandas",demandaDao.getDemandasEstudiante(user.getNif()));
        model.addAttribute("demanda",new Demanda());
        model.addAttribute("skills", skillDao.getSkillsActivas());
        model.addAttribute("list", "misDemandas");
        model.addAttribute("nombre", nombre);
        session.removeAttribute("nombre");
        model.addAttribute("editado", editado);
        session.removeAttribute("editado");
        model.addAttribute("eliminado", eliminado);
        session.removeAttribute("eliminado");
        return "demanda/listMisDemandas";
    }

   @RequestMapping("/buscarDemandas/{idListado}")
    public String listBusqueda(HttpSession session,Model model, @ModelAttribute ("demanda") Demanda demanda, @PathVariable("idListado") int idListado){
        if (idListado != 0 && session.getAttribute("user") == null){
            session.setAttribute("nextUrl","/demanda/listDemandasUser");
            return "redirect:/login";
        }

        Usuario user = (Usuario)session.getAttribute("user");

        model.addAttribute("skills", skillDao.getSkillsActivas());
        model.addAttribute("filtrado", true);
        if (idListado == 0){
            if (demanda.getIniFecha() == null && demanda.getFinFecha()==null)
                model.addAttribute("demandas", demandaDao.getDemandasAsociadasASkill(demanda.getSkill(), "0","999999999"));
            else if(demanda.getIniFecha() == null)
                model.addAttribute("demandas", demandaDao.getDemandasAsociadasASkill(demanda.getSkill(), "0",demanda.getFinFecha().toString()));
            else if(demanda.getFinFecha()==null){
                System.out.println(demanda.getIniFecha());
                model.addAttribute("demandas", demandaDao.getDemandasAsociadasASkill(demanda.getSkill(), demanda.getIniFecha().toString(),"999999999"));}
            else
                model.addAttribute("demandas", demandaDao.getDemandasAsociadasASkill(demanda.getSkill(), demanda.getIniFecha().toString(), demanda.getFinFecha().toString()));
            model.addAttribute("list", "inicioDemandas");
            return "demanda/list";
        }
        else if (idListado == 1){
            if (demanda.getIniFecha() == null && demanda.getFinFecha()==null)
                model.addAttribute("demandas", demandaDao.getDemandasAsociadasASkillMenosMias(demanda.getSkill(),user.getNif(), "0","999999999"));
            else if(demanda.getIniFecha() == null)
                model.addAttribute("demandas", demandaDao.getDemandasAsociadasASkillMenosMias(demanda.getSkill(),user.getNif(), "0",demanda.getFinFecha().toString()));
            else if(demanda.getFinFecha()==null){
                System.out.println(demanda.getIniFecha());
                model.addAttribute("demandas", demandaDao.getDemandasAsociadasASkillMenosMias(demanda.getSkill(),user.getNif(), demanda.getIniFecha().toString(),"999999999"));}
            else
                model.addAttribute("demandas", demandaDao.getDemandasAsociadasASkillMenosMias(demanda.getSkill(),user.getNif(), demanda.getIniFecha().toString(), demanda.getFinFecha().toString()));
            model.addAttribute("list", "demandasUser");
            return "demanda/listDemandasUser";
        }
        else if (idListado == 2){
            if (demanda.getIniFecha() == null && demanda.getFinFecha()==null)
                model.addAttribute("demandas", demandaDao.getMisDemandasAsociadasASkill(demanda.getSkill(),user.getNif(), "0","999999999"));
            else if(demanda.getIniFecha() == null)
                model.addAttribute("demandas", demandaDao.getMisDemandasAsociadasASkill(demanda.getSkill(),user.getNif(), "0",demanda.getFinFecha().toString()));
            else if(demanda.getFinFecha()==null){
                System.out.println(demanda.getIniFecha());
                model.addAttribute("demandas", demandaDao.getMisDemandasAsociadasASkill(demanda.getSkill(),user.getNif(), demanda.getIniFecha().toString(),"999999999"));}
            else
                model.addAttribute("demandas", demandaDao.getMisDemandasAsociadasASkill(demanda.getSkill(),user.getNif(), demanda.getIniFecha().toString(), demanda.getFinFecha().toString()));

            model.addAttribute("list", "misDemandas");
            return "demanda/listMisDemandas";
        }
        else {
            if (demanda.getIniFecha() == null && demanda.getFinFecha()==null)
                model.addAttribute("demandas", demandaDao.getDemandasAsociadasASkill(demanda.getSkill(), "0","999999999"));
            else if(demanda.getIniFecha() == null)
                model.addAttribute("demandas", demandaDao.getDemandasAsociadasASkill(demanda.getSkill(), "0",demanda.getFinFecha().toString()));
            else if(demanda.getFinFecha()==null){
                System.out.println(demanda.getIniFecha());
                model.addAttribute("demandas", demandaDao.getDemandasAsociadasASkill(demanda.getSkill(), demanda.getIniFecha().toString(),"999999999"));}
            else
                model.addAttribute("demandas", demandaDao.getDemandasAsociadasASkill(demanda.getSkill(), demanda.getIniFecha().toString(), demanda.getFinFecha().toString()));
            model.addAttribute("list", "skpDemandas");
            return "demanda/listSKP";
        }
    }
}
