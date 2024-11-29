/*
 * Copyright the original author or authors.
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

package org.citrusframework.groovy.dsl.container

import java.time.Duration

import static org.citrusframework.actions.EchoAction.Builder.echo
import static org.citrusframework.container.Wait.Builder.waitFor

name "WaitForTest"
author "Christoph"
status "FINAL"
description "Sample test in Groovy"

actions {
    $(waitFor()
        .file()
        .path("classpath:org/citrusframework/groovy/test-request-payload.xml"))

    $(waitFor()
        .file()
        .path("classpath:org/citrusframework/groovy/test-request-payload.xml")
        .time(Duration.ofMillis(10000))
        .interval(2000))

    $(waitFor()
        .http()
        .url("https://citrusframework.org"))

    $(waitFor()
        .http()
        .url("https://citrusframework.org/doesnotexist")
        .time(Duration.ofMillis(3000))
        .timeout(2000)
        .method("GET")
        .status(404))

    $(waitFor()
        .message()
            .name("request"))

    $(waitFor()
        .execution()
            .action(echo().message("Citrus rocks!")))
}
