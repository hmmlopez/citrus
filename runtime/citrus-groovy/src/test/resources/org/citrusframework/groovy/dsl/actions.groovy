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

package org.citrusframework.groovy.dsl

import static org.citrusframework.actions.EchoAction.Builder.echo
import static org.citrusframework.container.Iterate.Builder.iterate

actions {
    $(echo("Hello from Citrus!"))

    $(iterate().condition('i < 5').actions(echo('Hello ${i}'), echo('ByeBye ${i}')))
}
