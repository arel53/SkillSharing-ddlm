package es.uji.ei1027.skillSharing.dao;

import es.uji.ei1027.skillSharing.modelo.Estudiante;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EstudianteRowMapper implements RowMapper<Estudiante> {

    public Estudiante mapRow(ResultSet rs, int rowNum) throws SQLException{
        Estudiante estudiante = new Estudiante();
        estudiante.setNif(rs.getString("nif"));
        estudiante.setNombre(rs.getString("nombre"));
        estudiante.setApellido(rs.getString("apellido"));
        estudiante.setEmail(rs.getString("email"));
        estudiante.setGrado(rs.getString("grado"));
        estudiante.setEdad(rs.getInt("edad"));
        estudiante.setSexo(rs.getString("sexo"));
        estudiante.setDireccion(rs.getString("direccion"));
        estudiante.setHoras(rs.getInt("horas"));
        estudiante.setRutaimg(rs.getString("rutaimg"));
        return estudiante;
    }

    //nombres de la base en "" setE
}
