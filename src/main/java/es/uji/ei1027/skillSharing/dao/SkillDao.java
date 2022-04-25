package es.uji.ei1027.skillSharing.dao;

import es.uji.ei1027.skillSharing.modelo.Demanda;
import es.uji.ei1027.skillSharing.modelo.Oferta;
import es.uji.ei1027.skillSharing.modelo.Skill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class SkillDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){jdbcTemplate = new JdbcTemplate(dataSource);}

    public void addSkill(Skill skill){
        jdbcTemplate.update("INSERT INTO skill(nombre, activo, nivel, descripcion) VALUES(?, ? ,? ,?)",
                skill.getNombre(), true, skill.getNivel(), skill.getDescripcion());
    }


    public void endSkill(String idSkill){jdbcTemplate.update("UPDATE skill SET activo = FALSE WHERE id_skill = ?", Integer.parseInt(idSkill));}

    public void endOfertasSkill(String idSkill){jdbcTemplate.update("UPDATE oferta SET activa = FALSE WHERE id_skill = ?", Integer.parseInt(idSkill));}

    public void endDemandasSkill(String idSkill){jdbcTemplate.update("UPDATE demanda SET activa = FALSE WHERE id_skill = ?", Integer.parseInt(idSkill));}


    public void updateSkill(Skill skill) {
        jdbcTemplate.update("UPDATE skill SET activo = ?, nivel = ?, descripcion = ? WHERE id_skill = ?", skill.isActivo(), skill.getNivel(), skill.getDescripcion(), skill.getIdSkill());
    }

    public Skill getSkill(String idSkill) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM skill WHERE id_skill = ?", new SkillRowMapper(), Integer.parseInt(idSkill));
        }
        catch (EmptyResultDataAccessException e){
            return null;
        }
    }

    public List<Oferta> getOfertasSkill(String idSkill) {
        try {
            return jdbcTemplate.query("SELECT * FROM oferta WHERE id_skill = ?", new OfertaRowMapper(), Integer.parseInt(idSkill));
        }
        catch (EmptyResultDataAccessException e){
            return null;
        }
    }

    public List<Demanda> getDemandasSkill(String idSkill) {
        try {
            return jdbcTemplate.query("SELECT * FROM demanda WHERE id_skill = ?", new DemandaRowMapper(), Integer.parseInt(idSkill));
        }
        catch (EmptyResultDataAccessException e){
            return null;
        }
    }

    public List<Skill> getSkillsActivas() {
        try {
            return jdbcTemplate.query("SELECT * FROM skill WHERE activo = TRUE", new SkillRowMapper());
        }
        catch (EmptyResultDataAccessException e) {
            return null;
        }
    }


    public List<Skill> getSkillsTodas() {
        try {
            return jdbcTemplate.query("SELECT * FROM skill", new SkillRowMapper());
        }
        catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}