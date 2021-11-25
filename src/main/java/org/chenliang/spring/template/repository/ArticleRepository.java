package org.chenliang.spring.template.repository;

import org.chenliang.spring.template.model.entity.Article;
import org.chenliang.spring.template.model.entity.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    Page<Article> findByTags(Tag tag, Pageable pageable);
}
