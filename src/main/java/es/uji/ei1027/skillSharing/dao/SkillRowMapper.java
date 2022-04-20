package es.uji.ei1027.skillSharing.dao;


import es.uji.ei1027.skillSharing.modelo.Skill;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SkillRowMapper implements RowMapper<Skill> {
    public Skill mapRow(ResultSet rs, int rowNum)throws SQLException {
        Skill skill=new Skill();
        skill.setIdSkill(rs.getInt("id_skill"));
        skill.setNombre(rs.getString("nombre"));
        skill.setActivo(rs.getBoolean("activo"));
        skill.setNivel(rs.getString("nivel"));
        skill.setDescripcion(rs.getString("descripcion"));
        return skill;
    }
}
