git remote set-url origin https://github.com/YOUR_GITHUB_USERNAME/seleniumforge.git git remote -v

#!/bin/bash set -euo pipefail

Branch name
BRANCH="feature/phases7-13" REMOTE="origin" # change if your fork remote is named differently

echo "Ensure your working tree is clean and you're on the correct fork clone." read -p "Proceed? (y/N): " confirm if [[ "$confirm" != "y" && "$confirm" != "Y" ]]; then echo "Aborted." exit 1 fi

echo "Creating branch $BRANCH" git checkout -b "$BRANCH"

Create directories (if not exist)
mkdir -p src/main/resources mkdir -p src/main/java/com/seleniumforge/constants mkdir -p src/main/java/com/seleniumforge/enums mkdir -p src/main/java/com/seleniumforge/utilities mkdir -p test/suites mkdir -p .github/workflows mkdir -p .github/ISSUE_TEMPLATE

echo "Writing files..."

cat > src/main/resources/log4j2.xml <<'EOL'

<?xml version="1.0" encoding="UTF-8"?> <Configuration status="WARN"> <Appenders> <Console name="Console" target="SYSTEM_OUT"> <PatternLayout pattern="%d{yyyy-MM-dd HH:mm:ss} %-5p %c{1}:%L - %m%n"/> </Console>
Code
<RollingFile name="RollingFile" fileName="logs/framework.log" filePattern="logs/framework-%d{yyyy-MM-dd}-%i.log.gz">
  <PatternLayout pattern="%d{yyyy-MM-dd HH:mm:ss} %-5p %c{1}:%L - %m%n"/>
  <Policies>
    <TimeBasedTriggeringPolicy />
    <SizeBasedTriggeringPolicy size="10MB" />
  </Policies>
  <DefaultRolloverStrategy max="14" />
</RollingFile>

<Async name="Async">
  <AppenderRef ref="Console" />
  <AppenderRef ref="RollingFile" />
</Async>
</Appenders> <Loggers> <Logger name="com.seleniumforge" level="info" additivity="false"> <AppenderRef ref="Console" /> <AppenderRef ref="RollingFile" /> </Logger>
Code
<Root level="warn">
  <AppenderRef ref="Console" />
  <AppenderRef ref="RollingFile" />
</Root>
</Loggers> </Configuration> EOL
cat > src/main/java/com/seleniumforge/constants/FrameworkConstants.java <<'EOL' package com.seleniumforge.constants;

