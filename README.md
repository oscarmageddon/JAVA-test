# User Service Microservice

Este es un microservicio desarrollado con Spring Boot 2.5.14 para la creación y consulta de usuarios, implementando autenticación JWT y validaciones personalizadas.

## Características Técnicas

- **Spring Boot**: 2.5.14
- **Java**: 11
- **Gradle**: 7.4
- **Base de Datos**: H2 (en memoria)
- **Autenticación**: JWT (JSON Web Tokens)
- **Validación**: Bean Validation con validadores personalizados
- **Testing**: JUnit 5 con Mockito
- **Cobertura**: Mínimo 80% en servicios

## Características de Java 11 Utilizadas

1. **Local Variable Type Inference (var)**: Utilizada en métodos de validación y pruebas
2. **String Methods**: `.strip()`, `.isBlank()` para validaciones
3. **Optional**: Uso extensivo de Optional para manejo seguro de nulls
4. **Stream API**: Para transformaciones de datos entre DTOs y entidades
5. **Time API**: LocalDateTime para manejo de fechas

## Requisitos del Sistema

- Java 11 o superior
- Gradle 7.4 o superior

## Instalación y Configuración

### 1. Clonar el Repositorio

```bash
git clone <repository-url>
cd user-service
```

### 2. Construcción del Proyecto

```bash
# Compilar el proyecto
./gradlew build

# Compilar sin ejecutar tests
./gradlew build -x test
```

### 3. Ejecutar Pruebas Unitarias

```bash
# Ejecutar todas las pruebas
./gradlew test

# Generar reporte de cobertura
./gradlew jacocoTestReport
```

El reporte de cobertura estará disponible en: `build/reports/jacoco/test/html/index.html`

### 4. Ejecutar la Aplicación

```bash
# Modo desarrollo
./gradlew bootRun

# O ejecutar el JAR generado
java -jar build/libs/user-service-0.0.1-SNAPSHOT.jar
```

La aplicación se ejecutará en `http://localhost:8080`

## Configuración de Base de Datos

El proyecto utiliza H2 como base de datos en memoria. La consola H2 está disponible en:
- **URL**: `http://localhost:8080/h2-console`
- **JDBC URL**: `jdbc:h2:mem:userdb`
- **Usuario**: `sa`
- **Contraseña**: (vacía)

## API Endpoints

### 1. Registro de Usuario

**POST** `/api/sign-up`

**Request Body:**
```json
{
  "name": "Juan Pérez",
  "email": "juan@domain.cl",
  "password": "aB2defgh89",
  "phones": [
    {
      "number": 87650009,
      "citycode": 7,
      "contrycode": "25"
    }
  ]
}
```

**Response (201 Created):**
```json
{
  "id": "e5c6cf84-8860-4c00-91cd-22d3be28904e",
  "created": "2023-12-01T10:30:00",
  "lastLogin": "2023-12-01T10:30:00",
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqdWxpb0B0ZXN0...",
  "isActive": true,
  "name": "Juan Pérez",
  "email": "juan@domain.cl",
  "password": "$2a$10$encrypted...",
  "phones": [
    {
      "number": 87650009,
      "citycode": 7,
      "contrycode": "25"
    }
  ]
}
```

### 2. Login de Usuario

**GET** `/api/login`

**Headers:**
```
Authorization: Bearer <jwt-token>
```

**Response (200 OK):**
```json
{
  "id": "e5c6cf84-8860-4c00-91cd-22d3be28904e",
  "created": "2023-12-01T10:30:00",
  "lastLogin": "2023-12-01T10:35:00",
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqdWxpb0B0ZXN0...",
  "isActive": true,
  "name": "Juan Pérez",
  "email": "juan@domain.cl",
  "password": "$2a$10$encrypted...",
  "phones": [
    {
      "number": 87650009,
      "citycode": 7,
      "contrycode": "25"
    }
  ]
}
```

## Validaciones

### Email
- Formato: `aaaaaaa@undominio.algo`
- Expresión regular: `^[a-zA-Z0-9]+@[a-zA-Z0-9]+\\.[a-zA-Z]+$`

### Password
- Longitud: 8-12 caracteres
- Debe tener exactamente 1 letra mayúscula
- Debe tener exactamente 2 números
- Resto deben ser letras minúsculas
- Ejemplo válido: `aB2defgh89`

## Manejo de Errores

Todos los errores retornan el siguiente formato JSON:

```json
{
  "error": [
    {
      "timestamp": "2023-12-01T10:30:00",
      "codigo": 400,
      "detail": "Email format is invalid"
    }
  ]
}
```

