package es.uji.ei1027.skillSharing.dao;

import es.uji.ei1027.skillSharing.modelo.Estudiante;
import es.uji.ei1027.skillSharing.modelo.Oferta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class OfertaDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){jdbcTemplate = new JdbcTemplate(dataSource);}

    public void addOferta(Oferta oferta){
        jdbcTemplate.update("INSERT INTO oferta VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)",
        oferta.getAndIncrement(), oferta.getEstudiante(), oferta.getHoras(), oferta.getIniFecha(),
        oferta.getFinFecha(),true,oferta.getSkill(),oferta.getNivel(),oferta.getDescripcion());
    }

    public void deleteOferta(String idOferta){jdbcTemplate.update("DELETE FROM oferta WHERE id_oferta = ?", idOferta);}

    public void endOferta(String idOferta) { jdbcTemplate.update("UPDATE oferta SET activa = FALSE WHERE id_oferta = ?", idOferta);}


    public void updateOferta(Oferta oferta) {
        jdbcTemplate.update("UPDATE oferta SET estudiante = ?, horas = ?, ini_fecha = ?, " +
                        "fin_fecha = ?, activa = ?, skill = ?, nivel = ?, direccion = ?, descripcion = ? WHERE id_oferta = ?", oferta.getEstudiante(),
                oferta.getHoras(), oferta.getIniFecha(), oferta.getFinFecha(), oferta.isActiva(), oferta.getSkill(), oferta.getNivel(), oferta.getDescripcion(), oferta.getIdOferta());
    }

    public Oferta getOferta(String idOferta) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM oferta WHERE id_oferta = ?", new OfertaRowMapper(), idOferta);
        }
        catch (EmptyResultDataAccessException e){
            return null;
        }
    }

    public List<Oferta> getOfertas() {
        try {
            return jdbcTemplate.query("SELECT * FROM oferta", new OfertaRowMapper());
        }
        catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}
