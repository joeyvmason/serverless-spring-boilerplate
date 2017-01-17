package com.joeyvmason.serverless.spring.domain.articles;


import com.joeyvmason.serverless.spring.application.exceptions.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/articles")
public class ArticleController {
    private static final Logger LOG = LoggerFactory.getLogger(ArticleController.class);

    private final ArticleRepository articleRepository;

    @Autowired
    public ArticleController(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Article findOne(@PathVariable String id) {
        LOG.info("Finding Article({})", id);
        return articleRepository.findOne(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public Collection<Article> findAll() {
        LOG.info("Finding all Articles");
        return articleRepository.findAll();
    }

    @RequestMapping(method = RequestMethod.POST)
    public Article createArticle(@RequestBody ArticleForm articleForm) {
        LOG.info("Creating new Article with Title({})", articleForm.getTitle());
        Article article = new Article(articleForm.getTitle(), articleForm.getBody());
        article = articleRepository.save(article);
        LOG.info("Creating new Article({})", article);
        return article;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Article updateArticle(@PathVariable String id, @RequestBody ArticleForm articleForm) throws Exception {
        LOG.info("Updating Article({})", id);

        Article article = articleRepository.findOne(id);
        if (article == null) {
            throw new NotFoundException();
        }

        article.setTitle(articleForm.getTitle());
        article.setBody(articleForm.getBody());

        return articleRepository.save(article);
    }


}
