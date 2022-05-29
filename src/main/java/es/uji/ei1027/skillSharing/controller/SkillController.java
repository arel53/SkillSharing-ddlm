package es.uji.ei1027.skillSharing.controller;

import es.uji.ei1027.skillSharing.dao.SkillDao;
import es.uji.ei1027.skillSharing.modelo.Skill;
import es.uji.ei1027.skillSharing.modelo.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
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
    public String addSkill(Model model, HttpSession session){
        if (session.getAttribute("user") == null){
            session.setAttribute("nextUrl","/skill/add");
            return "redirect:../login";
        }
        Usuario user = (Usuario) session.getAttribute("user");

        if (!user.isSkp())
            return "redirect:/forbiden";
        model.addAttribute("skill", new Skill());
        return "skill/add";
    }


    @RequestMapping(value="/add", method= RequestMethod.POST)
    public String processAddSubmit(@ModelAttribute("skill") Skill skill, BindingResult bindingResult, @RequestParam("foto") MultipartFile foto, HttpSession session) throws IOException {
        if (session.getAttribute("user") == null){
            session.setAttribute("nextUrl","/skill/add");
            return "redirect:../login";
        }
        Usuario user = (Usuario) session.getAttribute("user");
        session.setAttribute("anadido", skill.getNombre() + " "+ skill.getNivel());

        if (!user.isSkp())
            return "redirect:/forbiden";
        if (bindingResult.hasErrors()) {
            return "skill/add";
        }
        if (foto.isEmpty()){
            //bindingResult.rejectValue();
            //System.out.println("empotyfoto");
            return "redirect:/skill/add ";
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
    public String editSkill(Model model, @PathVariable String idSkill, HttpSession session){
        if (session.getAttribute("user") == null){
            session.setAttribute("nextUrl","/skill/update/" + idSkill);
            return "redirect:../../login";
        }
        Usuario user = (Usuario) session.getAttribute("user");

        if (!user.isSkp())
            return "redirect:/forbiden";

        model.addAttribute("skill", skillDao.getSkill(idSkill));
        return "skill/update";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String processUpdateSubmit( @ModelAttribute("skill") Skill skill, BindingResult bindingResult, HttpSession session){
        if (session.getAttribute("user") == null){
            session.setAttribute("nextUrl","/update");
            return "redirect:../login";
        }
        Usuario user = (Usuario) session.getAttribute("user");

        if (!user.isSkp())
            return "redirect:/forbiden";

        if (bindingResult.hasErrors()){
            return "skill/update";
        }
        skillDao.updateSkill(skill);
        session.setAttribute("editado", skill.getNombre() + " " + skill.getNivel());
        return "redirect:list";
    }

    @RequestMapping(value = "/delete/{idSkill}")
    public String processDeleteSkill(@PathVariable String idSkill, HttpSession session){
        if (session.getAttribute("user") == null){
            session.setAttribute("nextUrl","/skill/delete/" + idSkill);
            return "redirect:../../login";
        }
        Usuario user = (Usuario) session.getAttribute("user");

        if (!user.isSkp())
            return "redirect:/forbiden";

        Skill skill = skillDao.getSkill(idSkill);
        skillDao.endSkill(idSkill);
        skillDao.endOfertasSkill(idSkill);
        skillDao.endDemandasSkill(idSkill);
        session.setAttribute("eliminado", skill.getNombre() + " " + skill.getNivel());
        return "redirect:../list";
    }

    @RequestMapping("/list")
    public String listSkills(Model model, @SessionAttribute(name = "anadido", required = false) String anadido, @SessionAttribute(name = "eliminado", required = false) String eliminado,
                             @SessionAttribute(name = "editado", required = false) String editado, HttpSession session){
        if (session.getAttribute("user") == null){
            session.setAttribute("nextUrl","/skill/list");
            return "redirect:../login";
        }
        Usuario user = (Usuario) session.getAttribute("user");

        if (!user.isSkp())
            return "redirect:/forbiden";

        List<Skill> skills =skillDao.getSkillsActivas();
        for (Skill s : skills){
            s.setNumeroOfertas(skillDao.getNumOfertasSkill(s.getIdSkill()));
            s.setNumeroDemandas(skillDao.getNumDemandasSkill(s.getIdSkill()));
        }

        model.addAttribute("skills", skills);
        model.addAttribute("eliminado", eliminado);
        session.removeAttribute("eliminado");
        model.addAttribute("editado", editado);
        session.removeAttribute("editado");
        model.addAttribute("anadido", anadido);
        session.removeAttribute("anadido");
        return "skill/list";
    }

}
