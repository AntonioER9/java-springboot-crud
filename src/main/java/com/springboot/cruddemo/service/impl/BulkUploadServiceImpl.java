package com.springboot.cruddemo.service.impl;

import com.springboot.cruddemo.dto.BulkUploadResponseDTO;
import com.springboot.cruddemo.dto.EmployeeDTO;
import com.springboot.cruddemo.entity.Employee;
import com.springboot.cruddemo.repository.EmployeeRepository;
import com.springboot.cruddemo.service.BulkUploadService;
import com.springboot.cruddemo.util.CsvProcessingUtil;
import com.springboot.cruddemo.util.EmployeeMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
public class BulkUploadServiceImpl implements BulkUploadService {

    private final CsvProcessingUtil csvProcessingUtil;
    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

    public BulkUploadServiceImpl(CsvProcessingUtil csvProcessingUtil,
                               EmployeeRepository employeeRepository,
                               EmployeeMapper employeeMapper) {
        this.csvProcessingUtil = csvProcessingUtil;
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
    }

    @Override
    @Transactional
    public BulkUploadResponseDTO uploadEmployeesFromCsv(MultipartFile csvFile) {
        // Parse CSV file
        List<EmployeeDTO> employeeDTOs = csvProcessingUtil.parseCsvFile(csvFile);

        List<EmployeeDTO> createdEmployees = new ArrayList<>();
        List<String> errors = new ArrayList<>();

        int totalRecords = employeeDTOs.size();
        int successfulRecords = 0;
        int failedRecords = 0;

        // Process each employee
        for (int i = 0; i < employeeDTOs.size(); i++) {
            EmployeeDTO employeeDTO = employeeDTOs.get(i);
            int lineNumber = i + 2; // +2 because line 1 is header and arrays are 0-based

            try {
                // Check for duplicate email
                if (isDuplicateEmail(employeeDTO.getEmail())) {
                    errors.add("Line " + lineNumber + ": Email already exists - " + employeeDTO.getEmail());
                    failedRecords++;
                    continue;
                }

                // Convert DTO to Entity and save
                Employee employee = employeeMapper.toEntity(employeeDTO);
                Employee savedEmployee = employeeRepository.save(employee);

                // Convert back to DTO and add to success list
                EmployeeDTO savedEmployeeDTO = employeeMapper.toDTO(savedEmployee);
                createdEmployees.add(savedEmployeeDTO);
                successfulRecords++;

            } catch (Exception e) {
                errors.add("Line " + lineNumber + ": " + e.getMessage());
                failedRecords++;
            }
        }

        return new BulkUploadResponseDTO(
                totalRecords,
                successfulRecords,
                failedRecords,
                errors,
                createdEmployees
        );
    }

    private boolean isDuplicateEmail(String email) {
        return employeeRepository.existsByEmail(email);
    }
}
