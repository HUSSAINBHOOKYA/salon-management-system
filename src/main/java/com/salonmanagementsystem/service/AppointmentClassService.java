package com.salonmanagementsystem.service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.salonmanagementsystem.dto.AppointmentRequestDTO;
import com.salonmanagementsystem.dto.PaginationResponse;
import com.salonmanagementsystem.entity.AppointmentClass;
import com.salonmanagementsystem.entity.BillClass;
import com.salonmanagementsystem.entity.EmployeeClass;
import com.salonmanagementsystem.entity.Salon;
import com.salonmanagementsystem.entity.SalonService;
import com.salonmanagementsystem.entity.TimeSlotClass;
import com.salonmanagementsystem.enums.BookingStatus;
import com.salonmanagementsystem.enums.PaymentStatus;
import com.salonmanagementsystem.enums.TimeSlotStatus;
import com.salonmanagementsystem.repository.AppointmentRepository;
import com.salonmanagementsystem.repository.BillRepository;
import com.salonmanagementsystem.repository.EmployeeRepository;
import com.salonmanagementsystem.repository.SalonRepository;
import com.salonmanagementsystem.repository.ServiceRepository;
import com.salonmanagementsystem.repository.TimeSlotRepository;

import jakarta.transaction.Transactional;

@Service
public class AppointmentClassService {

    // =========================================
    // REPOSITORIES
    // =========================================

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private TimeSlotRepository timeSlotRepository;

    @Autowired
    private SalonRepository salonRepository;

    @Autowired
    private BillRepository billRepository;

    // =========================================
    // BILL SERVICE
    // =========================================

    @Autowired
    private BillClassService billClassService;

    // =========================================
    // TIME FORMAT
    // =========================================

    private static final DateTimeFormatter TIME_FORMATTER =
            new DateTimeFormatterBuilder()
                    .parseCaseInsensitive()
                    .appendPattern("hh:mm a")
                    .toFormatter(Locale.ENGLISH)
                    .withResolverStyle(ResolverStyle.SMART);

    // =========================================
    // SLOT INTERVAL
    // =========================================

    private static final long SLOT_INTERVAL_MINUTES = 30;

    // =========================================
    // ADD APPOINTMENT
    // CUSTOMER BOOKING
    // =========================================

