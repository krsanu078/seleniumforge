# SeleniumForge

[![Java](https://img.shields.io/badge/Java-17-blue)](https://www.oracle.com/java/)
[![Maven](https://img.shields.io/badge/Maven-3.x-blueviolet)](https://maven.apache.org/)
[![Selenium](https://img.shields.io/badge/Selenium-4.x-green)](https://www.selenium.dev/)

Production-ready, enterprise-grade Selenium Automation Framework implemented in Java (seleniumforge). This repository provides a scalable, maintainable, and configurable automation framework suitable for SDETs and QA teams.

---

## Project Overview

SeleniumForge is a modular Selenium-based automation framework built with Java 17, Maven, Selenium 4, TestNG and industry-grade utilities. It follows SOLID principles, Clean Code conventions and is intended to be used in real-world CI pipelines.

Goals:
- Provide a stable ThreadLocal WebDriver implementation for parallel execution.
- Centralize configuration through a singleton ConfigReader.
- Provide reusable utilities (explicit waits, screenshots, Excel/JSON readers).
- Integrate reporting (Extent Reports) and logging (Log4j2).
- Offer Page Object Model (POM) pages for a sample AUT (SauceDemo) with clear business-method APIs.
- Support cross-browser and headless execution via WebDriverManager.

---

## Architecture (Mermaid)

```mermaid
flowchart TD
  A[Tests (test/*)] -->|use| B[BaseTest]
  B -->|initializes| C[DriverFactory / ThreadLocal]
  C -->|creates| D[BrowserFactory]
  B -->|uses| E[Pages]
  E -->|use| F[BasePage]
  B -->|logs| G[Log4j2]
  B -->|reports| H[ExtentReports]
  I[Utilities] -->|provide| E
  I -->|provide| B
  subgraph Core
    B
    C
    D
    F
    E
  end
  subgraph Support
    I[Utilities]
    G[Log4j2]
    H[ExtentReports]
    J[Listeners]
    K[ConfigReader]
  end
```

---

## Folder Structure

seleniumforge
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com.seleniumforge
│   │   │       ├── base
│   │   │       ├── config
│   │   │       ├── constants
│   │   │       ├── driver
│   │   │       ├── pages
│   │   │       ├── reports
│   │   │       ├── listeners
│   │   │       ├── utilities
│   │   │       ├── exceptions
│   │   │       ├── enums
│   │   │       └── factories
│   └── resources
│       ├── config.properties
│       ├── log4j2.xml
│       └── testdata.xlsx
├── test
│   ├── java
│   │   └── (test classes organized by packages and groups)
│   ├── smoke
│   ├── regression
│   └── sanity
├── screenshots
├── reports
├── logs
├── pom.xml
├── testng.xml
├── README.md
└── .github/workflows/ci.yml

Note: The repository uses a ThreadLocal WebDriver strategy and Page Object Model. Tests should inherit from BaseTest.

---

## Framework Features

- ThreadLocal WebDriver (parallel-safe)
- BrowserFactory with WebDriverManager (Chrome, Firefox, Edge, headless support)
- Singleton ConfigReader for centralized configuration
- BaseTest with setup/teardown and automatic navigation to base URL
- BasePage (planned) with reusable wrappers (click, type, jsClick, waits, frame/window handling)
- PageObjects for AUT (SauceDemo) — business methods only
- Utilities: WaitUtil, ScreenshotUtil, ExcelUtil, JsonUtil
- Reporting: Extent Reports with environment/system info and screenshots
- Logging: Log4j2 configuration (console + file)
- Retry Analyzer and TestNG listeners for robust execution
- CI integration via GitHub Actions (CI workflow will run maven test and archive reports)

---

## Tech Stack

- Java 17
- Maven
- Selenium 4
- TestNG
- WebDriverManager
- Extent Reports
- Log4j2
- Apache POI (Excel)
- Jackson (JSON)

---

## Design Patterns Used

- Singleton (ConfigReader, ExtentManager)
- Factory (BrowserFactory, DriverFactory)
- Page Object Model (Pages inherit BasePage)
- ThreadLocal (DriverFactory) for parallel-safe WebDriver instances
- Utility classes (stateless helpers)

---

## How to Run

Prerequisites:
- Java 17 installed and JAVA_HOME configured
- Maven 3.6+

Build and compile (without tests):

```
mvn -U -DskipTests package
```

Run full test suite:

```
mvn test
```

Run a specific TestNG group (e.g., smoke):

```
mvn test -Dgroups=smoke
```

Or run a specific suite XML (once created):

```
mvn -Dtestng.suiteXmlFiles=smoke.xml test
```

Notes:
- Configure `src/main/resources/config.properties` to set `browser`, `url`, `environment`, `timeouts`, `headless`, `parallel`.
- For parallel runs, set `parallel=true` in config and configure TestNG suite to use `parallel="methods"` or `classes` as needed.

---

## How to Execute Smoke Suite

Two options:
1) Using TestNG groups (recommended):
```
mvn test -Dgroups=smoke
```
2) Using a dedicated suite XML (if present):
```
mvn -Dtestng.suiteXmlFiles=test/suites/smoke.xml test
```

---

## How to Execute Regression Suite

```
mvn test -Dgroups=regression
```
or with suite XML:

```
mvn -Dtestng.suiteXmlFiles=test/suites/regression.xml test
```

---

## Parallel Execution

- ThreadLocal WebDriver enables parallel tests without WebDriver collisions.
- Configure TestNG suite with `parallel="methods|classes|tests"` and `thread-count`.
- Set `parallel=true` in `config.properties` (the framework reads this flag for optional behavior).

Example TestNG snippet:

```xml
<suite name="ParallelSuite" parallel="methods" thread-count="4">
  <test name="Regression">
    <classes>
      <class name="tests.YourTestClass"/>
    </classes>
  </test>
</suite>
```

---

## Extent Report (placeholder)

Reports are generated in the `reports/` directory as `ExtentReport_<timestamp>.html` once tests run. Screenshots are saved in `screenshots/` and embedded in the Extent report for failures and skips.

![ExtentReportPlaceholder](docs/extent-placeholder.png)

---

## GitHub Actions

CI pipeline (to be implemented) will:
- Run on push and pull request
- Setup JDK 17
- Cache Maven dependencies
- Run `mvn clean test`
- Upload/Archive Extent Reports as build artifacts

A workflow is planned at `.github/workflows/ci.yml`.

---

## Future Roadmap

- Complete BasePage and POMs for SauceDemo and additional demo sites
- Add data-driven examples (data providers, Excel, JSON)
- Add Cross-browser matrix in GitHub Actions (matrix strategy)
- Add Dockerized test execution
- Add integration with test case management / Jira
- Add Docker/BrowserStack/SauceLabs integration for cloud runs

---

## License

This repository is released under the MIT License. See `LICENSE` for details.

---

## Contribution Guide

- Fork the repo and create a feature branch (feature/<short-description>)
- Follow Java 17, Clean Code and SOLID principles
- Add unit/automation tests and update test suites
- Update `CHANGELOG.md` for notable changes
- Open a PR with a clear description and link to related issues

---

If you want, I will now implement Phase 2 (BasePage) following the existing code style and packages, ensuring all Page Objects inherit from it. Reply `proceed` to continue.
