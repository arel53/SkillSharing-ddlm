package es.uji.ei1027.skillSharing.controller;

import es.uji.ei1027.skillSharing.dao.OfertaDao;
import es.uji.ei1027.skillSharing.dao.SkillDao;
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
@RequestMapping("/oferta")
public class OfertaController {

    private OfertaDao ofertaDao;
    private SkillDao skillDao;

    @Autowired
    public void setOfertaDao(OfertaDao ofertaDao){this.ofertaDao=ofertaDao;}
    @Autowired
    public void setSkillDao(SkillDao skillDao){this.skillDao=skillDao;}

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

    @RequestMapping(value="/add", method= RequestMethod.POST)
    public String processAddSubmit(HttpSession session, @ModelAttribute("oferta") Oferta oferta,
                                  BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            System.out.println(oferta);
            return "oferta/add";
        }
        Usuario user = (Usuario)session.getAttribute("user");
        oferta.setEstudiante(user.getNif());
        ofertaDao.addOferta(oferta);
        return "redirect:listMisOfertas";
    }

    @RequestMapping(value = "/update/{idOferta}", method = RequestMethod.GET)
    public String editOferta(Model model, @PathVariable String idOferta){
        Oferta o = ofertaDao.getOferta(idOferta);
        model.addAttribute("oferta", o);
        model.addAttribute("skills", skillDao.getSkillsActivas());
        model.addAttribute("skill", o.getSkill());
        return "oferta/update";
    }

    @RequestMapping(value="/update", method = RequestMethod.POST)
    public String processUpdateSubmit(
            @ModelAttribute("oferta") Oferta oferta,
            BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return "oferta/update";
        }
        ofertaDao.updateOferta(oferta);
        return "redirect:list";
    }

    @RequestMapping(value = "/delete/{idOferta}")
    public String  processDeleteOferta(HttpSession session,@PathVariable String idOferta){
        if (session.getAttribute("user") == null){
            session.setAttribute("nextUrl","/usuario/list");
            return "login";
        }
        Usuario user = (Usuario)session.getAttribute("user");
        if (!user.isSkp()){
            Oferta of = (Oferta)ofertaDao.getOferta(idOferta);
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
    public String listOfertas(Model model){
        model.addAttribute("ofertas",ofertaDao.getOfertas());
        return "oferta/list";
    }

    @RequestMapping("/listSKP")
    public String listOfertasSKP(Model model){
        model.addAttribute("ofertas",ofertaDao.getOfertas());
        return "oferta/listSKP";
    }


    @RequestMapping("/listOfertasUser")
    public String listOfertasUser(HttpSession session, Model model){

        if (session.getAttribute("user") == null){
            session.setAttribute("nextUrl","/usuario/list");
            return "login";
        }
        Usuario user = (Usuario)session.getAttribute("user");

        model.addAttribute("ofertas",ofertaDao.getTodasOfertasMenosMias(user.getNif()));
        return "oferta/listOfertasUser";
    }
    @RequestMapping("/listMisOfertas")
    public String listMisOfertas(HttpSession session,Model model){
        if (session.getAttribute("user") == null){
            session.setAttribute("nextUrl","/usuario/list");
            return "login";
        }
        Usuario user = (Usuario)session.getAttribute("user");

        model.addAttribute("misOfertas",ofertaDao.getOfertasEstudiante(user.getNif()));
        return "oferta/listMisOfertas";
    }


    // TODO Falta listOfertas de SKP, hay que pensar entre todos que listar y como
}