    @Transactional
    public AppointmentClass addAppointment(
            AppointmentRequestDTO request) {

        // =====================================
        // VALIDATE REQUEST
        // =====================================

        if (request == null) {
            throw new RuntimeException(
                    "Appointment request is required");
        }

        if (request.getCustomerName() == null
                || request.getCustomerName().isBlank()) {

            throw new RuntimeException(
                    "Customer Name is required");
        }

        if (request.getCustomerPhone() == null
                || request.getCustomerPhone().isBlank()) {

            throw new RuntimeException(
                    "Customer Phone is required");
        }

        if (request.getSalonId() == null) {
            throw new RuntimeException(
                    "Salon is required");
        }

        if (request.getServiceIds() == null
                || request.getServiceIds().isEmpty()) {

            throw new RuntimeException(
                    "At least one Service is required");
        }

        if (request.getEmployeeId() == null) {
            throw new RuntimeException(
                    "Employee is required");
        }

        if (request.getTimeSlotId() == null) {
            throw new RuntimeException(
                    "Time Slot is required");
        }

        if (request.getAppointmentDate() == null) {
            throw new RuntimeException(
                    "Appointment date is required");
        }

        if (request.getAppointmentTime() == null
                || request.getAppointmentTime().isBlank()) {

            throw new RuntimeException(
                    "Appointment time is required");
        }

        // =====================================
        // REMOVE DUPLICATE SERVICE IDS
        // =====================================

        List<Long> serviceIds =
                new ArrayList<>(
                        new HashSet<>(
                                request.getServiceIds()));

        // =====================================
        // FIND SALON
        // =====================================

        Salon salon =
                salonRepository.findById(
                        request.getSalonId())
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Salon Not Found"));

        // =====================================
        // FIND EMPLOYEE
        // =====================================

        EmployeeClass employee =
                employeeRepository.findById(
                        request.getEmployeeId())
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Employee Not Found"));

        // =====================================
        // CHECK EMPLOYEE → SALON
        // =====================================

        if (employee.getSalon() == null
                || !employee.getSalon().getId()
                        .equals(salon.getId())) {

            throw new RuntimeException(
                    "Selected Employee does not belong "
                            + "to the selected Salon");
        }

        // =====================================
        // FIND ALL SERVICES
        // =====================================

        List<SalonService> selectedServices =
                new ArrayList<>();

        for (Long serviceId : serviceIds) {

            if (serviceId == null) {
                throw new RuntimeException(
                        "Invalid Service ID");
            }

            SalonService service =
                    serviceRepository.findById(serviceId)
                            .orElseThrow(
                                    () -> new RuntimeException(
                                            "Service Not Found: "
                                                    + serviceId));

            // =================================
            // CHECK SERVICE → SALON
            // =================================

            if (service.getSalon() == null
                    || !service.getSalon().getId()
                            .equals(salon.getId())) {

                throw new RuntimeException(
                        "Selected Service does not belong "
                                + "to the selected Salon");
            }

            // =================================
            // CHECK EMPLOYEE → SERVICE
            // =================================

            if (employee.getServices() == null
                    || employee.getServices().stream()
                            .noneMatch(
                                    assignedService ->
                                            assignedService.getId()
                                                    .equals(service.getId()))) {

                throw new RuntimeException(
                        "Service '"
                                + service.getServiceName()
                                + "' is not assigned to the "
                                + "selected Employee");
            }

            selectedServices.add(service);
        }

        // =====================================
        // CALCULATE TOTAL PRICE
        // =====================================

        double totalPrice = 0.0;

        for (SalonService service : selectedServices) {

            if (service.getPrice() != null) {
                totalPrice += service.getPrice();
            }
        }

        // =====================================
        // CALCULATE TOTAL DURATION
        // =====================================

        int totalDuration = 0;

        for (SalonService service : selectedServices) {

            if (service.getDuration() != null
                    && service.getDuration() > 0) {

                totalDuration += service.getDuration();
            }
        }

        if (totalDuration <= 0) {
            throw new RuntimeException(
                    "Selected services do not have "
                            + "a valid duration");
        }

        // =====================================
        // FIND STARTING TIME SLOT
        // =====================================

        TimeSlotClass startingSlot =
                timeSlotRepository.findById(
                        request.getTimeSlotId())
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Time Slot Not Found"));

        // =====================================
        // CHECK APPOINTMENT DATE
        // → TIME SLOT DATE
        // =====================================

        if (!request.getAppointmentDate()
                .equals(startingSlot.getSlotDate())) {

            throw new RuntimeException(
                    "Appointment date does not match "
                            + "selected Time Slot");
        }

        // =====================================
        // CHECK APPOINTMENT TIME
        // → TIME SLOT TIME
        // =====================================

        if (!normalizeTime(
                request.getAppointmentTime())
                .equals(
                        normalizeTime(
                                startingSlot.getSlotTime()))) {

            throw new RuntimeException(
                    "Appointment time does not match "
                            + "selected Time Slot");
        }

        // =====================================
        // CHECK TIME SLOT → SALON
        // =====================================

        if (startingSlot.getSalon() == null
                || !startingSlot.getSalon().getId()
                        .equals(salon.getId())) {

            throw new RuntimeException(
                    "Selected Time Slot does not belong "
                            + "to the selected Salon");
        }

        // =====================================
        // CHECK TIME SLOT → EMPLOYEE
        // =====================================

        if (startingSlot.getEmployee() == null
                || !startingSlot.getEmployee().getId()
                        .equals(employee.getId())) {

            throw new RuntimeException(
                    "Selected Time Slot does not belong "
                            + "to the selected Employee");
        }

        // =====================================
        // CHECK STARTING SLOT STATUS
        // =====================================

        if (startingSlot.getSlotStatus()
                == TimeSlotStatus.BOOKED) {

            throw new RuntimeException(
                    "This Time Slot Is Already Booked");
        }

        if (startingSlot.getSlotStatus()
                != TimeSlotStatus.AVAILABLE) {

            throw new RuntimeException(
                    "This Time Slot Is Not Available");
        }

        // =====================================
        // FIND ALL EMPLOYEE SLOTS FOR DATE
        // =====================================

        List<TimeSlotClass> employeeSlots =
                timeSlotRepository
                        .findBySalonIdAndEmployeeIdAndSlotDateAndSlotStatus(
                                salon.getId(),
                                employee.getId(),
                                request.getAppointmentDate(),
                                TimeSlotStatus.AVAILABLE);

        // =====================================
        // SORT SLOTS BY TIME
        // =====================================

        employeeSlots.sort(
                Comparator.comparing(
                        slot -> parseTime(
                                slot.getSlotTime())));

        // =====================================
        // FIND REQUIRED CONTINUOUS SLOTS
        // =====================================

        int requiredSlots =
                (int) Math.ceil(
                        (double) totalDuration
                                / SLOT_INTERVAL_MINUTES);

        List<TimeSlotClass> slotsToBook =
                findContinuousAvailableSlots(
                        employeeSlots,
                        startingSlot,
                        requiredSlots);

        // =====================================
        // CHECK CONTINUOUS AVAILABILITY
        // =====================================

        if (slotsToBook.size() < requiredSlots) {

            throw new RuntimeException(
                    "Not enough continuous time available "
                            + "for the selected services. "
                            + "Total duration is "
                            + totalDuration
                            + " minutes.");
        }

        // =====================================
        // BOOK ALL REQUIRED SLOTS
        // =====================================

        for (TimeSlotClass slot : slotsToBook) {

            slot.setSlotStatus(
                    TimeSlotStatus.BOOKED);

            timeSlotRepository.save(slot);
        }

        // =====================================
        // CREATE APPOINTMENT
        // =====================================

        AppointmentClass appointment =
                new AppointmentClass();

        // =====================================
        // CUSTOMER DETAILS
        // =====================================

        appointment.setCustomerName(
                request.getCustomerName());

        appointment.setCustomerPhone(
                request.getCustomerPhone());

        appointment.setCustomerEmail(
                request.getCustomerEmail());

        // =====================================
        // APPOINTMENT DATE
        // =====================================

        appointment.setAppointmentDate(
                request.getAppointmentDate());

        // =====================================
        // APPOINTMENT TIME
        // =====================================

        appointment.setAppointmentTime(
                normalizeTime(
                        request.getAppointmentTime()));

        // =====================================
        // TOTAL DURATION
        // =====================================

        appointment.setTotalDuration(
                totalDuration);

        // =====================================
        // TOTAL PRICE
        // =====================================

        appointment.setTotalPrice(
                totalPrice);

        // =====================================
        // BOOKING STATUS
        // =====================================

        if (request.getBookingStatus() != null
                && !request.getBookingStatus().isBlank()) {

            appointment.setBookingStatus(
                    BookingStatus.valueOf(
                            request.getBookingStatus()
                                    .toUpperCase()));

        } else {

            appointment.setBookingStatus(
                    BookingStatus.BOOKED);
        }

        // =====================================
        // PAYMENT STATUS
        // =====================================

        if (request.getPaymentStatus() != null
                && !request.getPaymentStatus().isBlank()) {

            appointment.setPaymentStatus(
                    PaymentStatus.valueOf(
                            request.getPaymentStatus()
                                    .toUpperCase()));

        } else {

            appointment.setPaymentStatus(
                    PaymentStatus.PENDING);
        }

        // =====================================
        // VERIFIED RELATIONSHIPS
        // =====================================

        appointment.setSalon(salon);

        appointment.setSalonService(
                selectedServices.get(0));

        appointment.setServices(
                selectedServices);

        appointment.setEmployee(
                employee);

        appointment.setTimeSlot(
                startingSlot);

        // =====================================
        // SAVE APPOINTMENT
        // =====================================

        AppointmentClass savedAppointment =
                appointmentRepository.save(
                        appointment);

        // =====================================
        // AUTOMATIC BILL CREATION
        // =====================================

        BillClass bill =
                new BillClass();

        bill.setAppointment(
                savedAppointment);

        bill.setPaymentStatus(
                PaymentStatus.PENDING);

        bill.setPaymentMethod(
                "CASH");

        billClassService.addBill(bill);

        // =====================================
        // RETURN SAVED APPOINTMENT
        // =====================================

        return savedAppointment;
    }

