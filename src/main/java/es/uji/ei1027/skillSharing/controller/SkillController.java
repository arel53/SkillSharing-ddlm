package es.uji.ei1027.skillSharing.controller;

import es.uji.ei1027.skillSharing.dao.SkillDao;
import es.uji.ei1027.skillSharing.modelo.Skill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

@Controller
@RequestMapping("/skill")
public class SkillController {

    private SkillDao skillDao;

    @Autowired
    public void setSkillDao(SkillDao skillDao) {this.skillDao = skillDao;}

    @RequestMapping(value = "/add")
    public String addSkill(Model model){
        model.addAttribute("skill", new Skill());
        return "skill/add";
    }


    @RequestMapping(value="/add", method= RequestMethod.POST)
    public String processAddSubmit(@ModelAttribute("skill") Skill skill, BindingResult bindingResult, @RequestParam("foto") MultipartFile foto) throws IOException {
        if (bindingResult.hasErrors()) {
            return "skill/add";
        }
        if (foto.isEmpty()){
            //bindingResult.rejectValue();
            System.out.println("empotyfoto");
            return "skill/add ";
        }
        //System.out.println(foto.getOriginalFilename());

        Path di = Paths.get("src//main//resources//static/imagenes/skill");
        String ra = di.toFile().getAbsolutePath();
        byte[] imgb = foto.getBytes();
        Path ruta = Paths.get(ra+"//"+foto.getOriginalFilename());
        Files.write(ruta,imgb, StandardOpenOption.CREATE);
        String rutabd = "/imagenes/skill/"+foto.getOriginalFilename();
        skill.setRutaim(rutabd);
        skillDao.addSkill(skill);
        return "redirect:list";
    }

    @RequestMapping( value = "/update/{idSkill}", method = RequestMethod.GET)
    public String editSkill(Model model, @PathVariable String idSkill){

        model.addAttribute("skill", skillDao.getSkill(idSkill));
        return "skill/update";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String processUpdateSubmit( @ModelAttribute("skill") Skill skill, @RequestParam("foto") MultipartFile foto, BindingResult bindingResult) throws IOException {
        if (bindingResult.hasErrors()){
            return "skill/update";
        }
        Path di = Paths.get("src//main//resources//static/imagenes/skill");
        String ra = di.toFile().getAbsolutePath();

        byte[] imgb = foto.getBytes();
        Path ruta = Paths.get(ra+"//"+foto.getOriginalFilename());
        Files.write(ruta,imgb, StandardOpenOption.CREATE);
        String rutabd = "/imagenes/skill/"+foto.getOriginalFilename();
        skill.setRutaim(rutabd);
        skillDao.updateSkill(skill);
        return "redirect:list";
    }

    @RequestMapping(value = "/delete/{idSkill}")
    public String processDeleteSkill(@PathVariable String idSkill){
        skillDao.endSkill(idSkill);
        skillDao.endOfertasSkill(idSkill);
        skillDao.endDemandasSkill(idSkill);
        return "redirect:../list";
    }

    @RequestMapping("/list")
    public String listSkills(Model model) throws IOException {
        List<Skill> skills =skillDao.getSkillsActivas();
        for (Skill s : skills){
            s.setNumeroOfertas(skillDao.getNumOfertasSkill(s.getIdSkill()));
            s.setNumeroDemandas(skillDao.getNumDemandasSkill(s.getIdSkill()));
        }

        model.addAttribute("skills", skills);
        return "skill/list";
    }

}
