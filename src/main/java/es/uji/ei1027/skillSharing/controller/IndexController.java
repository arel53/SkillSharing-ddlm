package es.uji.ei1027.skillSharing.controller;

import es.uji.ei1027.skillSharing.dao.DemandaDao;
import es.uji.ei1027.skillSharing.dao.OfertaDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller

@RequestMapping("/inicio")
public class IndexController {

    private OfertaDao ofertaDao;
    private DemandaDao demandaDao;

    @Autowired
    public void setOfertaDao(OfertaDao ofertaDao) {this.ofertaDao = ofertaDao;}
    @Autowired
    public void setDemandaDao(DemandaDao demandaDao) {this.demandaDao = demandaDao;}


    @RequestMapping(value = "/principal")
    public String principal(Model model){
        model.addAttribute("ofertas", ofertaDao.getOfertas());
        model.addAttribute("demandas", demandaDao.getDemandas());
        return "inicio/pp";
    }



}
