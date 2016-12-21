package com.joeyvmason.serverless.spring.application;

import com.joeyvmason.serverless.spring.articles.ArticleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterMethod;

@ContextConfiguration(classes = {IntegrationTestConfig.class})
public class BaseIntegrationTest extends AbstractTestNGSpringContextTests {
    private static final Logger LOG = LoggerFactory.getLogger(BaseIntegrationTest.class);

    @Autowired
    private ArticleRepository articleRepository;

    @AfterMethod
    public void tearDown() throws Exception {
        LOG.info("Deleting all Articles");
        articleRepository.deleteAll();
    }

}
