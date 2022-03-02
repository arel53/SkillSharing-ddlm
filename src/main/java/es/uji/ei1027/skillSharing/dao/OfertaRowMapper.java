package es.uji.ei1027.skillSharing.dao;

import es.uji.ei1027.skillSharing.modelo.Oferta;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OfertaRowMapper implements RowMapper<Oferta> {
    public Oferta mapRow(ResultSet rs, int rowNum)throws SQLException{
        Oferta oferta=new Oferta();
        oferta.setIdOferta(rs.getInt("id_oferta"));
        oferta.setEstudiante(rs.getString("estudiante"));
        oferta.setHoras(rs.getInt("horas"));
        oferta.setIniFecha(rs.getDate("ini_fecha"));
        oferta.setFinFecha(rs.getDate("fin_fecha"));
        oferta.setActiva(rs.getBoolean("activa"));
        oferta.setSkill(rs.getString("skill"));
        oferta.setNivel(rs.getString("nivel"));
        oferta.setDescripcion(rs.getString("descripcion"));
        return oferta;
    }
}
