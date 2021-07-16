package de.kreth.clubhelper.invoice.db;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import de.kreth.clubhelper.invoice.data.Article;

public interface ArticleRepository extends CrudRepository<Article, Integer> {

    List<Article> findByUserId(int userId);
}
