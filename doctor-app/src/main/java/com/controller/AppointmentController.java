package com.example.doctorapp.controller;

import com.example.doctorapp.entity.Appointment;
import com.example.doctorapp.entity.Doctor;
import com.example.doctorapp.entity.Patient;
import com.example.doctorapp.repository.AppointmentRepository;
import com.example.doctorapp.repository.DoctorRepository;
import com.example.doctorapp.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;

    @PostMapping
    public Appointment bookAppointment(@RequestParam Long doctorId,
                                       @RequestParam Long patientId,
                                       @RequestParam String appointmentDateTime) {

        Doctor doctor = doctorRepository.findById(doctorId).orElseThrow();
        Patient patient = patientRepository.findById(patientId).orElseThrow();
        LocalDateTime dateTime = LocalDateTime.parse(appointmentDateTime);

        // Check if doctor is already booked
        if (appointmentRepository.existsByDoctorAndAppointmentDateTime(doctor, dateTime)) {
            throw new RuntimeException("Doctor already has an appointment at this time!");
        }

        Appointment appt = new Appointment();
        appt.setDoctor(doctor);
        appt.setPatient(patient);
        appt.setAppointmentDateTime(dateTime);
        return appointmentRepository.save(appt);
    }

    @GetMapping
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    @GetMapping("/{id}")
    public Appointment getAppointmentById(@PathVariable Long id) {
        return appointmentRepository.findById(id).orElseThrow();
    }

    @DeleteMapping("/{id}")
    public void cancelAppointment(@PathVariable Long id) {
        appointmentRepository.deleteById(id);
    }
}