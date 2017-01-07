package com.joeyvmason.serverless.spring.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testng.annotations.BeforeMethod;

import javax.servlet.Filter;

@ContextConfiguration(classes = {MvcConfig.class})
@WebAppConfiguration
public class BaseMvcIntegrationTest extends BaseIntegrationTest {

    protected MockMvc mockMvc;

    @Autowired
    protected WebApplicationContext webApplicationContext;

    @BeforeMethod
    public void setup() throws Exception {
        mockMvc = setUpMvcMock();
    }

    public MockMvc setUpMvcMock(Filter... filters) throws Exception {
        DefaultMockMvcBuilder defaultMockMvcBuilder = MockMvcBuilders.webAppContextSetup(webApplicationContext);

        for (Filter filter : filters) {
            defaultMockMvcBuilder.addFilter(filter, "/*");
        }

        return defaultMockMvcBuilder.build();
    }
}
