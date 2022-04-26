package es.uji.ei1027.skillSharing.controller;

import es.uji.ei1027.skillSharing.dao.EstudianteDao;
import es.uji.ei1027.skillSharing.dao.UsuarioDao;
import es.uji.ei1027.skillSharing.modelo.Estudiante;
import es.uji.ei1027.skillSharing.modelo.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;

public class UsuarioValidator implements Validator {
    @Override
    public boolean supports(Class<?> cls) {
        return Usuario.class.equals(cls);
    }
    /*private UsuarioDao usuarioDao;
    @Autowired
    public void setUsuarioDao(UsuarioDao usuarioDao) {
        this.usuarioDao=usuarioDao;
    }*/

    @Override
    public void validate(Object obj, Errors errors) {
        Usuario user = (Usuario)obj;

        if (user.getUsername().trim().equals("")){
            errors.rejectValue("username", "obligatori",
                    "Cal introduir un nom d' usuari");
        }
        if (user.getPassword().trim().equals("")){
            errors.rejectValue("password", "obligatori",
                    "Cal introduir una contrasenya");}

        if (user.getNif().trim().equals("")){
            errors.rejectValue("nif", "obligatori",
                    "Cal introduir un nif");
        }

        //List<Usuario> listaU = usuarioDao.getUsuarios();
        /*
        for (Usuario u: listaU){
            if (u.getUsername().equals(user.getUsername())){
                errors.rejectValue("username", "repetido",
                        "El nombre de usuario ya esta en uso");

            }
        }
        List<Estudiante> listaE = estudianteDao.getEstudiantes();
        Boolean encontrado=false;
        for (Estudiante e: listaE){
            if (e.getNif().equals(user.getNif())){
                encontrado = true;
                break;
            }
        }
        if (!encontrado){
            errors.rejectValue("nif", "not_found",
                    "El nif de la persona no se ha encontrado");
        }

         */
    }
}
