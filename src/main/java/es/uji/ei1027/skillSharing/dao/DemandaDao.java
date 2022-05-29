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
                demanda.getFinFecha(),true,demanda.getSkill(),demanda.getDescripcion());
    }

    public int devuelveUltimoId(){
        return jdbcTemplate.queryForObject("SELECT MAX(id_demanda) FROM demanda", Integer.class);
    }


    public void endDemanda(String idDemanda) { jdbcTemplate.update("UPDATE demanda SET activa = FALSE WHERE id_demanda = ?", Integer.parseInt(idDemanda));}


    public void updateDemanda(Demanda demanda) {
        jdbcTemplate.update("UPDATE demanda SET  estudiante = ?, horas = ?, ini_fecha = ?, " +
                        "fin_fecha = ?, activa = ?, id_skill = ?, descripcion = ? WHERE id_demanda = ?", demanda.getEstudiante(),
                demanda.getHoras(), demanda.getIniFecha(), demanda.getFinFecha(), demanda.isActiva(), demanda.getSkill(), demanda.getDescripcion(), demanda.getIdDemanda());
    }

    public Demanda getDemanda(String idDemanda) {
        try {
            return jdbcTemplate.queryForObject("SELECT d.*, s.nombre AS nombre_skill, e.rutaimg, s.rutaim, s.nivel AS nivel_skill FROM demanda AS d JOIN skill as S USING(id_skill) JOIN estudiante AS e ON (d.estudiante = e.nif) WHERE d.id_demanda = ?", new DemandaRowMapper(), Integer.parseInt(idDemanda));
        }
        catch (EmptyResultDataAccessException e){
            return null;
        }
    }

    public List<Demanda> getDemandas() {
        try {
            return jdbcTemplate.query("SELECT d.id_demanda, e.rutaimg, s.rutaim, e.nombre || ' '  || e.apellido AS estudiante, d.horas, d.ini_fecha, d.fin_fecha, d.id_skill, d.activa, d.descripcion, s.nombre AS nombre_skill, s.nivel AS nivel_skill FROM demanda AS d JOIN skill as S USING(id_skill) JOIN estudiante AS e ON (d.estudiante = e.nif) WHERE d.activa= TRUE", new DemandaRowMapper());
        }
        catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<Demanda> getDemandasEstudiante(String nif) {
        try {
            return jdbcTemplate.query("SELECT d.id_demanda, e.rutaimg, s.rutaim, e.nombre || ' '  || e.apellido AS estudiante, d.horas, d.ini_fecha, d.fin_fecha, d.id_skill, d.activa, d.descripcion, s.nombre AS nombre_skill, s.nivel AS nivel_skill FROM demanda AS d JOIN skill as S USING(id_skill) JOIN estudiante AS e ON (d.estudiante = e.nif) WHERE d.activa= TRUE and estudiante = ?", new DemandaRowMapper(), nif);
        }
        catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<Demanda> getTodasDemandasMenosMias(String estudiante) {
        try {
            return jdbcTemplate.query("SELECT d.id_demanda, e.rutaimg, s.rutaim, e.nombre || ' '  || e.apellido AS estudiante, d.horas, d.ini_fecha, d.fin_fecha, d.id_skill, d.activa, d.descripcion, s.nombre AS nombre_skill, s.nivel AS nivel_skill FROM demanda AS d JOIN skill as S USING(id_skill) JOIN estudiante AS e ON (d.estudiante = e.nif) WHERE d.activa= TRUE and estudiante <> ?", new DemandaRowMapper(), estudiante);
        }
        catch (EmptyResultDataAccessException e) {
            return null;
        }
    }


    public List<Demanda> getDemandasAsociadasASkill(int idSkill) {
        try {
            return jdbcTemplate.query("SELECT d.id_demanda, e.rutaimg, s.rutaim, e.nombre || ' '  || e.apellido AS estudiante, d.horas, d.ini_fecha, d.fin_fecha, d.id_skill, d.activa, d.descripcion, s.nombre AS nombre_skill, s.nivel AS nivel_skill FROM demanda AS d JOIN skill as S USING(id_skill) JOIN estudiante AS e ON (d.estudiante = e.nif) WHERE d.activa= TRUE and id_skill = ? ", new DemandaRowMapper(), idSkill);
        }
        catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<Demanda> getDemandasAsociadasASkill(int idSkill, String iniFecha, String finFecha) {
        try {
            return jdbcTemplate.query("SELECT d.id_demanda, e.rutaimg, s.rutaim, e.nombre || ' '  || e.apellido AS estudiante, d.horas, d.ini_fecha, d.fin_fecha, d.id_skill, d.activa, d.descripcion, s.nombre AS nombre_skill, s.nivel AS nivel_skill FROM demanda AS d JOIN skill as S USING(id_skill) JOIN estudiante AS e ON (d.estudiante = e.nif) WHERE d.activa= TRUE and id_skill = ? and to_char(d.ini_fecha, 'YYYY-MM-DD') >= ? and to_char(d.fin_fecha, 'YYYY-MM-DD') <= ?", new DemandaRowMapper(), idSkill, iniFecha, finFecha);
        }
        catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<Demanda> getDemandasAsociadasASkillMenosMias(int idSkill, String estudiante, String iniFecha, String finFecha) {
        try {
            return jdbcTemplate.query("SELECT d.id_demanda, e.rutaimg, s.rutaim, e.nombre || ' '  || e.apellido AS estudiante, d.horas, d.ini_fecha, d.fin_fecha, d.id_skill, d.activa, d.descripcion, s.nombre AS nombre_skill, s.nivel AS nivel_skill FROM demanda AS d JOIN skill as S USING(id_skill) JOIN estudiante AS e ON (d.estudiante = e.nif) WHERE d.activa= TRUE and id_skill = ? and estudiante <> ? and to_char(d.ini_fecha, 'YYYY-MM-DD') >= ? and to_char(d.fin_fecha, 'YYYY-MM-DD') <= ?", new DemandaRowMapper(), idSkill, estudiante, iniFecha, finFecha);
        }
        catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<Demanda> getMisDemandasAsociadasASkill(int idSkill, String estudiante, String iniFecha, String finFecha) {
        try {
            return jdbcTemplate.query("SELECT d.id_demanda, e.rutaimg, s.rutaim, e.nombre || ' '  || e.apellido AS estudiante, d.horas, d.ini_fecha, d.fin_fecha, d.id_skill, d.activa, d.descripcion, s.nombre AS nombre_skill, s.nivel AS nivel_skill FROM demanda AS d JOIN skill as S USING(id_skill) JOIN estudiante AS e ON (d.estudiante = e.nif) WHERE d.activa= TRUE and id_skill = ? and estudiante = ? and to_char(d.ini_fecha, 'YYYY-MM-DD') >= ? and to_char(d.fin_fecha, 'YYYY-MM-DD') <= ?", new DemandaRowMapper(), idSkill, estudiante, iniFecha, finFecha);
        }
        catch (EmptyResultDataAccessException e) {
            return null;
        }
    }


    public List<String> getDemandasEstudiantesEnviarCorreo(int idSkill) {
        try {
            return jdbcTemplate.queryForList("SELECT DISTINCT LOWER(e.email) FROM demanda AS d JOIN skill as S USING(id_skill) JOIN estudiante AS e ON (d.estudiante = e.nif) WHERE d.activa= TRUE and id_skill = ?",String.class , idSkill);
        }
        catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}
