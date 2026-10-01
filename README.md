# API and Playwright testing

## REST Assured / TestNG
`cd qa/api && mvn test` (Java 17 and Maven). Six independent tests execute in
parallel against JSONPlaceholder: GET schema/content, owner filtering, 404,
POST echo, PATCH response and DELETE contract. TestNG HTML/XML reports:
`target/surefire-reports/`. Override with `-Dapi.baseUrl=https://...`.

JSONPlaceholder simulates writes; these tests do **not** claim persistence,
authentication or permission coverage. Schema validation is Draft 4. The API
suite is separate from Selenium so API checks require no browser.

## Playwright / TypeScript
`cd qa/playwright && npm ci && npx playwright install chromium && npm test`.
Eight tests cover login rejection, cart independence, add/remove, sorting,
required checkout fields and full purchase with item/total assertions.
Each test receives a fresh browser context; the authenticated fixture logs in
per test. No shared cart, arbitrary sleeps or test-order dependencies.
Two workers; HTML/JUnit reports, screenshots and traces retained on failure.
This covers core flows also present in Selenium, not all 40 BDD scenarios.

## Docker and CI
From repository root:
```
docker build -f qa/api/Dockerfile -t qa-api .
docker run --rm -v "$PWD/qa/api/target:/suite/target" qa-api
docker build -f qa/playwright/Dockerfile -t qa-browser .
docker run --rm --init --ipc=host -v "$PWD/qa/playwright/playwright-report:/workspace/qa/playwright/playwright-report" qa-browser
```
GitHub Actions builds both Docker images in separate jobs and uploads reports
even when tests fail. Live practice services can fail independently of code.
Do not infer passed remote CI from workflow configuration alone.

## Selenium comparison
Selenium uses Java Page Objects, explicit waits and Cucumber/TestNG. Playwright
uses TypeScript fixtures, isolated contexts, auto-waiting locators and traces.
Compare failure diagnostics and maintainability; no speed improvement is claimed
without measuring both against equivalent scenarios on the same runner.
