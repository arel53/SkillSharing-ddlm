package es.uji.ei1027.skillSharing.controller;

import es.uji.ei1027.skillSharing.dao.EstudianteDao;
import es.uji.ei1027.skillSharing.modelo.Estudiante;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/estudiante")
public class EstudianteController {

    private EstudianteDao estudianteDao;

    @Autowired
    public void setEstudianteDao(EstudianteDao estudianteDao){this.estudianteDao=estudianteDao;}

    @RequestMapping(value = "/get/{nif}", method = RequestMethod.GET)
    public String getEstudiante(Model model, @PathVariable String nif){
        Estudiante estudiante = estudianteDao.getEstudiante(nif);
        model.addAttribute("estudiante", estudiante);
        return "estudiante/get";
    }


    @RequestMapping("/list")
    public String listDemandas(Model model){
        model.addAttribute("estudiantes",estudianteDao.getEstudiantes());
        return "estudiante/list";
    }
}
