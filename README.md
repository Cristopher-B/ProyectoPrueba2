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

## Variables de Entorno

Para evitar la exposición de credenciales sensibles en el código fuente, el sistema utiliza las siguientes variables de entorno:

- **DB_URL:** URL de conexión JDBC a la base de datos.
- **DB_USER:** Identificador del proyecto o usuario de Supabase.
- **DB_PASSWORD:** Contraseña de la base de datos.

## Lógica de Negocio

El sistema implementa las siguientes reglas de negocio:

- El acceso a la gestión de usuarios está restringido exclusivamente a usuarios con rol **Administrador**, validado mediante el método `validarPermisosAdmin()`.

- El rol **Administrador** tiene acceso únicamente al sistema de Gestión de Usuarios, el cual permite:
  - Crear, visualizar y eliminar usuarios.
  - Generar reportes en PDF, tanto individuales como generales.

- El rol **Analista** accede directamente al sistema de Gestión de Licencias.

