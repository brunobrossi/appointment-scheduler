package com.sesame.appointmentscheduler.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "appointments")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonDeserialize(builder = Appointment.AppointmentBuilder.class)
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @NotNull
    @Column(name = "appointment_date")
    private LocalDateTime appointmentDate;
    @NotNull
    @Column(name = "appointment_duration_mins")
    private int appointmentDurationInMins;
    @NotNull
    @Column(name = "name_of_doctor")
    private String doctorName;
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private AppointmentStatus status;
    @NotNull
    @Column(name = "price")
    private double price;

    @JsonPOJOBuilder(withPrefix = "")
    public static class AppointmentBuilder {
    }

}
