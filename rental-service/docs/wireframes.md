# Havenly Rental Listing Wireframes

These low-fidelity layouts describe the key screens and actions before visual styling. They cover the renter journey from discovery through booking or a property visit, the host publishing journey, and the renter's booking dashboard.

## 1. Explore homes

```text
+----------------------------------------------------------------------------------+
| havenly        [ Search city or neighborhood...              ] Explore Saved My trips |
+----------------------------------------------------------------------------------+
| Find your next address                         [Map] [More filters]              |
| [ City / neighborhood                 ] [ Move-in date ] [ Guests ]              |
| Monthly budget: [₹10,000] --slider-- [₹1,00,000]    [ Search homes ]             |
| Home type: [Apartment] [Villa] [Studio] [House]                                  |
| More filters: Size [min sqft] [max sqft]  Beds [Any v]  Baths [Any v]            |
|               Features [Parking] [Furnished] [Pet friendly] [Air conditioning]   |
|----------------------------------------------------------------------------------|
| 24 homes in Pune                         Sort by [Recommended v]  Compare (2)    |
| +--------------------------+  +--------------------------+                       |
| | [ Property photo       ] |  | [ Property photo       ] |                       |
| | ♡                  4 photos| | ♡                  3 photos|                    |
| | The Courtyard House      |  | A little house in trees  |                       |
| | Pune · Apartment         |  | Bengaluru · House        |                       |
| | 2 beds · 2 baths · 920 sqft| | 3 beds · 2 baths · 1300 sqft|                  |
| | ₹28,500 / month          |  | ₹42,000 / month          |                       |
| | [ ] Compare              |  | [ ] Compare              |                       |
| +--------------------------+  +--------------------------+                       |
|                  [ Previous ]  1  2  3  [ Next ]                                 |
+----------------------------------------------------------------------------------+
```

**Interactions:** Location suggestions select a place and update the location filter. Budget uses both minimum and maximum amounts. More filters exposes property-size bounds, bedrooms, bathrooms, and feature checkboxes. Search applies filters; cards show rent, location, size, and key features. Compare checkboxes add homes to a comparison tray, which opens a side-by-side comparison of rent, deposit, area, rooms, type, and amenities. Saving a card adds it to Saved homes.

## 2. Property details

```text
+----------------------------------------------------------------------------------+
| [< Back to explore]                                                              |
| +--------------------------------------+  +-------------------+----------------+ |
| |                                      |  | [ photo 2       ] | [ photo 3    ] | |
| |          Main property photo        |  +-------------------+----------------+ |
| |                                      |                                      | |
| +--------------------------------------+                         [View photos] | |
| Verified home     The Courtyard House                         [♡ Save] [Share]   |
| Pune, MH · Apartment                                                            |
| 2 bedrooms   2 bathrooms   920 sqft   Furnished                                |
| About this home: description and included amenities                            |
| [ Map / neighborhood ]                  [ Nearby essentials ]                  |
|----------------------------------------------------------------------------------|
| Price summary                         | Request a booking                      |
| ₹28,500 / month                       | Move-in date [ yyyy-mm-dd ]             |
| Security deposit ₹57,000              | Lease length [ 12 months v ]            |
| First payment estimate ₹85,500        | Guests [ 1 v ]                         |
|                                       | [ Request to book ]                    |
|                                       | [ Schedule a property visit ]          |
|                                       | You won't be charged until confirmed   |
+----------------------------------------------------------------------------------+
```

**Interactions:** The gallery displays all uploaded photos. Rent and deposit are visible before either action. Request to book opens the booking review screen; schedule a property visit opens the visit form. Both actions preserve the selected property and return here after completion.

## 3. Schedule a property visit

```text
+--------------------------------------------------------------+
| Schedule a visit                                      [Close] |
| The Courtyard House · Pune                                   |
| Choose a date [ yyyy-mm-dd ]                                |
| Available time [ 10:00 AM v ]                               |
| Visit type  (•) In person  ( ) Video tour                   |
| Your name [________________________]                         |
| Email      [________________________]                         |
| Phone      [________________________]                         |
| Note for the host [____________________________________]     |
| [ ] I agree to share my contact details with this host       |
|                           [ Confirm visit request ]          |
+--------------------------------------------------------------+
| Confirmation: Visit requested · date · time · property       |
| [ Add to calendar ] [ Back to property ]                      |
+--------------------------------------------------------------+
```

**Flow:** Validate required fields and a future date; submit the visit request; show a confirmation with the selected slot and a clear pending/confirmed state. If the slot is unavailable, keep entered details and allow another time to be selected.

## 4. Booking review and confirmation

```text
+----------------------------------------------------------------------------------+
| [< Property details]       Review your booking                                   |
| +-------------------------------------+  +-------------------------------------+ |
| | The Courtyard House · Pune          |  | Your stay                            | |
| | [thumbnail] 2 beds · 2 baths        |  | Move in [ yyyy-mm-dd ]               | |
| | Monthly rent              ₹28,500   |  | Lease length [ 12 months v ]         | |
| | Security deposit          ₹57,000   |  | Guests [ 1 v ]                       | |
| +-------------------------------------+  |-------------------------------------| |
|                                          | Total amount          ₹3,99,000       |
| Payment plan                             | Advance paid          [ ₹99,750 ]     |
| Advance due now              ₹99,750    | Pending balance       ₹2,99,250       |
| Remaining balance            ₹2,99,250  | Payment status        Partial         |
|                                          | [ Confirm booking request ]          |
|                                          | Payment is not processed here        |
+----------------------------------------------------------------------------------+
| Confirmation: Booking request received · Request #123 · Pending approval         |
| [ View my bookings ] [ Return to homes ]                                         |
+----------------------------------------------------------------------------------+
```

