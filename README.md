# PulsePass: Capa de Controladores REST (Fase 3)

API REST de PulsePass, una plataforma de eventos, artistas y entradas.
Esta fase expone mediante HTTP los casos de uso de la capa de Servicios, sin duplicar reglas de negocio.

## Tecnologías

Java 21, Spring Boot 4.1.1, Spring MVC, Bean Validation, JUnit 5, Mockito y MockMvc.

## Arquitectura

```
Cliente HTTP -> Controller -> Service (interfaz) -> (Repository / base de datos)
                    |
          GlobalExceptionHandler -> ErrorResponse
```

- Los Controllers solo traducen HTTP: reciben el pedido, llaman al Service y devuelven un DTO.
- Las reglas de negocio viven en la capa Service (no se reimplementan aquí).
- Ningún endpoint devuelve entidades JPA: solo DTOs.
- Los errores se centralizan en `GlobalExceptionHandler` con el formato `ErrorResponse`.

## Endpoints

### Venues
| Método | Endpoint | Respuesta |
|---|---|---|
| GET | `/api/venues/{code}` | 200 / 404 |
| GET | `/api/venues/active` | 200 |

### Eventos
| Método | Endpoint | Respuesta |
|---|---|---|
| POST | `/api/events` | 201 / 400 |
| GET | `/api/events/{eventCode}` | 200 / 404 |
| GET | `/api/events/published` | 200 |
| PATCH | `/api/events/{eventCode}/publish` | 200 / 409 |
| POST | `/api/events/{eventCode}/artists/{artistId}` | 200 / 409 |
| GET | `/api/events/by-artist?stageName=...` | 200 |

### Artistas
| Método | Endpoint | Respuesta |
|---|---|---|
| GET | `/api/artists/{id}` | 200 / 404 |
| GET | `/api/artists/by-stage-name?stageName=...` | 200 / 404 |
| GET | `/api/artists/active` | 200 |

### Usuarios
| Método | Endpoint | Respuesta |
|---|---|---|
| POST | `/api/users` | 201 / 400 / 409 |
| GET | `/api/users/by-email?email=...` | 200 / 404 |
| GET | `/api/users/by-username?username=...` | 200 / 404 |

### Tickets
| Método | Endpoint | Respuesta |
|---|---|---|
| POST | `/api/tickets` | 201 / 400 / 404 / 409 |
| GET | `/api/tickets/{ticketCode}` | 200 / 404 |
| GET | `/api/tickets/by-user?email=...` | 200 |
| GET | `/api/events/{eventCode}/tickets/paid` | 200 |
| PATCH | `/api/tickets/{ticketCode}/cancel` | 200 / 409 |
| PATCH | `/api/tickets/{ticketCode}/use` | 200 / 409 |

## Manejo de errores

Todos los errores usan el mismo formato:

```json
{
  "timestamp": "2026-10-04T20:00:00",
  "status": 404,
  "error": "Not Found",
  "message": "Event not found: CMF-2026",
  "details": {}
}
```

| Condición | Código HTTP |
|---|---|
| Bean Validation falla o JSON mal formado | 400 |
| `ResourceNotFoundException` | 404 |
| `DuplicateResourceException` | 409 |
| `BusinessRuleException` | 409 |
| Error inesperado | 500 |

## Cómo ejecutar las pruebas

Las pruebas usan `@WebMvcTest`, MockMvc y Services simulados con `@MockitoBean`.
No necesitan base de datos ni Maven instalado (se usa el Maven Wrapper incluido).

```
.\mvnw clean test
```

En Linux o macOS: `./mvnw clean test`. El resultado esperado es `BUILD SUCCESS` con 33 pruebas.

## Estructura

```
src/main/java/com/pulsepass
  controller/   VenueController, EventController, ArtistController,
                UserController, TicketController
  dto/request/  CreateEventRequest, RegisterUserRequest, PurchaseTicketRequest
  dto/response/ VenueResponse, EventResponse, EventSummaryResponse,
                ArtistResponse, UserResponse, TicketResponse, ErrorResponse
  exception/    ResourceNotFoundException, DuplicateResourceException,
                BusinessRuleException, GlobalExceptionHandler
  model/        EventCategory, EventStatus, TicketType, TicketStatus
  service/      Interfaces de los 5 Services
src/test/java/com/pulsepass/controller
                Una clase de pruebas por cada Controller
```

## Nota sobre los Services

Este proyecto contiene solo las **interfaces** de los Services. Sus implementaciones
pertenecen a la Fase 2. En las pruebas, los Services se reemplazan con mocks.