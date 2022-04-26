package es.uji.ei1027.skillSharing.dao;

import es.uji.ei1027.skillSharing.modelo.Usuario;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;


public class UsuarioRowMapper implements RowMapper<Usuario> {
    @Override
    public Usuario mapRow(ResultSet rs, int rowNum) throws SQLException {
        Usuario user = new Usuario();
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setSkp(rs.getBoolean("skp"));
        user.setActive(rs.getBoolean("active"));
        user.setNIF(rs.getString("nif"));
        user.setDescripcion("descripcion");
        return user;
    }
}
