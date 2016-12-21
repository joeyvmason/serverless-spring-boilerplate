package com.joeyvmason.serverless.spring.articles;

public class ArticleForm {

    private String title;
    private String body;

    public ArticleForm() {}

    public ArticleForm(String title, String body) {
        this.title = title;
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
