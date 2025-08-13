# Diagrama de Secuencia - User Service

## Secuencia 1: Registro de Usuario (Sign-Up)

```
Cliente          Controller       Service         Repository      JwtUtil         PasswordEncoder
  │                 │               │               │               │                    │
  │ POST /sign-up   │               │               │               │                    │
  │────────────────►│               │               │               │                    │
  │                 │               │               │               │                    │
  │                 │ @Valid        │               │               │                    │
  │                 │ validate()    │               │               │                    │
  │                 │◄──────────────│               │               │                    │
  │                 │               │               │               │                    │
  │                 │ createUser()  │               │               │                    │
  │                 │──────────────►│               │               │                    │
  │                 │               │               │               │                    │
  │                 │               │existsByEmail()│               │                    │
  │                 │               │──────────────►│               │                    │
  │                 │               │     false     │               │                    │
  │                 │               │◄──────────────│               │                    │
  │                 │               │               │               │                    │
  │                 │               │               │               │ encode(password)   │
  │                 │               │──────────────────────────────────────────────────►│
  │                 │               │               │               │  encoded_password  │
  │                 │               │◄──────────────────────────────────────────────────│
  │                 │               │               │               │                    │
  │                 │               │               │               │generateToken(email)│
  │                 │               │──────────────────────────────►│                    │
  │                 │               │               │               │     jwt_token      │
  │                 │               │◄──────────────────────────────│                    │
  │                 │               │               │               │                    │
  │                 │               │   save(user)  │               │                    │
  │                 │               │──────────────►│               │                    │
  │                 │               │  saved_user   │               │                    │
  │                 │               │◄──────────────│               │                    │
  │                 │               │               │               │                    │
  │                 │ UserResponse  │               │               │                    │
  │                 │◄──────────────│               │               │                    │
  │                 │               │               │               │                    │
  │ 201 Created     │               │               │               │                    │
  │◄────────────────│               │               │               │                    │
  │ UserResponse    │               │               │               │                    │
```

### Flujo Alternativo: Usuario Ya Existe

```
Cliente          Controller       Service         Repository
  │                 │               │               │
  │ POST /sign-up   │               │               │
  │────────────────►│               │               │
  │                 │ createUser()  │               │
  │                 │──────────────►│               │
  │                 │               │existsByEmail()│
  │                 │               │──────────────►│
  │                 │               │     true      │
  │                 │               │◄──────────────│
  │                 │               │               │
  │                 │ UserAlreadyExistsException    │
  │                 │◄──────────────│               │
  │                 │               │               │
  │ 409 Conflict    │               │               │
  │◄────────────────│               │               │
  │ ErrorResponse   │               │               │
```

## Secuencia 2: Login de Usuario

```
Cliente          Controller       Service         Repository      JwtUtil
  │                 │               │               │               │
  │ GET /login      │               │               │               │
  │ Bearer token    │               │               │               │
  │────────────────►│               │               │               │
  │                 │               │               │               │
  │                 │ loginUser()   │               │               │
  │                 │──────────────►│               │               │
  │                 │               │               │               │
  │                 │               │               │               │extractUsername(token)
  │                 │               │──────────────────────────────►│
  │                 │               │               │               │     username
  │                 │               │◄──────────────────────────────│
  │                 │               │               │               │
  │                 │               │               │               │validateToken(token, username)
  │                 │               │──────────────────────────────►│
  │                 │               │               │               │      true
  │                 │               │◄──────────────────────────────│
  │                 │               │               │               │
  │                 │               │findByEmail()  │               │
  │                 │               │──────────────►│               │
  │                 │               │     user      │               │
  │                 │               │◄──────────────│               │
  │                 │               │               │               │
  │                 │               │               │               │generateToken(email)
  │                 │               │──────────────────────────────►│
  │                 │               │               │               │   new_token
  │                 │               │◄──────────────────────────────│
  │                 │               │               │               │
  │                 │               │   save(user)  │               │
  │                 │               │──────────────►│               │
  │                 │               │ updated_user  │               │
  │                 │               │◄──────────────│               │
  │                 │               │               │               │
  │                 │ UserResponse  │               │               │
  │                 │◄──────────────│               │               │
  │                 │               │               │               │
  │ 200 OK          │               │               │               │
  │◄────────────────│               │               │               │
  │ UserResponse    │               │               │               │
```

### Flujo Alternativo: Token Inválido

