# Rental Service
## Quality Assurance Report

**Test date:** 2026-09-30  
**Overall verdict:** `PARTIAL`  
**Release recommendation:** Do not use with real renters or landlords until authentication, inquiry delivery, and search gaps are addressed.

> Core listing, property persistence, publishing, and inquiry-storage flows passed. The current app remains a prototype: credentials are not verified, some search controls are inactive, and inquiries do not reach landlords.

## Executive Summary

The application starts successfully. Four listings load from the backend, property changes persist in file-backed H2, and the inquiry endpoint stores a valid submission. Three backend integration tests pass, and the frontend production build succeeds.

Testing also found user-facing gaps in authentication, date/guest filtering, saved-list persistence, draft saving, and amenity storage. At the narrowest tested viewport, the create-listing screen has a 2 px horizontal overflow.

## Scenario Results

| Area | Situation | Result |
|---|---|---|
| Startup and listing load | Start frontend and backend; load Home and `GET /api/properties` | `PASS` - HTTP 200; four sample listings returned. |
| Property CRUD | Create, read, update, and delete a property through the Spring integration flow | `PASS` - covered by the backend test suite. |
| City and type search | Search Jaipur; select Apartment | `PASS` - one matching listing shown for each search. |
| Rent filter | Search at Rs 10,000 and Rs 100,000 | `PASS` - zero and four matches respectively. |
| Pagination | Advance from page 1 to page 2 | `PASS` - remaining two listings shown. |
| Move-in and guest filters | Set a future date and 3+ guests, then search | `FAIL` - results still included all four listings. |
| All filters control | Open All filters | `FAIL` - no dialog or filter view appeared. |
| Listing details and images | Open a home; verify visible listing images load | `PASS` - selected property details displayed; both visible images loaded at 900 px natural width. |
| Inquiry submission | Submit a name, email, and message for an existing property | `PASS` - API returned HTTP 201 with an inquiry ID; confirmation appeared. |
| Inquiry validation | Submit an empty name and an unknown property ID | `PASS` - API returned HTTP 400 and HTTP 404 respectively. |
| Inquiry persistence | Save an inquiry through Spring integration test and read it from the repository | `PASS` - covered by the backend test suite. |
| Publish validation | Publish without title, city, or rent | `PASS` - required-field message appeared. |
| Publish and cleanup | Publish temporary listing, read it back, then delete it | `PASS` - HTTP 200 create, rent read back, HTTP 204 delete, then HTTP 404 lookup. |
| Database restart | Create temporary listing, restart Spring Boot, fetch and delete it | `PASS` - record survived restart; four sample listings remained after cleanup. |
| Amenities | Select Wi-Fi and read the published property back | `FAIL` - amenity was absent from the API response. |
| Saved listings | Save a listing, view Saved, then reload | `PARTIAL` - save works in-session; saved list is empty after reload. |
| Sign-in gate | Sign out and try to list a place | `PARTIAL` - dialog opens, but an unregistered email and arbitrary nonempty password are accepted. |
| Save draft | Click Save draft on an empty form | `FAIL` - form closes and returns Home without saving a draft. |
| Responsive layouts | Check Home, detail, profile, saved, and create views at 320, 390, 768, 1024, and 1440 px | `PARTIAL` - mobile menu works and tablet grid uses two columns; create form overflows by 2 px at 320 px. No overflow measured at 390 px and above. |

## Release Findings

| Priority | Finding | Evidence |
|---|---|---|
| High | Authentication is client-side only. Any valid-format email and nonempty password are accepted; user state is stored in local storage. | Unregistered test credentials opened the protected listing form. No backend authentication was configured. |
| High | Move-in date and guest count do not filter results. | Search returned all four listings after both controls were changed. |
| High | Inquiry submission does not notify landlords. | Inquiry is stored, but there is no email delivery or host inbox. |
| Medium | Save draft exits the form without saving. | Clicking it returned to Home with no draft confirmation. |
| Medium | Saved listings disappear after reload. | Saved item was visible before reload and absent afterward. |
| Medium | Listing amenities do not persist; photo persistence is not implemented in the API/model. | Selected Wi-Fi was absent on read-back; the backend property model has no amenity or photo fields. |
| Low | Create-listing layout has a narrow-screen overflow. | At the 320 px viewport check, document width was 383 px and client width 381 px. |
| Informational | Profile reservation details and counts are demo values. | Profile view renders fixed booking information rather than API-backed reservations. |

## Responsive Coverage

| View | Tested widths | Outcome |
|---|---|---|
| Home, detail, profile, saved | 320, 390, 768, 1024, 1440 px | Layouts adapted; no horizontal overflow measured. |
| Create listing | 320, 390, 768, 1024, 1440 px | 2 px overflow at 320 px; no overflow measured at 390 px and above. |
| Navigation and listing grid | Phone and tablet sizes | Mobile menu opened and navigated; tablet listing grid displayed two columns. |

## Test Environment

| Component | Configuration |
|---|---|
| Frontend | Vite/React at `http://localhost:5173` |
| Backend | Spring Boot at `http://localhost:8082` |
| Database | File-backed H2; four sample properties available |
| Browser checks | VS Code integrated Playwright browser; scenarios run interactively |
| Backend tests | 3 passed, 0 failed |
| Frontend build | `npm.cmd --prefix rental-service\frontend run build` passed |
| Infrastructure | Docker unavailable; local H2 used |

No frontend test script, repeatable browser E2E suite, or testing-strategy document was present. Browser scenarios in this report were not saved as automated tests.

## Coverage Limits and Test Data

Real authentication and authorization, email delivery, payments, booking creation, uploaded-photo storage, and external map/geocoding behavior were not validated. Photo upload persistence is not implemented in the current API/model.

Temporary listing records were deleted; four sample listings remain. One synthetic inquiry from the browser test remains in local H2 because the application has no inquiry-delete endpoint.
