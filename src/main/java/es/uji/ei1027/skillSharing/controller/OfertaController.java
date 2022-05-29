package es.uji.ei1027.skillSharing.controller;

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

import javax.servlet.http.HttpSession;
import java.util.List;


class OfertaValidator implements Validator {
    @Override
    public boolean supports(Class<?> cls) {
        return Oferta.class.isAssignableFrom(cls);
    }
    @Override
    public void validate(Object obj, Errors errors) {
        Oferta of = (Oferta) obj;
        if (of.getIniFecha() == null)
            errors.rejectValue("iniFecha", "empty_fechai", "Debes introducir una fecha de inicio");
        if (of.getFinFecha() == null)
            errors.rejectValue("finFecha", "empty_fechaf", "Debes introducir una fecha de final");
        if (of.getDescripcion().equals(""))
            errors.rejectValue("descripcion","empty_descr", "Debes introducir una descripción");
        if (of.getHoras() == 0 )
            errors.rejectValue("horas","horas0","El número de horas debe ser superior a 0");
        if (of.getHoras() < 0 )
            errors.rejectValue("horas","horas_neg","El número de horas debe ser superior a 0");
    }
}


@Controller
@RequestMapping("/oferta")
public class OfertaController {

    private OfertaDao ofertaDao;
    private SkillDao skillDao;
    private DemandaDao demandaDao;

    @Autowired
    public void setOfertaDao(OfertaDao ofertaDao){this.ofertaDao=ofertaDao;}
    @Autowired
    public void setSkillDao(SkillDao skillDao){this.skillDao=skillDao;}
    @Autowired
    public void setDemandaDao(DemandaDao demandaDao) {this.demandaDao = demandaDao;}


    @RequestMapping(value = "/add")
    public String addOferta(HttpSession session, Model model){
        if (session.getAttribute("user") == null){
            session.setAttribute("nextUrl","/oferta/add");
            return "redirect:/login";
        }
        model.addAttribute("oferta", new Oferta());
        model.addAttribute("skills", skillDao.getSkillsActivas());
        return "oferta/add";
    }

    @RequestMapping(value="/add", method= RequestMethod.POST) //alumno
    public String processAddSubmit(HttpSession session,Model model, @ModelAttribute("oferta") Oferta oferta,
                                  BindingResult bindingResult) {
        model.addAttribute("skills", skillDao.getSkillsActivas());

        if (session.getAttribute("user") == null){
            session.setAttribute("nextUrl","/oferta/add");
            return "redirect:/login";
        }
        OfertaValidator ofertaValidator = new OfertaValidator();
        ofertaValidator.validate(oferta, bindingResult);
        if (bindingResult.hasErrors()) {
            return "oferta/add";
        }
        Usuario user = (Usuario)session.getAttribute("user");
        oferta.setEstudiante(user.getNif());
        List<Demanda> demandaAsociadaSkill = demandaDao.getDemandasAsociadasASkill(oferta.getSkill());
        ofertaDao.addOferta(oferta);
        Skill skillInfo = skillDao.getSkill(oferta.getSkill() + "");
        session.setAttribute("nombre", skillInfo.getNombre() + " " + skillInfo.getNivel());
        if (demandaAsociadaSkill.isEmpty())
            return "redirect:listMisOfertas";
        else
            return "redirect:/demanda/listDemandasUser/"+ oferta.getSkill()+"/"+ofertaDao.devuelveUltimoId();
    }

    @RequestMapping(value = "/update/{idOferta}", method = RequestMethod.GET) //alumno
    public String editOferta(HttpSession session, Model model, @PathVariable String idOferta){
        if (session.getAttribute("user") == null){
            session.setAttribute("nextUrl","/oferta/update/"+idOferta);
            return "redirect:/login";
        }
        Oferta o = ofertaDao.getOferta(idOferta);
        model.addAttribute("oferta", o);
        model.addAttribute("skills", skillDao.getSkillsActivas());
        model.addAttribute("skill", o.getSkill());
        return "oferta/update";
    }

    @RequestMapping(value="/update", method = RequestMethod.POST) //alumno
    public String processUpdateSubmit(Model model, HttpSession session,@ModelAttribute("oferta") Oferta oferta,
            BindingResult bindingResult){
        if (session.getAttribute("user") == null){
            session.setAttribute("nextUrl","/oferta/update/"+oferta.getIdOferta());
            return "redirect:/login";
        }
        model.addAttribute("skills", skillDao.getSkillsActivas());
        OfertaValidator ofertaValidator = new OfertaValidator();
        ofertaValidator.validate(oferta, bindingResult);
        if (bindingResult.hasErrors()) {
            return "oferta/update";
        }
        Usuario user = (Usuario) session.getAttribute("user");
        session.setAttribute("editado", true);
        if (!user.getNif().equals(oferta.getEstudiante())){
            return "redirect:/forbiden";
        }
        ofertaDao.updateOferta(oferta);
        return "redirect:listMisOfertas";
    }

