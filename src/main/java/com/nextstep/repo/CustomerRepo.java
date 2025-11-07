package com.nextstep.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nextstep.model.Customer;
import com.nextstep.model.Hotel;

@Repository
public interface CustomerRepo extends JpaRepository<Customer, String> {

}
