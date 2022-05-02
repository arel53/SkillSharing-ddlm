package es.uji.ei1027.skillSharing.controller;

import es.uji.ei1027.skillSharing.dao.ColaboracionDao;
import es.uji.ei1027.skillSharing.modelo.Colaboracion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/colaboracion")
public class ColaboracionController {

    private ColaboracionDao colaboracionDao;

    @Autowired
    public void setColaboracionDao(ColaboracionDao colaboracionDao){this.colaboracionDao=colaboracionDao;}

    @RequestMapping(value = "/add")
    public String addColaboracion(Model model){
        model.addAttribute("colaboracion", new Colaboracion());
        return "colaboracion/add";
    }

    @RequestMapping(value="/add", method = RequestMethod.POST)
    public String processAddSubmit(@ModelAttribute("colaboracion") Colaboracion colaboracion,
                                   BindingResult bindingResult){

        if (bindingResult.hasErrors())
            return "colaboracion/add";
        colaboracionDao.addColaboracion(colaboracion);
        return "redirect:list";
    }

    @RequestMapping(value = "/update/{idOferta}/{idDemanda}", method = RequestMethod.GET)
    public String editColaboracion(Model model, @PathVariable String idOferta,
                                                @PathVariable String idDemanda){
        model.addAttribute("colaboracion",colaboracionDao.getColaboracion(idOferta,idDemanda));
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

    @RequestMapping(value = "/delete/{idOferta}/{idDemanda}")
    public String  processDeleteDemanda(@PathVariable String idOferta, @PathVariable String idDemanda){
        colaboracionDao.endColaboracion(idOferta,idDemanda);
        return "redirect:../../list";
    }

    @RequestMapping("/list")
    public String listDemandas(Model model){
        model.addAttribute("colaboraciones",colaboracionDao.getColaboraciones());
        return "demanda/list";
    }

}