    @RequestMapping(value = "/delete/{idOferta}")
    public String  processDeleteOferta(HttpSession session,@PathVariable String idOferta){

        if (session.getAttribute("user") == null){
            session.setAttribute("nextUrl","/oferta/delete/"+idOferta);
            return "redirect:/login";
        }
        Usuario user = (Usuario)session.getAttribute("user");
        session.setAttribute("eliminado", true);
        if (!user.isSkp()){
            Oferta of = (Oferta) ofertaDao.getOferta(idOferta);
            if (user.getNif().equals(of.getEstudiante())){
                ofertaDao.endOferta(idOferta);
                return "redirect:../../listMisOfertas";
            }else{
                return "redirect:/forbiden";
            }
        }else{
            ofertaDao.endOferta(idOferta);
            return "redirect:../listSKP";
        }
    }

    @RequestMapping("/list")
    public String listOfertas(Model model, HttpSession session){
        if (session.getAttribute("user") == null){
            session.setAttribute("nextUrl","/oferta/list");
            return "redirect:/login";
        }
        model.addAttribute("ofertas",ofertaDao.getOfertas());
        model.addAttribute("oferta",new Oferta());
        List<Skill> skills = skillDao.getSkillsActivas();
        model.addAttribute("skills", skills);
        model.addAttribute("list", "inicioOfertas");
        return "oferta/list";
    }

    @RequestMapping("/listSKP")
    public String listOfertasSKP(Model model, @SessionAttribute(name = "eliminado", required = false) String eliminado, HttpSession session){

        if (session.getAttribute("user") == null){
            session.setAttribute("nextUrl","/oferta/listSKP");
            return "redirect:../login";
        }
        Usuario user = (Usuario)session.getAttribute("user");
        if (!user.isSkp()){
            return "redirect:/forbiden";
        }

        model.addAttribute("ofertas",ofertaDao.getOfertas());
        model.addAttribute("oferta",new Oferta());
        model.addAttribute("skills", skillDao.getSkillsActivas());
        model.addAttribute("list", "skpOfertas");
        model.addAttribute("eliminado", eliminado);
        session.removeAttribute("eliminado");
        return "oferta/listSKP";
    }

    @RequestMapping("/listOfertasUser/{idSkill}/{idDemanda}")
    public String listOfertasAsociadasSkillUser(HttpSession session, Model model, @PathVariable String idSkill, @PathVariable String idDemanda){

        if (session.getAttribute("user") == null){
            session.setAttribute("nextUrl","/listOfertas/User");
            return "redirect:/login";
        }
        Usuario user = (Usuario)session.getAttribute("user");

        model.addAttribute("ofertas",ofertaDao.getOfertasAsociadasASkill(Integer.parseInt(idSkill)));
        model.addAttribute("demanda",demandaDao.getDemanda(idDemanda));
        return "oferta/listOfertasEnlazadas";
    }


    @RequestMapping("/listOfertasUser")
    public String listOfertasUser(HttpSession session, Model model){

        if (session.getAttribute("user") == null){
            session.setAttribute("nextUrl","/oferta/listOfertasUser");
            return "redirect:../../login";
        }
        Usuario user = (Usuario)session.getAttribute("user");

        model.addAttribute("ofertas",ofertaDao.getTodasOfertasMenosMias(user.getNif()));
        model.addAttribute("oferta",new Oferta());
        model.addAttribute("skills", skillDao.getSkillsActivas());
        model.addAttribute("list", "ofertasUser");
        return "oferta/listOfertasUser";
    }


    @RequestMapping("/listMisOfertas")
    public String listMisOfertas(HttpSession session, Model model, @SessionAttribute(name = "nombre", required = false) String nombre,
                                 @SessionAttribute(name = "editado", required = false) String editado, @SessionAttribute(name = "eliminado", required = false) String eliminado){
        if (session.getAttribute("user") == null){
            session.setAttribute("nextUrl","/oferta/listMisOfertas");
            return "redirect:/login";
        }
        Usuario user = (Usuario)session.getAttribute("user");

        model.addAttribute("ofertas",ofertaDao.getOfertasEstudiante(user.getNif()));
        model.addAttribute("oferta",new Oferta());
        model.addAttribute("skills", skillDao.getSkillsActivas());
        model.addAttribute("list", "misOfertas");
        model.addAttribute("nombre", nombre);
        session.removeAttribute("nombre");
        model.addAttribute("editado", editado);
        session.removeAttribute("editado");
        model.addAttribute("eliminado", eliminado);
        session.removeAttribute("eliminado");
        return "oferta/listMisOfertas";
    }