    // =========================================
    // FIND CONTINUOUS AVAILABLE SLOTS
    // =========================================

    private List<TimeSlotClass> findContinuousAvailableSlots(
            List<TimeSlotClass> availableSlots,
            TimeSlotClass startingSlot,
            int requiredSlots) {

        List<TimeSlotClass> result =
                new ArrayList<>();

        LocalTime startTime =
                parseTime(
                        startingSlot.getSlotTime());

        for (TimeSlotClass slot :
                availableSlots) {

            if (!parseTime(
                    slot.getSlotTime())
                    .equals(startTime)) {

                continue;
            }

            List<TimeSlotClass> candidate =
                    new ArrayList<>();

            candidate.add(slot);

            LocalTime expectedTime =
                    startTime.plusMinutes(
                            SLOT_INTERVAL_MINUTES);

            for (TimeSlotClass nextSlot :
                    availableSlots) {

                if (candidate.size()
                        >= requiredSlots) {

                    break;
                }

                LocalTime nextTime =
                        parseTime(
                                nextSlot.getSlotTime());

                if (nextTime.equals(expectedTime)) {

                    candidate.add(nextSlot);

                    expectedTime =
                            expectedTime.plusMinutes(
                                    SLOT_INTERVAL_MINUTES);
                }
            }

            if (candidate.size()
                    >= requiredSlots) {

                if (candidate.get(0).getId()
                        .equals(startingSlot.getId())) {

                    return candidate;
                }
            }
        }

        return result;
    }

    // =========================================
    // PARSE TIME
    // =========================================

    private LocalTime parseTime(String time) {

        if (time == null || time.isBlank()) {

            throw new RuntimeException(
                    "Invalid Time Slot time");
        }

        String normalizedTime =
                time.trim()
                        .replaceAll("\\s+", " ");

        // =====================================
        // 12-HOUR FORMAT
        // =====================================

        try {

            return LocalTime.parse(
                    normalizedTime,
                    TIME_FORMATTER);

        } catch (DateTimeParseException ignored) {
        }

        // =====================================
        // 24-HOUR FORMAT
        // =====================================

        try {

            return LocalTime.parse(
                    normalizedTime);

        } catch (DateTimeParseException ignored) {

            throw new RuntimeException(
                    "Invalid Time Slot format: "
                            + time);
        }
    }

    // =========================================
    // NORMALIZE TIME
    // =========================================

    private String normalizeTime(String time) {

        if (time == null || time.isBlank()) {

            throw new RuntimeException(
                    "Time is required");
        }

        LocalTime parsedTime =
                parseTime(time);

        return parsedTime.format(
                TIME_FORMATTER);
    }

