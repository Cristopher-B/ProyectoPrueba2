package ec.edu.sistemalicencias.config;

import ec.edu.sistemalicencias.model.exceptions.BaseDatosException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase de configuración para la conexión a la base de datos PostgreSQL (Supabase).
 * Implementa el patrón Singleton para gestionar una única instancia de configuración.
 */
public class DatabaseConfig {

    // Instancia única (Singleton)
    private static DatabaseConfig instancia;

    // Parámetros de conexión
    private final String url;
    private final String usuario;
    private final String password;
    private final String driver;

    /**
     * Constructor privado para implementar Singleton
     */
    private DatabaseConfig() {
        // 1. CAMBIO: Inicializar el driver explícitamente antes de usarlo
        this.driver = "org.postgresql.Driver";

        // 2. PARÁMETROS: Usando tus credenciales de Supabase
        this.url = "jdbc:postgresql://aws-1-us-east-1.pooler.supabase.com:6543/postgres?sslmode=require";
        this.usuario = "postgres.jpcjuctexasnykagmxfp";
        this.password = "*VB2QR#Y+t$u/F3";

        try {
            // Cargar el driver JDBC de PostgreSQL
            Class.forName(this.driver);
        } catch (ClassNotFoundException e) {
            // CAMBIO: Mensaje corregido a PostgreSQL
            System.err.println("Error al cargar el driver PostgreSQL: " + e.getMessage());
        }
    }

    /**
     * Obtiene la instancia única de DatabaseConfig (Singleton)
     */
    public static synchronized DatabaseConfig getInstance() {
        if (instancia == null) {
            instancia = new DatabaseConfig();
        }
        return instancia;
    }

    /**
     * Crea y retorna una conexión a la base de datos
     */
    public Connection obtenerConexion() throws BaseDatosException {
        try {
            return DriverManager.getConnection(url, usuario, password);
        } catch (SQLException e) {
            throw new BaseDatosException(
                    "Error al conectar con Supabase: " + e.getMessage(),
                    e
            );
        }
    }

    /**
     * Cierra una conexión de forma segura
     */
    public void cerrarConexion(Connection conexion) {
        if (conexion != null) {
            try {
                if (!conexion.isClosed()) {
                    conexion.close();
                }
            } catch (SQLException e) {
                System.err.println("Error al cerrar la conexión: " + e.getMessage());
            }
        }
    }

    /**
     * Verifica si la conexión a la base de datos es válida
     */
    public boolean verificarConexion() {
        try (Connection conn = obtenerConexion()) {
            return conn != null && !conn.isClosed();
        } catch (Exception e) {
            System.err.println("Error al verificar conexión: " + e.getMessage());
            return false;
        }
    }

    // Getters necesarios
    public String getUrl() { return url; }
    public String getUsuario() { return usuario; }
    public String getDriver() { return driver; }
}