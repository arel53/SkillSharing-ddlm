package es.uji.ei1027.skillSharing.controller;

import es.uji.ei1027.skillSharing.dao.UsuarioDao;
import es.uji.ei1027.skillSharing.modelo.Usuario;
import org.jasypt.util.password.BasicPasswordEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/usuario")
public class UserController {
    private UsuarioDao usuarioDao;
    @Autowired
    public void setUsuarioDao(UsuarioDao usuarioDao) {
        this.usuarioDao=usuarioDao;
    }
    @RequestMapping("/list")
    public String listUsuarios(Model model) {
        model.addAttribute("usuarios", usuarioDao.getUsuarios());
        return "usuario/list";
    }
    @RequestMapping(value="/add")
    public String addUsuario(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "usuario/add";
    }
    @RequestMapping(value="/add", method= RequestMethod.POST)
    public String processAddSubmit(@ModelAttribute("usuario") Usuario usuario,
                                   BindingResult bindingResult) {
        UsuarioValidator usuarioValidator = new UsuarioValidator();
        usuarioValidator.validate(usuario, bindingResult);
        if (bindingResult.hasErrors())
            return "usuario/add";
        //System.out.println(usuario.toString());
        BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();
        usuario.setPassword(passwordEncryptor.encryptPassword(usuario.getPassword()));
        usuarioDao.addUsuario(usuario);
        return "redirect:list";
    }
    @RequestMapping(value="/update/{username}", method = RequestMethod.GET)
    public String editUsuario(Model model, @PathVariable String username) {
        model.addAttribute("usuario", usuarioDao.getUsuario(username));
        return "usuario/update";
    }
    @RequestMapping(value="/update", method = RequestMethod.POST)
    public String processUpdateSubmit(@ModelAttribute("usuario") Usuario usuario,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "usuario/update";
        usuarioDao.updateUsuario(usuario);
        return "redirect:list";
    }
    @RequestMapping(value="/delete/{username}")
    public String processDelete(@PathVariable String username) {
        usuarioDao.deleteUsuario(username);
        return "redirect:../list";
    }
    @RequestMapping(value="/passwd/{username}", method = RequestMethod.GET)
    public String editPassword(Model model, @PathVariable String username) {
        model.addAttribute("usuario", usuarioDao.getUsuario(username));
        return "usuario/passwd";
    }
    @RequestMapping(value="/passwd", method = RequestMethod.POST)
    public String processPasswdSubmit(@ModelAttribute("usuario") Usuario usuario,
                                      BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "usuario/passwd";
        BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();
        usuario.setPassword(passwordEncryptor.encryptPassword(usuario.getPassword()));
        usuarioDao.updateUsuario(usuario);
        return "redirect:update/"+usuario.getUsername();
    }
}
