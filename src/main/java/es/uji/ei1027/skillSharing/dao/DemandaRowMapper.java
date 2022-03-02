package es.uji.ei1027.skillSharing.dao;

import es.uji.ei1027.skillSharing.modelo.Demanda;
import es.uji.ei1027.skillSharing.modelo.Oferta;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DemandaRowMapper implements RowMapper<Demanda> {
    public Demanda mapRow(ResultSet rs, int rowNum)throws SQLException {
        Demanda demanda=new Demanda();
        demanda.setIdDemanda(rs.getInt("id_demanda"));
        demanda.setEstudiante(rs.getString("estudiante"));
        demanda.setHoras(rs.getInt("horas"));
        demanda.setIniFecha(rs.getDate("ini_fecha"));
        demanda.setFinFecha(rs.getDate("fin_fecha"));
        demanda.setActiva(rs.getBoolean("activa"));
        demanda.setSkill(rs.getString("skill"));
        demanda.setNivel(rs.getString("nivel"));
        demanda.setDescripcion(rs.getString("descripcion"));
        return demanda;
    }
}
