package com.iucyh.novelservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;
import java.util.UUID;

@NoRepositoryBean
public interface PublicEntityRepository<T, ID> extends JpaRepository<T, ID> {

    Optional<T> findByPublicId(UUID publicId);
    Optional<IdResult> findIdByPublicId(UUID publicId);
}
