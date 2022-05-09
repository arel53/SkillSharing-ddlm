package es.uji.ei1027.skillSharing.dao;

import es.uji.ei1027.skillSharing.modelo.Colaboracion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class ColaboracionDao {


    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) { jdbcTemplate = new JdbcTemplate(dataSource); }


    public void addColaboracion(Colaboracion colaboracion) {
        jdbcTemplate.update("INSERT INTO colaboracion(id_oferta, id_demanda, ini_fecha, activa, rate, comentario, horas) VALUES(?, ?, ?, ?, ?, ?, ?)", colaboracion.getIdOferta(),
                colaboracion.getIdDemanda(), colaboracion.getIniFecha(), true,
                null, colaboracion.getComentario(),colaboracion.getHoras());
    }

    public void endColaboracion(String idColaboracion) {
        jdbcTemplate.update("UPDATE colaboracion SET activa = FALSE WHERE id_colaboracion = ?", idColaboracion);
    }

    public void updateColaboracion(Colaboracion colaboracion) {
        jdbcTemplate.update("UPDATE colaboracion SET  id_colaboracion = ?, id_demanda = ?, ini_fecha = ?, fin_fecha = ?, " +
                "activa = ?, rate = ?, comentario = ?,horas = ? WHERE id_colaboracion = ?",
                colaboracion.getIdColaboracion(), colaboracion.getIdDemanda(), colaboracion.getIniFecha(), colaboracion.getFinFecha(), colaboracion.isActiva(),
                colaboracion.getRate(), colaboracion.getComentario(),colaboracion.getHoras(), colaboracion.getIdColaboracion());
    }

    public Colaboracion getColaboracion(String idColaboracion) {
        try {
            return jdbcTemplate.queryForObject("SELECT t.*, es.nombre || ' ' || es.apellido AS nombre_apellido_demandante, es.nif AS nif_demandante, s.nombre || ' ' || s.nivel AS skill  " +
                    "                      FROM ( SELECT c.* AS cs, e.nombre || ' ' || e.apellido AS nombre_apellido_ofertante, e.nif AS nif_ofertante " +
                    "                             FROM colaboracion AS c JOIN oferta AS o USING(id_oferta) " +
                    "                             JOIN estudiante as e ON(e.nif = o.estudiante) " +
                    "                           ) AS t " +
                    "                      JOIN demanda AS d USING(id_demanda) " +
                    "                      JOIN skill as S USING(id_skill) " +
                    "                      JOIN estudiante AS es ON(es.nif = d.estudiante) " +
                    "                      WHERE t.id_colaboracion = ?", new ColaboracionRowMapper(), idColaboracion);
        }
        catch (EmptyResultDataAccessException e){
            return null;
        }

    }

    public List<Colaboracion> getColaboraciones() {
        try {
            return jdbcTemplate.query("SELECT t.*, es.nombre || ' ' || es.apellido AS nombre_apellido_demandante, s.nombre || ' ' || s.nivel AS skill " +
                    "FROM ( SELECT c.* AS cs, e.nombre || ' ' || e.apellido AS nombre_apellido_ofertante " +
                    "       FROM colaboracion AS c " +
                    "       JOIN oferta AS o USING(id_oferta) " +
                    "       JOIN estudiante as e ON(e.nif = o.estudiante)" +
                    "     ) AS t " +
                    "JOIN demanda AS d USING(id_demanda) " +
                    "JOIN skill as S USING(id_skill) " +
                    "JOIN estudiante AS es ON(es.nif = d.estudiante)", new ColaboracionRowMapper());
        }catch (EmptyResultDataAccessException e){
            return null;
        }
    }

    public List<Colaboracion> getColaboracionesEstudiante(String nif) {
        try {
            return jdbcTemplate.query("SELECT t.*, es.nombre || ' ' || es.apellido AS nombre_apellido_demandante, es.nif AS nif_demandante, s.nombre || ' ' || s.nivel AS skill " +
                    "                      FROM ( SELECT c.* AS cs, e.nombre || ' ' || e.apellido AS nombre_apellido_ofertante, e.nif AS nif_ofertante " +
                    "                             FROM colaboracion AS c JOIN oferta AS o USING(id_oferta) " +
                    "                             JOIN estudiante as e ON(e.nif = o.estudiante) " +
                    "                           ) AS t " +
                    "                      JOIN demanda AS d USING(id_demanda) " +
                    "                      JOIN skill as S USING(id_skill) " +
                    "                      JOIN estudiante AS es ON(es.nif = d.estudiante) " +
                    "                      WHERE es.nif = ? or t.nif_ofertante = ?", new ColaboracionRowMapper(), nif, nif);
        }
        catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    
}
