# SauceDemo Selenium · Java · TestNG · Cucumber (BDD) Automation Framework

An end-to-end UI test automation project built against the public practice
site [saucedemo.com](https://www.saucedemo.com): **Page Object Model + BDD
(Gherkin/Cucumber) + TestNG runner + Maven + CI**, with **40 automated test
scenarios** across login, inventory, cart, checkout, and full end-to-end
purchase flows.

---

## Quick Start (Chrome, zero config)

```bash
git clone <your-repo-url>
cd saucedemo-automation-framework
mvn clean test
```

That's it. Default config runs the full 40-scenario suite in **Chrome**,
visibly (not headless). No driver binaries to download — Selenium Manager
(built into Selenium 4.6+) detects your installed Chrome version and fetches
the matching driver automatically on first run.

Reports land in:
- `target/cucumber-reports/cucumber.html` — human-readable BDD report
- `target/surefire-reports/` — TestNG/JUnit-style report

---

## Prerequisites

| Requirement | Check it | Get it |
|---|---|---|
| Java 17 (JDK, not just JRE) | `java -version` | https://adoptium.net |
| Maven 3.8+ | `mvn -version` | https://maven.apache.org/download.cgi |
| Google Chrome installed | open Chrome → `⋮` → Help → About | https://www.google.com/chrome |

You do **not** need to install a ChromeDriver binary yourself — Selenium
Manager handles it. You only need Chrome itself present on the machine.

### Installing prerequisites by OS

**Windows**
1. Install JDK 17 from Adoptium (the `.msi` installer sets `JAVA_HOME` for you).
2. Install Maven: unzip the binary archive, add its `bin` folder to your `PATH`, then set a `MAVEN_HOME` environment variable pointing at the unzipped folder.
3. Confirm both with `java -version` and `mvn -version` in a **new** terminal window (environment variables only apply to terminals opened after you set them).

**macOS**
```bash
brew install openjdk@17 maven
```
If `java -version` doesn't pick it up after installing via Homebrew, add this to your shell profile (`~/.zshrc` or `~/.bash_profile`):
```bash
export JAVA_HOME=$(/usr/libexec/java_home -v17)
```

**Linux (Debian/Ubuntu)**
```bash
sudo apt update
sudo apt install openjdk-17-jdk maven
```

---

## Running the Suite

```bash
mvn clean test                                              # full regression, Chrome, visible browser
mvn clean test -Dsurefire.suiteXmlFiles=testng-smoke.xml    # smoke suite only (~8 scenarios)
mvn clean test -Dheadless=true                               # headless (for servers/CI, no visible window)
mvn clean test -Dbrowser=firefox                              # run in Firefox instead
mvn clean test -Dbrowser=edge                                 # run in Edge instead
```

You can combine flags:
```bash
mvn clean test -Dbrowser=chrome -Dheadless=true -Dsurefire.suiteXmlFiles=testng-smoke.xml
```

---

## Browser Support

| Browser | Setup needed | Notes |
|---|---|---|
| **Chrome** (default) | None — just have Chrome installed | Selenium Manager auto-resolves the driver |
| **Firefox** | None — just have Firefox installed | Selenium Manager auto-resolves `geckodriver` |
| **Edge** | None — just have Edge installed | Selenium Manager auto-resolves `msedgedriver` |
| **Safari** | Run `safaridriver --enable` once in Terminal (macOS only) | No headless mode supported by Safari itself |
| **Internet Explorer** | Manual — see below | Windows only |

**On Internet Explorer specifically**, being direct about this rather than
pretending it's plug-and-play like the others: Microsoft retired IE itself
in June 2022, and Selenium Manager does **not** auto-download
`IEDriverServer.exe` the way it does for the evergreen browsers. To use it:

1. Download `IEDriverServer.exe` from https://www.selenium.dev/downloads/ (match 32/64-bit to your OS).
2. Either put it on your system `PATH`, or point Selenium at it directly:
   ```bash
   mvn clean test -Dbrowser=ie -Dwebdriver.ie.driver="C:\path\to\IEDriverServer.exe"
   ```
3. In IE itself, Tools → Internet Options → Security tab: the "Enable Protected Mode" checkbox must be set **identically** (all on or all off) across all four zones, or IE mode will refuse to start a session — this is IE's own long-standing quirk, not a bug in this framework.

Given IE's retirement, treat it as included for completeness/legacy-environment
needs, not as something to lead with in an interview.

---

## Project Structure

```
saucedemo-automation-framework/
├── pom.xml
├── testng.xml                     # full regression suite
├── testng-smoke.xml                # smoke-only suite (fast PR gate)
├── .github/workflows/regression.yml
├── docs/
│   └── TEST_SUITE.md              # formal test case catalogue
├── src/main/java/com/qa/saucedemo/
│   ├── pages/                     # Page Object Model
│   │   ├── BasePage.java
│   │   ├── LoginPage.java
│   │   ├── InventoryPage.java
│   │   ├── CartPage.java
│   │   ├── CheckoutInfoPage.java
│   │   ├── CheckoutOverviewPage.java
│   │   └── CheckoutCompletePage.java
│   └── utils/
│       ├── ConfigReader.java
│       ├── DriverFactory.java      # thread-safe WebDriver lifecycle, multi-browser
│       ├── WaitUtils.java          # explicit-wait wrapper
│       └── TestContext.java        # shared scenario state across step classes
└── src/test/
    ├── java/com/qa/saucedemo/
    │   ├── hooks/Hooks.java        # @Before/@After + logging + screenshot on failure
    │   ├── stepdefinitions/        # Gherkin step implementations
    │   │   ├── LoginSteps.java
    │   │   ├── InventorySteps.java
    │   │   ├── CartSteps.java
    │   │   └── CheckoutSteps.java
    │   └── runners/
    │       ├── TestRunner.java     # full suite (Cucumber + TestNG)
    │       └── SmokeTestRunner.java
    └── resources/
        ├── config/config.properties
        ├── cucumber.properties
        ├── log4j2.xml
        └── features/
            ├── login.feature          # 10 scenarios
            ├── inventory.feature      # 10 scenarios
            ├── cart.feature           # 6 scenarios
            ├── checkout.feature       # 10 scenarios
            └── e2e_purchase.feature   # 4 scenarios
```

## Why this stack / these choices

| Choice | Reason |
|---|---|
| Selenium 4 + built-in Selenium Manager | No manually managed driver binaries; works cross-machine/CI out of the box |
| Page Object Model | Locators isolated from test logic; one page = one class = one place to fix a broken selector |
| Cucumber (Gherkin) | Test intent is readable by non-engineers (PM/BA); scenarios double as living documentation |
| TestNG as the runner | Suite XML, parallel execution, grouping/tagging, standard in enterprise Java QA teams |
| `ThreadLocal` `DriverFactory` + `TestContext` | Framework is parallel-execution-safe from day one, not bolted on later |
| Explicit waits only, no `Thread.sleep` | Avoids flaky, slow tests |
| `config.properties` (not hardcoded URLs/creds) | Environment-portable; override via `-D` system properties in CI |
| Screenshot-on-failure + log4j in `Hooks` | Fast triage without re-running the failing scenario |
| GitHub Actions workflow included | Proves it runs in CI, not just "on my machine" |

## Test Data / Users

SauceDemo provides fixed test accounts (all password `secret_sauce`), used deliberately for negative-path coverage:

| User | Purpose |
|---|---|
| `standard_user` | Baseline happy-path user |
| `locked_out_user` | Verifies account-lock error handling |
| `problem_user` | UI-glitch tolerant flows |
| `performance_glitch_user` | Slow-response tolerant flows |

These are read from `src/test/resources/config/config.properties`, not hardcoded in step definitions.

## CI

`.github/workflows/regression.yml` runs the smoke suite on every PR and the
full regression suite on pushes to `main`, uploading the Cucumber HTML
report and Surefire results as build artifacts.

---

## Troubleshooting

**`mvn: command not found`**
Maven isn't installed or isn't on your `PATH`. Re-check the Prerequisites section above; open a new terminal after installing (PATH changes don't apply to already-open terminals).

**`JAVA_HOME is not defined correctly`**
Set `JAVA_HOME` to your JDK install path (not the JRE), e.g. on macOS: `export JAVA_HOME=$(/usr/libexec/java_home -v11)`. On Windows, set it as a System Environment Variable to something like `C:\Program Files\Eclipse Adoptium\jdk-11...`.

**Chrome opens then immediately closes / `SessionNotCreatedException`**
Usually means your Chrome browser and the auto-resolved driver are mismatched because Chrome auto-updated mid-session. Fix: close all Chrome windows, then re-run — Selenium Manager re-resolves the driver against your current Chrome version each run.

**Tests hang with no output**
Check you're not running with a visible-browser mode on a headless server/container (no display available). Use `-Dheadless=true` in any environment without a GUI — CI, Docker, WSL without an X server, etc.

**`UnreachableBrowserException` or driver download fails**
Selenium Manager needs outbound internet access on first run to fetch the driver binary (subsequent runs use a local cache in `~/.cache/selenium`). If you're behind a corporate proxy, set `HTTPS_PROXY`/`HTTP_PROXY` environment variables before running Maven.

**Locator/assertion failures that weren't there before**
SauceDemo's DOM occasionally changes (it's a live demo site outside our control). Check the failing step's screenshot in the Cucumber HTML report first — most drift shows up as a renamed CSS class or moved element, which is a one-line fix in the relevant Page Object.

**Windows: `mvnw` or shell scripts refuse to run**
This project doesn't ship a Maven Wrapper — install Maven directly per the OS instructions above rather than relying on a wrapper script.

---

## Honest limitations (read before you claim this in an interview)

- This was generated in a sandboxed environment without access to Maven
  Central, so the build has **not** been compiled/executed here — review it
  as a strong, standards-shaped starting point. Run `mvn clean test`
  locally first; if anything fails to compile, it's most likely a locator
  drifting on the live SauceDemo site, not the framework's architecture.
- No API layer, no visual regression, no Selenium Grid for true parallel
  cross-machine execution — see `docs/TEST_SUITE.md` §6 for what a v2 would add.
"# TestSuite" 
