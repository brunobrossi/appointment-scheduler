package com.sesame.appointmentscheduler.service;

import com.sesame.appointmentscheduler.entity.Appointment;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Sort;

public interface AppointmentService {

    Optional<Appointment> findById(Long id);

    Appointment save(Appointment appointment);

    void deleteById(Long id);

    List<Appointment> findByAppointmentDateBetween(LocalDate startDate, LocalDate endDate, Sort sort);

}
