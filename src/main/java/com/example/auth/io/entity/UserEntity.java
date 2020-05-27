package com.example.auth.io.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

@Entity(name = "users")
@Data
public class UserEntity implements Serializable {
    private static final long serialVersionUID = 3909094926504612711L;

    @Id
    @GeneratedValue
    private long id;
    @Column(length = 30, nullable = false, unique = true)
    private String userId;
    @Column(length = 30, nullable = false)
    private String firstName;
    @Column(length = 30, nullable = false)
    private String lastName;
    @Column(length = 100, nullable = false, unique = true)
    private String email;
    @Column(length = 100, nullable = false)
    private String encryptedPassword;
    private String emailVerificationToken;
    private boolean emailVerificationStatus;
}
