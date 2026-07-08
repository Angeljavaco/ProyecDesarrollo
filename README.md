# ProyecDesarrollo

Proyecto full stack organizado en dos aplicaciones principales: un backend con Spring Boot y un frontend con Angular.

## Estructura del proyecto

```text
ProyecDesarrollo/
├── backend/
│   ├── src/
│   ├── build.gradle
│   ├── settings.gradle
│   ├── gradlew
│   ├── gradlew.bat
│   └── .env
├── frontend/
│   ├── src/
│   ├── angular.json
│   ├── package.json
│   └── proxy.conf.json
└── README.md
```

## Ejecutar el backend

Desde la raiz del proyecto:

```bash
cd backend
./gradlew bootRun
```

En Windows tambien puedes usar:

```bash
cd backend
.\gradlew.bat bootRun
```

El backend usa las variables definidas en `backend/.env` para conectarse a PostgreSQL.

## Ejecutar el frontend

Desde la raiz del proyecto:

```bash
cd frontend
npm install
npm start
```

El frontend Angular levanta en `http://localhost:4200` y usa `proxy.conf.json` para redirigir las rutas `/api` al backend en `http://localhost:8080`.

## Tecnologias utilizadas

- Java 21
- Spring Boot 3.3.2
- Spring Security
- Spring Data JPA
- PostgreSQL
- Gradle
- Angular 19
- Bootstrap 5
- TypeScript
