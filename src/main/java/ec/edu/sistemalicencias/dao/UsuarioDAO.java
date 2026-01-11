package ec.edu.sistemalicencias.dao;

import ec.edu.sistemalicencias.config.DatabaseConfig;
import ec.edu.sistemalicencias.model.entities.Usuario;
import ec.edu.sistemalicencias.model.exceptions.BaseDatosException;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    public Usuario login(String user, String pass) throws BaseDatosException {
        String sql = "SELECT * FROM usuarios WHERE usuario = ? AND clave = ?";
        try (Connection conn = DatabaseConfig.getInstance().obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user);
            stmt.setString(2, pass); // Texto plano según requerimiento

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearUsuario(rs);
                }
            }
        } catch (SQLException e) {
            throw new BaseDatosException("Error en login", e);
        }
        return null;
    }

    public void crear(Usuario usuario) throws BaseDatosException {
        String sql = "INSERT INTO usuarios (usuario, clave, rol, nombre_completo, cedula, email) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.getInstance().obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario.getUsuario());
            stmt.setString(2, usuario.getClave());
            stmt.setString(3, usuario.getRol());
            stmt.setString(4, usuario.getNombreCompleto());
            stmt.setString(5, usuario.getCedula());
            stmt.setString(6, usuario.getEmail());
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new BaseDatosException("Error al crear usuario: " + e.getMessage(), e);
        }
    }

    public List<Usuario> listarPorFechas(LocalDate inicio, LocalDate fin) throws BaseDatosException {
        // En Postgres usamos ::date para comparar solo la parte de fecha de un timestamp
        String sql = "SELECT * FROM usuarios WHERE rol = 'ANALISTA'";
        if (inicio != null && fin != null) {
            sql += " AND created_at::date BETWEEN ? AND ?";
        }
        List<Usuario> lista = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getInstance().obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            if (inicio != null && fin != null) {
                stmt.setDate(1, Date.valueOf(inicio));
                stmt.setDate(2, Date.valueOf(fin));
            }
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearUsuario(rs));
                }
            }
        } catch (SQLException e) {
            throw new BaseDatosException("Error al listar usuarios en Postgres", e);
        }
        return lista;
    }

    private Usuario mapearUsuario(ResultSet rs) throws SQLException {
        Usuario u = new Usuario();
        u.setId(rs.getLong("id"));
        u.setUsuario(rs.getString("usuario"));
        u.setClave(rs.getString("clave"));
        u.setRol(rs.getString("rol"));
        u.setNombreCompleto(rs.getString("nombre_completo"));
        u.setCedula(rs.getString("cedula"));
        u.setEmail(rs.getString("email"));

        // Usar getTimestamp y verificar null antes de convertir a LocalDateTime
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            u.setFechaCreacion(createdAt.toLocalDateTime());
        }
        return u;
    }
}