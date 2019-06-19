package com.sesame.appointmentscheduler.Repository;

import com.sesame.appointmentscheduler.entity.Appointment;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByAppointmentDateBetween(LocalDateTime startDate, LocalDateTime endDate, Sort sort);

}
