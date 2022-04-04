package es.uji.ei1027.skillSharing.dao;

import es.uji.ei1027.skillSharing.modelo.Demanda;
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
        jdbcTemplate.update("INSERT INTO demanda(estudiante, horas, ini_fecha, fin_fecha, activa ,id_skill, descripcion) VALUES(?,?,?,?,?,?,?)",
                demanda.getEstudiante(), demanda.getHoras(), demanda.getIniFecha(),
                demanda.getFinFecha(),true,demanda.getIdSkill(),demanda.getDescripcion());
    }

 // No es necesario   public void deleteDemanda(String idDemanda){jdbcTemplate.update("DELETE FROM demanda WHERE id_demanda = ?", idDemanda);}

    public void endDemanda(String idDemanda) { jdbcTemplate.update("UPDATE demanda SET activa = FALSE WHERE id_demanda = ?", Integer.parseInt(idDemanda));}


    public void updateDemanda(Demanda demanda) {
        jdbcTemplate.update("UPDATE demanda SET  estudiante = ?, horas = ?, ini_fecha = ?, " +
                        "fin_fecha = ?, activa = ?, id_skill = ?, descripcion = ? WHERE id_demanda = ?", demanda.getEstudiante(),
                demanda.getHoras(), demanda.getIniFecha(), demanda.getFinFecha(), demanda.isActiva(), demanda.getIdSkill(), demanda.getDescripcion(), demanda.getIdDemanda());
    }

    public Demanda getDemanda(String idDemanda) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM demanda WHERE id_demanda = ?", new DemandaRowMapper(), Integer.parseInt(idDemanda));
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
