# Render Deployment (Docker)

## 1) Database schema
Run `schema.sql` on your MySQL database:

```sql
SOURCE schema.sql;
```

If `SOURCE` is not available, copy/paste the SQL manually in your MySQL client.

## 2) Create Render Web Service
1. Push this repo to GitHub.
2. In Render, create a new `Web Service`.
3. Select `Deploy from render.yaml` (Blueprint) or select Docker runtime manually.

## 3) Set environment variables on Render
- `PORT=10000`
- `MYSQLHOST=<your-db-host>`
- `MYSQLPORT=4000`
- `MYSQLDATABASE=voting_system`
- `MYSQLUSER=<your-db-user>`
- `MYSQLPASSWORD=<your-mysql-password>`
- `MYSQL_SSL_MODE=REQUIRED`
- `ADMIN_DEFAULT_PASSWORD=<set-strong-password>`
- `TWILIO_ACCOUNT_SID=<optional>`
- `TWILIO_AUTH_TOKEN=<optional>`
- `TWILIO_FROM_NUMBER=<optional>`

## 4) Deploy
Render will build with `Dockerfile` and run:

```bash
java -jar /app/app.jar
```
