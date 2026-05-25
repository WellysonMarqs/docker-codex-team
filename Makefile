.PHONY: backend-lint backend-build backend-test frontend-install frontend-lint frontend-build frontend-test app-up app-down

backend-lint:
	cd apps/backend && mvn checkstyle:check

backend-build:
	cd apps/backend && mvn package -DskipTests

backend-test:
	cd apps/backend && mvn test

frontend-install:
	cd apps/frontend && npm install

frontend-lint:
	cd apps/frontend && npm run lint

frontend-build:
	cd apps/frontend && npm run build

frontend-test:
	cd apps/frontend && npm test

app-up:
	docker compose --profile app up --build

app-down:
	docker compose --profile app down
