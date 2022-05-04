package es.uji.ei1027.skillSharing.dao;

import es.uji.ei1027.skillSharing.modelo.Demanda;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DemandaRowMapper implements RowMapper<Demanda> {
    public Demanda mapRow(ResultSet rs, int rowNum)throws SQLException {
        Demanda demanda=new Demanda();
        demanda.setIdDemanda(rs.getInt("id_demanda"));
        demanda.setEstudiante(rs.getString("estudiante"));
        demanda.setHoras(rs.getInt("horas"));
        demanda.setIniFecha(rs.getDate("ini_fecha").toLocalDate());
        demanda.setFinFecha(rs.getDate("fin_fecha").toLocalDate());
        demanda.setActiva(rs.getBoolean("activa"));
        demanda.setSkill(rs.getInt("id_skill"));
        demanda.setDescripcion(rs.getString("descripcion"));
        demanda.setNombreSkill(rs.getString("nombre_skill"));
        demanda.setNivelSkill(rs.getString("nivel_skill"));
        return demanda;
    }
}
