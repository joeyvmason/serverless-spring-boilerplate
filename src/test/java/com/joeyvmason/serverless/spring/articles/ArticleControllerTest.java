package com.joeyvmason.serverless.spring.articles;

import com.amazonaws.serverless.proxy.internal.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.internal.model.AwsProxyResponse;
import com.amazonaws.serverless.proxy.internal.testutils.AwsProxyRequestBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.joeyvmason.serverless.spring.application.BaseIntegrationTest;
import org.apache.commons.lang3.RandomStringUtils;
import org.fest.assertions.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.testng.annotations.Test;


import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;

public class ArticleControllerTest extends BaseIntegrationTest {
    private static CollectionType COLLECTION_TYPE = TypeFactory.defaultInstance().constructCollectionType(List.class, Article.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ArticleRepository articleRepository;

    @Test
    public void shouldFindAll() throws Exception {
        //given
        Article article1 = articleRepository.save(ArticleTestBuilder.valid().build());
        Article article2 = articleRepository.save(ArticleTestBuilder.valid().build());

        //when
        AwsProxyRequest request = new AwsProxyRequestBuilder("/articles", "GET").build();
        AwsProxyResponse response = handler.proxy(request, lambdaContext);

        //then
        assertThat(response.getStatusCode()).isEqualTo(200);

        List<Article> articlesFromResponse = objectMapper.readValue(response.getBody(), COLLECTION_TYPE);
        assertThat(articlesFromResponse).containsOnly(article1, article2);
    }

    @Test
    public void shouldCreateArticle() throws Exception {
        //given
        ArticleForm articleForm = new ArticleForm(RandomStringUtils.randomAlphanumeric(10), RandomStringUtils.randomAlphanumeric(10));

        //when
        String body = objectMapper.writeValueAsString(articleForm);
        AwsProxyRequest request = new AwsProxyRequestBuilder("/articles", "POST")
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .body(body)
                .build();

        AwsProxyResponse response = handler.proxy(request, lambdaContext);

        //then
        assertThat(response.getStatusCode()).isEqualTo(200);

        Article article1 = objectMapper.readValue(response.getBody(), Article.class);
        Assertions.assertThat(article1.getId()).isNotNull();
        assertThat(article1.getTitle()).isEqualTo(articleForm.getTitle());
        assertThat(article1.getBody()).isEqualTo(articleForm.getBody());

        request = new AwsProxyRequestBuilder(String.format("/articles/%s", article1.getId()), "GET")
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .body(body)
                .build();

        response = handler.proxy(request, lambdaContext);
        assertThat(response.getStatusCode()).isEqualTo(200);
        Article article2 = objectMapper.readValue(response.getBody(), Article.class);
        assertThat(article2.getId()).isEqualTo(article1.getId());
    }

    @Test
    public void shouldUpdateArticle() throws Exception {
        //given
        Article article = articleRepository.save(ArticleTestBuilder.valid().build());
        ArticleForm articleForm = new ArticleForm(RandomStringUtils.randomAlphanumeric(10), RandomStringUtils.randomAlphanumeric(10));

        //when
        String body = objectMapper.writeValueAsString(articleForm);
        AwsProxyRequest request = new AwsProxyRequestBuilder(String.format("/articles/%s", article.getId()), "PUT")
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .body(body)
                .build();

        AwsProxyResponse response = handler.proxy(request, lambdaContext);

        //then
        assertThat(response.getStatusCode()).isEqualTo(200);
        Article articleFromResponse = objectMapper.readValue(response.getBody(), Article.class);
        Assertions.assertThat(articleFromResponse.getId()).isEqualTo(article.getId());
        assertThat(articleFromResponse.getTitle()).isEqualTo(articleForm.getTitle());
        assertThat(articleFromResponse.getBody()).isEqualTo(articleForm.getBody());
    }
}
