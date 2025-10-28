package com.springboot.cruddemo.util;

import com.springboot.cruddemo.dto.EmployeeDTO;
import com.springboot.cruddemo.exception.CsvProcessingException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Component
public class CsvProcessingUtil {

    private static final String CSV_DELIMITER = ",";
    private static final int EXPECTED_COLUMNS = 3;
    private static final String[] EXPECTED_HEADERS = {"firstName", "lastName", "email"};

    public List<EmployeeDTO> parseCsvFile(MultipartFile file) {
        validateFile(file);

        List<EmployeeDTO> employees = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            int lineNumber = 0;
            boolean headerProcessed = false;

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                line = line.trim();

                if (line.isEmpty()) {
                    continue; // Skip empty lines
                }

                if (!headerProcessed) {
                    validateHeaders(line, lineNumber);
                    headerProcessed = true;
                    continue;
                }

                try {
                    EmployeeDTO employee = parseCsvLine(line, lineNumber);
                    employees.add(employee);
                } catch (Exception e) {
                    throw new CsvProcessingException("Error processing line " + lineNumber + ": " + e.getMessage());
                }
            }

            if (employees.isEmpty()) {
                throw new CsvProcessingException("No valid employee records found in CSV file");
            }

        } catch (IOException e) {
            throw new CsvProcessingException("Error reading CSV file: " + e.getMessage(), e);
        }

        return employees;
    }

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new CsvProcessingException("CSV file is required and cannot be empty");
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !originalFilename.toLowerCase().endsWith(".csv")) {
            throw new CsvProcessingException("File must have .csv extension");
        }

        // Check file size (limit to 5MB)
        if (file.getSize() > 5 * 1024 * 1024) {
            throw new CsvProcessingException("CSV file size cannot exceed 5MB");
        }
    }

    private void validateHeaders(String headerLine, int lineNumber) {
        String[] headers = headerLine.split(CSV_DELIMITER);

        if (headers.length != EXPECTED_COLUMNS) {
            throw new CsvProcessingException("Invalid header format at line " + lineNumber +
                    ". Expected " + EXPECTED_COLUMNS + " columns: firstName,lastName,email");
        }

        for (int i = 0; i < headers.length; i++) {
            String header = headers[i].trim();
            if (!header.equals(EXPECTED_HEADERS[i])) {
                throw new CsvProcessingException("Invalid header at line " + lineNumber +
                        ". Expected: " + String.join(",", EXPECTED_HEADERS));
            }
        }
    }

    private EmployeeDTO parseCsvLine(String line, int lineNumber) {
        String[] values = line.split(CSV_DELIMITER);

        if (values.length != EXPECTED_COLUMNS) {
            throw new CsvProcessingException("Invalid number of columns. Expected " + EXPECTED_COLUMNS +
                    " but got " + values.length);
        }

        String firstName = values[0].trim();
        String lastName = values[1].trim();
        String email = values[2].trim();

        validateEmployeeData(firstName, lastName, email);

        return new EmployeeDTO(firstName, lastName, email);
    }

    private void validateEmployeeData(String firstName, String lastName, String email) {
        if (firstName.isEmpty()) {
            throw new CsvProcessingException("First name cannot be empty");
        }

        if (lastName.isEmpty()) {
            throw new CsvProcessingException("Last name cannot be empty");
        }

        if (email.isEmpty()) {
            throw new CsvProcessingException("Email cannot be empty");
        }

        // Basic email validation
        if (!email.contains("@") || !email.contains(".")) {
            throw new CsvProcessingException("Invalid email format: " + email);
        }
    }
}
