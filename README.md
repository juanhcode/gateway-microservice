# Microservicio de Gateway

## Descripción General

Este documento proporciona una introducción al Microservicio Gateway, que sirve como el punto de entrada central para todas las solicitudes de clientes en nuestra arquitectura de microservicios. Presenta la arquitectura fundamental, los componentes clave y las funcionalidades principales del servicio gateway.

Para obtener información detallada sobre la configuración de rutas, consulta **Routing Configuration**. Para documentación más profunda sobre los patrones de resiliencia, consulta **Resilience Patterns**.

## Propósito y Función

El Microservicio Gateway es una implementación de Spring Cloud Gateway que actúa como puerta de enlace API para nuestro ecosistema de microservicios. Proporciona un único punto de entrada para que las aplicaciones cliente interactúen con nuestros servicios backend, manejando preocupaciones transversales como:

- Enrutamiento de solicitudes a los servicios backend apropiados
- Autenticación y autorización
- Mecanismos de ruptura de circuito y fallback
- Lógica de reintento de solicitudes
- Registro y monitoreo

### Archivos fuente relevantes

- `src/main/java/com/develop/gateway_microservice/GatewayMicroserviceApplication.java`
- `src/main/resources/application.yml`
- `pom.xml`

## Arquitectura del Sistema

El Microservicio Gateway se ubica en el centro de nuestro sistema distribuido, mediando toda la comunicación entre los clientes externos y los microservicios internos.

### Arquitectura de Alto Nivel

**Componentes del Gateway**:

- Clientes Externos
- Gateway Microservice (:8080)
- User Service (:8081)
- Auth Service (:8082)
- Notifications Service (:8083)

**Módulos internos**:

- Configuración de rutas
- Filtro de autenticación
- Circuit breakers
- Mecanismo de reintento
- Controladores fallback

## Componentes Clave

### Sistema de Enrutamiento

El gateway enruta solicitudes entrantes a los servicios backend apropiados basándose en predicados de ruta:

| ID Ruta | URI Base | Predicado de Ruta | Notas |
|---------|----------|-------------------|-------|
| user-service | http://user-microservice:8081 | /users/** | Requiere autenticación y tiene reintento |
| auth-service | http://auth-service:8082 | /auth/** | Público, no requiere autenticación |
| notifications-service | http://notifications-service:8083 | /notifications/** | Protegido por circuito |

### Mecanismos de Resiliencia

El Gateway implementa varios patrones de resiliencia para garantizar estabilidad:

- Circuit breakers para las rutas: /users/**, /auth/** y /notifications/**
- Lógica de reintento (hasta 2 intentos) para User Service
- Mecanismos fallback para cada servicio cuando el circuito está abierto

**Configuración de Circuit Breakers**:

| Parámetro | Valor | Descripción |
|-----------|-------|-------------|
| slidingWindowSize | 10 | Número de llamadas consideradas para tasa de error |
| minimumNumberOfCalls | 5 | Mínimo de llamadas antes de calcular tasa de error |
| failureRateThreshold | 50 | Porcentaje de fallos para abrir circuito |
| waitDurationInOpenState | 10s | Tiempo de espera antes de estado semiabierto |
| permittedNumberOfCallsInHalfOpenState | 3 | Llamadas de prueba en semiabierto |

## Stack Tecnológico

El Gateway está construido con las siguientes tecnologías:

- Spring Boot 3.4.3
- Spring Cloud 2024.0.0
- Java 17
- Spring Cloud Gateway
- Resilience4j
- JWT Authentication (jjwt 0.12.6)
- WebFlux (Programación Reactiva)
- Spring Boot Actuator

## Funcionalidades Clave

### Enrutamiento Centralizado

El gateway enruta solicitudes basadas en los patrones de URL definidos en `application.yml`.

### Seguridad

Se implementa autenticación JWT para rutas protegidas como `/users/**`, garantizando acceso solo a usuarios autenticados.

### Patrones de Resiliencia

- Circuit Breakers: Previenen fallos en cascada
- Retry: Reintentos automáticos para fallos transitorios
- Fallback: Rutas alternativas cuando un servicio está inactivo

### Monitoreo y Observabilidad

- Configuración avanzada de logs:
```yaml
logging:
  level:
    org.springframework.cloud.gateway: DEBUG
    org.springframework.web: INFO
    com.tu.paquete.gateway: WARN
```
- Spring Boot Actuator habilitado

## Flujo de Solicitudes en el Gateway

```plaintext
Client Application --> Gateway Microservice --> [Auth/User/Notifications Service]

Alternativas:
- Autenticación exitosa / fallida
- Servicio disponible / no disponible
- Circuit breaker activado -> fallback
```

## Documentación Relacionada

- Arquitectura del Sistema
- Configuración de Rutas
- Patrones de Resiliencia
- Implementación de Circuit Breakers
- Servicios Fallback
- Lógica de Reintento
- Seguridad
- Autenticación JWT