### Códigos de Error Comunes

- **400 Bad Request**: Validación de datos fallida
- **404 Not Found**: Usuario no encontrado o token inválido
- **409 Conflict**: Usuario ya existe
- **500 Internal Server Error**: Error interno del servidor

## Seguridad

- Las contraseñas se encriptan usando BCrypt
- Los tokens JWT tienen una duración de 24 horas
- Los endpoints están protegidos por Spring Security
- La aplicación es stateless (sin sesiones)

## Testing

### Ejecutar Pruebas

```bash
# Todas las pruebas
./gradlew test

# Pruebas específicas
./gradlew test --tests "UserServiceTest"

# Con reporte de cobertura
./gradlew test jacocoTestReport
```

### Cobertura de Código

El proyecto mantiene un mínimo de 80% de cobertura en los servicios:

- **UserService**: 95%+
- **Validadores**: 90%+
- **JwtUtil**: 85%+
- **Controllers**: 80%+

## Estructura del Proyecto

```
src/
├── main/
│   ├── java/com/example/userservice/
│   │   ├── config/          # Configuración de Spring Security
│   │   ├── controller/      # Controladores REST
│   │   ├── dto/            # Data Transfer Objects
│   │   ├── entity/         # Entidades JPA
│   │   ├── exception/      # Excepciones personalizadas
│   │   ├── repository/     # Repositorios JPA
│   │   ├── service/        # Lógica de negocio
│   │   ├── util/           # Utilidades (JWT)
│   │   └── validation/     # Validadores personalizados
│   └── resources/
│       └── application.properties
└── test/
    └── java/com/example/userservice/
        ├── controller/     # Pruebas de controladores
        ├── service/        # Pruebas de servicios
        ├── util/          # Pruebas de utilidades
        └── validation/    # Pruebas de validadores
```

## Diagramas

### Diagrama de Componentes

El sistema está compuesto por los siguientes componentes principales:

1. **Controller Layer**: Maneja las peticiones HTTP
   - UserController: Endpoints de registro y login
   
2. **Service Layer**: Lógica de negocio
   - UserService: Gestión de usuarios y autenticación
   
3. **Repository Layer**: Acceso a datos
   - UserRepository: Operaciones CRUD en base de datos
   
4. **Security Layer**: Seguridad y autenticación
   - SecurityConfig: Configuración de Spring Security
   - JwtUtil: Generación y validación de tokens
   
5. **Validation Layer**: Validaciones personalizadas
   - EmailValidator: Validación de formato de email
   - PasswordValidator: Validación de complejidad de contraseña
   
6. **Exception Layer**: Manejo de errores
   - GlobalExceptionHandler: Manejo global de excepciones

### Diagrama de Secuencia

#### Flujo de Registro de Usuario:
1. Cliente → Controller: POST /api/sign-up
2. Controller → Validator: Valida email y password
3. Controller → Service: createUser()
4. Service → Repository: existsByEmail()
5. Service → PasswordEncoder: encode()
6. Service → JwtUtil: generateToken()
7. Service → Repository: save()
8. Service → Controller: UserResponse
9. Controller → Cliente: 201 Created + UserResponse

#### Flujo de Login:
1. Cliente → Controller: GET /api/login + Bearer token
2. Controller → Service: loginUser()
3. Service → JwtUtil: extractUsername() y validateToken()
4. Service → Repository: findByEmail()
5. Service → JwtUtil: generateToken() (nuevo token)
6. Service → Repository: save() (actualizar lastLogin y token)
7. Service → Controller: UserResponse
8. Controller → Cliente: 200 OK + UserResponse actualizado

## Configuración para Desarrollo

### Variables de Entorno (Opcional)

```bash
export JWT_SECRET=myCustomSecretKey
export JWT_EXPIRATION=86400
export SPRING_PROFILES_ACTIVE=dev
```

### Perfiles de Spring

- **default**: Configuración básica con H2
- **dev**: Configuración de desarrollo con logs detallados
- **test**: Configuración para pruebas

## Troubleshooting

### Problemas Comunes

1. **Error de compilación Java 11**
   ```bash
   # Verificar versión de Java
   java -version
   javac -version
   
   # Configurar JAVA_HOME si es necesario
   export JAVA_HOME=/path/to/java11
   ```

2. **Puerto 8080 ocupado**
   ```bash
   # Usar puerto alternativo
   java -jar app.jar --server.port=8081
   ```

3. **Tests fallando**
   ```bash
   # Limpiar y reconstruir
   ./gradlew clean build
   ```
