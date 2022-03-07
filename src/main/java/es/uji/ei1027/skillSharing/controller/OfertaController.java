package es.uji.ei1027.skillSharing.controller;

import es.uji.ei1027.skillSharing.dao.OfertaDao;
import es.uji.ei1027.skillSharing.modelo.Oferta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/oferta")
public class OfertaController {

    private OfertaDao ofertaDao;

    @Autowired
    public void setOfertaDao(OfertaDao ofertaDao){this.ofertaDao=ofertaDao;}

    @RequestMapping(value = "/add")
    public String addOferta(Model model){
        model.addAttribute("oferta", new Oferta());
        return "oferta/add";
    }

    @RequestMapping(value="/add", method= RequestMethod.POST)
    public String processAddSubmit(@ModelAttribute("oferta") Oferta oferta,
                                  BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "oferta/add";
        ofertaDao.addOferta(oferta);
        return "redirect:list";
    }

    @RequestMapping(value = "/update/{idOferta}", method = RequestMethod.GET)
    public String editOferta(Model model, @PathVariable String idOferta){
        model.addAttribute("oferta",ofertaDao.getOferta(Integer.parseInt(idOferta)));
        return "oferta/update";
    }

    @RequestMapping(value="/update", method = RequestMethod.POST)
    public String processUpdateSubmit(
            @ModelAttribute("oferta") Oferta oferta,
            BindingResult bindingResult){
        System.out.println(oferta);
        if (bindingResult.hasErrors()) {
            System.out.println(oferta);
            return "oferta/update";
        }
        ofertaDao.updateOferta(oferta);
        return "redirect:list";
    }

    @RequestMapping(value = "/delete/{idOferta}")
    public String  processDeleteOferta(@PathVariable String idOferta){
        ofertaDao.endOferta(idOferta);
        return "redirected:../list";
    }

    @RequestMapping("/list")
    public String listOfertas(Model model){
        model.addAttribute("ofertas",ofertaDao.getOfertas());
        return "oferta/list";
    }

}