**Flow:** Confirm the property, move-in date, lease length, total, advance, and remaining balance before submission. The booking request is created with `REQUESTED` status; payment status is derived from advance and remaining balance. Show success only after the booking API confirms creation, and show a recoverable error if it fails.

## 5. My bookings

```text
+----------------------------------------------------------------------------------+
| My Havenly       [ Upcoming ] [ Past ] [ Visit requests ]                        |
|----------------------------------------------------------------------------------|
| +-------------------------------------------------------------------------------+ |
| | [ photo ] The Courtyard House · Pune                 [ Pending approval ]     | |
| | Move-in 01 Oct 2026 · 12 months · 2 guests                                   | |
| | ₹3,99,000 total   ₹99,750 advance paid   ₹2,99,250 pending   Partial          | |
| | [ View property ] [ View payment details ] [ Cancel request ]                | |
| +-------------------------------------------------------------------------------+ |
| +-------------------------------------------------------------------------------+ |
| | Visit · A little house in trees · 04 Nov 2026, 11:00 AM   [ Requested ]       | |
| | In person · Host confirmation pending          [ Change time ] [ Cancel ]     | |
| +-------------------------------------------------------------------------------+ |
| No bookings yet? [ Explore homes ]                                               |
+----------------------------------------------------------------------------------+
```

**Interactions:** Upcoming and Past group bookings by move-in date and booking status. Payment details show total, advance paid, pending balance, and payment status. Visit requests are displayed separately from confirmed bookings. Empty, loading, and API error states are provided, with a path back to Explore.

## 6. Publish a property — three-step host flow

### Step 1 of 3: Listing basics

```text
+----------------------------------------------------------------------------------+
| List your place                                           Step 1 / 3  ●──○──○   |
| Give your listing a name [____________________________________________]          |
| Property type [ Apartment v ]   Furnishing [ Unfurnished v ]                     |
| Address [_____________________________________________________________]          |
| City [________________________] State [________] PIN code [___________]          |
| Description [_________________________________________________________]          |
|             [_________________________________________________________]          |
| Bedrooms [ - 2 + ]  Bathrooms [ - 1 + ]  Area [________] sqft                    |
|                                                    [ Save draft ] [ Next: Photos ]|
+----------------------------------------------------------------------------------+
```

### Step 2 of 3: Photos and features

```text
+----------------------------------------------------------------------------------+
| List your place                                           Step 2 / 3  ●──●──○   |
| Upload up to 5 JPG or PNG photos (10 MB max each)                               |
| +-----------------------+ +-----------------------+                             |
| |     Add photos        | | [preview] [Remove]    |  Drag/drop or Browse         |
| +-----------------------+ +-----------------------+                             |
| Features and amenities                                                         |
| [ ] Wi-Fi  [ ] Parking  [ ] Air conditioning  [ ] Laundry                       |
| [ ] Pet friendly  [ ] Furnished  [ ] Balcony  [ ] Security                      |
| [ Back ]                                      [ Save draft ] [ Next: Pricing ]   |
+----------------------------------------------------------------------------------+
```

### Step 3 of 3: Pricing and publish

```text
+----------------------------------------------------------------------------------+
| List your place                                           Step 3 / 3  ●──●──●   |
| Monthly rent (₹) [________________]   Security deposit (₹) [________________]    |
| Availability [ Available v ]            Available from [ yyyy-mm-dd ]            |
|----------------------------------------------------------------------------------|
| Preview listing                                                                   |
| [cover photo]  Listing title · City · type · bedrooms · area                     |
|                ₹ rent / month · deposit · selected amenities                     |
|----------------------------------------------------------------------------------|
| [ Back to photos ]                                         [ Publish listing ]   |
+----------------------------------------------------------------------------------+
| Published: Your listing is live · [ View listing ] [ List another property ]     |
+----------------------------------------------------------------------------------+
```

**Flow:** Step 1 captures the property identity, address, description, and size. Step 2 collects photos and searchable features. Step 3 captures rent, deposit, and availability, then previews the final listing. Back and Save draft preserve entered values. Inline validation identifies missing or invalid values at the current step. Publish is disabled while saving and routes to the new listing only after the API succeeds.

## 7. Shared states and responsive behavior

- Keep primary actions visible and clearly labeled; show loading, empty, validation, success, and API failure states.
- On narrow screens, stack search controls, listing cards, property details, and price summary; keep booking and visit actions full-width and reachable.
- Preserve filter, form, and booking values when moving between steps or recovering from an error.
- Use explicit currency labels, monthly rent units, area units (`sqft`), date labels, and booking/payment status badges.
- Clearly distinguish inquiry, visit request, booking request, and payment; an inquiry does not create a booking.
