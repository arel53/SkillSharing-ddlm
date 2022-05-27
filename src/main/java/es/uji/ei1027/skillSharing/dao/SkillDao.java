package es.uji.ei1027.skillSharing.dao;

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
        jdbcTemplate.update("INSERT INTO skill(nombre, activo, nivel, descripcion, rutaim) VALUES(?, ? ,? ,?, ?)",
                skill.getNombre(), true, skill.getNivel(), skill.getDescripcion(), skill.getRutaim());
    }


    public void endSkill(String idSkill){jdbcTemplate.update("UPDATE skill SET activo = FALSE WHERE id_skill = ?", Integer.parseInt(idSkill));}

    public void endOfertasSkill(String idSkill){jdbcTemplate.update("UPDATE oferta SET activa = FALSE WHERE id_skill = ?", Integer.parseInt(idSkill));}

    public void endDemandasSkill(String idSkill){jdbcTemplate.update("UPDATE demanda SET activa = FALSE WHERE id_skill = ?", Integer.parseInt(idSkill));}


    public void updateSkill(Skill skill) {
        jdbcTemplate.update("UPDATE skill SET activo = ?, nivel = ?, descripcion = ?, rutaim = ? WHERE id_skill = ?", skill.isActivo(), skill.getNivel(), skill.getDescripcion(),skill.getRutaim() ,skill.getIdSkill());
    }

    public Skill getSkill(String idSkill) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM skill WHERE id_skill = ?", new SkillRowMapper(), Integer.parseInt(idSkill));
        }
        catch (EmptyResultDataAccessException e){
            return null;
        }
    }

    public int getNumOfertasSkill(int idSkill) {
        try {
            Integer numOfertas = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM oferta WHERE id_skill = ?", Integer.class, idSkill);
            if (numOfertas == null)
                return -1;
            return numOfertas;
        }
        catch (EmptyResultDataAccessException e){
            return -1;
        }
    }

    public int getNumDemandasSkill(int idSkill) {
        try {
            Integer numDemandas = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM demanda WHERE id_skill = ?", Integer.class, idSkill);
            if (numDemandas == null)
                return -1;
            return numDemandas;

        }
        catch (EmptyResultDataAccessException e){
            return -1;
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