    // =========================================
    // GET ALL APPOINTMENTS
    // =========================================

    public List<AppointmentClass> getAllAppointments() {

        return appointmentRepository.findAll();
    }

    // =========================================
    // GET MY APPOINTMENTS
    // LOGGED-IN CUSTOMER
    // =========================================

    @Transactional
    public List<AppointmentClass> getMyAppointments(
            String customerEmail) {

        if (customerEmail == null
                || customerEmail.isBlank()) {

            throw new RuntimeException(
                    "Authenticated customer email is required");
        }

        return appointmentRepository.findByCustomerEmail(
                customerEmail.trim());
    }

    // =========================================
    // GET APPOINTMENT BY ID
    // =========================================

    public AppointmentClass getAppointmentById(
            Long id) {

        return appointmentRepository.findById(id)
                .orElseThrow(
                        () -> new RuntimeException(
                                "Appointment Not Found"));
    }

    // =========================================
    // UPDATE APPOINTMENT
    // =========================================
    //
    // IMPORTANT:
    // If only Booking Status / Payment Status
    // changes, schedulingChanged remains FALSE.
    //
    // Therefore:
    // - No time-slot validation
    // - No slot release
    // - No slot booking
    // - No continuous-slot error
    //
    // If salon/employee/service/date/time/slot
    // actually changes, schedulingChanged becomes TRUE
    // and full scheduling validation runs.
    // =========================================

