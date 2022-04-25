package es.uji.ei1027.skillSharing.controller;

import es.uji.ei1027.skillSharing.dao.SkillDao;
import es.uji.ei1027.skillSharing.modelo.Skill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.LinkedList;
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
    public String processAddSubmit(@ModelAttribute("skill") Skill skill, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return "skill/add";
        }
        skillDao.addSkill(skill);
        return "redirect:list";
    }

    @RequestMapping( value = "/update/{idSkill}", method = RequestMethod.GET)
    public String editOferta(Model model, @PathVariable String idSkill){
        model.addAttribute("skill", skillDao.getSkill(idSkill));
        return "skill/update";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String processUpdateSubmit( @ModelAttribute("skill") Skill skill, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            return "skill/update";
        }
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
    public String listSkills(Model model){
        List<Skill> skills =skillDao.getSkillsActivas();
        for (Skill s : skills){
            s.setNumeroOfertas(skillDao.getOfertasSkill(s.getIdSkill()+"").size());
            s.setNumeroDemandas(skillDao.getDemandasSkill(s.getIdSkill()+"").size());
        }
        model.addAttribute("skills", skills);
        return "skill/list";
    }

}
