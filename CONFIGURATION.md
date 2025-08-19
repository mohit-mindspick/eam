# Configuration Guide

## Database Configuration

The EAM project supports multiple database configurations using Spring profiles:

### Environment Variables

You can customize the database configuration by setting environment variables:

```bash
# PostgreSQL Configuration
export POSTGRES_HOST=localhost
export POSTGRES_PORT=5434
export POSTGRES_DB=eam
export POSTGRES_USER=postgres
export POSTGRES_PASSWORD=your_password

# Application Configuration
export SERVER_PORT=8080
export JWT_SECRET=your_jwt_secret_key
export JWT_EXPIRATION_MS=3600000
```

### Profile-Specific Configuration

#### H2 Profile (`application-h2.properties`)
- In-memory database
- No external dependencies
- Includes H2 console
- Automatic schema creation and data initialization

#### PostgreSQL Profile (`application-postgres.properties`)
- Persistent database
- Connection pooling with HikariCP
- Optimized for production use
- Manual data initialization required

#### Development Profile (`application-dev.properties`)
- Extends H2 profile
- Enhanced logging and debugging
- SQL query logging enabled

#### Production Profile (`application-prod.properties`)
- Extends PostgreSQL profile
- Reduced logging for performance
- Security hardening enabled

## Running with Different Profiles

### H2 Database (Development/Testing)
```bash
./run-h2.sh
# or
./gradlew bootRun --args='--spring.profiles.active=h2'
```

### PostgreSQL Database (Production)
```bash
# Start PostgreSQL
docker-compose up -d postgres

# Run application
./run-postgres.sh
# or
./gradlew bootRun --args='--spring.profiles.active=postgres'
```

### Development Mode (Enhanced Logging)
```bash
./run-dev.sh
# or
./gradlew bootRun --args='--spring.profiles.active=dev'
```

### Production Mode (Optimized)
```bash
./gradlew bootRun --args='--spring.profiles.active=prod'
```

## Database Migration

### From H2 to PostgreSQL

1. Start PostgreSQL:
   ```bash
   docker-compose up -d postgres
   ```

2. Run the application with PostgreSQL profile:
   ```bash
   ./run-postgres.sh
   ```

3. The application will automatically create the schema using `ddl-auto=update`

### Data Initialization

#### H2 (Automatic)
- Schema is created automatically
- Data is initialized from `schema.sql` if present
- Data is recreated on each application restart

#### PostgreSQL (Manual)
- Schema is created automatically
- Data must be loaded manually:
  ```bash
  psql -h localhost -p 5434 -U postgres -d eam -f src/main/resources/schema.sql
  ```

## Connection Details

### H2 Console
- URL: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: `password`

### PostgreSQL
- Host: `localhost:5434`
- Database: `eam`
- Username: `postgres`
- Password: `password`

## Integration with Authenticator

### Shared Database Configuration

When using PostgreSQL, both EAM and Authenticator can share the same database:

1. **EAM Database**: `localhost:5434/eam`
2. **Authenticator Database**: `localhost:5433/authenticator`

### Separate Databases (Recommended)

For production, it's recommended to use separate databases:

- **EAM**: `localhost:5434/eam`
- **Authenticator**: `localhost:5433/authenticator`

This provides better isolation and security.

## Troubleshooting

### Common Issues

1. **PostgreSQL Connection Failed**
   - Ensure PostgreSQL is running: `docker-compose ps`
   - Check port availability: `netstat -an | grep 5434`
   - Verify credentials in `application-postgres.properties`

2. **H2 Console Not Accessible**
   - Ensure H2 profile is active
   - Check if H2 console is enabled in `application-h2.properties`

3. **Data Not Persisting (PostgreSQL)**
   - Check if `ddl-auto=update` is set
   - Verify database permissions
   - Check application logs for errors

4. **Port Conflicts**
   - EAM uses port 8080 (application) and 5434 (PostgreSQL)
   - Authenticator uses port 8082 (application) and 5433 (PostgreSQL)

### Logs

Enable debug logging for database issues:
```bash
./gradlew bootRun --args='--spring.profiles.active=dev'
```

## Performance Tuning

### PostgreSQL Optimization

For production PostgreSQL deployments:

1. **Connection Pool Settings**:
   ```properties
   spring.datasource.hikari.maximum-pool-size=20
   spring.datasource.hikari.minimum-idle=10
   ```

2. **JPA Settings**:
   ```properties
   spring.jpa.properties.hibernate.jdbc.batch_size=20
   spring.jpa.properties.hibernate.order_inserts=true
   spring.jpa.properties.hibernate.order_updates=true
   ```

3. **Query Optimization**:
   ```properties
   spring.jpa.properties.hibernate.query.in_clause_parameter_padding=true
   spring.jpa.properties.hibernate.query.fail_on_pagination_over_collection_fetch=true
   ```