/**

FrameworkConstants holds commonly used constant values for the framework. */ public final class FrameworkConstants {

private FrameworkConstants() { // utility }

public static final String REPORTS_DIR = "reports"; public static final String SCREENSHOTS_DIR = "screenshots"; public static final String LOGS_DIR = "logs"; public static final String TEST_DATA_DIR = "src/test/resources"; public static final String EXTENT_REPORT_PREFIX = "ExtentReport_"; } EOL

cat > src/main/java/com/seleniumforge/enums/Environment.java <<'EOL' package com.seleniumforge.enums;

/**

Supported test environments. */ public enum Environment { LOCAL, QA, STAGING, PROD } EOL
cat > src/main/java/com/seleniumforge/utilities/LogHelper.java <<'EOL' package com.seleniumforge.utilities;

import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger;

/**

LogHelper standardizes log messages with contextual information. */ public final class LogHelper {

private static final Logger LOGGER = LogManager.getLogger("com.seleniumforge");

private LogHelper() { // utility }

public static void info(String context, String message) { LOGGER.info("[{}] {}", context, message); }

public static void debug(String context, String message) { LOGGER.debug("[{}] {}", context, message); }

public static void error(String context, String message, Throwable ex) { LOGGER.error("[{}] {}", context, message, ex); } } EOL

cat > src/main/java/com/seleniumforge/utilities/RandomDataGenerator.java <<'EOL' package com.seleniumforge.utilities;

import org.apache.commons.lang3.RandomStringUtils;

/**

RandomDataGenerator provides small utilities to generate random test data. */ public final class RandomDataGenerator {

private RandomDataGenerator() { }

public static String randomAlphaNumeric(int length) { return RandomStringUtils.randomAlphanumeric(length); }

public static String randomNumeric(int length) { return RandomStringUtils.randomNumeric(length); } } EOL

cat > test/suites/smoke.xml <<'EOL'

<?xml version="1.0" encoding="UTF-8"?> <!DOCTYPE suite SYSTEM "https://testng.org/testng-1.0.dtd"> <suite name="Smoke Suite" verbose="1" parallel="false"> <test name="Smoke Tests"> <groups> <run> <include name="smoke"/> </run> </groups> <classes> <class name="com.seleniumforge.tests.LoginTest"/> </classes> </test> </suite> EOL
cat > test/suites/regression.xml <<'EOL'

<?xml version="1.0" encoding="UTF-8"?> <!DOCTYPE suite SYSTEM "https://testng.org/testng-1.0.dtd"> <suite name="Regression Suite" verbose="1" parallel="false"> <test name="Regression Tests"> <groups> <run> <include name="regression"/> </run> </groups> <classes> <class name="com.seleniumforge.tests.InvalidLoginTest"/> <class name="com.seleniumforge.tests.SearchProductTest"/> <class name="com.seleniumforge.tests.AddToCartTest"/> <class name="com.seleniumforge.tests.RemoveFromCartTest"/> <class name="com.seleniumforge.tests.CheckoutTest"/> <class name="com.seleniumforge.tests.LogoutTest"/> </classes> </test> </suite> EOL
cat > test/suites/sanity.xml <<'EOL'

<?xml version="1.0" encoding="UTF-8"?> <!DOCTYPE suite SYSTEM "https://testng.org/testng-1.0.dtd"> <suite name="Sanity Suite" verbose="1" parallel="false"> <test name="Sanity Tests"> <groups> <run> <include name="sanity"/> </run> </groups> <classes> </classes> </test> </suite> EOL
cat > test/suites/crossbrowser.xml <<'EOL'

<?xml version="1.0" encoding="UTF-8"?> <!DOCTYPE suite SYSTEM "https://testng.org/testng-1.0.dtd"> <suite name="CrossBrowser Suite" verbose="1" parallel="false"> <test name="Cross Browser Tests"> <parameter name="browser" value="chrome"/> <classes> <class name="com.seleniumforge.tests.LoginTest"/> <class name="com.seleniumforge.tests.AddToCartTest"/> </classes> </test> </suite> EOL
cat > test/suites/parallel.xml <<'EOL'

<?xml version="1.0" encoding="UTF-8"?> <!DOCTYPE suite SYSTEM "https://testng.org/testng-1.0.dtd"> <suite name="Parallel Suite" verbose="1" parallel="methods" thread-count="4"> <test name="Parallel Tests"> <classes> <class name="com.seleniumforge.tests.LoginTest"/> <class name="com.seleniumforge.tests.AddToCartTest"/> <class name="com.seleniumforge.tests.CheckoutTest"/> </classes> </test> </suite> EOL
cat > .github/workflows/ci.yml <<'EOL' name: CI

on: push: branches: [ main ] pull_request: branches: [ main ]

jobs: build: runs-on: ubuntu-latest

Code
steps:
  - name: Checkout repo
    uses: actions/checkout@v4

  - name: Set up JDK 17
    uses: actions/setup-java@v4
    with:
      distribution: temurin
      java-version: '17'

  - name: Cache Maven packages
    uses: actions/cache@v4
    with:
      path: ~/.m2/repository
      key: ${{ runner.os }}-maven-${{ hashFiles('**/pom.xml') }}
      restore-keys: |
        ${{ runner.os }}-maven-

  - name: Build with Maven
    run: mvn -B -U -DskipTests package

  - name: Run tests
    run: mvn -B test -Dheadless=true

  - name: Archive Extent Report
    if: always()
    uses: actions/upload-artifact@v4
    with:
      name: extent-report
      path: reports/archive/*.zip

  - name: Upload raw reports
    if: always()
    uses: actions/upload-artifact@v4
    with:
      name: reports-html
      path: reports/*.html
EOL

cat > CONTRIBUTING.md <<'EOL'

Contributing to SeleniumForge
Thank you for your interest in contributing to SeleniumForge. By contributing, you agree to follow the project's code of conduct. EOL

cat > CODE_OF_CONDUCT.md <<'EOL'

Code of Conduct
Be respectful and professional. EOL

cat > LICENSE <<'EOL' MIT License

Copyright (c) 2026 Sanu kumar EOL

cat > CHANGELOG.md <<'EOL'

CHANGELOG
[1.0.0] - 2026-08-06
Initial framework scaffold and features. EOL
cat > SECURITY.md <<'EOL'

Security
Report vulnerabilities privately to maintainers. EOL

mkdir -p .github/ISSUE_TEMPLATE cat > .github/ISSUE_TEMPLATE/bug_report.md <<'EOL'
name: Bug report about: Create a report to help us improve
EOL

cat > .github/ISSUE_TEMPLATE/feature_request.md <<'EOL'
name: Feature request about: Suggest an idea for this project
EOL

cat > .github/PULL_REQUEST_TEMPLATE.md <<'EOL'

Pull Request Template
 My code follows the style guidelines of this project
 I have added tests
 I have added documentation EOL
echo "Adding files to git..." git add -A

git commit -m "Phases 7-13: logging, suites, CI workflow and repo docs/templates"

echo "Pushing branch to $REMOTE..." git push "
R
E
M
O
T
E
"
"
BRANCH"

echo "Done. Branch pushed: $BRANCH" echo echo "Open a Pull Request in GitHub from branch: $BRANCH -> main" echo "Suggested PR title: Phases 7-13: logging, TestNG suites, CI workflow, documentation" echo "Suggested PR body: See description in the assistant's previous message."

exit 0

chmod +x apply_phases7_13.sh ./apply_phases7_13.sh
