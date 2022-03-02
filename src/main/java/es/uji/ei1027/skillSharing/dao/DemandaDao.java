package es.uji.ei1027.skillSharing.dao;

import es.uji.ei1027.skillSharing.modelo.Demanda;
import es.uji.ei1027.skillSharing.modelo.Oferta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class DemandaDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){jdbcTemplate = new JdbcTemplate(dataSource);}

    public void addDemanda(Demanda demanda){
        jdbcTemplate.update("INSERT INTO oferta VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)",
                demanda.getIdDemanda(), demanda.getEstudiante(), demanda.getHoras(), demanda.getIniFecha(),
                demanda.getFinFecha(),demanda.isActiva(),demanda.getSkill(),demanda.getNivel(),demanda.getDescripcion());
    }

    public void deleteDemanda(String idOferta){jdbcTemplate.update("DELETE FROM estudiante WHERE id_oferta = ?", idOferta);}


    public void updateDemanda(Demanda demanda) {
        jdbcTemplate.update("UPDATE estudiante SET id_demanda = ?, estudiante = ?, horas = ?, ini_fecha = ?, " +
                        "fin_fecha = ?, activa = ?, skill = ?, nivel = ?, direccion = ?, descripcion = ?", demanda.getIdDemanda(), demanda.getEstudiante(),
                demanda.getHoras(), demanda.getIniFecha(), demanda.getFinFecha(), demanda.isActiva(), demanda.getSkill(), demanda.getNivel(), demanda.getDescripcion());
    }

    public Oferta getDemanda(String idDemanda) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM estudiante WHERE id_demanda = ?", new OfertaRowMapper(), idDemanda);
        }
        catch (EmptyResultDataAccessException e){
            return null;
        }
    }

    public List<Demanda> getDemandas() {
        try {
            return jdbcTemplate.query("SELECT * FROM demanda", new DemandaRowMapper());
        }
        catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}
