package org.chenliang.spring.template;

import org.chenliang.spring.template.model.entity.Article;
import org.chenliang.spring.template.model.entity.Tag;
import org.chenliang.spring.template.repository.ArticleRepository;
import org.chenliang.spring.template.repository.TagRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

@SpringBootTest
class DbTest {
    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private TagRepository tagRepository;

    @Test
    @Transactional
    void manyToMany() {
        Tag tag = new Tag();
        tag.setName("test");
        Tag createdTag = tagRepository.save(tag);

        Article article = new Article();
        article.setTitle("foo");
        article.setContent("bar");
        Article savedArticle = articleRepository.save(article);

        articleRepository.findById(savedArticle.getId())
                .ifPresent(article1 -> {
                    article1.getTags().add(tag);
                });
    }
}
