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

package org.citrusframework.http.integration;

import org.citrusframework.annotations.CitrusTestSource;
import org.citrusframework.common.TestLoader;
import org.citrusframework.testng.spring.TestNGCitrusSpringSupport;
import org.testng.annotations.Test;

@Test
public class HttpServerIT extends TestNGCitrusSpringSupport {

    @CitrusTestSource(type = TestLoader.SPRING, name = "HttpServerIT")
    public void testHttpServer() {}

    @Test
    @CitrusTestSource(type = TestLoader.SPRING, name = "HttpServerStandaloneIT")
    public void serverStandaloneIT() {}

    @Test
    @CitrusTestSource(type = TestLoader.SPRING, name = "HttpParallelRequest_1_IT")
    public void parallelRequestsIterateIT() {}

    @Test
    @CitrusTestSource(type = TestLoader.SPRING, name = "HttpParallelRequest_2_IT")
    public void parallelRequestsEndpointUriIT() {}

    @Test
    @CitrusTestSource(type = TestLoader.SPRING, name = "HttpParallelRequest_3_IT")
    public void parallelRequestsIT() {}
}
