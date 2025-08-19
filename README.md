# Enterprise Asset Management (EAM) System

A comprehensive Spring Boot application for managing enterprise assets, inventory, work orders, maintenance, and authentication with multi-tenant support.

## 🚀 Features

- **Multi-tenant Architecture**: Support for multiple organizations/tenants
- **User Management**: Role-based access control with permissions
- **Asset Management**: Complete asset lifecycle management
- **Work Order Management**: Create and track maintenance work orders
- **Internationalization (i18n)**: Multi-language support with automatic translation
- **JWT Authentication**: Secure API access with JSON Web Tokens
- **RESTful APIs**: Comprehensive REST endpoints for all operations

## 🗄️ Database Configuration

The EAM project supports multiple database configurations using Spring profiles:

### Available Profiles

1. **H2 Profile** (`h2`) - In-memory database for development and testing
   - Fast startup and no external dependencies
   - Data is lost when application stops
   - Includes H2 console for database inspection

2. **PostgreSQL Profile** (`postgres`) - Production-ready database
   - Persistent data storage
   - Better performance for large datasets
   - Connection pooling and optimization

3. **Development Profile** (`dev`) - H2 with enhanced logging
   - Includes SQL query logging
   - Debug information for development

4. **Production Profile** (`prod`) - PostgreSQL with optimized settings
   - Reduced logging for better performance
   - Security hardening

### Database Setup

#### PostgreSQL Setup with Docker
```bash
# Start PostgreSQL container
docker-compose up -d postgres

# Check if PostgreSQL is running
docker-compose ps
```

#### Manual PostgreSQL Setup
1. Install PostgreSQL on your system
2. Create a database named `eam`
3. Update connection details in `application-postgres.properties`

### Database Access

#### H2 Console (when using H2 profile)
- URL: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: `password`

#### PostgreSQL Connection
- Host: `localhost:5434`
- Database: `eam`
- Username: `postgres`
- Password: `password`

## 🏗️ Architecture
- **Tenant Management**: Multi-tenant support with organization isolation
- **User & Role Management**: Hierarchical role-based access control
- **Industry Management**: Industry classification for tenants
- **Subscription Management**: Subscription plans and feature management
- **Location Management**: Hierarchical location structure
- **User Groups**: Group-based user organization

### Technology Stack
- **Spring Boot 3.x**: Core framework
- **Spring Security**: Authentication and authorization
- **Spring Data JPA**: Data persistence
- **H2 Database**: In-memory database for development
- **Gradle**: Build tool
- **JWT**: Token-based authentication
- **i18n Library**: Internationalization support

## 📋 Prerequisites

- Java 21 or higher
- Gradle 8.x
- curl (for API testing)
- jq (optional, for JSON formatting)

## 🛠️ Setup and Installation

### 1. Clone the Repository
```bash
git clone <repository-url>
cd eam
```

### 2. Build the Project
```bash
./gradlew build
```

### 3. Run the Application

#### Option 1: H2 Database (Default - In-Memory)
```bash
./run-h2.sh
# or
./gradlew bootRun --args='--spring.profiles.active=h2'
```

#### Option 2: PostgreSQL Database (Production)
```bash
# Start PostgreSQL with Docker
docker-compose up -d postgres

# Run the application
./run-postgres.sh
# or
./gradlew bootRun --args='--spring.profiles.active=postgres'
```

#### Option 3: Development Mode (H2 with enhanced logging)
```bash
./run-dev.sh
# or
./gradlew bootRun --args='--spring.profiles.active=dev'
```

#### Option 4: Production Mode (PostgreSQL with optimized settings)
```bash
./gradlew bootRun --args='--spring.profiles.active=prod'
```

The application will start on `http://localhost:8080`

### 4. Verify Application Health
```bash
curl http://localhost:8080/actuator/health
```

Expected response: `{"status":"UP"}`

## 🧪 Testing with Shell Scripts

The project includes comprehensive shell scripts for testing the Tenant API functionality.

### Available Test Scripts

1. **`test-tenant-api.sh`** - Comprehensive API testing
2. **`test-tenant-api-simple.sh`** - Simplified testing with better error handling

### Running the Test Scripts

#### Prerequisites
- Make sure the EAM application is running on `http://localhost:8080`
- Ensure the scripts are executable

```bash
# Make scripts executable (if not already done)
chmod +x test-tenant-api.sh
chmod +x test-tenant-api-simple.sh
```

#### Run Comprehensive Tests
```bash
./test-tenant-api.sh
```

This script tests:
- ✅ Industry creation and retrieval
- ✅ Subscription creation and retrieval
- ✅ Tenant CRUD operations
- ✅ User group management
- ✅ Location management
- ✅ Application health checks

#### Run Simplified Tests
```bash
./test-tenant-api-simple.sh
```

This script provides:
- ✅ Better error handling for duplicate data
- ✅ Cleaner output with less verbose messages
- ✅ Unique codes to avoid constraint violations
- ✅ Focused testing on core functionality

### Test Script Features

#### 🎨 Visual Output
- **✅ Green**: Success messages
- **❌ Red**: Error messages
- **ℹ️ Blue**: Information messages
- **📋 Purple**: Section headers

