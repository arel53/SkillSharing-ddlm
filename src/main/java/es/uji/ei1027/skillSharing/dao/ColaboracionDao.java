package es.uji.ei1027.skillSharing.dao;

import es.uji.ei1027.skillSharing.modelo.Colaboracion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Repository
public class ColaboracionDao {


    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) { jdbcTemplate = new JdbcTemplate(dataSource); }


    public void addColaboracion(Colaboracion colaboracion) {
        jdbcTemplate.update("INSERT INTO colaboracion(id_oferta, id_demanda, ini_fecha, fin_fecha,activa, rate, comentario, horas) VALUES(?, ?, ?, ?, ?, ?, ?, ?)", colaboracion.getIdOferta(),
                colaboracion.getIdDemanda(), colaboracion.getIniFecha(), colaboracion.getFinFecha(), true,
                null, colaboracion.getComentario(),colaboracion.getHoras());
    }

    public void endColaboracion(String idColaboracion) {
        jdbcTemplate.update("UPDATE colaboracion SET activa = FALSE WHERE id_colaboracion = ?", Integer.parseInt(idColaboracion));
    }

    public void updateColaboracion(Colaboracion colaboracion) {
        jdbcTemplate.update("UPDATE colaboracion SET id_oferta = ?, id_demanda = ?, ini_fecha = ?, fin_fecha = ?, " +
                "activa = ?, rate = ?, comentario = ?,horas = ? WHERE id_colaboracion = ?",
                colaboracion.getIdOferta(), colaboracion.getIdDemanda(), colaboracion.getIniFecha(), colaboracion.getFinFecha(), colaboracion.isActiva(),
                colaboracion.getRate(), colaboracion.getComentario(),colaboracion.getHoras(), colaboracion.getIdColaboracion());
    }

    public Colaboracion getColaboracion(String idColaboracion) {
        try {
            return jdbcTemplate.queryForObject("SELECT t.*, es.nombre || ' ' || es.apellido AS nombre_apellido_demandante, es.rutaimg as rutaimgDemandante, s.rutaim, es.nif AS nif_demandante,d.estudiante AS nif_ofertante, s.nombre || ' ' || s.nivel AS skill  " +
                    "                      FROM ( SELECT c.* AS cs, e.nombre || ' ' || e.apellido AS nombre_apellido_ofertante, e.rutaimg as rutaimgOfertante, e.nif AS nif_ofertante " +
                    "                             FROM colaboracion AS c JOIN oferta AS o USING(id_oferta) " +
                    "                             JOIN estudiante as e ON(e.nif = o.estudiante) " +
                    "                           ) AS t " +
                    "                      JOIN demanda AS d USING(id_demanda) " +
                    "                      JOIN skill as S USING(id_skill) " +
                    "                      JOIN estudiante AS es ON(es.nif = d.estudiante) " +
                    "                      WHERE t.id_colaboracion = ?", new ColaboracionRowMapper(), Integer.parseInt(idColaboracion));
        }
        catch (EmptyResultDataAccessException e){
            return null;
        }

    }

    public List<Colaboracion> getColaboraciones() {
        try {
            return jdbcTemplate.query("SELECT t.*, es.nombre || ' ' || es.apellido AS nombre_apellido_demandante, es.rutaimg as rutaimgDemandante, s.rutaim, es.nif AS nif_demandante ,d.estudiante AS nif_ofertante, s.nombre || ' ' || s.nivel AS skill " +
                    "FROM ( SELECT c.* AS cs, e.nombre || ' ' || e.apellido AS nombre_apellido_ofertante, e.rutaimg as rutaimgOfertante " +
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
            return jdbcTemplate.query("SELECT t.*, es.nombre || ' ' || es.apellido AS nombre_apellido_demandante, es.rutaimg as rutaimgDemandante, s.rutaim, es.nif AS nif_demandante ,d.estudiante AS nif_ofertante, s.nombre || ' ' || s.nivel AS skill " +
                    "                      FROM ( SELECT c.* AS cs, e.nombre || ' ' || e.apellido AS nombre_apellido_ofertante, e.rutaimg as rutaimgOfertante, e.nif AS nif_ofertante " +
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

    public List<Colaboracion> getColaboracionesEstudianteActivas(String nif) {
        try {
            return jdbcTemplate.query("SELECT t.*, es.nombre || ' ' || es.apellido AS nombre_apellido_demandante, es.rutaimg as rutaimgDemandante , s.rutaim, es.nif AS nif_demandante ,d.estudiante AS nif_ofertante, s.nombre || ' ' || s.nivel AS skill " +
                    "                      FROM ( SELECT c.* AS cs, e.nombre || ' ' || e.apellido AS nombre_apellido_ofertante, e.rutaimg as rutaimgOfertante, e.nif AS nif_ofertante " +
                    "                             FROM colaboracion AS c JOIN oferta AS o USING(id_oferta) " +
                    "                             JOIN estudiante as e ON(e.nif = o.estudiante) " +
                    "                           ) AS t " +
                    "                      JOIN demanda AS d USING(id_demanda) " +
                    "                      JOIN skill as S USING(id_skill) " +
                    "                      JOIN estudiante AS es ON(es.nif = d.estudiante) " +
                    "                      WHERE (es.nif = ? or t.nif_ofertante = ?) AND t.activa = TRUE", new ColaboracionRowMapper(), nif, nif);
        }
        catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<Colaboracion> getColaboracionesEstudianteNoActivas(String nif) {
        try {
            return jdbcTemplate.query("SELECT t.*, es.nombre || ' ' || es.apellido AS nombre_apellido_demandante, es.rutaimg as rutaimgDemandante, s.rutaim, es.nif AS nif_demandante ,d.estudiante AS nif_ofertante, s.nombre || ' ' || s.nivel AS skill " +
                    "                      FROM ( SELECT c.* AS cs, e.nombre || ' ' || e.apellido AS nombre_apellido_ofertante, e.rutaimg as rutaimgOfertante, e.nif AS nif_ofertante " +
                    "                             FROM colaboracion AS c JOIN oferta AS o USING(id_oferta) " +
                    "                             JOIN estudiante as e ON(e.nif = o.estudiante) " +
                    "                           ) AS t " +
                    "                      JOIN demanda AS d USING(id_demanda) " +
                    "                      JOIN skill as S USING(id_skill) " +
                    "                      JOIN estudiante AS es ON(es.nif = d.estudiante) " +
                    "                      WHERE (es.nif = ? or t.nif_ofertante = ?) AND t.comentario IS NOT NULL", new ColaboracionRowMapper(), nif, nif);
        }
        catch (EmptyResultDataAccessException e) {
            return null;
        }
    }


    public int getHorasOfrecidasEstudiante(String dni){
        try {
            Integer numHorasOfrecidas = jdbcTemplate.queryForObject("SELECT SUM(c.horas) FROM colaboracion AS c JOIN oferta AS o USING(id_oferta) WHERE o.estudiante = ? AND c.activa = FALSE GROUP BY o.estudiante", Integer.class, dni);
            if (numHorasOfrecidas == null)
                return 0;
            return numHorasOfrecidas;
        }catch (EmptyResultDataAccessException e){
            return 0;
        }
    }


    public float getHorasOfrecidasEstudiantePorHorasOfertasTotales(String dni) {
        try {
            Float numHorasOfrecidas = jdbcTemplate.queryForObject("SELECT SUM(c.horas) FROM colaboracion AS c JOIN oferta AS o USING(id_oferta) WHERE o.estudiante = ? AND c.activa = FALSE GROUP BY o.estudiante", Float.class, dni);
            Float numHorasTotalesOfertasColaboraciones = jdbcTemplate.queryForObject("SELECT SUM(c.horas) FROM colaboracion AS c JOIN oferta USING(id_oferta)", Float.class);
            System.out.println(numHorasOfrecidas);
            System.out.println(numHorasTotalesOfertasColaboraciones);
            if (numHorasOfrecidas == null || numHorasTotalesOfertasColaboraciones == null)
                return 0;
            System.out.println(numHorasOfrecidas / numHorasTotalesOfertasColaboraciones);
            BigDecimal bd = BigDecimal.valueOf((numHorasOfrecidas / numHorasTotalesOfertasColaboraciones) * 100).setScale(2, RoundingMode.HALF_UP);
            return bd.floatValue();
        }catch (EmptyResultDataAccessException e){
            return 0;
        }
    }


    public int getHorasRecibidasEstudiante(String dni){
        try {
            Integer numHorasRecibidas = jdbcTemplate.queryForObject("SELECT SUM(c.horas) FROM colaboracion AS c JOIN demanda AS d USING(id_demanda) WHERE d.estudiante = ? AND c.activa = FALSE GROUP BY d.estudiante", Integer.class, dni);
            if (numHorasRecibidas == null)
                return 0;
            return numHorasRecibidas;
        }catch (EmptyResultDataAccessException e){
            return 0;
        }
    }

    public float getHorasDemandadasEstudiantePorHorasDemandasTotales(String dni) {
        try {
            Float numHorasDemandadas = jdbcTemplate.queryForObject("SELECT SUM(c.horas) FROM colaboracion AS c JOIN demanda AS o USING(id_demanda) WHERE o.estudiante = ? AND c.activa = FALSE GROUP BY o.estudiante", Float.class, dni);
            Float numHorasTotalesOfertasColaboraciones = jdbcTemplate.queryForObject("SELECT SUM(c.horas) FROM colaboracion AS c JOIN demanda USING(id_demanda)", Float.class);
            if (numHorasDemandadas == null || numHorasTotalesOfertasColaboraciones == null)
                return 0;
            BigDecimal bd = BigDecimal.valueOf((numHorasDemandadas / numHorasTotalesOfertasColaboraciones) * 100).setScale(2, RoundingMode.HALF_UP);
            return bd.floatValue();
        }catch (EmptyResultDataAccessException e){
            return 0;
        }
    }



    
}
