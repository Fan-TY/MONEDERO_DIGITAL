Este proyecto sigue el patrón MVC (Modelo-Vista-Controlador), implementado con las convenciones estándar de Spring Boot.
A diferencia del MVC académico de 3 capas, Spring Boot organiza el Modelo en sub-capas especializadas para separar mejor las responsabilidades,
una práctica estándar en el desarrollo profesional con este framework.

VISTA (V)    
templates/  →  HTML + Thymeleaf}

CONTROLADOR (C)   
controller/  →  Recibe y responde peticiones HTTP

MODELO (M)
service/     → Lógica de negocio y reglas del sistema    
pattern/     → Patrones de diseño (Singleton, Factory, Prototype, State)                          
repository/  → Acceso a datos vía Spring Data JPA         
model/       → Entidades JPA (Usuario, Cuenta)  

BASE DE DATOS (BD)                          
MySQL → Tablas + Stored Procedure
