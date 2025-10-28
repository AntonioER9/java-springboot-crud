package com.springboot.cruddemo.dto;

import java.util.List;

public class BulkUploadResponseDTO {

    private int totalRecords;
    private int successfulRecords;
    private int failedRecords;
    private List<String> errors;
    private List<EmployeeDTO> createdEmployees;

    // Constructors
    public BulkUploadResponseDTO() {
    }

    public BulkUploadResponseDTO(int totalRecords, int successfulRecords, int failedRecords,
                               List<String> errors, List<EmployeeDTO> createdEmployees) {
        this.totalRecords = totalRecords;
        this.successfulRecords = successfulRecords;
        this.failedRecords = failedRecords;
        this.errors = errors;
        this.createdEmployees = createdEmployees;
    }

    // Getters and Setters
    public int getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(int totalRecords) {
        this.totalRecords = totalRecords;
    }

    public int getSuccessfulRecords() {
        return successfulRecords;
    }

    public void setSuccessfulRecords(int successfulRecords) {
        this.successfulRecords = successfulRecords;
    }

    public int getFailedRecords() {
        return failedRecords;
    }

    public void setFailedRecords(int failedRecords) {
        this.failedRecords = failedRecords;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public List<EmployeeDTO> getCreatedEmployees() {
        return createdEmployees;
    }

    public void setCreatedEmployees(List<EmployeeDTO> createdEmployees) {
        this.createdEmployees = createdEmployees;
    }

    @Override
    public String toString() {
        return "BulkUploadResponseDTO{" +
                "totalRecords=" + totalRecords +
                ", successfulRecords=" + successfulRecords +
                ", failedRecords=" + failedRecords +
                ", errors=" + errors +
                ", createdEmployees=" + createdEmployees +
                '}';
    }
}
