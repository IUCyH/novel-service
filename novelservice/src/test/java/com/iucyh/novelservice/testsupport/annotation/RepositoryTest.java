package com.iucyh.novelservice.testsupport.annotation;

import com.iucyh.novelservice.testsupport.config.TestJpaConfig;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Import(TestJpaConfig.class)
@DataJpaTest
public @interface RepositoryTest {
}
