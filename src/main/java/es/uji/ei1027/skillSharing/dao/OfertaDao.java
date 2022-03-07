package es.uji.ei1027.skillSharing.dao;

import es.uji.ei1027.skillSharing.modelo.Estudiante;
import es.uji.ei1027.skillSharing.modelo.Oferta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class OfertaDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){jdbcTemplate = new JdbcTemplate(dataSource);}

    public void addOferta(Oferta oferta){
        System.out.println("hola");
        jdbcTemplate.update("INSERT INTO oferta VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)",
        oferta.getAndIncrement(), oferta.getEstudiante(), oferta.getHoras(), oferta.getIniFecha(),
        oferta.getFinFecha(),true,oferta.getSkill(),oferta.getNivel(),oferta.getDescripcion());
    }
    //este no deberia existir
    //public void deleteOferta(String idOferta){jdbcTemplate.update("DELETE FROM oferta WHERE id_oferta = ?", idOferta);}

    public void endOferta(String idOferta) { jdbcTemplate.update("UPDATE oferta SET activa = FALSE WHERE id_oferta = ?", Integer.parseInt(idOferta));}


    public void updateOferta(Oferta oferta) {
        jdbcTemplate.update("UPDATE oferta SET estudiante = ?, horas = ?, ini_fecha = ?, " +
                        "fin_fecha = ?, activa = ?, skill = ?, nivel = ?, descripcion = ? WHERE id_oferta = ?", oferta.getEstudiante(),
                oferta.getHoras(), oferta.getIniFecha(), oferta.getFinFecha(), oferta.isActiva(), oferta.getSkill(), oferta.getNivel(), oferta.getDescripcion(), oferta.getIdOferta());
    }

    public Oferta getOferta(String idOferta) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM oferta WHERE id_oferta = ?", new OfertaRowMapper(), Integer.parseInt(idOferta));
        }
        catch (EmptyResultDataAccessException e){
            return null;
        }
    }

    public List<Oferta> getOfertas() {
        try {
            return jdbcTemplate.query("SELECT * FROM oferta WHERE activa= TRUE", new OfertaRowMapper());
        }
        catch (EmptyResultDataAccessException e) {
            return new ArrayList<Oferta>();
        }
    }
}
//skl