    @Transactional
    public AppointmentClass updateAppointment(
            Long id,
            AppointmentClass appointmentClass) {

        // =====================================
        // FIND EXISTING APPOINTMENT
        // =====================================

        AppointmentClass existingAppointment =
                appointmentRepository.findById(id)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Appointment Not Found"));

        // =====================================
        // DETERMINE WHETHER SCHEDULING CHANGED
        // =====================================

        boolean schedulingChanged = false;

        // =====================================
        // CHECK SALON CHANGE
        // =====================================

        if (appointmentClass.getSalon() != null
                && appointmentClass.getSalon().getId() != null) {

            Long incomingSalonId =
                    appointmentClass.getSalon().getId();

            Long existingSalonId =
                    existingAppointment.getSalon() != null
                            ? existingAppointment
                                    .getSalon()
                                    .getId()
                            : null;

            if (existingSalonId == null
                    || !incomingSalonId.equals(
                            existingSalonId)) {

                schedulingChanged = true;
            }
        }

        // =====================================
        // CHECK EMPLOYEE CHANGE
        // =====================================

        if (appointmentClass.getEmployee() != null
                && appointmentClass.getEmployee().getId() != null) {

            Long incomingEmployeeId =
                    appointmentClass.getEmployee().getId();

            Long existingEmployeeId =
                    existingAppointment.getEmployee() != null
                            ? existingAppointment
                                    .getEmployee()
                                    .getId()
                            : null;

            if (existingEmployeeId == null
                    || !incomingEmployeeId.equals(
                            existingEmployeeId)) {

                schedulingChanged = true;
            }
        }

        // =====================================
        // CHECK TIME SLOT CHANGE
        // =====================================

        if (appointmentClass.getTimeSlot() != null
                && appointmentClass.getTimeSlot().getId() != null) {

            Long incomingTimeSlotId =
                    appointmentClass.getTimeSlot().getId();

            Long existingTimeSlotId =
                    existingAppointment.getTimeSlot() != null
                            ? existingAppointment
                                    .getTimeSlot()
                                    .getId()
                            : null;

            if (existingTimeSlotId == null
                    || !incomingTimeSlotId.equals(
                            existingTimeSlotId)) {

                schedulingChanged = true;
            }
        }

        // =====================================
        // CHECK DATE CHANGE
        // =====================================

        if (appointmentClass.getAppointmentDate() != null) {

            LocalDate incomingDate =
                    appointmentClass
                            .getAppointmentDate();

            LocalDate existingDate =
                    existingAppointment
                            .getAppointmentDate();

            if (existingDate == null
                    || !incomingDate.equals(existingDate)) {

                schedulingChanged = true;
            }
        }

        // =====================================
        // CHECK TIME CHANGE
        // =====================================

        if (appointmentClass.getAppointmentTime() != null
                && !appointmentClass
                        .getAppointmentTime()
                        .isBlank()) {

            String incomingTime =
                    normalizeTime(
                            appointmentClass
                                    .getAppointmentTime());

            String existingTime =
                    existingAppointment
                            .getAppointmentTime() != null
                                    ? normalizeTime(
                                            existingAppointment
                                                    .getAppointmentTime())
                                    : null;

            if (existingTime == null
                    || !incomingTime.equals(
                            existingTime)) {

                schedulingChanged = true;
            }
        }

        // =====================================
        // CHECK MULTIPLE SERVICES CHANGE
        // =====================================

        if (appointmentClass.getServices() != null
                && !appointmentClass
                        .getServices()
                        .isEmpty()) {

            Set<Long> incomingServiceIds =
                    appointmentClass
                            .getServices()
                            .stream()
                            .filter(service ->
                                    service != null
                                            && service.getId() != null)
                            .map(SalonService::getId)
                            .collect(
                                    Collectors.toSet());

            Set<Long> existingServiceIds =
                    existingAppointment.getServices() != null
                            ? existingAppointment
                                    .getServices()
                                    .stream()
                                    .filter(service ->
                                            service != null
                                                    && service.getId() != null)
                                    .map(SalonService::getId)
                                    .collect(
                                            Collectors.toSet())
                            : new HashSet<>();

            if (!incomingServiceIds.equals(
                    existingServiceIds)) {

                schedulingChanged = true;
            }
        }

        // =====================================
        // CHECK SINGLE SERVICE CHANGE
        // OLD COMPATIBILITY
        // =====================================

        if (appointmentClass.getSalonService() != null
                && appointmentClass
                        .getSalonService()
                        .getId() != null) {

            Long incomingServiceId =
                    appointmentClass
                            .getSalonService()
                            .getId();

            Long existingServiceId = null;

            if (existingAppointment.getSalonService() != null) {

                existingServiceId =
                        existingAppointment
                                .getSalonService()
                                .getId();
            }

            if (existingServiceId == null
                    || !incomingServiceId.equals(
                            existingServiceId)) {

                schedulingChanged = true;
            }
        }

        // =====================================
        // SCHEDULING CHANGE
        // =====================================

        if (schedulingChanged) {

            // =================================
            // SALON IS REQUIRED
            // =================================

            if (appointmentClass.getSalon() == null
                    || appointmentClass
                            .getSalon()
                            .getId() == null) {

                throw new RuntimeException(
                        "Salon is required");
            }

            // =================================
            // FIND SALON
            // =================================

            Salon salon =
                    salonRepository.findById(
                            appointmentClass
                                    .getSalon()
                                    .getId())
                            .orElseThrow(
                                    () -> new RuntimeException(
                                            "Salon Not Found"));

            // =================================
            // FIND SERVICES
            // =================================

            List<SalonService> selectedServices =
                    new ArrayList<>();

            // =================================
            // MULTIPLE SERVICE LIST
            // =================================

            if (appointmentClass.getServices() != null
                    && !appointmentClass
                            .getServices()
                            .isEmpty()) {

                Set<Long> serviceIds =
                        new HashSet<>();

                for (SalonService requestedService :
                        appointmentClass.getServices()) {

                    if (requestedService == null
                            || requestedService.getId() == null) {

                        throw new RuntimeException(
                                "Invalid Service");
                    }

                    serviceIds.add(
                            requestedService.getId());
                }

                for (Long serviceId :
                        serviceIds) {

                    SalonService service =
                            serviceRepository
                                    .findById(serviceId)
                                    .orElseThrow(
                                            () -> new RuntimeException(
                                                    "Service Not Found"));

                    validateServiceForSalon(
                            service,
                            salon);

                    selectedServices.add(service);
                }

            } else {

                // =================================
                // OLD SINGLE SERVICE COMPATIBILITY
                // =================================

                if (appointmentClass
                            .getSalonService() == null
                        || appointmentClass
                                .getSalonService()
                                .getId() == null) {

                    throw new RuntimeException(
                            "Service is required");
                }

                SalonService service =
                        serviceRepository.findById(
                                appointmentClass
                                        .getSalonService()
                                        .getId())
                                .orElseThrow(
                                        () -> new RuntimeException(
                                                "Service Not Found"));

                validateServiceForSalon(
                        service,
                        salon);

                selectedServices.add(service);
            }

            // =================================
            // FIND EMPLOYEE
            // =================================

            if (appointmentClass.getEmployee() == null
                    || appointmentClass
                            .getEmployee()
                            .getId() == null) {

                throw new RuntimeException(
                        "Employee is required");
            }

            EmployeeClass employee =
                    employeeRepository.findById(
                            appointmentClass
                                    .getEmployee()
                                    .getId())
                            .orElseThrow(
                                    () -> new RuntimeException(
                                            "Employee Not Found"));

            // =================================
            // CHECK EMPLOYEE → SALON
            // =================================

            if (employee.getSalon() == null
                    || !employee.getSalon().getId()
                            .equals(salon.getId())) {

                throw new RuntimeException(
                        "Selected Employee does not belong "
                                + "to the selected Salon");
            }

            // =================================
            // CHECK EMPLOYEE → SERVICES
            // =================================

            for (SalonService service :
                    selectedServices) {

                validateEmployeeService(
                        employee,
                        service);
            }

            // =================================
            // FIND NEW TIME SLOT
            // =================================

            if (appointmentClass.getTimeSlot() == null
                    || appointmentClass
                            .getTimeSlot()
                            .getId() == null) {

                throw new RuntimeException(
                        "Time Slot is required");
            }

            TimeSlotClass newSlot =
                    timeSlotRepository.findById(
                            appointmentClass
                                    .getTimeSlot()
                                    .getId())
                            .orElseThrow(
                                    () -> new RuntimeException(
                                            "Time Slot Not Found"));

            // =================================
            // DATE VALIDATION
            // =================================

            if (appointmentClass
                    .getAppointmentDate() == null) {

                throw new RuntimeException(
                        "Appointment date is required");
            }

            if (!appointmentClass
                    .getAppointmentDate()
                    .equals(newSlot.getSlotDate())) {

                throw new RuntimeException(
                        "Appointment date does not match "
                                + "selected Time Slot");
            }

            // =================================
            // TIME VALIDATION
            // =================================

            if (appointmentClass
                    .getAppointmentTime() == null
                    || appointmentClass
                            .getAppointmentTime()
                            .isBlank()) {

                throw new RuntimeException(
                        "Appointment time is required");
            }

            if (!normalizeTime(
                    appointmentClass
                            .getAppointmentTime())
                    .equals(
                            normalizeTime(
                                    newSlot.getSlotTime()))) {

                throw new RuntimeException(
                        "Appointment time does not match "
                                + "selected Time Slot");
            }

            // =================================
            // SLOT → SALON
            // =================================

            if (newSlot.getSalon() == null
                    || !newSlot.getSalon().getId()
                            .equals(salon.getId())) {

                throw new RuntimeException(
                        "Selected Time Slot does not belong "
                                + "to the selected Salon");
            }

            // =================================
            // SLOT → EMPLOYEE
            // =================================

            if (newSlot.getEmployee() == null
                    || !newSlot.getEmployee().getId()
                            .equals(employee.getId())) {

                throw new RuntimeException(
                        "Selected Time Slot does not belong "
                                + "to the selected Employee");
            }

            // =================================
            // CALCULATE TOTAL DURATION
            // =================================

            int totalDuration = 0;

            for (SalonService service :
                    selectedServices) {

                if (service.getDuration() != null
                        && service.getDuration() > 0) {

                    totalDuration +=
                            service.getDuration();
                }
            }

            if (totalDuration <= 0) {

                throw new RuntimeException(
                        "Selected services do not have "
                                + "a valid duration");
            }

            // =================================
            // CALCULATE TOTAL PRICE
            // =================================

            double totalPrice = 0.0;

            for (SalonService service :
                    selectedServices) {

                if (service.getPrice() != null) {

                    totalPrice +=
                            service.getPrice();
                }
            }

            // =================================
            // GET AVAILABLE SLOTS
            // =================================

            List<TimeSlotClass> availableSlots =
                    timeSlotRepository
                            .findBySalonIdAndEmployeeIdAndSlotDateAndSlotStatus(
                                    salon.getId(),
                                    employee.getId(),
                                    appointmentClass
                                            .getAppointmentDate(),
                                    TimeSlotStatus.AVAILABLE);

            availableSlots.sort(
                    Comparator.comparing(
                            slot -> parseTime(
                                    slot.getSlotTime())));

            // =================================
            // REQUIRED SLOTS
            // =================================

            int requiredSlots =
                    (int) Math.ceil(
                            (double) totalDuration
                                    / SLOT_INTERVAL_MINUTES);

            // =================================
            // OLD SLOT
            // =================================

            TimeSlotClass oldSlot =
                    existingAppointment.getTimeSlot();

            boolean slotChanged =
                    oldSlot == null
                            || !oldSlot.getId()
                                    .equals(
                                            newSlot.getId());

            List<TimeSlotClass> slotsToBook =
                    new ArrayList<>();

            // =================================
            // SLOT CHANGED
            // =================================

            if (slotChanged) {

                // =================================
                // NEW SLOT MUST BE AVAILABLE
                // =================================

                if (newSlot.getSlotStatus()
                        == TimeSlotStatus.BOOKED) {

                    throw new RuntimeException(
                            "Selected Time Slot Is Already Booked");
                }

                if (newSlot.getSlotStatus()
                        != TimeSlotStatus.AVAILABLE) {

                    throw new RuntimeException(
                            "Selected Time Slot Is Not Available");
                }

                // =================================
                // FIND CONTINUOUS SLOTS
                // =================================

                slotsToBook =
                        findContinuousAvailableSlots(
                                availableSlots,
                                newSlot,
                                requiredSlots);

                if (slotsToBook.size()
                        < requiredSlots) {

                    throw new RuntimeException(
                            "Not enough continuous time "
                                    + "available for the selected services. "
                                    + "Total duration is "
                                    + totalDuration
                                    + " minutes.");
                }

                // =================================
                // RELEASE OLD SLOTS
                // =================================

                releaseAppointmentSlots(
                        existingAppointment);

                // =================================
                // BOOK NEW SLOTS
                // =================================

                for (TimeSlotClass slot :
                        slotsToBook) {

                    slot.setSlotStatus(
                            TimeSlotStatus.BOOKED);

                    timeSlotRepository.save(slot);
                }

            } else {

                // =================================
                // SAME SLOT
                //
                // No need to change slot status.
                // Existing appointment already owns
                // this booked slot.
                // =================================

                slotsToBook =
                        findExistingAppointmentSlots(
                                existingAppointment,
                                requiredSlots);
            }

            // =================================
            // UPDATE SCHEDULING DATA
            // =================================

            existingAppointment.setAppointmentDate(
                    appointmentClass
                            .getAppointmentDate());

            existingAppointment.setAppointmentTime(
                    normalizeTime(
                            appointmentClass
                                    .getAppointmentTime()));

            existingAppointment.setTotalDuration(
                    totalDuration);

            existingAppointment.setTotalPrice(
                    totalPrice);

            existingAppointment.setSalon(
                    salon);

            existingAppointment.setSalonService(
                    selectedServices.get(0));

            existingAppointment.setServices(
                    selectedServices);

            existingAppointment.setEmployee(
                    employee);

            existingAppointment.setTimeSlot(
                    newSlot);
        }

        // =====================================
        // UPDATE CUSTOMER DETAILS
        // =====================================

        if (appointmentClass.getCustomerName() != null) {

            existingAppointment.setCustomerName(
                    appointmentClass
                            .getCustomerName());
        }

        if (appointmentClass.getCustomerPhone() != null) {

            existingAppointment.setCustomerPhone(
                    appointmentClass
                            .getCustomerPhone());
        }

        if (appointmentClass.getCustomerEmail() != null) {

            existingAppointment.setCustomerEmail(
                    appointmentClass
                            .getCustomerEmail());
        }

        // =====================================
        // UPDATE BOOKING STATUS
        // =====================================

        if (appointmentClass.getBookingStatus()
                != null) {

            existingAppointment.setBookingStatus(
                    appointmentClass
                            .getBookingStatus());
        }

        // =====================================
        // UPDATE PAYMENT STATUS
        // =====================================

        PaymentStatus updatedPaymentStatus =
                appointmentClass.getPaymentStatus();

        if (updatedPaymentStatus == null) {

            updatedPaymentStatus =
                    existingAppointment
                            .getPaymentStatus() != null
                                    ? existingAppointment
                                            .getPaymentStatus()
                                    : PaymentStatus.PENDING;
        }

        existingAppointment.setPaymentStatus(
                updatedPaymentStatus);

        // =====================================
        // SAVE UPDATED APPOINTMENT
        // =====================================

        AppointmentClass savedAppointment =
                appointmentRepository.save(
                        existingAppointment);

     // =====================================================
     // UPDATE RELATED BILL
     // APPOINTMENT → BILL
     // =====================================================

     Optional<BillClass> relatedBill =
             billRepository.findByAppointmentId(
                     savedAppointment.getId());

     if (relatedBill.isPresent()) {

         BillClass bill =
                 relatedBill.get();

         bill.setPaymentStatus(
                 updatedPaymentStatus);

         billRepository.save(bill);
     }

        // =====================================
        // RETURN UPDATED APPOINTMENT
        // =====================================

        return savedAppointment;
    }

