package com.springboot.cruddemo.service;

import com.springboot.cruddemo.dto.BulkUploadResponseDTO;
import org.springframework.web.multipart.MultipartFile;

public interface BulkUploadService {

    BulkUploadResponseDTO uploadEmployeesFromCsv(MultipartFile csvFile);
}
