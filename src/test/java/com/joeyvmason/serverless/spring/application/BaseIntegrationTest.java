package com.joeyvmason.serverless.spring.application;

import com.amazonaws.serverless.proxy.internal.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.internal.model.AwsProxyResponse;
import com.amazonaws.serverless.proxy.internal.testutils.MockLambdaContext;
import com.amazonaws.serverless.proxy.spring.SpringLambdaContainerHandler;
import com.joeyvmason.serverless.spring.articles.ArticleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;
import org.testng.annotations.AfterMethod;


@ContextConfiguration(classes = {MvcConfig.class, IntegrationTestConfig.class})
@WebAppConfiguration
@TestExecutionListeners(inheritListeners = false, listeners = {DependencyInjectionTestExecutionListener.class})
public class BaseIntegrationTest extends AbstractTestNGSpringContextTests {
    private static final Logger LOG = LoggerFactory.getLogger(BaseIntegrationTest.class);

    @Autowired
    protected MockLambdaContext lambdaContext;

    @Autowired
    protected SpringLambdaContainerHandler<AwsProxyRequest, AwsProxyResponse> handler;

    @Autowired
    private ArticleRepository articleRepository;

    @AfterMethod
    public void tearDown() throws Exception {
        LOG.info("Deleting all Articles");
        articleRepository.deleteAll();
    }
}