    // =========================================
    // FIND EXISTING APPOINTMENT SLOTS
    // =========================================
    //
    // Used when appointment details have not
    // changed and we only update status/payment.
    //
    // This method DOES NOT query AVAILABLE slots
    // and does not modify slot status.
    // =========================================

    private List<TimeSlotClass> findExistingAppointmentSlots(
            AppointmentClass appointment,
            int requiredSlots) {

        List<TimeSlotClass> result =
                new ArrayList<>();

        if (appointment == null
                || appointment.getTimeSlot() == null) {

            return result;
        }

        TimeSlotClass startingSlot =
                appointment.getTimeSlot();

        result.add(startingSlot);

        if (requiredSlots <= 1) {
            return result;
        }

        if (startingSlot.getSalon() == null
                || startingSlot.getEmployee() == null
                || appointment.getAppointmentDate() == null) {

            return result;
        }

        List<TimeSlotClass> bookedSlots =
                timeSlotRepository
                        .findBySalonIdAndEmployeeIdAndSlotDateAndSlotStatus(
                                startingSlot
                                        .getSalon()
                                        .getId(),
                                startingSlot
                                        .getEmployee()
                                        .getId(),
                                appointment
                                        .getAppointmentDate(),
                                TimeSlotStatus.BOOKED);

        bookedSlots.sort(
                Comparator.comparing(
                        slot -> parseTime(
                                slot.getSlotTime())));

        LocalTime expectedTime =
                parseTime(
                        startingSlot.getSlotTime())
                        .plusMinutes(
                                SLOT_INTERVAL_MINUTES);

        for (TimeSlotClass slot :
                bookedSlots) {

            if (result.size()
                    >= requiredSlots) {

                break;
            }

            if (parseTime(
                    slot.getSlotTime())
                    .equals(expectedTime)) {

                result.add(slot);

                expectedTime =
                        expectedTime.plusMinutes(
                                SLOT_INTERVAL_MINUTES);
            }
        }

        return result;
    }

