/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.language.xquery.springboot;


import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.spring.boot.CamelAutoConfiguration;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.annotation.DirtiesContext;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.camel.test.spring.junit5.CamelSpringBootTest;


@DirtiesContext
@CamelSpringBootTest
@SpringBootTest(
    classes = {
        CamelAutoConfiguration.class,
        XQueryWithFlworTest.class,
        XQueryWithFlworTest.TestConfiguration.class
    }
)
public class XQueryWithFlworTest {

    @Autowired
    ProducerTemplate template;

      
    @Test
    public void testWithFlworExpression() {
        String xml
                = "<items><item><id>3</id><name>third</name></item><item><id>1</id><name>first</name></item><item><id>2</id><name>second</name></item></items>";
        String expectedOrderedIds = "<base><id>1</id><id>3</id></base>";
        String orderedIds = template.requestBody("direct:flwor-expression", xml, String.class);
        assertEquals(expectedOrderedIds, orderedIds);
    }
    
    // *************************************
    // Config
    // *************************************

    @Configuration
    public class TestConfiguration {

        @Bean
        public RouteBuilder routeBuilder() {
            return new RouteBuilder() {
                @Override
                public void configure() {
                    from("direct:flwor-expression").to("xquery:org/apache/camel/component/xquery/flwor-expression.xquery");
                }
            };
        }
    }
}
