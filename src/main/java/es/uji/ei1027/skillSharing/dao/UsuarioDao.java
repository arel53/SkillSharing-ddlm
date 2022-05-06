package es.uji.ei1027.skillSharing.dao;

import es.uji.ei1027.skillSharing.modelo.Usuario;
import org.jasypt.util.password.BasicPasswordEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UsuarioDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) { jdbcTemplate = new JdbcTemplate(dataSource); }

    public void addUsuario(Usuario user){
        jdbcTemplate.update("INSERT INTO usuario VALUES(?, ?, ?, ?, ?, ?)", user.getUsername(),
                user.getPassword(),user.isSkp(), user.isActive(), user.getNif(),user.getDescripcion());

    }
    public void deleteUsuario(String username) {
        jdbcTemplate.update("DELETE FROM usuario WHERE username = ?", username);
    }

    public void updateUsuario(Usuario user) {
        jdbcTemplate.update("UPDATE usuario SET password = ?, skp = ?, " +
                        "active = ?, nif = ?, descripcion = ? WHERE username = ?", user.getPassword(),user.isSkp(),
                user.isActive(), user.getNif(),user.getDescripcion(),user.getUsername());
    }
    public Usuario getUsuario(String username) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM usuario WHERE username = ?", new UsuarioRowMapper(), username);
        }
        catch (EmptyResultDataAccessException e){
            return null;
        }
    }
    public List<Usuario> getUsuarios() {
        try {
            return jdbcTemplate.query("SELECT * FROM usuario ORDER BY username", new UsuarioRowMapper());
        }
        catch (EmptyResultDataAccessException e) {
            return new ArrayList<Usuario>();
        }
    }
    public Usuario loadUser(Usuario usuario){
        Usuario user;
        user = getUsuario(usuario.getUsername());
        if (user == null)
            return null;
        BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();
        if (passwordEncryptor.checkPassword(usuario.getPassword(), user.getPassword())) {
            user.setPassword("-");
            return user;
        }
        else {
            return null; // bad login!
        }
    }

    public List<Usuario> getUsuariosInactivos() {
        try {
            return jdbcTemplate.query("SELECT * FROM usuario WHERE active = FALSE", new UsuarioRowMapper());
        }
        catch (EmptyResultDataAccessException e){
            return new ArrayList<>();
        }
    }

 }