#### 📊 Test Coverage
- **Industries API**: Create and retrieve industries
- **Subscriptions API**: Create and retrieve subscription plans
- **Tenants API**: Full CRUD operations (Create, Read, Update, Delete)
- **Related APIs**: UserGroups, Locations
- **Health Checks**: Application status verification

#### 🔧 Error Handling
- Application availability check before running tests
- Duplicate data handling
- JSON response formatting (with `jq` if available)
- Clear success/failure reporting

### Example Test Output

```bash
==========================================
EAM Tenant API Test Script
==========================================
Base URL: http://localhost:8080

📋 Checking if application is running...
✅ Application is running

📋 Test 1: Creating Industries
Creating Technology industry...
✅ Technology industry created successfully
Response: {"id":1,"industryCode":"TECH","name":"Technology"}

📋 Test 2: Retrieving All Industries
ℹ️ All Industries:
[
  {
    "id": 1,
    "industryCode": "TECH",
    "name": "Technology"
  }
]

📋 Test Summary
✅ Tenant API tests completed!
ℹ️ Endpoints tested:
  - POST /api/industries
  - GET /api/industries
  - POST /api/subscriptions
  - GET /api/subscriptions
  - POST /api/tenants
  - GET /api/tenants
  - GET /api/tenants/{id}
  - PUT /api/tenants/{id}
  - GET /actuator/health
==========================================
```

## 🔌 API Endpoints

### Core APIs
- **Industries**: `/api/industries`
- **Subscriptions**: `/api/subscriptions`
- **Tenants**: `/api/tenants`
- **User Groups**: `/api/user-groups`
- **Locations**: `/api/locations`

### Application Health
- **Health Check**: `/actuator/health`
- **H2 Console**: `/h2-console` (development only)

## 🔐 JWT Authentication

### JWT Payload Example

#### Before Role Expansion
```json
{
  "username": "alice",
  "roles": ["ADMIN"],
  "permissions": ["PERMISSION_EXPORT"]
}
```

#### After Role Expansion (in JWT)
```json
{
  "username": "alice",
  "roles": ["ADMIN"],
  "permissions": [
    "PERMISSION_CREATE",
    "PERMISSION_READ",
    "PERMISSION_UPDATE",
    "PERMISSION_DELETE",
    "PERMISSION_EXPORT"
  ]
}
```

### Secured Endpoint Example
```java
@HasPermission("PERMISSION_CREATE")
@PostMapping("/api/permissions")
public ResponseEntity<Permission> createPermission(@RequestBody Permission permission) { ... }
```

### Error Handling
- **401 Unauthorized**: Invalid or expired JWT
- **403 Forbidden**: JWT does not contain required permission

## 🗄️ Database

### Database Options

#### H2 Database (Development/Testing)
- **Type**: H2 In-Memory Database
- **Console**: `http://localhost:8080/h2-console`
- **JDBC URL**: `jdbc:h2:mem:testdb`
- **Username**: `sa`
- **Password**: `password`

#### PostgreSQL Database (Production)
- **Type**: PostgreSQL Persistent Database
- **Host**: `localhost:5434`
- **Database**: `eam`
- **Username**: `postgres`
- **Password**: `password`

### Schema
The application automatically creates the following tables:
- `tenants` - Organization/tenant information
- `industries` - Industry classifications
- `subscriptions` - Subscription plans
- `users` - User accounts
- `roles` - Role definitions
- `permissions` - Permission definitions
- `user_groups` - User group management
- `locations` - Location hierarchy
- `translations` - i18n translations
- `tenant_i18n_config` - Tenant-specific i18n settings

## 🚀 Development

### Project Structure
```
eam/
├── src/main/java/com/eam/
│   ├── auth/           # Authentication and authorization
│   ├── issue/          # Issue/work order management
│   └── util/           # Utility classes
├── src/main/resources/
│   ├── application.properties
│   ├── application-h2.properties
│   ├── application-postgres.properties
│   ├── application-dev.properties
│   ├── application-prod.properties
│   └── schema.sql
├── docker-compose.yml
├── run-h2.sh
├── run-postgres.sh
├── run-dev.sh
├── test-tenant-api.sh          # Comprehensive API tests
├── test-tenant-api-simple.sh   # Simplified API tests
└── README.md
```

### Building and Running
```bash
# Build the project
./gradlew build

# Run the application
./gradlew bootRun

# Run tests
./gradlew test

# Clean build
./gradlew clean build
```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests
5. Run the test scripts to verify functionality
6. Submit a pull request

## 📝 License

This project is licensed under the MIT License.

## 🆘 Troubleshooting

### Common Issues

1. **Application won't start**
   - Check Java version (requires Java 21+)
   - Verify port 8080 is not in use
   - Check application logs for errors

2. **Test scripts fail**
   - Ensure application is running on `http://localhost:8080`
   - Check if scripts are executable (`chmod +x *.sh`)
   - Verify curl is installed

3. **Database connection issues**
   - H2 console: `http://localhost:8080/h2-console`
   - Check application.properties for database settings

### Getting Help
- Check application logs for detailed error messages
- Verify all prerequisites are installed
- Run health check: `curl http://localhost:8080/actuator/health`