```
Cliente          Controller       Service         JwtUtil
  │                 │               │               │
  │ GET /login      │               │               │
  │ Bearer token    │               │               │
  │────────────────►│               │               │
  │                 │ loginUser()   │               │
  │                 │──────────────►│               │
  │                 │               │               │
  │                 │               │               │extractUsername(token)
  │                 │               │──────────────►│
  │                 │               │               │   username
  │                 │               │◄──────────────│
  │                 │               │               │
  │                 │               │               │validateToken(token, username)
  │                 │               │──────────────►│
  │                 │               │               │     false
  │                 │               │◄──────────────│
  │                 │               │               │
  │                 │ UserNotFoundException         │
  │                 │◄──────────────│               │
  │                 │               │               │
  │ 404 Not Found   │               │               │
  │◄────────────────│               │               │
  │ ErrorResponse   │               │               │
```

## Secuencia 3: Validación de Entrada (Bean Validation)

```
Cliente          Controller    Validators         Service
  │                 │               │               │
  │ POST /sign-up   │               │               │
  │ (invalid data)  │               │               │
  │────────────────►│               │               │
  │                 │               │               │
  │                 │ @Valid        │               │
  │                 │──────────────►│               │
  │                 │               │               │
  │                 │               │EmailValidator │
  │                 │               │.isValid()     │
  │                 │               │──────────────►│
  │                 │               │    false      │
  │                 │               │◄──────────────│
  │                 │               │               │
  │                 │               │PasswordValidator
  │                 │               │.isValid()     │
  │                 │               │──────────────►│
  │                 │               │    false      │
  │                 │               │◄──────────────│
  │                 │               │               │
  │                 │ ValidationErrors              │
  │                 │◄──────────────│               │
  │                 │               │               │
  │ 400 Bad Request │               │               │
  │◄────────────────│               │               │
  │ ErrorResponse   │               │               │
  │ (validation errors)             │               │
```

## Secuencia 4: Manejo Global de Excepciones

```
Controller       Service         ExceptionHandler      Cliente
  │                 │                    │                │
  │ createUser()    │                    │                │
  │────────────────►│                    │                │
  │                 │                    │                │
  │                 │ throws             │                │
  │                 │ UserAlreadyExistsException         │
  │◄────────────────│                    │                │
  │                 │                    │                │
  │ Exception thrown│                    │                │
  │─────────────────────────────────────►│                │
  │                 │                    │                │
  │                 │                    │handleUserAlreadyExists()
  │                 │                    │───────────────►│
  │                 │                    │                │
  │                 │                    │ create         │
  │                 │                    │ ErrorResponse  │
  │                 │                    │───────────────►│
  │                 │                    │                │
  │ ResponseEntity<ErrorResponse>        │                │
  │◄─────────────────────────────────────│                │
  │                 │                    │                │
  │ 409 Conflict    │                    │                │
  │ ErrorResponse   │                    │                │
  │───────────────────────────────────────────────────────►│
```

## Componentes Participantes

### Controllers
- **UserController**: Maneja las peticiones HTTP REST

### Services  
- **UserService**: Contiene la lógica de negocio principal

### Repositories
- **UserRepository**: Acceso a datos usando Spring Data JPA

### Utilities
- **JwtUtil**: Generación y validación de tokens JWT
- **PasswordEncoder**: Encriptación de contraseñas (BCrypt)

### Validators
- **EmailValidator**: Valida formato de email
- **PasswordValidator**: Valida complejidad de contraseña

### Exception Handlers
- **GlobalExceptionHandler**: Manejo centralizado de excepciones

## Patrones de Diseño Identificados

1. **MVC (Model-View-Controller)**: Separación entre controlador, servicio y repositorio
2. **Dependency Injection**: Inyección de dependencias entre componentes
3. **Strategy Pattern**: Diferentes validadores implementando la misma interfaz
4. **Template Method**: Spring Boot maneja el flujo de peticiones HTTP
5. **Chain of Responsibility**: Manejo de excepciones en cadena
6. **Factory Pattern**: Creación de tokens JWT
7. **Repository Pattern**: Acceso abstracto a datos

## Características de Calidad Demostradas

- **Seguridad**: Validación de entrada, encriptación de contraseñas, tokens JWT
- **Mantenibilidad**: Separación de responsabilidades, inyección de dependencias
- **Testabilidad**: Componentes desacoplados, fáciles de mockear
- **Escalabilidad**: Arquitectura stateless con JWT
- **Robustez**: Manejo comprehensivo de excepciones
- **Usabilidad**: Respuestas HTTP claras y consistentes