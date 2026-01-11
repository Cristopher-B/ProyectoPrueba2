package ec.edu.sistemalicencias.model.entities;

import ec.edu.sistemalicencias.model.exceptions.DocumentoInvalidoException;
import ec.edu.sistemalicencias.model.interfaces.Validable;
import java.time.LocalDateTime;

public class Usuario implements Validable {
    private Long id;
    private String usuario;
    private String clave;
    private String rol;
    private String nombreCompleto;
    private String cedula;
    private String email;
    // Se inicializa para evitar NullPointerException en el PDF si el DAO no lo llena
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    public Usuario() {}

    public Usuario(String usuario, String clave, String rol, String nombreCompleto, String cedula, String email) {
        this.usuario = usuario;
        this.clave = clave;
        this.rol = rol;
        this.nombreCompleto = nombreCompleto;
        this.cedula = cedula;
        this.email = email;
        this.fechaCreacion = LocalDateTime.now();
    }

    @Override
    public boolean validar() throws DocumentoInvalidoException {
        if (usuario == null || usuario.trim().isEmpty()) throw new DocumentoInvalidoException("El usuario es obligatorio.");
        if (clave == null || clave.trim().isEmpty()) throw new DocumentoInvalidoException("La clave es obligatoria.");
        if (rol == null || rol.trim().isEmpty()) throw new DocumentoInvalidoException("El rol es obligatorio.");
        if (nombreCompleto == null || nombreCompleto.trim().isEmpty()) throw new DocumentoInvalidoException("El nombre es obligatorio.");
        // Validación de cédula ecuatoriana básica
        if (cedula == null || cedula.trim().length() != 10) throw new DocumentoInvalidoException("La cédula debe tener 10 dígitos.");
        return true;
    }

    @Override
    public String obtenerMensajeValidacion() {
        return "Usuario validado correctamente";
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }
    public String getClave() { return clave; }
    public void setClave(String clave) { this.clave = clave; }
    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }
    public String getCedula() { return cedula; }
    public void setCedula(String cedula) { this.cedula = cedula; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    /**
     * Devuelve la fecha de creación.
     * Se agrega una validación extra para que nunca retorne null al PDF.
     */
    public LocalDateTime getFechaCreacion() {
        return (fechaCreacion == null) ? LocalDateTime.now() : fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}