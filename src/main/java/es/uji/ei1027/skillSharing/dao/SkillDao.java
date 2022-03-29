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
        jdbcTemplate.update("INSERT INTO skill VALUES(nombre, activo, nivel)",
                skill.getNombre(), skill.isActivo(), skill.getNivel());
    }

// No es necesario    public void deleteSkill(String nombre){jdbcTemplate.update("DELETE FROM skill WHERE nombre = ?", nombre);}


    public void endSkill(int idSkill){jdbcTemplate.update("UPDATE skill SET activa = FALSE WHERE id_skill = ?", idSkill);}


    public void updateSkill(Skill skill) {
        jdbcTemplate.update("UPDATE skill SET activo = ?, nivel = ? WHERE id_skill = ?", skill.isActivo(), skill.getNivel(), skill.getIdSkill());
    }

    public Skill getSkill(int idSkill) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM skill WHERE id_skill = ?", new SkillRowMapper(), idSkill);
        }
        catch (EmptyResultDataAccessException e){
            return null;
        }
    }

    public List<Skill> getSkills() {
        try {
            return jdbcTemplate.query("SELECT * FROM skill", new SkillRowMapper());
        }
        catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}