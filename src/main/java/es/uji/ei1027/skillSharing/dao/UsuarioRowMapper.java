package es.uji.ei1027.skillSharing.dao;

import es.uji.ei1027.skillSharing.modelo.Usuario;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;


public final class UsuarioRowMapper implements RowMapper<Usuario> {
    @Override
    public Usuario mapRow(ResultSet rs, int rowNum) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setUsername(rs.getString("username"));
        usuario.setPassword(rs.getString("password"));
        usuario.setSkp(rs.getBoolean("skp"));
        usuario.setActive(rs.getBoolean("active"));
        usuario.setNif(rs.getString("nif"));
        usuario.setDescripcion(rs.getString("descripcion"));
        return usuario;
    }
}
