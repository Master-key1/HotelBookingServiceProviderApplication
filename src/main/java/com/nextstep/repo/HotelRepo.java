package com.nextstep.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nextstep.model.Hotel;

@Repository
public interface HotelRepo extends JpaRepository<Hotel, String> {

}
