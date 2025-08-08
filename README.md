# PersonalHubApp

### Un Centro Personal de Gestión con Kotlin y Jetpack Compose

---

## Descripción del Proyecto 📱

**PersonalHubApp** es una aplicación móvil desarrollada con **Kotlin** y **Jetpack Compose** que funciona como un sistema personal de gestión de tareas, notas y organización. Diseñada bajo los principios de una interfaz de usuario intuitiva (UI) y una arquitectura modular, la aplicación sirve como un centro neurálgico para que los usuarios gestionen sus responsabilidades diarias, proyectos y objetivos personales de manera eficiente.

## Tecnologías Clave 🛠️

* **Lenguaje de Programación**: Kotlin
* **Framework de UI**: Jetpack Compose, utilizado para construir una interfaz de usuario moderna, reactiva y declarativa.
* **Gestión de Dependencias**: Gradle Kotlin DSL, para una configuración de proyecto robusta y tipo-segura.
* **Persistencia de Datos**: Almacenamiento local de datos mediante archivos JSON, optimizando el rendimiento y el acceso sin conexión.
* **Serialización**: Kotlinx Serialization, para manejar de forma eficiente la conversión de objetos Kotlin a JSON y viceversa.
* **Componentes Adicionales**:
    * **Compose-richtext**: Integrado para ofrecer capacidades de formato de texto enriquecido (Markdown) en las notas, permitiendo una organización más visual y estructurada.
    * **Material Icons Extended**: Biblioteca de iconos que enriquece la UI con una amplia gama de elementos visuales.
    * **Jetpack Navigation**: Para una navegación fluida y segura entre las diferentes secciones de la aplicación.
    * **Lifecycle-viewmodel-compose**: Asegura una gestión de estado robusta y un ciclo de vida consistente para la UI.

---

## Hoja de Ruta y Futuras Mejoras 🚀

El diseño actual de **PersonalHubApp** sienta las bases para una evolución continua del producto. Las siguientes funcionalidades y mejoras están planificadas para las próximas iteraciones, transformando la aplicación en una herramienta de gestión personal aún más completa:

* **Mejoras de Usabilidad y Funcionalidades**:
    * Expandir las capacidades de cada módulo existente (Salud, Estudio, Finanzas, Productividad) para ofrecer una mayor personalización y flexibilidad.
    * Optimizar y desarrollar nuevas características específicas en cada sección para mejorar la usabilidad y la experiencia del usuario, resolviendo puntos de fricción e identificando nuevas oportunidades de valor.

* **Almacenamiento en la Nube, Autenticación y Cifrado de Datos**:
    * Implementación de un robusto sistema de gestión de acceso con una base de datos **MongoDB**. Esto permitirá la autenticación segura de usuarios para el control total sobre quién puede acceder a la aplicación.
    * **Control de Acceso**: La aplicación verificará en la base de datos si el usuario existe y si tiene permisos activos antes de otorgar acceso. Este sistema está diseñado para revocar el acceso de forma remota si es necesario.
    * **Autenticación Biométrica y Desbloqueo de Datos**: Después de un inicio de sesión exitoso o la autenticación biométrica (huella dactilar), la aplicación descifrará los archivos JSON almacenados localmente. Los datos permanecerán cifrados cuando la sesión no esté activa, garantizando que solo el usuario autenticado tenga acceso a su información personal, incluso si el dispositivo cae en manos no autorizadas.
