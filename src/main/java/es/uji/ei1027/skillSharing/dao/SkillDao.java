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
        jdbcTemplate.update("INSERT INTO skill VALUES(?, ?)",
                skill.getNombre(), skill.isActivo());
    }

// No es necesario    public void deleteSkill(String nombre){jdbcTemplate.update("DELETE FROM skill WHERE nombre = ?", nombre);}


    public void endSkill(String nombre){jdbcTemplate.update("UPDATE skill SET activa = FALSE WHERE nombre = ?", nombre);}


    public void updateSkill(Skill skill) {
        jdbcTemplate.update("UPDATE skill SET nivel = ? WHERE nombre = ?", skill.isActivo(), skill.getNombre());
    }

    public Skill getSkill(String nombre) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM skill WHERE nombre = ?", new SkillRowMapper(), nombre);
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