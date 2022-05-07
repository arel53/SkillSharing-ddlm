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
        jdbcTemplate.update("INSERT INTO colaboracion(id_oferta, id_demanda, ini_fecha, fin_fecha, activa, rate, comentario, horas) VALUES(?, ?, ?, ?, ?, ?, ?, ?)", colaboracion.getIdOferta(),
                colaboracion.getIdDemanda(), colaboracion.getIniFecha(), colaboracion.getFinFecha(), true,
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
            return jdbcTemplate.queryForObject("SELECT * FROM colaboracion WHERE id_colaboracion = ?", new ColaboracionRowMapper(), idColaboracion);
        }
        catch (EmptyResultDataAccessException e){
            return null;
        }

    }

    public List<Colaboracion> getColaboraciones() {
        try {
            return jdbcTemplate.query("SELECT * FROM colaboracion", new ColaboracionRowMapper());
        }
        catch (EmptyResultDataAccessException e){
            return null;
        }
    }

    public List<Colaboracion> getColaboracionesPorOferta(String idOferta) {
        try {
            return jdbcTemplate.query("SELECT * FROM colaboracion WHERE id_oferta = ?", new ColaboracionRowMapper(), idOferta);
        }
        catch (EmptyResultDataAccessException e){
            return null;
        }
    }

    public List<Colaboracion> getColaboracionesPorDemanda(String idDemanda) {
        try {
            return jdbcTemplate.query("SELECT * FROM colaboracion WHERE id_demanda = ?", new ColaboracionRowMapper(), idDemanda);
        }
        catch (EmptyResultDataAccessException e){
            return null;
        }
    }

    public List<Colaboracion> getColaboracionesEstudiante(String nif) {
        try {
            return jdbcTemplate.query("select * from colaboracion " +
                    "where id_oferta in ( select id_oferta from oferta where estudiante=? ) or " +
                    "id_demanda in ( select id_demanda from demanda where estudiante=? )", new ColaboracionRowMapper(), nif, nif);
        }
        catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    
}
