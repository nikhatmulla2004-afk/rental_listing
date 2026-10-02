# Havenly frontend

React + Vite client for the rental service MVC views.

## Run

```powershell
npm.cmd install
npm.cmd run dev
```

The Vite server proxies `/api` requests to the Spring Boot service at `http://localhost:8082`. Start the backend first for live property data; the UI includes curated fallback data so the browse experience remains usable while the API is offline.

## Deploy on Vercel

Deploy this directory as the frontend project. Select the directory containing this `package.json` as the Vercel Root Directory, use the Vite preset, set the build command to `npm run build`, and set the output directory to `dist`.

Add this Vercel environment variable for Preview and Production:

```text
VITE_API_BASE_URL=https://<your-hosted-backend>/api
```

The value is embedded during the Vite build, so redeploy after changing it. When it is unset, the app uses `/api`; that path is only proxied to localhost by the development server.

Vercel will host the frontend, not this Spring Boot server. Host the backend separately and configure its CORS policy for the Vercel domain. The current file-backed H2 database is for local development; use a managed persistent database for a hosted backend. Do not put database credentials or other secrets in `VITE_` variables because they are public in the built frontend.

### Render backend

The workspace-root `render.yaml` defines a Docker-based Spring Boot API and a Render Postgres database. Push the full workspace, including this frontend and the `rental-service` backend directory, to a Git repository. In the Render Dashboard, create a **Blueprint** from that repository and select the workspace root where `render.yaml` lives. The Blueprint uses the free plans for a demo.

After the API deploys, copy its `onrender.com` URL into the Vercel project's `VITE_API_BASE_URL` environment variable, appending `/api` (for example, `https://rental-service-api.onrender.com/api`), then redeploy the frontend.

Free Render web services can sleep when idle, so the first request may take about a minute. Free Render Postgres expires after 30 days; export or migrate the data before then if you need to keep it. The `render` Spring profile uses Postgres; H2 remains the local development database.

## Views

- Browse: search, filters, responsive listing grid, skeleton and empty states
- Details: gallery, property specifications, neighborhood panel, pricing and inquiry form
- Create: validated multi-section listing form with upload preview boundary
- Profile: reservation dashboard and account summary
