/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naturemorning.parallax.repository;

import com.naturemorning.parallax.models.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author ARK-121
 */
public interface ReservationRepository extends JpaRepository<Reservation, Long>{
    //User findByEmail(String email);
}
