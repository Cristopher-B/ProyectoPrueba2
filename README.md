# Sistema de Gestión de Licencias – Módulo de Inicio de Sesión
Este repositorio contiene el módulo de inicio de sesión del Sistema de Gestión de Licencias, desarrollado en Java 21. 
El sistema implementa persistencia en la nube mediante Supabase (PostgreSQL) y sigue una arquitectura en capas orientada a la escalabilidad, seguridad y mantenimiento.

## Arquitectura del Sistema
El proyecto utiliza una arquitectura en capas diseñada para garantizar escalabilidad, mantenibilidad y seguridad:

- **Capa de Datos (DAO):**  
  Gestión de la persistencia utilizando JDBC y PostgreSQL.

- **Capa de Servicio:**  
  Implementación de la lógica de negocio, incluyendo validaciones de edad, verificación de pruebas psicométricas y control de seguridad.

- **Capa de Vista:**  
  Interfaces gráficas desarrolladas con Java Swing.

- **Configuración:**  
  Implementación del patrón Singleton para la gestión de conexiones a la base de datos y el manejo de variables de entorno.

## Configuración de Base de Datos (Supabase)

El sistema se conecta a una base de datos PostgreSQL en la nube mediante Supabase, utilizando el puerto **6543 (Transaction Pooler)** para optimizar el manejo de conexiones en aplicaciones Java.

### Requisitos de Conexión

- **Host:** aws-1-us-east-1.pooler.supabase.com  
- **Puerto:** 6543  
- **Driver:** org.postgresql.Driver  
- **Modo SSL:** Requerido (?sslmode=require)

Variables de Entorno
Para evitar la exposición de credenciales en el código fuente, el sistema utiliza las siguientes variables:
1. DB_URL: URL de conexión JDBC.
2. DB_USER: Identificador del proyecto de Supabase.
3. DB_PASSWORD: Contraseña de la base de datos.

Lógica de Negocio
Se han implementado las siguientes reglas:
1. El acceso a la gestión de usuarios está restringido exclusivamente a roles de Administrador mediante el método validarPermisosAdmin().
2. El rol Administrador accede solamente al sistema de Gestión de Usuarios que permite la creación, visualización y eliminación de usuarios; también puede generar reportes en PDF individuales o en su totalidad de usuarios.
3. El rol de Analista accede directamente al sistema de Gestión de Licencias.
