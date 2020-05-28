package com.example.auth.io.entity;

import lombok.Data;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Entity(name="addresses")
@Data
public class AddressEntity implements Serializable {
    private static final long serialVersionUID = 7603730536678519052L;
    @Id
    @GeneratedValue
    private long id;
    @Column(length = 30, unique = true, nullable = false)
    private String addressId;
    @Column(length = 100, nullable = false)
    private String street;
    @Column(length = 50, nullable = false)
    private String city;

    @ManyToOne
    @JoinColumn(name = "users_id")
    private UserEntity userDetails;
}
