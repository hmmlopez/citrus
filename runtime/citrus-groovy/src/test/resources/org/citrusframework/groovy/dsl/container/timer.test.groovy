/*
 * Copyright the original author or authors.
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.citrusframework.groovy.dsl.container

import static org.citrusframework.actions.EchoAction.Builder.echo
import static org.citrusframework.container.Timer.Builder.timer

name "TimerTest"
author "Christoph"
status "FINAL"
description "Sample test in Groovy"

actions {
    $(timer()
        .id("timer1")
        .fork(true)
        .delay(5000)
        .interval(2000)
        .repeatCount(1)
        .actions(
            echo().message("1")
        )
    )

    $(timer()
        .id("timer2")
        .fork(false)
        .delay(500)
        .interval(200)
        .repeatCount(2)
        .actions(
            echo().message("1"),
            echo().message("2")
        )
    )

    $(timer()
        .fork(true)
        .actions(
            echo().message("1")
        )
    )
}
