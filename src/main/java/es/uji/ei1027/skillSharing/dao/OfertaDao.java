package es.uji.ei1027.skillSharing.dao;

import es.uji.ei1027.skillSharing.modelo.Oferta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Repository
public class OfertaDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){jdbcTemplate = new JdbcTemplate(dataSource);}

    public void addOferta(Oferta oferta){
        System.out.println("hola");
        jdbcTemplate.update("INSERT INTO oferta(estudiante, horas, ini_fecha, fin_fecha, activa ,id_skill, descripcion) VALUES(?,?,?,?,?,?,?)", oferta.getEstudiante(), oferta.getHoras(), oferta.getIniFecha(),
        oferta.getFinFecha(),true,oferta.getSkill(),oferta.getDescripcion());
    }

    public int devuelveUltimoId(){
        return jdbcTemplate.queryForObject("SELECT MAX(id_oferta) FROM oferta", Integer.class);
    }
    public void endOferta(String idOferta) { jdbcTemplate.update("UPDATE oferta SET activa = FALSE WHERE id_oferta = ?", Integer.parseInt(idOferta));}


    public void updateOferta(Oferta oferta) {
        jdbcTemplate.update("UPDATE oferta SET estudiante = ?, horas = ?, ini_fecha = ?, " +
                        "fin_fecha = ?, id_skill = ?, descripcion = ? WHERE id_oferta = ?", oferta.getEstudiante(),
                oferta.getHoras(), oferta.getIniFecha(), oferta.getFinFecha(), oferta.getSkill(),oferta.getDescripcion(), oferta.getIdOferta());
    }

    public Oferta getOferta(String idOferta) {
        try {
            return jdbcTemplate.queryForObject("SELECT o.*, s.nombre AS nombre_skill, e.rutaimg, s.rutaim, s.nivel AS nivel_skill FROM oferta AS o JOIN skill as S USING(id_skill) JOIN estudiante AS e ON (o.estudiante = e.nif) WHERE o.id_oferta = ?", new OfertaRowMapper(), Integer.parseInt(idOferta));
        }
        catch (EmptyResultDataAccessException e){
            return null;

        }
    }

    public List<Oferta> getOfertas() {
        try {
            return jdbcTemplate.query("SELECT o.id_oferta, e.rutaimg, s.rutaim, e.nombre || ' '  || e.apellido AS estudiante, o.horas, o.ini_fecha, o.fin_fecha, o.id_skill, o.activa, o.descripcion, s.nombre AS nombre_skill, s.nivel AS nivel_skill FROM oferta AS o JOIN skill as S USING(id_skill) JOIN estudiante AS e ON (o.estudiante = e.nif) WHERE o.activa= TRUE", new OfertaRowMapper());
        }
        catch (EmptyResultDataAccessException e) {
            return new ArrayList<Oferta>();
        }

    }

    public List<Oferta> getOfertasEstudiante(String estudiante) {
        try {
            return jdbcTemplate.query("SELECT o.*, s.nombre AS nombre_skill, e.rutaimg, s.rutaim, s.nivel AS nivel_skill FROM oferta AS o JOIN skill as S USING(id_skill) JOIN estudiante AS e ON (o.estudiante = e.nif) WHERE o.activa= TRUE and estudiante = ?", new OfertaRowMapper(), estudiante);
        }
        catch (EmptyResultDataAccessException e) {
            return new ArrayList<Oferta>();
        }

    }

    public List<Oferta> getTodasOfertasMenosMias(String estudiante) {

        try {
            return jdbcTemplate.query("SELECT o.id_oferta, e.rutaimg, s.rutaim, e.nombre || ' '  || e.apellido AS estudiante, o.horas, o.ini_fecha, o.fin_fecha, o.id_skill, o.activa, o.descripcion, s.nombre AS nombre_skill, s.nivel AS nivel_skill FROM oferta AS o JOIN skill as S USING(id_skill) JOIN estudiante AS e ON (o.estudiante = e.nif) WHERE o.activa= TRUE and estudiante <> ?", new OfertaRowMapper(), estudiante);
        }
        catch (EmptyResultDataAccessException e) {
            return new ArrayList<Oferta>();
        }

    }

    public List<Oferta> getOfertasAsociadasASkill(int skill) {
        try {
            return jdbcTemplate.query("SELECT o.id_oferta, e.rutaimg, s.rutaim, e.nombre || ' '  || e.apellido AS estudiante, o.horas, o.ini_fecha, o.fin_fecha, o.id_skill, o.activa, o.descripcion, s.nombre AS nombre_skill, s.nivel AS nivel_skill FROM oferta AS o JOIN skill as s USING(id_skill) JOIN estudiante AS e ON (o.estudiante = e.nif) WHERE o.activa= TRUE and s.id_skill=? ", new OfertaRowMapper(), skill);
        }
        catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<Oferta> getOfertasAsociadasASkill(int skill, String iniFecha, String finFecha) {
        try {
            return jdbcTemplate.query("SELECT o.id_oferta, e.rutaimg, s.rutaim, e.nombre || ' '  || e.apellido AS estudiante, o.horas, o.ini_fecha, o.fin_fecha, o.id_skill, o.activa, o.descripcion, s.nombre AS nombre_skill, s.nivel AS nivel_skill FROM oferta AS o JOIN skill as s USING(id_skill) JOIN estudiante AS e ON (o.estudiante = e.nif) WHERE o.activa= TRUE and s.id_skill=? and to_char(o.ini_fecha, 'YYYY-MM-DD') >=? and to_char(o.fin_fecha, 'YYYY-MM-DD') <=?", new OfertaRowMapper(), skill, iniFecha, finFecha );
        }
        catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<Oferta> getOfertasAsociadasASkillMenosMias(int skill,String estudiante, String iniFecha, String finFecha) {
        try {
            return jdbcTemplate.query("SELECT o.id_oferta, e.rutaimg, s.rutaim, e.nombre || ' '  || e.apellido AS estudiante, o.horas, o.ini_fecha, o.fin_fecha, o.id_skill, o.activa, o.descripcion, s.nombre AS nombre_skill, s.nivel AS nivel_skill FROM oferta AS o JOIN skill as s USING(id_skill) JOIN estudiante AS e ON (o.estudiante = e.nif) WHERE o.activa= TRUE and s.id_skill=? and estudiante <> ? and to_char(o.ini_fecha, 'YYYY-MM-DD') >=? and to_char(o.fin_fecha, 'YYYY-MM-DD') <=?", new OfertaRowMapper(), skill, estudiante, iniFecha, finFecha );
        }
        catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<Oferta> getMisOfertasAsociadasASkill(int skill,String estudiante, String iniFecha, String finFecha) {
        try {
            return jdbcTemplate.query("SELECT o.id_oferta, e.rutaimg, s.rutaim, e.nombre || ' '  || e.apellido AS estudiante, o.horas, o.ini_fecha, o.fin_fecha, o.id_skill, o.activa, o.descripcion, s.nombre AS nombre_skill, s.nivel AS nivel_skill FROM oferta AS o JOIN skill as s USING(id_skill) JOIN estudiante AS e ON (o.estudiante = e.nif) WHERE o.activa= TRUE and s.id_skill=? and estudiante = ? and to_char(o.ini_fecha, 'YYYY-MM-DD') >=? and to_char(o.fin_fecha, 'YYYY-MM-DD') <=?", new OfertaRowMapper(), skill, estudiante, iniFecha, finFecha );
        }
        catch (EmptyResultDataAccessException e) {
            return null;
        }
    }



}
//skl
