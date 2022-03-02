package es.uji.ei1027.skillSharing.dao;

import es.uji.ei1027.skillSharing.modelo.Estudiante;
import es.uji.ei1027.skillSharing.modelo.Oferta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class OfertaDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){jdbcTemplate = new JdbcTemplate(dataSource);}

    public void addOferta(Oferta oferta){
        jdbcTemplate.update("INSERT INTO oferta VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)",
        oferta.getIdOferta(), oferta.getEstudiante(), oferta.getHoras(), oferta.getIniFecha(),
        oferta.getFinFecha(),oferta.isActiva(),oferta.getSkill(),oferta.getNivel(),oferta.getDescripcion());
    }

    public void deleteOferta(String idOferta){jdbcTemplate.update("DELETE FROM estudiante WHERE id_oferta = ?", idOferta);}


    public void updateOferta(Oferta oferta) {
        jdbcTemplate.update("UPDATE estudiante SET id_oferta = ?, estudiante = ?, horas = ?, ini_fecha = ?, " +
                        "fin_fecha = ?, activa = ?, skill = ?, nivel = ?, direccion = ?, descripcion = ?", oferta.getIdOferta(), oferta.getEstudiante(),
                oferta.getHoras(), oferta.getIniFecha(), oferta.getFinFecha(), oferta.isActiva(), oferta.getSkill(), oferta.getNivel(), oferta.getDescripcion());
    }

    public Oferta getOferta(String idOferta) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM estudiante WHERE id_oferta = ?", new OfertaRowMapper(), idOferta);
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
