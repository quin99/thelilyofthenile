# The Lily of the Nile

A full-stack florist e-commerce application selling floral arrangements, bracelets, and trinkets. Built with Angular (frontend) and Spring Boot (backend), backed by PostgreSQL.

---

## Tech Stack

| Layer     | Technology |
|-----------|-----------|
| Frontend  | Angular 20 + Tailwind CSS 4 |
| Backend   | Spring Boot 3 (Java 17) |
| Database  | PostgreSQL 15 |
| Auth      | JWT (HS256) |
| Payments  | Stripe *(in progress)* |
| Container | Docker + Docker Compose |

---

## Running the Application

### Option 1 — Docker (recommended)

Starts the backend, frontend, and PostgreSQL database in one command.

```bash
docker-compose up
```

| Service  | URL |
|----------|-----|
| Frontend | http://localhost:4200 |
| Backend  | http://localhost:8080 |

To stop:
```bash
docker-compose down
```

To stop and remove all data:
```bash
docker-compose down -v
```

---

### Option 2 — Manual (run each service separately)

You will need Java 17+, Node.js 18+, and a running PostgreSQL 15 instance.

**1. Set environment variables**

```bash
export DB_URL=jdbc:postgresql://localhost:5432/thelilyofthenile_db
export DB_USERNAME=thelilyofthenile_user
export DB_PASSWORD=your_db_password
export JWT_SECRET=your-256-bit-secret
```

**2. Start the backend**

```bash
cd backend
./mvnw spring-boot:run
# Runs on http://localhost:8080
```

**3. Start the frontend** (in a separate terminal)

```bash
cd frontend
npm install
npm start
# Runs on http://localhost:4200
```

---

## Environment Variables

| Variable             | Used By  | Description |
|----------------------|----------|-------------|
| `DB_URL`             | Backend  | PostgreSQL JDBC URL |
| `DB_USERNAME`        | Backend  | Database user |
| `DB_PASSWORD`        | Backend  | Database password |
| `JWT_SECRET`         | Backend  | HS256 signing secret (min 32 chars) |
| `STRIPE_SECRET_KEY`  | Backend  | Stripe secret key *(not yet implemented)* |
| `STRIPE_WEBHOOK_SECRET` | Backend | Stripe webhook signing secret *(not yet implemented)* |
| `STRIPE_PUBLISHABLE_KEY` | Frontend | Stripe publishable key *(not yet implemented)* |

> Never commit real values for any of these. Use a `.env` file locally (gitignored) or your hosting provider's secrets manager in production.

---

## Project Structure

```
thelilyofthenile/
├── backend/
│   ├── src/main/java/com/thelilyofthenile/backend/
│   │   ├── controller/     # REST endpoints
│   │   ├── service/        # Business logic
│   │   ├── repository/     # Data access (JPA)
│   │   ├── model/          # JPA entities
│   │   ├── dto/            # Request/response DTOs
│   │   └── security/       # JWT filter + config
│   └── src/main/resources/
│       └── application.yml
├── frontend/
│   └── src/app/
│       ├── core/
│       │   ├── models/     # TypeScript interfaces
│       │   ├── services/   # HTTP services
│       │   ├── interceptors/
│       │   └── guards/
│       ├── shared/
│       │   └── components/ # Nav, footer, product card
│       └── features/       # Page components
│           ├── home/
│           ├── catalog/
│           ├── product/
│           ├── cart/
│           ├── checkout/
│           └── account/
├── docker-compose.yml
└── README.md
```

---

## API Reference

Base path: `/api/v1`

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| POST | `/customers/register` | None | Create account |
| POST | `/customers/login` | None | Login, returns JWT |
| GET | `/products` | None | List all products |
| GET | `/products?category=FLOWERS` | None | Filter by category |
| GET | `/products/{id}` | None | Get single product |
| POST | `/products` | Required | Create product |
| PUT | `/products/{id}` | Required | Update product |
| DELETE | `/products/{id}` | Required | Delete product |
| GET | `/cart` | Required | Get cart |
| POST | `/cart/add` | Required | Add item to cart |
| DELETE | `/cart/remove/{productId}` | Required | Remove item |
| DELETE | `/cart/clear` | Required | Clear cart |
| POST | `/orders/place` | Required | Place order from cart |
| GET | `/orders` | Required | Get order history |

---

## Testing

**Backend unit tests:**
```bash
cd backend
./mvnw test
```

**Frontend E2E tests (Cypress):**
```bash
cd frontend
npx cypress run
```

**Single spec:**
```bash
npx cypress run --spec "cypress/e2e/products/product-listing.cy.ts"
```

---

## Deployment

Production deployment is not yet configured. Planned approach:

1. Host backend on **Railway** or **Render**
2. Host frontend (static build) on **Vercel** or **Netlify**
3. Managed PostgreSQL via Railway or **Supabase**
4. Set all environment variables via the hosting provider's dashboard
5. Configure Stripe webhooks to point at the production backend URL

For Apple Pay to work in production, the domain must be HTTPS and a Stripe domain verification file must be hosted at `/.well-known/apple-developer-merchantid-domain-association`.
