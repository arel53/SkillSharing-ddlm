package es.uji.ei1027.skillSharing.dao;


import es.uji.ei1027.skillSharing.modelo.Niveles;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class NivelesRowMapper implements RowMapper<Niveles> {
    @Override
    public Niveles mapRow(ResultSet rs, int rowNum) throws SQLException {
        Niveles nivel = new Niveles();
        nivel.setNombre(rs.getString("nombre"));
        nivel.setNivel(rs.getString("nivel"));
        return nivel;
    }
}
