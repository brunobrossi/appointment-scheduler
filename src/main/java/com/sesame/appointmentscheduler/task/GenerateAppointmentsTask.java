package com.sesame.appointmentscheduler.task;

import com.sesame.appointmentscheduler.entity.Appointment;
import com.sesame.appointmentscheduler.entity.AppointmentStatus;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class GenerateAppointmentsTask {

    private final String url
        = "http://localhost:9090/api/v1/appointments";
    RestTemplate restTemplate = new RestTemplate();
    ScheduledExecutorService scheduledExecutorService =
        Executors.newScheduledThreadPool(1);

    /*
      This task will run every 50s, to generate a random executor between 1 and 10 minutes
     */
    @Scheduled(fixedRate = 60000)
    public void start() {
        int time = new Random().nextInt(10);
        log.info("Generating new task for appointment in {} minutes", time);
        ScheduledFuture scheduledFuture =
            scheduledExecutorService.schedule(new Runnable() {
                                                  @Override
                                                  public void run() {
                                                      restTemplate.postForObject(url, generateRandomAppointment(), Appointment.class);
                                                  }
                                              }, time
                ,
                TimeUnit.MINUTES);
    }


    /*
    Random generator for the propose of the task only
     */
    private Appointment generateRandomAppointment() {

        List<AppointmentStatus> statuses =
            Collections.unmodifiableList(Arrays.asList(AppointmentStatus.values()));


        return Appointment.builder()
            .appointmentDate(LocalDateTime.now().plusDays(new Random().nextInt(30)))
            .appointmentDurationInMins(new Random().nextInt(60))
            .doctorName("RAND" + new Random().nextInt(5))
            .price(randomDouble())
            .status(statuses.get(new Random().nextInt(statuses.size())))
            .build();
    }

    private static double randomDouble() {
        Random r = new Random();
        return (double) 10 + ((double) 100 - (double) 10) * r.nextDouble();
    }

}
