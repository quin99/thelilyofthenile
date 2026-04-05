# The Lily of the Nile — Claude Code Instructions

## Project Overview
A full-stack florist e-commerce application selling floral arrangements, bracelets, and trinkets.
Built solo by a fullstack engineer. Claude is both a coding partner and code reviewer on this project.

---

## Tech Stack
- **Frontend**: Angular (TypeScript)
- **Backend**: Spring Boot (Java)
- **Database**: PostgreSQL
- **Build Tools**: Maven (backend), Angular CLI (frontend)
- **Testing**: JUnit 5 (backend), Cypress (E2E frontend)
- **Version Control**: Git

---

## Project Structure
```
thelilyofthenile/
├── backend/          # Spring Boot application
│   ├── src/main/java/
│   ├── src/main/resources/
│   │   └── application.yml
│   └── pom.xml
├── frontend/         # Angular application
│   ├── src/
│   │   ├── app/
│   │   ├── assets/
│   │   └── environments/
│   ├── cypress/
│   └── package.json
└── docker-compose.yml
```

---

## Core Domain Entities
- **Product** — floral arrangements, bracelets, trinkets (name, price, category, description, images, stock)
- **Category** — Flowers, Bracelets, Trinkets, Seasonal
- **Order** — customer purchase with line items and status
- **OrderItem** — individual product in an order with quantity and price snapshot
- **Customer** — name, email, address, order history
- **Cart** — session-based or user-linked shopping cart

---

## Coding Standards

### Angular / TypeScript
- Use `OnPush` change detection on all components
- Prefer `async` pipe in templates over manual subscriptions
- Use Angular services for all HTTP calls — no direct HTTP calls from components
- Use standalone components (Angular 17+)
- RxJS: use `takeUntilDestroyed()` for subscription cleanup
- Always type your observables — no `Observable<any>`
- File naming: `product-card.component.ts`, `product.service.ts`, `product.model.ts`

### Spring Boot / Java
- Use constructor injection — never field injection (`@Autowired` on fields)
- Services handle business logic; repositories handle data access only
- Return `ResponseEntity<T>` from all controllers
- Use DTOs for API requests and responses — never expose JPA entities directly
- Use `@Valid` on all request body parameters
- Package structure: `controller`, `service`, `repository`, `model`, `dto`, `exception`

### General
- No secrets or credentials in code — use environment variables and `application.yml` profiles
- Write meaningful commit messages: `feat: add product listing page`, `fix: correct cart total calculation`
- Prefer simple solutions — do not over-engineer

---

## API Conventions
- Base path: `/api/v1`
- REST conventions: `GET /api/v1/products`, `POST /api/v1/orders`, `PUT /api/v1/products/{id}`
- Error responses: `{ "error": "message", "status": 400 }`
- Use `PageRequest` for paginated product listings

---

## Environment Setup

### Backend
```bash
cd backend
./mvnw spring-boot:run
# Runs on http://localhost:8080
```

### Frontend
```bash
cd frontend
npm install
ng serve
# Runs on http://localhost:4200
```

### Full Stack (Docker)
```bash
docker-compose up
```

---

## Testing Expectations
- New backend service methods should have a JUnit 5 unit test
- New Angular components should have a Cypress spec in `cypress/e2e/`
- Run Cypress headless: `npx cypress run`
- Run a single Cypress spec: `npx cypress run --spec "cypress/e2e/products/product-listing.cy.ts"`

---

## What Claude Should Always Do
- Plan before coding — write out the approach before writing any implementation
- Ask clarifying questions if the requirement is ambiguous before proceeding
- Follow the package structure and naming conventions above consistently
- Keep changes minimal — only touch what is necessary for the task
- Write clean, readable code — prefer clarity over cleverness
- After completing a task, provide a short summary of what changed and why

## What Claude Should Never Do
- Never put business logic inside Angular components — that belongs in services
- Never expose JPA entities directly through REST endpoints — always use DTOs
- Never hardcode URLs, credentials, or environment-specific values
- Never use `any` type in TypeScript without flagging it as technical debt
- Never skip the planning step for tasks involving multiple files or layers

---

## Lessons Learned
<!-- Claude: update this section whenever a mistake is corrected or a pattern is established -->

---

## Current Focus
<!-- Claude: update this section at the start of each working session with what we're actively building -->
```
