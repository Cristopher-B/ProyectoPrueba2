package ec.edu.sistemalicencias.config;

import ec.edu.sistemalicencias.model.entities.Usuario;

/**
 * Singleton para manejar la sesión del usuario actual en memoria.
 */
public class SesionUsuario {
    private static SesionUsuario instancia;
    private Usuario usuarioLogueado;

    private SesionUsuario() {}

    public static SesionUsuario getInstancia() {
        if (instancia == null) {
            instancia = new SesionUsuario();
        }
        return instancia;
    }

    public void iniciarSesion(Usuario usuario) {
        this.usuarioLogueado = usuario;
    }

    public void cerrarSesion() {
        this.usuarioLogueado = null;
    }

    public Usuario getUsuarioLogueado() {
        return usuarioLogueado;
    }

    public boolean haySesionActiva() {
        return usuarioLogueado != null;
    }
}