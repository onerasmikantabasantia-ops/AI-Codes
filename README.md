# Spring Boot CRUD Product Management with Chat Agent

A Spring Boot application providing REST endpoints for product CRUD operations, with an intelligent chat-based agent interface using tools and persistent chat memory.

## Features

- **Product CRUD Operations**: Create, Read, Update, Delete products via REST endpoints
- **Chat-Based Agent**: Send natural-language requests via `/agent/chat` endpoint
- **Tool Registry**: Modular tool-based architecture for handling different commands
- **Chat Memory**: Persistent chat history stored in H2 database
- **Product Search**: Find products by ID, name, or price

## Project Structure

```
src/main/java/com/example/crud/
├── agent/                    # Business logic agent
│   └── ProductAgent.java
├── controller/              # REST controllers
│   ├── ProductController.java
│   ├── ProductAgentController.java
│   └── dto/
│       ├── ChatRequest.java
│       └── ChatResponse.java
├── entity/                  # JPA entities
│   ├── Product.java
│   └── ChatMessage.java
├── memory/                  # Chat memory management
│   └── ChatMemory.java
├── repository/             # Data access
│   ├── ProductRepository.java
│   └── ChatMessageRepository.java
├── service/                # Service layer
│   └── ProductService.java
└── tools/                  # Tool registry and implementations
    ├── Tool.java
    ├── ToolRegistry.java
    ├── CreateProductTool.java
    ├── UpdateProductTool.java
    ├── DeleteProductTool.java
    ├── SearchProductTool.java
    ├── CountProductsTool.java
    ├── FindByNameTool.java
    └── FindByPriceTool.java
```

## API Endpoints

### Product Management (Direct REST API)

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/products` | Get all products |
| GET | `/products/{id}` | Get product by ID |
| POST | `/products` | Create new product |
| PUT | `/products/{id}` | Update product |
| DELETE | `/products/{id}` | Delete product |
| GET | `/products/count/total` | Count total products |
| GET | `/products/search/by-name?name=X` | Find products by name |
| GET | `/products/search/by-price?price=X` | Find products by price |

### Chat Agent Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/agent/chat` | Send chat request with command |
| GET | `/agent/history` | View chat history (persistent) |
| DELETE | `/agent/history` | Clear all chat history from database |

## Usage Examples

### Chat Agent Requests (POST /agent/chat)

**Create Product**
```json
{
  "message": "create product",
  "product": { "name": "Widget", "price": 9.99 }
}
```

**Update Product**
```json
{
  "message": "update product",
  "id": 1,
  "product": { "name": "Widget v2", "price": 11.50 }
}
```

**Delete Product**
```json
{
  "message": "delete product",
  "id": 1
}
```

**Search All Products**
```json
{
  "message": "search product"
}
```

**Count Products**
```json
{
  "message": "count products"
}
```

**Find by Name**
```json
{
  "message": "find by name",
  "name": "Widget"
}
```

**Find by Price**
```json
{
  "message": "find by price",
  "price": 9.99
}
```

### View Chat History
```
GET /agent/history
```

Returns list of recent chat requests (up to 50, newest first).

### Clear Chat History
```
DELETE /agent/history
```

Clears all chat messages from database and memory.

## Technology Stack

- **Framework**: Spring Boot 3.5.0
- **Database**: H2 (in-memory)
- **ORM**: Spring Data JPA
- **Language**: Java 17
- **Build Tool**: Maven

## Key Components

### Tools (Tool Registry Pattern)

Each operation is implemented as a modular "Tool":
- Tool interface defines: `name()`, `matches(String)`, `execute(ChatRequest)`
- `ToolRegistry` discovers all Tool beans and matches incoming messages
- New tools can be added by creating a new @Component implementing Tool

### Chat Memory

- Persists all chat requests to H2 database
- Keeps last 50 messages in memory for performance
- Auto-loads history on application startup
- Can be cleared via `/agent/history` DELETE endpoint

### Message Format

Chat messages are stored with:
- Message text
- Product JSON (if applicable)
- Product ID, name, price (extracted fields)
- Timestamp (auto-generated)

## Building & Running

### Build
```bash
mvn clean package
```

### Run
```bash
java -jar target/crud-0.0.1.jar
```

Application runs on `http://localhost:8080`

## Database

H2 database configuration in `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=update
```

## Future Enhancements

- Add session-based chat memory (per user)
- Implement natural language processing for flexible command matching
- Add request/response validation with error handling
- Create API documentation with Swagger/OpenAPI
- Add unit and integration tests
- Implement authentication/authorization
- Support for bulk operations
- Add filtering and sorting to search endpoints
