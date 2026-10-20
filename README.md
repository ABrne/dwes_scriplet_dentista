# Proyecto Clínica Dental

Aplicación web para gestionar pacientes y citas odontológicas. Está implementada con el patrón **Modelo-Vista-Controlador (MVC)**, Jakarta Servlets, JSP/JSTL y JDBC sobre MySQL.

## Arquitectura

- **Modelo:** `Paciente`, `PacienteOpcion`, `Cita` y `Odontologo` representan los datos que se muestran y procesan.
- **Acceso a datos:** `PacienteDAO`, `CitaDAO` y `OdontologoDAO` contienen las consultas SQL. `ConexionBD` centraliza la conexión JDBC.
- **Controladores:** `PacienteServlet` (`/pacientes`) y `CitaServlet` (`/citas`) reciben las peticiones, validan parámetros, llaman a los DAO y reenvían o redirigen a las vistas.
- **Vistas:** JSP con JSTL y Expression Language, sin scriptlets, guardadas bajo `WEB-INF/views`. Al estar dentro de `WEB-INF`, no se pueden solicitar directamente desde el navegador.

En la raíz pública de la aplicación solo están `index.jsp` y `estilos.css`. La página de inicio enlaza a las rutas de los controladores.

## Estructura

```
src/main/
├── java/es/dwes/dentista/
│   ├── dao/
│   │   ├── ConexionBD.java
│   │   ├── PacienteDAO.java
│   │   ├── CitaDAO.java
│   │   └── OdontologoDAO.java
│   ├── model/
│   │   ├── Paciente.java
│   │   ├── PacienteOpcion.java
│   │   ├── Cita.java
│   │   └── Odontologo.java
│   └── web/
│       ├── PacienteServlet.java
│       └── CitaServlet.java
├── resources/dentista.sql
└── webapp/
    ├── index.jsp
    ├── estilos.css
    └── WEB-INF/views/
        ├── pacientes.jsp
        ├── formulario-paciente.jsp
        ├── citas.jsp
        └── formulario-cita.jsp
```

## Rutas y operaciones

Las rutas se muestran relativas al contexto de la aplicación, que por defecto es `/scriplet_dentista`.

| Método y ruta | Operación |
|---|---|
| `GET /pacientes` | Lista pacientes y el número de citas de cada uno. |
| `GET /pacientes?nuevo=1` | Muestra el formulario de alta de paciente. |
| `POST /pacientes` con `accion=guardar` | Valida e inserta un paciente. Después redirige al listado. |
| `POST /pacientes` con `accion=borrar` | Elimina el paciente indicado por `id_paciente`. La base de datos elimina sus citas asociadas. |
| `GET /citas` | Lista las citas de hoy y futuras. |
| `GET /citas?todas=1` | Muestra el histórico completo de citas. |
| `GET /citas?nuevo=1` | Muestra el formulario de cita con selectores de paciente y odontólogo. Admite `id_paciente` para preseleccionar un paciente. |
| `POST /citas` con `accion=guardar` | Valida e inserta una cita. Después redirige al listado de próximas citas. |
| `POST /citas` con `accion=borrar` | Elimina la cita indicada por `id_cita`; conserva el filtro histórico si se envió `todas=1`. |

Los formularios usan campos ocultos como `accion`, `id_paciente`, `id_cita` y `todas` para enviar información junto con el `POST`. Un campo oculto no es secreto ni confiable: el usuario puede alterarlo, por eso los servlets validan el tipo y el valor de los parámetros antes de operar. Las inserciones y eliminaciones usan `PreparedStatement`; las claves foráneas de la base de datos también protegen las relaciones.

## Base de datos

El script `src/main/resources/dentista.sql` crea la base `dentista` y las tablas:

| Tabla | Datos principales |
|---|---|
| `paciente` | Datos personales y de contacto; DNI único. |
| `odontologos` | Nombre y especialidad del profesional. |
| `cita` | Fecha, hora, motivo y claves foráneas de paciente y odontólogo. |

La clave foránea de `cita.id_paciente` usa `ON DELETE CASCADE`: al borrar un paciente se borran también sus citas. La cita almacena `id_odontologo`, relacionado con la tabla `odontologos`.

Desde la carpeta del proyecto, importa la base con:

```bash
mysql -u user -puser < src/main/resources/dentista.sql
```

Las fechas de las citas de ejemplo se calculan con `DATE_ADD(CURDATE(), INTERVAL n DAY)`, así que el script genera citas futuras y pasadas respecto al día de importación.

La conexión está centralizada en `ConexionBD.java`. Los valores iniciales son `localhost:3306`, base de datos `dentista`, usuario `user` y contraseña `user`. Para usar otros valores, se modifican las constantes de esa clase.

## Requisitos

- Java 21.
- Maven 3.8 o superior.
- MySQL Server accesible con los valores configurados en `ConexionBD`.
- Apache Tomcat 10.1.x o 11.x.

La aplicación usa el namespace `jakarta.*` y la API Jakarta Servlet 5.0, proporcionada por el contenedor. Las vistas usan Jakarta Tags (JSTL) 3.0, incluido en el WAR. No es compatible directamente con Tomcat 9, que usa el antiguo namespace `javax.*`.

## Compilación y despliegue

Desde la carpeta que contiene `pom.xml`, ejecuta:

```bash
mvn clean package
```

Maven genera `target/scriplet_dentista.war`. Copia ese archivo a la carpeta `webapps` de Tomcat 10.1/11 e inicia o reinicia el servidor. El nombre del WAR determina el contexto:

```text
http://localhost:8080/scriplet_dentista/
```

Para entregar el proyecto comprimido, incluye el código fuente y excluye las carpetas generadas `target/` y `build/`.
