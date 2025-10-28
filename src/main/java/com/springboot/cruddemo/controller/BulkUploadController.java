package com.springboot.cruddemo.controller;

import com.springboot.cruddemo.dto.BulkUploadResponseDTO;
import com.springboot.cruddemo.service.BulkUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/bulk")
public class BulkUploadController {

    private final BulkUploadService bulkUploadService;

    public BulkUploadController(BulkUploadService bulkUploadService) {
        this.bulkUploadService = bulkUploadService;
    }

    /**
     * Upload employees from CSV file
     *
     * CSV Format:
     * firstName,lastName,email
     * Carlos,Garcia,carlos.garcia@example.com
     * María,Rodríguez,maria.rodriguez@example.com
     *
     * @param file CSV file containing employee data
     * @return BulkUploadResponseDTO with upload results
     */
    @PostMapping(value = "/employees/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BulkUploadResponseDTO> uploadEmployees(
            @RequestParam("file") MultipartFile file) {

        BulkUploadResponseDTO response = bulkUploadService.uploadEmployeesFromCsv(file);

        // Return 201 if all records were successful, 207 (Multi-Status) if partial success
        HttpStatus status = response.getFailedRecords() == 0 ?
                HttpStatus.CREATED : HttpStatus.MULTI_STATUS;

        return ResponseEntity.status(status).body(response);
    }
}
