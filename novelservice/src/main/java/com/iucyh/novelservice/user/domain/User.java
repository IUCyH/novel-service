package com.iucyh.novelservice.user.domain;

import com.iucyh.novelservice.common.domain.PublicEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class User extends PublicEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 320, nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(length = 20, nullable = false, unique = true)
    private String nickname;

    @Column(length = 100, nullable = false)
    private String bio;

    public static User of(String email, String password, String nickname, String bio) {
        User user = new User();
        user.email = email.strip();
        user.password = password;
        user.nickname = nickname.strip();
        user.bio = bio;
        return user;
    }
}
