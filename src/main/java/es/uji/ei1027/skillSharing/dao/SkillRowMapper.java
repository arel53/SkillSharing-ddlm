package es.uji.ei1027.skillSharing.dao;


import es.uji.ei1027.skillSharing.modelo.Skill;
import org.springframework.jdbc.core.RowMapper;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class SkillRowMapper implements RowMapper<Skill> {


    public Skill mapRow(ResultSet rs, int rowNum)throws SQLException {
        Skill skill=new Skill();
        skill.setIdSkill(rs.getInt("id_skill"));
        skill.setNombre(rs.getString("nombre"));
        skill.setActivo(rs.getBoolean("activo"));
        skill.setNivel(rs.getString("nivel"));
        skill.setDescripcion(rs.getString("descripcion"));
        skill.setRutaim(rs.getString("rutaim"));
//        InputStream is = new ByteArrayInputStream(rs.getBytes("skimg"));
//        try {
//            skill.setSkimg(ImageIO.read( is));
//        } catch (IOException e) {
//            e.printStackTrace();
//        };

        return skill;
    }
}
