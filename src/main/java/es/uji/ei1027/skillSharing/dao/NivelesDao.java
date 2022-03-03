package es.uji.ei1027.skillSharing.dao;

import es.uji.ei1027.skillSharing.modelo.Niveles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class NivelesDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) { jdbcTemplate = new JdbcTemplate(dataSource); }

    public void addNivel(Niveles nivel) {
        jdbcTemplate.update("INSERT INTO niveles VALUES(?)", nivel.getNombre());
    }

    public void updateNivel(Niveles nivel) {
        jdbcTemplate.update("UPDATE FROM niveles WHERE nombre = ? ", nivel.getNombre());
    }

    public void deleteNivel(String nombre) {
        jdbcTemplate.update("DELETE FROM niveles WHERE nombre = ? ", nombre);
    }

    public List<Niveles> getNivelesporNombre(String nombre){
        try {
            return jdbcTemplate.query("SELECT * FROM niveles WHERE nombre = ?", new NivelesRowMapper(), nombre);
        }
        catch (EmptyResultDataAccessException e){
            return null;
        }
    }

    public List<Niveles> getNiveles() {
        try {
            return jdbcTemplate.query("SELECT * FROM niveles", new NivelesRowMapper());
        }
        catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}

