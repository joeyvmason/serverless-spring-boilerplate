package com.joeyvmason.serverless.spring.articles;


import com.amazonaws.serverless.exceptions.ContainerInitializationException;
import com.amazonaws.serverless.proxy.internal.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.internal.model.AwsProxyResponse;
import com.amazonaws.serverless.proxy.internal.testutils.AwsProxyRequestBuilder;
import com.amazonaws.serverless.proxy.internal.testutils.MockLambdaContext;
import com.amazonaws.serverless.proxy.spring.SpringLambdaContainerHandler;
import com.amazonaws.services.lambda.runtime.Context;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.joeyvmason.serverless.spring.application.BaseMvcIntegrationTest;
import com.joeyvmason.serverless.spring.application.MvcConfig;
import org.apache.commons.lang3.RandomStringUtils;
import org.fest.assertions.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.testng.annotations.Test;

import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ArticleControllerTest extends BaseMvcIntegrationTest {
    private static CollectionType COLLECTION_TYPE = TypeFactory.defaultInstance().constructCollectionType(List.class, Article.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ArticleRepository articleRepository;


    private static SpringLambdaContainerHandler<AwsProxyRequest, AwsProxyResponse> handler = null;

    private static Context lambdaContext = new MockLambdaContext();

    @Test
    public void shouldFindAll2() throws Exception {
        try {
            handler = SpringLambdaContainerHandler.getAwsProxyHandler(MvcConfig.class);
        } catch (ContainerInitializationException e) {
            e.printStackTrace();
            assertThat(true).isFalse();
        }


        Article article1 = articleRepository.save(ArticleTestBuilder.valid().build());
        Article article2 = articleRepository.save(ArticleTestBuilder.valid().build());


        AwsProxyRequest request = new AwsProxyRequestBuilder("/articles", "GET")
                .json()
//                .header(CUSTOM_HEADER_KEY, CUSTOM_HEADER_VALUE)
                .build();

        AwsProxyResponse output = handler.proxy(request, lambdaContext);

        assertThat(output.getStatusCode()).isEqualTo(200);

        List<Article> articlesFromResponse = objectMapper.readValue(output.getBody(), COLLECTION_TYPE);
        assertThat(articlesFromResponse).containsOnly(article1, article2);
    }

    @Test
    public void shouldFindAll() throws Exception {
        //given
        Article article1 = articleRepository.save(ArticleTestBuilder.valid().build());
        Article article2 = articleRepository.save(ArticleTestBuilder.valid().build());

        //when
        String jsonResponse = mockMvc.perform(get("/articles")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        //then
        List<Article> articlesFromResponse = objectMapper.readValue(jsonResponse, COLLECTION_TYPE);
        assertThat(articlesFromResponse).containsOnly(article1, article2);
    }

    @Test
    public void shouldCreateArticle() throws Exception {
        //given
        ArticleForm articleForm = new ArticleForm(RandomStringUtils.randomAlphanumeric(10), RandomStringUtils.randomAlphanumeric(10));

        //when
        String body = objectMapper.writeValueAsString(articleForm);
        String jsonResponse = mockMvc.perform(post("/articles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
        ).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        //then
        Article articleFromResponse = objectMapper.readValue(jsonResponse, Article.class);
        Assertions.assertThat(articleFromResponse.getId()).isNotNull();
        assertThat(articleFromResponse.getTitle()).isEqualTo(articleForm.getTitle());
        assertThat(articleFromResponse.getBody()).isEqualTo(articleForm.getBody());
    }

    @Test
    public void shouldUpdateArticle() throws Exception {
        //given
        Article article = articleRepository.save(ArticleTestBuilder.valid().build());
        ArticleForm articleForm = new ArticleForm(RandomStringUtils.randomAlphanumeric(10), RandomStringUtils.randomAlphanumeric(10));

        //when
        String body = objectMapper.writeValueAsString(articleForm);
        String jsonResponse = mockMvc.perform(put(String.format("/articles/%s", article.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(body)
        ).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

        //then
        Article articleFromResponse = objectMapper.readValue(jsonResponse, Article.class);
        Assertions.assertThat(articleFromResponse.getId()).isEqualTo(article.getId());
        assertThat(articleFromResponse.getTitle()).isEqualTo(articleForm.getTitle());
        assertThat(articleFromResponse.getBody()).isEqualTo(articleForm.getBody());
    }

}