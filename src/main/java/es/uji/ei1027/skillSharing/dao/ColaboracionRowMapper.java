package es.uji.ei1027.skillSharing.dao;

import es.uji.ei1027.skillSharing.modelo.Colaboracion;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ColaboracionRowMapper implements RowMapper<Colaboracion> {
    @Override
    public Colaboracion mapRow(ResultSet rs, int rowNum) throws SQLException {
        Colaboracion colaboracion = new Colaboracion();
        colaboracion.setIdOferta(rs.getString("id_oferta"));
        colaboracion.setIdDemanda(rs.getString("id_demanda"));
        colaboracion.setIniFecha(rs.getDate("ini_fecha"));
        colaboracion.setFinFecha(rs.getDate("fin_fecha"));
        colaboracion.setActiva(rs.getBoolean("activa"));
        colaboracion.setRate(rs.getInt("rate"));
        colaboracion.setComentario(rs.getString("comentario"));
        colaboracion.setHoras(rs.getInt("horas"));
        return colaboracion;
    }
}
