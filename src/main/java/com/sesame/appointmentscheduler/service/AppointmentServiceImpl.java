package com.sesame.appointmentscheduler.service;

import com.sesame.appointmentscheduler.Repository.AppointmentRepository;
import com.sesame.appointmentscheduler.entity.Appointment;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;

    public Optional<Appointment> findById(Long id) {
        return appointmentRepository.findById(id);
    }

    public Appointment save(Appointment appointment) {
        return appointmentRepository.save(appointment);
    }

    public void deleteById(Long id) {
        appointmentRepository.deleteById(id);
    }

    @Override
    public List<Appointment> findByAppointmentDateBetween(LocalDate startDate, LocalDate endDate, Sort sort) {
        return appointmentRepository
            .findByAppointmentDateBetween(startDate.atStartOfDay(), endDate.atTime(LocalTime.MAX), sort);
    }

}