    // =========================================
    // VALIDATE SERVICE → SALON
    // =========================================

    private void validateServiceForSalon(
            SalonService service,
            Salon salon) {

        if (service.getSalon() == null
                || !service.getSalon().getId()
                        .equals(salon.getId())) {

            throw new RuntimeException(
                    "Selected Service does not belong "
                            + "to the selected Salon");
        }
    }

    // =========================================
    // VALIDATE EMPLOYEE → SERVICE
    // =========================================

    private void validateEmployeeService(
            EmployeeClass employee,
            SalonService service) {

        if (employee.getServices() == null
                || employee.getServices().stream()
                        .noneMatch(
                                assignedService ->
                                        assignedService
                                                .getId()
                                                .equals(
                                                        service.getId()))) {

            throw new RuntimeException(
                    "Service '"
                            + service.getServiceName()
                            + "' is not assigned to the "
                            + "selected Employee");
        }
    }

    // =========================================
    // RELEASE APPOINTMENT SLOTS
    // =========================================

    private void releaseAppointmentSlots(
            AppointmentClass appointment) {

        if (appointment == null
                || appointment.getTimeSlot() == null
                || appointment.getAppointmentDate() == null
                || appointment.getEmployee() == null
                || appointment.getTimeSlot().getSalon() == null) {

            return;
        }

        TimeSlotClass startingSlot =
                appointment.getTimeSlot();

        int duration =
                appointment.getTotalDuration() != null
                        ? appointment.getTotalDuration()
                        : 30;

        int requiredSlots =
                (int) Math.ceil(
                        (double) duration
                                / SLOT_INTERVAL_MINUTES);

        LocalTime startTime =
                parseTime(
                        startingSlot
                                .getSlotTime());

        List<TimeSlotClass> allSlots =
                timeSlotRepository
                        .findBySalonIdAndEmployeeIdAndSlotDateAndSlotStatus(
                                startingSlot
                                        .getSalon()
                                        .getId(),
                                startingSlot
                                        .getEmployee()
                                        .getId(),
                                appointment
                                        .getAppointmentDate(),
                                TimeSlotStatus.BOOKED);

        for (TimeSlotClass slot :
                allSlots) {

            LocalTime slotTime =
                    parseTime(
                            slot.getSlotTime());

            long difference =
                    Duration.between(
                            startTime,
                            slotTime)
                            .toMinutes();

            if (difference >= 0
                    && difference
                            < requiredSlots
                                    * SLOT_INTERVAL_MINUTES) {

                slot.setSlotStatus(
                        TimeSlotStatus.AVAILABLE);

                timeSlotRepository.save(slot);
            }
        }
    }

