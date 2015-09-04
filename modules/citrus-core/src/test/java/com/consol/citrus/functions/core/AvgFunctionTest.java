/*
 * Copyright 2006-2010 the original author or authors.
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

package com.consol.citrus.functions.core;

import com.consol.citrus.exceptions.InvalidFunctionUsageException;
import com.consol.citrus.testng.AbstractTestNGUnitTest;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.*;

/**
 * @author Christoph Deppisch
 */
public class AvgFunctionTest extends AbstractTestNGUnitTest {
    AvgFunction function = new AvgFunction();
    
    @Test
    public void testFunction() {
        List<String> params = new ArrayList<String>();
        params.add("3");
        params.add("3");
        params.add("3");
        
        Assert.assertEquals(function.execute(params, context), "3.0");
    }
    
    @Test(expectedExceptions = {NumberFormatException.class})
    public void testWrongParameterUsage() {
        function.execute(Collections.singletonList("no digit"), context);
    }
    
    @Test(expectedExceptions = {InvalidFunctionUsageException.class})
    public void testNoParameters() {
        function.execute(Collections.<String>emptyList(), context);
    }
}
