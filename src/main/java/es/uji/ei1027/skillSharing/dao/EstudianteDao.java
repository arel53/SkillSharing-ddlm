package es.uji.ei1027.skillSharing.dao;

import es.uji.ei1027.skillSharing.modelo.Estudiante;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class EstudianteDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) { jdbcTemplate = new JdbcTemplate(dataSource); }

    // Añade estudiante a la base de datos

    public void addEstudiante(Estudiante estudiante){
        jdbcTemplate.update("INSERT INTO estudiante VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                estudiante.getNif(), estudiante.getNombre(), estudiante.getApellido(), estudiante.getEmail(),
                estudiante.getGrado(), estudiante.getEdad(), estudiante.getSexo(), estudiante.getDireccion(),
                estudiante.getHoras(), estudiante.getRutaimg());
    }

    public void deleteEstudiante(String nif) {
        jdbcTemplate.update("DELETE FROM estudiante WHERE nif = ?", nif);
    }

    public void updateEstudiante(Estudiante estudiante) {
        jdbcTemplate.update("UPDATE estudiante SET nombre = ?, apellido = ?, email = ?, " +
                "grado = ?, edad = ?, sexo = ?, direccion = ?, horas = ?, rutaimg= ? WHERE nif = ?",estudiante.getNombre(), estudiante.getApellido(), estudiante.getEmail(),
                estudiante.getGrado(), estudiante.getEdad(), estudiante.getSexo(), estudiante.getDireccion(),
                estudiante.getHoras(), estudiante.getRutaimg(), estudiante.getNif());
    }

    public Estudiante getEstudiante(String nif) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM estudiante WHERE nif = ?", new EstudianteRowMapper(), nif);
        }
        catch (EmptyResultDataAccessException e){
            return null;
        }
    }

    public List<Estudiante> getEstudiantes() {
        try {
            return jdbcTemplate.query("SELECT * FROM estudiante", new EstudianteRowMapper());
        }
        catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<Estudiante> getEstudiantesSinCuentas() {
        try {
            return jdbcTemplate.query("SELECT e.* FROM usuario AS u RIGHT JOIN estudiante as e USING(nif) WHERE e.nif NOT IN (SELECT nif from usuario);", new EstudianteRowMapper());
        }
        catch (EmptyResultDataAccessException e) {
            return null;
        }
    }


}
