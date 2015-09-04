/*
 * Copyright 2006-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.consol.citrus.container;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract suit container actions executed before and after test suite run. Container decides
 * weather to execute according to given suite name and included test groups if any.
 *
 * @author Christoph Deppisch
 * @since 2.0
 */
public abstract class AbstractSuiteActionContainer extends AbstractActionContainer {

    /** Logger */
    private static Logger log = LoggerFactory.getLogger(AbstractSuiteActionContainer.class);

    /** List of suite names that match for this container */
    private List<String> suiteNames = new ArrayList<String>();

    /** List of test group names that match for this container */
    private List<String> testGroups = new ArrayList<String>();

    /**
     * Checks if this suite actions should execute according to suite name and included test groups.
     * @param suiteName
     * @param includedGroups
     * @return
     */
    public boolean shouldExecute(String suiteName, String[] includedGroups) {
        if (suiteNames.isEmpty() && testGroups.isEmpty()) {
            return true; // no restrictions container is always executed
        }

        if (StringUtils.hasText(suiteName)) {
            for (String suiteNameMatch : suiteNames) {
                if (suiteNameMatch.equals(suiteName)
                        && checkTestGroups(includedGroups)) {
                    return true;
                }
            }
        }

        if (checkTestGroups(includedGroups)) {
            return true;
        }

        if (log.isDebugEnabled())  {
            log.debug(String.format("Suite container restrictions did not match - do not execute container '%s'", getName()));
        }

        return false;
    }

    /**
     * Checks on included test groups if we should execute sequence. Included group list should have
     * at least one entry matching the sequence test groups restriction.
     *
     * @param includedGroups
     * @return
     */
    private boolean checkTestGroups(String[] includedGroups) {
        if (testGroups.isEmpty()) {
            return true;
        }

        if (includedGroups != null) {
            for (String includedGroup : includedGroups) {
                if (testGroups.contains(includedGroup)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Gets the test groups that restrict the container execution.
     * @return
     */
    public List<String> getTestGroups() {
        return testGroups;
    }

    /**
     * Sets the test groups that restrict the container execution.
     * @param testGroups
     */
    public void setTestGroups(List<String> testGroups) {
        this.testGroups = testGroups;
    }

    /**
     * Gets the suite names that restrict the container execution.
     * @return
     */
    public List<String> getSuiteNames() {
        return suiteNames;
    }

    /**
     * Sets the suite names that restrict the container execution.
     * @param suiteNames
     */
    public void setSuiteNames(List<String> suiteNames) {
        this.suiteNames = suiteNames;
    }
}
