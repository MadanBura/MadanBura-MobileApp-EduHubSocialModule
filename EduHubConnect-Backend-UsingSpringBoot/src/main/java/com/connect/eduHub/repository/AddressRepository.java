package com.connect.eduHub.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.connect.eduHub.model.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {
	
	Address findByUserId(Long userId);

}
