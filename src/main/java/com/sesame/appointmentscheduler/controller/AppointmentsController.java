package com.sesame.appointmentscheduler.controller;


import com.sesame.appointmentscheduler.entity.Appointment;
import com.sesame.appointmentscheduler.service.AppointmentService;
import io.micrometer.core.annotation.Timed;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1/appointments")
@Slf4j
@RequiredArgsConstructor
public class AppointmentsController {

    private final AppointmentService appointmentService;

    @PostMapping
    @Timed
    public ResponseEntity create(@Valid @RequestBody Appointment appointment) {
        log.info("Request for new appointment received for doctor {}, price {}", appointment.getDoctorName(),
            appointment.getPrice());
        return ResponseEntity.ok(appointmentService.save(appointment));
    }

    @GetMapping("/{id}")
    @Timed
    public ResponseEntity<Appointment> findById(@PathVariable Long id) {
        log.info("Searching for appointment with id {}", id);
        return appointmentService.findById(id)
            .map(record -> ResponseEntity.ok().body(record))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Appointment was not found!"));
    }

    @PutMapping("/{id}")
    @Timed
    public ResponseEntity<Appointment> update(@PathVariable Long id, @RequestBody Appointment appointment) {
        log.info("Updating for appointment with id {} to {}", id, appointment.getStatus());
        return appointmentService.findById(id)
            .map(record -> {
                record.setStatus(appointment.getStatus());
                Appointment updated = appointmentService.save(record);
                return ResponseEntity.ok().body(updated);
            }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Appointment was not found!"));
    }

    @DeleteMapping("/{id}")
    @Timed
    public ResponseEntity delete(@PathVariable Long id) {
        log.info("Deleting appointment with id {} ", id);
        return appointmentService.findById(id)
            .map(record -> {
                appointmentService.deleteById(id);
                return ResponseEntity.ok().body(Collections.singletonMap("deleted", true));
            })
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Appointment was not found!"));
    }

    @GetMapping("/search")
    @Timed
    public ResponseEntity<List<Appointment>> findByDateBetween(
        @RequestParam("startDate") @DateTimeFormat(iso = ISO.DATE) LocalDate startDate,
        @RequestParam("endDate") @DateTimeFormat(iso = ISO.DATE) LocalDate endDate,
        @RequestParam(required = false, defaultValue = "ASC") String sortType) {
        log.info("Search appointments with from {} to {}", startDate, endDate);
        if(endDate.isBefore(startDate)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "EndDate can't be before startDate");
        }
        return ResponseEntity.ok(appointmentService
            .findByAppointmentDateBetween(startDate, endDate, Sort.by(Direction
                .fromString(sortType), "price")));
    }

}
