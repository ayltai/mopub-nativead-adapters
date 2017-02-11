#!/bin/bash

function generateTestReports() {
    ./gradlew -i testDebugUnitTest jacocoTestReport adapters:uploadCoverageToCodacy --console=plain
    cp -r adapters/build/test-results/testDebugUnitTest/debug/*.xml $CIRCLE_TEST_REPORTS && cp -r adapters/build/reports/tests/testDebugUnitTest $CIRCLE_TEST_REPORTS && cp -r adapters/build/reports/jacoco/jacocoTestReport $CIRCLE_TEST_REPORTS
}

function inspectCode() {
    ./gradlew -i testDebugUnitTest adapters:customFindBugs --console=plain
    cp -r adapters/build/outputs/findbugs $CIRCLE_TEST_REPORTS
}
