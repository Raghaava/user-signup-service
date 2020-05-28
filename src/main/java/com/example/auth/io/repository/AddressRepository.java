package com.example.auth.io.repository;

import com.example.auth.io.entity.AddressEntity;
import com.example.auth.io.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<AddressEntity, Long> {
    AddressEntity findByAddressId(String addressId);

    List<AddressEntity> findByUserDetails(UserEntity userEntityFromDB);
}
