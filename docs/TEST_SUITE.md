# Test Suite Documentation — SauceDemo Automation Framework

**Application Under Test (AUT):** https://www.saucedemo.com
**Framework:** Selenium 4 · Java 11 · TestNG · Cucumber (BDD/Gherkin) · Page Object Model
**Total automated scenarios:** 40
**Suite files:** `testng.xml` (full regression) · `testng-smoke.xml` (smoke/PR gate)

## 1. Test Scope

| In scope | Out of scope |
|---|---|
| Login (positive/negative), Inventory, Cart, Checkout, End-to-End purchase flow | Payment gateway integration (SauceDemo mocks this), API layer, mobile app, cross-browser matrix beyond Chrome/Firefox |

## 2. Tagging Strategy

| Tag | Meaning | Used for |
|---|---|---|
| `@smoke` | Critical path, must pass before merge | PR gate (`testng-smoke.xml`) |
| `@regression` | Full functional coverage | Nightly/full run (`testng.xml`) |
| `@positive` | Happy-path scenario | Reporting/filtering |
| `@negative` | Invalid-input / error-path scenario | Reporting/filtering |
| `@e2e` | Multi-page, cross-feature journey | Release-candidate gate |

## 3. Test Case Catalogue

### 3.1 Login — `login.feature` (10 scenarios)

| ID | Scenario | Type | Priority |
|---|---|---|---|
| LOGIN-01 | Successful login with standard user | Positive / Smoke | High |
| LOGIN-02 | Successful login with problem user | Positive | Medium |
| LOGIN-03 | Successful login with performance glitch user | Positive | Medium |
| LOGIN-04 | Login blocked for locked out user | Negative | High |
| LOGIN-05 | Login fails with invalid password | Negative | High |
| LOGIN-06 | Login fails with invalid username | Negative | High |
| LOGIN-07 | Login fails with empty username | Negative | Medium |
| LOGIN-08 | Login fails with empty password | Negative | Medium |
| LOGIN-09 | Login fails with both fields empty | Negative | Medium |
| LOGIN-10 | Logout returns user to the login page | Positive | High |

### 3.2 Inventory — `inventory.feature` (10 scenarios)

| ID | Scenario | Type | Priority |
|---|---|---|---|
| INV-01 | Inventory page displays all six products | Positive / Smoke | High |
| INV-02 | Add a single item to the cart | Positive | High |
| INV-03 | Add multiple items to the cart | Positive | High |
| INV-04 | Remove an item from the inventory page after adding it | Positive | Medium |
| INV-05 | Cart badge is absent when cart is empty | Positive | Low |
| INV-06 | Sort products by name A to Z | Positive | Medium |
| INV-07 | Sort products by name Z to A | Positive | Medium |
| INV-08 | Sort products by price low to high | Positive | Medium |
| INV-09 | Sort products by price high to low | Positive | Medium |
| INV-10 | Navigating to the cart from the inventory page | Positive | Medium |

### 3.3 Cart — `cart.feature` (6 scenarios)

| ID | Scenario | Type | Priority |
|---|---|---|---|
| CART-01 | Cart correctly reflects a single added item | Positive / Smoke | High |
| CART-02 | Cart correctly reflects multiple added items | Positive | High |
| CART-03 | Remove an item directly from the cart page | Positive | Medium |
| CART-04 | Continue shopping returns to the inventory page | Positive | Low |
| CART-05 | Proceeding to checkout with an empty cart still reaches checkout info | Negative/Edge | Medium |
| CART-06 | Proceed to checkout from a populated cart | Positive | High |

### 3.4 Checkout — `checkout.feature` (10 scenarios)

| ID | Scenario | Type | Priority |
|---|---|---|---|
| CHK-01 | Complete checkout information with valid data | Positive / Smoke | High |
| CHK-02 | Checkout fails when first name is missing | Negative | High |
| CHK-03 | Checkout fails when last name is missing | Negative | High |
| CHK-04 | Checkout fails when postal code is missing | Negative | High |
| CHK-05 | Checkout overview shows correct item count | Positive | Medium |
| CHK-06 | Checkout overview total equals subtotal plus tax | Positive | High |
| CHK-07 | Successfully complete an order | Positive / Smoke | High |
| CHK-08 | Cancel from checkout information returns to cart | Positive | Low |
| CHK-09 | Cancel from checkout overview returns to cart | Positive | Low |
| CHK-10 | Back to products from order confirmation returns to inventory | Positive | Low |

### 3.5 End-to-End — `e2e_purchase.feature` (4 scenarios)

| ID | Scenario | Type | Priority |
|---|---|---|---|
| E2E-01 | Full purchase flow with a single item | Positive / Smoke | Critical |
| E2E-02 | Full purchase flow with multiple items | Positive | Critical |
| E2E-03 | Full purchase flow followed by logout | Positive | High |
| E2E-04 | Full purchase flow with sorted inventory selection | Positive | Medium |

**Total: 10 + 10 + 6 + 10 + 4 = 40 automated scenarios**

## 4. Traceability (Feature → Page Objects → Step Definitions)

| Feature file | Page Objects exercised | Step definition class |
|---|---|---|
| login.feature | LoginPage, InventoryPage | LoginSteps |
| inventory.feature | InventoryPage, CartPage | InventorySteps, LoginSteps |
| cart.feature | CartPage, CheckoutInfoPage | CartSteps, LoginSteps |
| checkout.feature | CheckoutInfoPage, CheckoutOverviewPage, CheckoutCompletePage | CheckoutSteps |
| e2e_purchase.feature | All pages | LoginSteps, InventorySteps, CartSteps, CheckoutSteps |

## 5. Execution Matrix

| Suite | Command | Runs |
|---|---|---|
| Smoke | `mvn clean test -Dsurefire.suiteXmlFiles=testng-smoke.xml` | `@smoke` only (~8 scenarios) |
| Full regression | `mvn clean test` | All 40 scenarios |
| Headless | append `-Dheadless=true` | Any suite |
| Cross-browser | append `-Dbrowser=firefox` | Any suite |

## 6. Known Gaps / Suggested Next Additions

These weren't in the original ask but are standard for a "real" automation project on a resume/repo — flagged rather than silently added everywhere:

- **Data-driven tests** via Cucumber `Scenario Outline` + `Examples` (e.g., parametrize LOGIN-05..09 into one outline) — kept as separate scenarios here for readability, but an outline version is a good "v2" commit to show iteration.
- **API-layer checks** — SauceDemo has no public API, so this repo is UI-only. If you add a second AUT with an API, layering `RestAssured` checks alongside UI ones is a strong signal of maturity.
- **Cross-browser grid** (Selenium Grid / BrowserStack) — currently local Chrome/Firefox only.
- **Allure reporting** — Extent/Cucumber HTML report is included; Allure is a common alternative worth adding if targeting roles that specifically ask for it.
- **Visual regression** (e.g., Applitools) — not included; mention only if the JD asks for it.
