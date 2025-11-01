package com.iucyh.novelservice.domain.dateentity.testsupport;

import com.iucyh.novelservice.domain.DateEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class TestDateEntity extends DateEntity {

    @Id
    @GeneratedValue
    private Long id;

    public Long getId() {
        return id;
    }
}
