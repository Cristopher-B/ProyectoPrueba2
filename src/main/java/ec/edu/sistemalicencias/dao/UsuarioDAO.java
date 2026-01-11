package ec.edu.sistemalicencias.dao;
import ec.edu.sistemalicencias.config.DatabaseConfig;
import ec.edu.sistemalicencias.model.entities.Usuario;
import ec.edu.sistemalicencias.model.exceptions.BaseDatosException;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    /**
     * Realiza la autenticación del usuario en la base de datos.
     */
    public Usuario login(String usuario, String clave) throws BaseDatosException {
        String sql = "SELECT * FROM usuarios WHERE usuario = ? AND clave = ?";
        try (Connection conn = DatabaseConfig.getInstance().obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario);
            stmt.setString(2, clave);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearUsuario(rs);
                }
            }
        } catch (SQLException e) {
            throw new BaseDatosException("Error al intentar iniciar sesión en Supabase: " + e.getMessage(), e);
        }
        return null;
    }

    /**
     * Inserta un nuevo usuario (Analista) en Supabase.
     */
    public void insertar(Usuario usuario) throws BaseDatosException {
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
            throw new BaseDatosException("Error al insertar usuario en la nube: " + e.getMessage(), e);
        }
    }

    /**
     * Lista usuarios filtrados por fecha o todos si los parámetros son null.
     */
    public List<Usuario> listarPorFechas(LocalDate inicio, LocalDate fin) throws BaseDatosException {
        List<Usuario> lista = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM usuarios WHERE rol = 'ANALISTA'");

        // El cast ::date es específico de PostgreSQL para manejar Timestamps
        if (inicio != null && fin != null) {
            sql.append(" AND created_at::date BETWEEN ? AND ?");
        }
        sql.append(" ORDER BY id DESC");

        try (Connection conn = DatabaseConfig.getInstance().obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

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
            throw new BaseDatosException("Error al listar usuarios: " + e.getMessage(), e);
        }
        return lista;
    }

    /**
     * Busca un usuario específico por su ID.
     */
    public Usuario buscarPorId(Long id) throws BaseDatosException {
        String sql = "SELECT * FROM usuarios WHERE id = ?";
        try (Connection conn = DatabaseConfig.getInstance().obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearUsuario(rs);
                }
            }
        } catch (SQLException e) {
            throw new BaseDatosException("Error al buscar usuario por ID", e);
        }
        return null;
    }

    /**
     * Elimina físicamente un registro de la base de datos.
     */
    public boolean eliminar(Long id) throws BaseDatosException {
        String sql = "DELETE FROM usuarios WHERE id = ?";
        try (Connection conn = DatabaseConfig.getInstance().obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new BaseDatosException("No se puede eliminar el usuario.", e);
        }
    }

    /**
     * Método auxiliar privado para mapear el ResultSet al objeto Entidad.
     */
    private Usuario mapearUsuario(ResultSet rs) throws SQLException {
        Usuario u = new Usuario();
        u.setId(rs.getLong("id"));
        u.setUsuario(rs.getString("usuario"));
        u.setClave(rs.getString("clave"));
        u.setRol(rs.getString("rol"));
        u.setNombreCompleto(rs.getString("nombre_completo"));
        u.setCedula(rs.getString("cedula"));
        u.setEmail(rs.getString("email"));

        Timestamp ts = rs.getTimestamp("created_at");
        if (ts != null) {
            u.setFechaCreacion(ts.toLocalDateTime());
        }
        return u;
    }
}
