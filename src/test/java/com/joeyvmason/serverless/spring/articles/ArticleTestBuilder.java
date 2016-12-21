package com.joeyvmason.serverless.spring.articles;

import org.apache.commons.lang3.RandomStringUtils;

public class ArticleTestBuilder {

    private String title = RandomStringUtils.randomAlphanumeric(10);
    private String body = RandomStringUtils.randomAlphanumeric(10);

    public static ArticleTestBuilder valid() {
        return new ArticleTestBuilder();
    }

    public Article build() {
        return new Article(title, body);
    }

    public ArticleTestBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    public ArticleTestBuilder withBody(String body) {
        this.body = body;
        return this;
    }
}
