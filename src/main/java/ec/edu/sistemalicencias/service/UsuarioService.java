package ec.edu.sistemalicencias.service;

import ec.edu.sistemalicencias.config.SesionUsuario;
import ec.edu.sistemalicencias.dao.UsuarioDAO;
import ec.edu.sistemalicencias.model.entities.Usuario;
import ec.edu.sistemalicencias.model.exceptions.BaseDatosException;
import ec.edu.sistemalicencias.model.interfaces.RolConstantes;

import java.time.LocalDate;
import java.util.List;

public class UsuarioService {

    private final UsuarioDAO usuarioDAO;

    public UsuarioService() {
        this.usuarioDAO = new UsuarioDAO();
    }

    /**
     * Autenticación de usuario.
     */
    public boolean autenticar(String usuario, String clave) throws BaseDatosException {
        if (usuario == null || clave == null) return false;

        // El DAO ahora usa login compatible con PostgreSQL
        Usuario u = usuarioDAO.login(usuario.trim(), clave.trim());
        if (u != null) {
            SesionUsuario.getInstancia().iniciarSesion(u);
            return true;
        }
        return false;
    }

    /**
     * Registra un nuevo analista (Solo Admin).
     */
    public void registrarAnalista(Usuario nuevoUsuario) throws BaseDatosException {
        validarPermisosAdmin();

        if (nuevoUsuario == null) {
            throw new IllegalArgumentException("Los datos del usuario no pueden estar vacíos.");
        }

        nuevoUsuario.setRol(RolConstantes.ANALISTA);
        // En Supabase, el ID es SERIAL, no lo enviamos
        usuarioDAO.insertar(nuevoUsuario);
    }

    /**
     * Elimina un usuario por ID (Solo Admin).
     */
    public void eliminarUsuario(Long id) throws BaseDatosException {
        validarPermisosAdmin();
        if (id == null) throw new IllegalArgumentException("ID inválido");
        usuarioDAO.eliminar(id);
    }

    /**
     * Busca un usuario por ID para reportes individuales.
     */
    public Usuario obtenerPorId(Long id) throws BaseDatosException {
        if (id == null) return null;
        return usuarioDAO.buscarPorId(id);
    }

    /**
     * Lista todos los analistas para la JTable de la vista.
     */
    public List<Usuario> obtenerTodosLosAnalistas() throws BaseDatosException {
        validarPermisosAdmin();
        // Pasamos null para que el DAO ignore el filtro de fechas
        return usuarioDAO.listarPorFechas(null, null);
    }

    /**
     * Obtiene lista filtrada para reportes PDF.
     */
    public List<Usuario> obtenerReporteAnalistas(LocalDate inicio, LocalDate fin) throws BaseDatosException {
        validarPermisosAdmin();
        return usuarioDAO.listarPorFechas(inicio, fin);
    }

    /**
     * Validación de seguridad centralizada.
     */
    private void validarPermisosAdmin() throws SecurityException {
        Usuario usuarioActual = SesionUsuario.getInstancia().getUsuarioLogueado();

        if (usuarioActual == null ||
                usuarioActual.getRol() == null ||
                !usuarioActual.getRol().equalsIgnoreCase(RolConstantes.ADMIN)) {

            throw new SecurityException("Acceso Denegado: Se requieren privilegios de Administrador.");
        }
    }

    public void cerrarSesion() {
        SesionUsuario.getInstancia().cerrarSesion();
    }
}