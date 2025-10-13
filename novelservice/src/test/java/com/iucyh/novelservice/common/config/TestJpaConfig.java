package com.iucyh.novelservice.common.config;

import com.iucyh.novelservice.repository.novel.query.NovelQueryRepository;
import com.iucyh.novelservice.repository.novel.query.NovelQueryRepositoryImpl;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestJpaConfig {

    @PersistenceContext
    private EntityManager em;

    @Bean
    public JPAQueryFactory jpaQueryFactory() {
        return new JPAQueryFactory(em);
    }

    @Bean
    public NovelQueryRepository novelQueryRepository() {
        return new NovelQueryRepositoryImpl(jpaQueryFactory());
    }
}
