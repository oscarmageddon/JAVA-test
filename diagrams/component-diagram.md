# Diagrama de Componentes - User Service

## Descripción General

Este diagrama muestra la arquitectura por capas del microservicio User Service, siguiendo los principios de separación de responsabilidades y arquitectura hexagonal.

## Componentes Principales

### 1. Presentation Layer (Capa de Presentación)
```
┌─────────────────────────────────────┐
│           UserController            │
│  ┌─────────────────────────────────┐│
│  │        REST Endpoints           ││
│  │  • POST /api/sign-up            ││
│  │  • GET /api/login               ││
│  └─────────────────────────────────┘│
└─────────────────────────────────────┘
                  │
                  │ HTTP Requests/Responses
                  ▼
```

### 2. Business Layer (Capa de Negocio)
```
┌─────────────────────────────────────┐
│            UserService              │
│  ┌─────────────────────────────────┐│
│  │       Business Logic            ││
│  │  • createUser()                 ││
│  │  • loginUser()                  ││
│  │  • validateUser()               ││
│  └─────────────────────────────────┘│
└─────────────────────────────────────┘
                  │
                  │ Service Calls
                  ▼
```

### 3. Data Access Layer (Capa de Acceso a Datos)
```
┌─────────────────────────────────────┐
│          UserRepository             │
│  ┌─────────────────────────────────┐│
│  │        JPA Repository           ││
│  │  • findByEmail()                ││
│  │  • existsByEmail()              ││
│  │  • save()                       ││
│  └─────────────────────────────────┘│
└─────────────────────────────────────┘
                  │
                  │ JPA/Hibernate
                  ▼
```

### 4. Database Layer (Capa de Base de Datos)
```
┌─────────────────────────────────────┐
│            H2 Database              │
│  ┌─────────────────────────────────┐│
│  │           Tables                ││
│  │  • users                        ││
│  │  • phones                       ││
│  └─────────────────────────────────┘│
└─────────────────────────────────────┘
```

### 5. Cross-Cutting Concerns (Aspectos Transversales)

#### Security Component
```
┌─────────────────────────────────────┐
│          SecurityConfig             │
│  ┌─────────────────────────────────┐│
│  │      Spring Security            ││
│  │  • Authentication               ││
│  │  • Authorization                ││
│  │  • Password Encoding            ││
│  └─────────────────────────────────┘│
└─────────────────────────────────────┘
```

#### JWT Utility Component
```
┌─────────────────────────────────────┐
│              JwtUtil                │
│  ┌─────────────────────────────────┐│
│  │         JWT Operations          ││
│  │  • generateToken()              ││
│  │  • validateToken()              ││
│  │  • extractClaims()              ││
│  └─────────────────────────────────┘│
└─────────────────────────────────────┘
```

#### Validation Component
```
┌─────────────────────────────────────┐
│           Validators                │
│  ┌─────────────────────────────────┐│
│  │      Custom Validators          ││
│  │  • EmailValidator               ││
│  │  • PasswordValidator            ││
│  └─────────────────────────────────┘│
└─────────────────────────────────────┘
```

#### Exception Handling Component
```
┌─────────────────────────────────────┐
│      GlobalExceptionHandler        │
│  ┌─────────────────────────────────┐│
│  │      Error Management           ││
│  │  • UserAlreadyExistsException   ││
│  │  • UserNotFoundException        ││
│  │  • ValidationException          ││
│  └─────────────────────────────────┘│
└─────────────────────────────────────┘
```

### 6. Data Transfer Objects (DTOs)
```
┌─────────────────────────────────────┐
│               DTOs                  │
│  ┌─────────────────────────────────┐│
│  │      Request/Response           ││
│  │  • UserSignUpRequest            ││
│  │  • UserResponse                 ││
│  │  • PhoneDto                     ││
│  │  • ErrorResponse                ││
│  └─────────────────────────────────┘│
└─────────────────────────────────────┘
```

### 7. Domain Entities
```
┌─────────────────────────────────────┐
│             Entities                │
│  ┌─────────────────────────────────┐│
│  │        JPA Entities             ││
│  │  • User                         ││
│  │  • Phone                        ││
│  └─────────────────────────────────┘│
└─────────────────────────────────────┘
```

## Flujo de Dependencias

```
HTTP Client
     │
     ▼
UserController ──────┐
     │               │
     ▼               │
UserService ─────────┤
     │               │
     ▼               ▼
UserRepository   JwtUtil
     │               │
     ▼               │
H2 Database          │
                     │
┌────────────────────┼────────────────────┐
│   Cross-Cutting    │                    │
│   Components       ▼                    │
│                                         │
│  SecurityConfig ◄─── PasswordEncoder    │
│  Validators                             │
│  ExceptionHandler                       │
└─────────────────────────────────────────┘
```

## Relaciones entre Componentes

- **Controller → Service**: Inyección de dependencia
- **Service → Repository**: Inyección de dependencia  
- **Service → JwtUtil**: Inyección de dependencia
- **Service → PasswordEncoder**: Inyección de dependencia
- **Controller → Validators**: A través de Bean Validation
- **All Layers → ExceptionHandler**: Manejo global de excepciones
- **Repository → Database**: JPA/Hibernate ORM
- **SecurityConfig → All Endpoints**: Intercepta peticiones HTTP

## Principios de Diseño Aplicados

1. **Separation of Concerns**: Cada capa tiene una responsabilidad específica
2. **Dependency Inversion**: Las capas superiores no dependen de implementaciones concretas
3. **Single Responsibility**: Cada componente tiene una única razón para cambiar
4. **Open/Closed**: Abierto para extensión, cerrado para modificación
5. **Loose Coupling**: Bajo acoplamiento entre componentes
6. **High Cohesion**: Alta cohesión dentro de cada componente

## Tecnologías por Componente

- **Presentation**: Spring Web MVC, Jackson
- **Business**: Spring Core, Bean Validation
- **Data Access**: Spring Data JPA, Hibernate
- **Database**: H2 Database
- **Security**: Spring Security, JWT
- **Testing**: JUnit 5, Mockito, Spring Test