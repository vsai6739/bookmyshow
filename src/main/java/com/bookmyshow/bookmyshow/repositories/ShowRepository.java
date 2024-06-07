package com.bookmyshow.bookmyshow.repositories;

import com.bookmyshow.bookmyshow.models.Show;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShowRepository extends JpaRepository<Show, Long> {
//    Map<Id, Entity>


    // 1. Class -> Interface
    // 2. Extend the JpaRepository
}
