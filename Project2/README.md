# Payly - Spring Boot Payment Application

A comprehensive Spring Boot application implementing a digital wallet system with user authentication, money transfers, transaction history, and Razorpay payment gateway integration.

## Features

### Phase 1: Basic Spring Boot Setup
- Spring Boot project with Maven
- MySQL database integration
- REST API architecture
- MVC pattern implementation

### Phase 2: Authentication System
- User registration and login
- Password hashing with BCrypt
- JWT token-based authentication
- Secure endpoints protection

### Phase 3: Wallet System
- Automatic wallet creation for new users
- Add money to wallet (dummy implementation)
- Check wallet balance
- Secure wallet operations

### Phase 4: Send Money Feature
- Peer-to-peer money transfers
- Balance validation (insufficient funds check)
- Transaction rollback on failure
- ACID compliance for data integrity

### Phase 5: Transaction System
- Transaction ID generation
- Transaction status tracking (SUCCESS, FAILED, PENDING)
- Timestamp logging
- Comprehensive transaction history

### Phase 6: Payment Gateway Integration
- Razorpay integration for real payments
- Order creation
- Payment verification with signature validation
- Test mode configuration

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- MySQL 8.0+
- Razorpay account (for payment features)

## Installation

1. Clone the repository
2. Configure MySQL database:
   ```sql
   CREATE DATABASE payly_db;
   ```
3. Update `application.properties` with your database credentials and Razorpay keys
4. Install dependencies:
   ```bash
   mvn clean install
   ```
5. Run the application:
   ```bash
   mvn spring-boot:run
   ```

## API Endpoints

### Authentication
- `POST /auth/register` - User registration
- `POST /auth/login` - User login

### Wallet
- `GET /wallet/balance` - Get wallet balance
- `POST /wallet/add-money` - Add money to wallet

### Transfers
- `POST /transfer/send` - Send money to another user

### Transactions
- `GET /transactions/history` - Get transaction history

### Payments
- `POST /payment/create-order` - Create Razorpay order
- `POST /payment/verify` - Verify payment

## Configuration

Update the following in `src/main/resources/application.properties`:

```properties
# Database
spring.datasource.username=your_mysql_username
spring.datasource.password=your_mysql_password

# JWT
jwt.secret=your_jwt_secret_key

# Razorpay
razorpay.key_id=your_razorpay_key_id
razorpay.key_secret=your_razorpay_key_secret
```

## Testing

Use Postman or similar tool to test the APIs. Include JWT token in Authorization header for protected endpoints:

```
Authorization: Bearer <your_jwt_token>
```

## Security

- Passwords are hashed using BCrypt
- JWT tokens for stateless authentication
- Input validation on all endpoints
- SQL injection prevention with JPA

## Troubleshooting

### Common Issues

1. **Database Connection Error**
   - Verify MySQL is running
   - Check database credentials in application.properties
   - Ensure database exists

2. **JWT Token Issues**
   - Verify jwt.secret is set
   - Check token expiration (default 24 hours)

3. **Razorpay Integration**
   - Use test keys for development
   - Verify webhook signatures for production

### Build Issues
- Ensure Java 17+ is installed
- Run `mvn clean compile` to check for compilation errors
- Check dependency versions in pom.xml

## Deployment

### Backend
- Build JAR: `mvn clean package`
- Run JAR: `java -jar target/payly-0.0.1-SNAPSHOT.jar`

### Database
- Use Railway, PlanetScale, or AWS RDS for production
- Run migrations on startup

### Frontend (Optional)
- React/Vue.js frontend can be deployed to Vercel/Netlify
- Connect to backend APIs

## Contributing

1. Fork the repository
2. Create feature branch
3. Commit changes
4. Push to branch
5. Create Pull Request

## License

This project is licensed under the MIT License.