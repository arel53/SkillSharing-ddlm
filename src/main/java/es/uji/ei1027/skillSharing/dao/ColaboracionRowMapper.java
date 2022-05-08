package es.uji.ei1027.skillSharing.dao;

import es.uji.ei1027.skillSharing.modelo.Colaboracion;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ColaboracionRowMapper implements RowMapper<Colaboracion> {
    @Override
    public Colaboracion mapRow(ResultSet rs, int rowNum) throws SQLException {
        Colaboracion colaboracion = new Colaboracion();
        colaboracion.setIdColaboracion(rs.getInt("id_colaboracion"));
        colaboracion.setIdOferta(rs.getInt("id_oferta"));
        colaboracion.setIdDemanda(rs.getInt("id_demanda"));
        colaboracion.setIniFecha(rs.getDate("ini_fecha").toLocalDate());
        if (rs.getDate("fin_fecha") != null)
            colaboracion.setFinFecha(rs.getDate("fin_fecha").toLocalDate());
        colaboracion.setActiva(rs.getBoolean("activa"));
        colaboracion.setRate(rs.getInt("rate"));
        colaboracion.setComentario(rs.getString("comentario"));
        colaboracion.setHoras(rs.getInt("horas"));
        colaboracion.setNombreApellidoOfertante(rs.getString("nombre_apellido_ofertante"));
        colaboracion.setNombreApellidoDemandante(rs.getString("nombre_apellido_demandante"));
        return colaboracion;
    }
}
