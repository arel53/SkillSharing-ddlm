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
        jdbcTemplate.update("INSERT INTO colaboracion VALUES(?, ?, ?, ?, ?, ?, ?, ?)", colaboracion.getIdOferta(),
                colaboracion.getIdDemanda(), colaboracion.getIniFecha(), colaboracion.getFinFecha(), colaboracion.isActiva(),
                colaboracion.getRate(), colaboracion.getComentario(),colaboracion.getHoras());
    }

    public void endColaboracion(String idOferta, String idDemanda) {
        jdbcTemplate.update("UPDATE colaboracion SET activa = FALSE WHERE id_oferta = ? AND id_demanda = ?", idOferta, idDemanda);
    }

    public void updateColaboracion(Colaboracion colaboracion) {
        jdbcTemplate.update("UPDATE colaboracion SET  id_demanda = ?, ini_fecha = ?, fin_fecha = ?, " +
                "activa = ?, rate = ?, comentario = ?,horas = ? WHERE id_oferta = ?",
                colaboracion.getIdDemanda(), colaboracion.getIniFecha(), colaboracion.getFinFecha(), colaboracion.isActiva(),
                colaboracion.getRate(), colaboracion.getComentario(),colaboracion.getHoras(), colaboracion.getIdOferta());
    }

    public Colaboracion getColaboracion(String idOferta, String idDemanda) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM colaboracion WHERE id_oferta = ? AND id_demanda = ?", new ColaboracionRowMapper(), idOferta, idDemanda);
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
}
