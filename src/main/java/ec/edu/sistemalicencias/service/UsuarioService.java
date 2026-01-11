package ec.edu.sistemalicencias.service;

import ec.edu.sistemalicencias.config.SesionUsuario;
import ec.edu.sistemalicencias.dao.UsuarioDAO;
import ec.edu.sistemalicencias.model.entities.Usuario;
import ec.edu.sistemalicencias.model.exceptions.BaseDatosException;
import ec.edu.sistemalicencias.model.exceptions.DocumentoInvalidoException;
import ec.edu.sistemalicencias.model.interfaces.RolConstantes;

import java.time.LocalDate;
import java.util.List;

public class UsuarioService {

    private final UsuarioDAO usuarioDAO;

    public UsuarioService() {
        this.usuarioDAO = new UsuarioDAO();
    }

    public boolean autenticar(String usuario, String clave) throws BaseDatosException {
        // Limpiamos espacios para evitar errores de digitación
        Usuario u = usuarioDAO.login(usuario.trim(), clave.trim());
        if (u != null) {
            SesionUsuario.getInstancia().iniciarSesion(u);
            return true;
        }
        return false;
    }

    public void registrarAnalista(Usuario nuevoUsuario) throws BaseDatosException, DocumentoInvalidoException, SecurityException {
        // Verificamos permisos del usuario en sesión
        validarPermisosAdmin();

        if (nuevoUsuario == null) {
            throw new IllegalArgumentException("Los datos del usuario no pueden estar vacíos.");
        }

        // Forzar rol de analista y asegurar limpieza de datos
        nuevoUsuario.setRol(RolConstantes.ANALISTA);

        // Ejecutar validaciones internas de la entidad (Cédula, email, etc)
        nuevoUsuario.validar();

        usuarioDAO.crear(nuevoUsuario);
    }

    public List<Usuario> obtenerReporteAnalistas(LocalDate inicio, LocalDate fin) throws BaseDatosException, SecurityException {
        validarPermisosAdmin();
        return usuarioDAO.listarPorFechas(inicio, fin);
    }

    /**
     * Método privado para reutilizar la lógica de seguridad en varios métodos.
     */
    private void validarPermisosAdmin() throws SecurityException {
        Usuario usuarioActual = SesionUsuario.getInstancia().getUsuarioLogueado();

        // Comprobación de seguridad robusta
        if (usuarioActual == null ||
                usuarioActual.getRol() == null ||
                !usuarioActual.getRol().trim().equalsIgnoreCase(RolConstantes.ADMIN)) {

            throw new SecurityException("¡Acceso Denegado! Se requieren privilegios de Administrador.");
        }
    }

    public void cerrarSesion() {
        SesionUsuario.getInstancia().cerrarSesion();
    }
}