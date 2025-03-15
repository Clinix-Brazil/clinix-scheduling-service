package com.clinix.api.clinixschedulingservice.repository;

import com.clinix.api.clinixschedulingservice.model.AppointmentScheduling;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentSchedulingRepository extends JpaRepository<AppointmentScheduling, Long> {

}