    // =========================================
    // DELETE APPOINTMENT
    // =========================================

    @Transactional
    public String deleteAppointment(Long id) {

        AppointmentClass appointment =
                appointmentRepository.findById(id)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Appointment Not Found"));

        // =====================================
        // RELEASE ALL BOOKED SLOTS
        // =====================================

        releaseAppointmentSlots(
                appointment);

        // =====================================
        // DELETE APPOINTMENT
        // =====================================

        appointmentRepository.delete(
                appointment);

        return "Appointment Deleted Successfully";
    }

    // =========================================
    // SEARCH APPOINTMENTS
    // =========================================

    public List<AppointmentClass>
    searchAppointments(
            String customerName) {

        Pageable pageable =
                PageRequest.of(
                        0,
                        Integer.MAX_VALUE);

        return appointmentRepository
                .findByCustomerNameContainingIgnoreCase(
                        customerName,
                        pageable)
                .getContent();
    }

    // =========================================
    // PAGINATION
    // =========================================

    public PaginationResponse<AppointmentClass>
    getAppointmentsPagination(
            int page,
            int size) {

        Pageable pageable =
                PageRequest.of(page, size);

        Page<AppointmentClass> appointmentPage =
                appointmentRepository.findAll(
                        pageable);

        return new PaginationResponse<>(
                appointmentPage.getContent(),
                appointmentPage.getNumber(),
                appointmentPage.getTotalPages(),
                appointmentPage.getTotalElements(),
                appointmentPage.getSize());
    }

    // =========================================
    // FILTER BY BOOKING STATUS
    // =========================================

    public List<AppointmentClass>
    getAppointmentsByBookingStatus(
            BookingStatus bookingStatus) {

        Pageable pageable =
                PageRequest.of(
                        0,
                        Integer.MAX_VALUE);

        return appointmentRepository
                .findByBookingStatus(
                        bookingStatus,
                        pageable)
                .getContent();
    }

    // =========================================
    // FILTER BY PAYMENT STATUS
    // =========================================

    public List<AppointmentClass>
    getAppointmentsByPaymentStatus(
            PaymentStatus paymentStatus) {

        Pageable pageable =
                PageRequest.of(
                        0,
                        Integer.MAX_VALUE);

        return appointmentRepository
                .findByPaymentStatus(
                        paymentStatus,
                        pageable)
                .getContent();
    }

    // =========================================
    // FILTER BY DATE
    // =========================================

    public List<AppointmentClass>
    getAppointmentsByDate(
            LocalDate date) {

        return appointmentRepository
                .findByAppointmentDate(date);
    }

    // =========================================
    // FILTER BY EMPLOYEE
    // =========================================

    public List<AppointmentClass>
    getAppointmentsByEmployee(
            Long employeeId) {

        return appointmentRepository
                .findByEmployeeId(employeeId);
    }
}