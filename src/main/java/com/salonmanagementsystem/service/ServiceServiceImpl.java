package com.salonmanagementsystem.service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.salonmanagementsystem.dto.ServiceRequest;
import com.salonmanagementsystem.dto.ServiceResponse;
import com.salonmanagementsystem.entity.Category;
import com.salonmanagementsystem.entity.Salon;
import com.salonmanagementsystem.entity.SalonService;
import com.salonmanagementsystem.exception.BadRequestException;
import com.salonmanagementsystem.exception.ResourceNotFoundException;
import com.salonmanagementsystem.repository.CategoryRepository;
import com.salonmanagementsystem.repository.SalonRepository;
import com.salonmanagementsystem.repository.ServiceRepository;
import com.salonmanagementsystem.util.FileUploadUtil;

@Service
public class ServiceServiceImpl
        implements ServiceService {


    private final ServiceRepository serviceRepository;

    private final CategoryRepository categoryRepository;

    private final SalonRepository salonRepository;

    private final FileUploadUtil fileUploadUtil;


    public ServiceServiceImpl(

            ServiceRepository serviceRepository,

            CategoryRepository categoryRepository,

            SalonRepository salonRepository,

            FileUploadUtil fileUploadUtil
    ) {

        this.serviceRepository =
                serviceRepository;

        this.categoryRepository =
                categoryRepository;

        this.salonRepository =
                salonRepository;

        this.fileUploadUtil =
                fileUploadUtil;
    }


    // =========================================
    // ADD SERVICE
    // =========================================

    @Override
    public ServiceResponse addService(
            ServiceRequest request
    ) {

        try {


            // =================================
            // FIND CATEGORY
            // =================================

            Category category =
                    categoryRepository
                            .findById(
                                    request.getCategoryId()
                            )
                            .orElseThrow(() ->
                                    new ResourceNotFoundException(
                                            "Category Not Found"
                                    ));


            // =================================
            // FIND SALON
            // =================================

            Salon salon =
                    salonRepository
                            .findById(
                                    request.getSalonId()
                            )
                            .orElseThrow(() ->
                                    new ResourceNotFoundException(
                                            "Salon Not Found"
                                    ));


            // =================================
            // UPLOAD IMAGE
            // =================================

            String imageUrl =
                    fileUploadUtil.uploadFile(
                            request.getImage(),
                            "services"
                    );


            // =================================
            // CREATE SERVICE
            // =================================

            SalonService service =
                    new SalonService();


            service.setServiceName(
                    request.getServiceName()
            );

            service.setDescription(
                    request.getDescription()
            );

            service.setPrice(
                    request.getPrice()
            );

            service.setDuration(
                    request.getDuration()
            );

            service.setStatus(
                    request.getStatus()
            );

            service.setImageUrl(
                    imageUrl
            );

            service.setCategory(
                    category
            );

            service.setSalon(
                    salon
            );


            // =================================
            // SAVE
            // =================================

            SalonService savedService =
                    serviceRepository.save(service);


            return mapToResponse(
                    savedService
            );


        } catch (IOException e) {

            throw new BadRequestException(
                    "Unable to upload service image."
            );

        }

    }


    // =========================================
    // GET ALL SERVICES
    // =========================================

    @Override
    public List<ServiceResponse>
    getAllServices() {

        return serviceRepository
                .findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

    }


    // =========================================
    // GET SERVICE BY ID
    // =========================================

    @Override
    public ServiceResponse getServiceById(
            Long id
    ) {

        SalonService service =
                serviceRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Service Not Found"
                                ));

        return mapToResponse(service);

    }


    // =========================================
    // GET SERVICES BY SALON
    // =========================================

    @Override
    public List<ServiceResponse>
    getServicesBySalon(
            Long salonId
    ) {


        // Check salon

        salonRepository
                .findById(salonId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Salon Not Found"
                        ));


        return serviceRepository
                .findBySalonId(salonId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

    }


    // =========================================
    // GET SERVICES BY EMPLOYEE
    // =========================================

    @Override
    public List<ServiceResponse>
    getServicesByEmployee(
            Long employeeId
    ) {

        return serviceRepository
                .findByEmployeesId(employeeId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

    }


    // =========================================
    // UPDATE SERVICE
    // =========================================

    @Override
    public ServiceResponse updateService(
            Long id,
            ServiceRequest request
    ) {

        try {


            // =================================
            // FIND SERVICE
            // =================================

            SalonService service =
                    serviceRepository
                            .findById(id)
                            .orElseThrow(() ->
                                    new ResourceNotFoundException(
                                            "Service Not Found"
                                    ));


            // =================================
            // FIND CATEGORY
            // =================================

            Category category =
                    categoryRepository
                            .findById(
                                    request.getCategoryId()
                            )
                            .orElseThrow(() ->
                                    new ResourceNotFoundException(
                                            "Category Not Found"
                                    ));


            // =================================
            // FIND SALON
            // =================================

            Salon salon =
                    salonRepository
                            .findById(
                                    request.getSalonId()
                            )
                            .orElseThrow(() ->
                                    new ResourceNotFoundException(
                                            "Salon Not Found"
                                    ));


            // =================================
            // UPDATE DETAILS
            // =================================

            service.setServiceName(
                    request.getServiceName()
            );

            service.setDescription(
                    request.getDescription()
            );

            service.setPrice(
                    request.getPrice()
            );

            service.setDuration(
                    request.getDuration()
            );

            service.setStatus(
                    request.getStatus()
            );

            service.setCategory(
                    category
            );

            service.setSalon(
                    salon
            );


            // =================================
            // UPDATE IMAGE
            // =================================

            if (request.getImage() != null &&
                    !request.getImage().isEmpty()) {

                String imageUrl =
                        fileUploadUtil.uploadFile(
                                request.getImage(),
                                "services"
                        );

                service.setImageUrl(
                        imageUrl
                );

            }


            // =================================
            // SAVE
            // =================================

            SalonService updatedService =
                    serviceRepository.save(service);


            return mapToResponse(
                    updatedService
            );


        } catch (IOException e) {

            throw new BadRequestException(
                    "Unable to upload service image."
            );

        }

    }


    // =========================================
    // DELETE SERVICE
    // =========================================

    @Override
    public void deleteService(
            Long id
    ) {

        SalonService service =
                serviceRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Service Not Found"
                                ));


        serviceRepository.delete(service);

    }


    // =========================================
    // ENTITY → RESPONSE
    // =========================================

    private ServiceResponse mapToResponse(
            SalonService service
    ) {

        ServiceResponse response =
                new ServiceResponse();


        response.setId(
                service.getId()
        );

        response.setServiceName(
                service.getServiceName()
        );

        response.setDescription(
                service.getDescription()
        );

        response.setPrice(
                service.getPrice()
        );

        response.setDuration(
                service.getDuration()
        );

        response.setImageUrl(
                service.getImageUrl()
        );

        response.setStatus(
                service.getStatus()
        );


        // =====================================
        // CATEGORY
        // =====================================

        if (service.getCategory() != null) {

            response.setCategoryId(
                    service.getCategory().getId()
            );

            response.setCategoryName(
                    service.getCategory()
                            .getCategoryName()
            );

        }


        // =====================================
        // SALON
        // =====================================

        if (service.getSalon() != null) {

            response.setSalonId(
                    service.getSalon().getId()
            );

            response.setSalonName(
                    service.getSalon().getSalonName()
            );

        }


        return response;

    }

}