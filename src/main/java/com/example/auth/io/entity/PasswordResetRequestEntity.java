package com.example.auth.io.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import java.io.Serializable;

@Entity(name="password_reset_requests")
@Data
public class PasswordResetRequestEntity implements Serializable {
    @Id
    @GeneratedValue
    private long id;
    private String passwordResetRequestToken;
    @OneToOne
    @JoinColumn(name="users_id")
    private UserEntity userDetails;
}