    @RequestMapping("/buscarOfertas/{idListado}")
    public String listBusqueda(HttpSession session,Model model, @ModelAttribute ("oferta") Oferta oferta, @PathVariable("idListado") int idListado){
        if (idListado != 0 && session.getAttribute("user") == null){
            session.setAttribute("nextUrl","/oferta/listOfertasUser");
            return "redirect:/login";
        }
        Usuario user = (Usuario)session.getAttribute("user");

        model.addAttribute("skills", skillDao.getSkillsActivas());
        model.addAttribute("filtrado", true);
        if (idListado == 0){
            if (oferta.getIniFecha() == null && oferta.getFinFecha()==null)
                model.addAttribute("ofertas", ofertaDao.getOfertasAsociadasASkill(oferta.getSkill(), "0","999999999"));
            else if(oferta.getIniFecha() == null)
                model.addAttribute("ofertas", ofertaDao.getOfertasAsociadasASkill(oferta.getSkill(), "0",oferta.getFinFecha().toString()));
            else if(oferta.getFinFecha()==null)
                model.addAttribute("ofertas", ofertaDao.getOfertasAsociadasASkill(oferta.getSkill(), oferta.getIniFecha().toString(),"999999999"));
            else
                model.addAttribute("ofertas", ofertaDao.getOfertasAsociadasASkill(oferta.getSkill(), oferta.getIniFecha().toString(), oferta.getFinFecha().toString()));
            model.addAttribute("list", "inicioOfertas");
            return "oferta/list";
        }
        else if (idListado == 1){
            if (oferta.getIniFecha() == null && oferta.getFinFecha()==null)
                model.addAttribute("ofertas", ofertaDao.getOfertasAsociadasASkillMenosMias(oferta.getSkill(),user.getNif(), "0","999999999"));
            else if(oferta.getIniFecha() == null)
                model.addAttribute("ofertas", ofertaDao.getOfertasAsociadasASkillMenosMias(oferta.getSkill(),user.getNif(), "0",oferta.getFinFecha().toString()));
            else if(oferta.getFinFecha()==null)
                model.addAttribute("ofertas", ofertaDao.getOfertasAsociadasASkillMenosMias(oferta.getSkill(),user.getNif(), oferta.getIniFecha().toString(),"999999999"));
            else
                model.addAttribute("ofertas", ofertaDao.getOfertasAsociadasASkillMenosMias(oferta.getSkill(),user.getNif(), oferta.getIniFecha().toString(), oferta.getFinFecha().toString()));

            model.addAttribute("list", "ofertasUser");
            return "oferta/listOfertasUser";
        }
        else if (idListado == 2){
            if (oferta.getIniFecha() == null && oferta.getFinFecha()==null)
                model.addAttribute("ofertas", ofertaDao.getMisOfertasAsociadasASkill(oferta.getSkill(),user.getNif(), "0","999999999"));
            else if(oferta.getIniFecha() == null)
                model.addAttribute("ofertas", ofertaDao.getMisOfertasAsociadasASkill(oferta.getSkill(),user.getNif(), "0",oferta.getFinFecha().toString()));
            else if(oferta.getFinFecha()==null)
                model.addAttribute("ofertas", ofertaDao.getMisOfertasAsociadasASkill(oferta.getSkill(),user.getNif(), oferta.getIniFecha().toString(),"999999999"));
            else
                model.addAttribute("ofertas", ofertaDao.getMisOfertasAsociadasASkill(oferta.getSkill(),user.getNif(), oferta.getIniFecha().toString(), oferta.getFinFecha().toString()));
            model.addAttribute("list", "misOfertas");
            return "oferta/listMisOfertas";
        }
        else {
            if (oferta.getIniFecha() == null && oferta.getFinFecha()==null)
                model.addAttribute("ofertas", ofertaDao.getOfertasAsociadasASkill(oferta.getSkill(), "0","999999999"));
            else if(oferta.getIniFecha() == null)
                model.addAttribute("ofertas", ofertaDao.getOfertasAsociadasASkill(oferta.getSkill(), "0",oferta.getFinFecha().toString()));
            else if(oferta.getFinFecha()==null)
                model.addAttribute("ofertas", ofertaDao.getOfertasAsociadasASkill(oferta.getSkill(), oferta.getIniFecha().toString(),"999999999"));
            else
                model.addAttribute("ofertas", ofertaDao.getOfertasAsociadasASkill(oferta.getSkill(), oferta.getIniFecha().toString(), oferta.getFinFecha().toString()));
            model.addAttribute("list", "skpOfertas");
            return "oferta/listSKP";
        }
    }




}

