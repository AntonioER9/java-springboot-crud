package com.springboot.cruddemo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.springboot.cruddemo.dto.EmployeeDTO;
import com.springboot.cruddemo.exception.BadRequestException;
import com.springboot.cruddemo.service.EmployeeService;
import com.springboot.cruddemo.util.PatchValidationUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final ObjectMapper objectMapper;
    private final PatchValidationUtil patchValidationUtil;

    public EmployeeController(EmployeeService theEmployeeService,
                            ObjectMapper theObjectMapper,
                            PatchValidationUtil thePatchValidationUtil) {
        this.employeeService = theEmployeeService;
        this.objectMapper = theObjectMapper;
        this.patchValidationUtil = thePatchValidationUtil;
    }

    // GET /employees - get all employees
    @GetMapping("/employees")
    public ResponseEntity<List<EmployeeDTO>> findAll() {
        List<EmployeeDTO> employees = employeeService.findAll();
        return ResponseEntity.ok(employees);
    }

    // GET /employees/{employeeId} - get employee by id
    @GetMapping("/employees/{employeeId}")
    public ResponseEntity<EmployeeDTO> getEmployee(@PathVariable int employeeId) {
        EmployeeDTO employee = employeeService.findById(employeeId);
        return ResponseEntity.ok(employee);
    }

    // POST /employees - create new employee
    @PostMapping("/employees")
    public ResponseEntity<EmployeeDTO> addEmployee(@RequestBody EmployeeDTO theEmployeeDTO) {
        // Force id to 0 to ensure new employee creation
        theEmployeeDTO.setId(0);

        EmployeeDTO savedEmployee = employeeService.save(theEmployeeDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedEmployee);
    }

    // PUT /employees - update existing employee
    @PutMapping("/employees")
    public ResponseEntity<EmployeeDTO> updateEmployee(@RequestBody EmployeeDTO theEmployeeDTO) {
        // Validate that employee exists before updating
        if (theEmployeeDTO.getId() <= 0) {
            throw new BadRequestException("Employee ID is required for update operation");
        }

        // Check if employee exists
        employeeService.findById(theEmployeeDTO.getId());

        EmployeeDTO updatedEmployee = employeeService.save(theEmployeeDTO);
        return ResponseEntity.ok(updatedEmployee);
    }

    // PATCH /employees/{employeeId} - partial update employee
    @PatchMapping("/employees/{employeeId}")
    public ResponseEntity<EmployeeDTO> patchEmployee(@PathVariable int employeeId,
                                                   @RequestBody Map<String, Object> patchPayload) {

        // Validate patch payload
        patchValidationUtil.validatePatchFields(patchPayload);
        patchValidationUtil.validateFieldValues(patchPayload);

        // Get existing employee
        EmployeeDTO existingEmployee = employeeService.findById(employeeId);

        // Apply patch using ObjectMapper
        EmployeeDTO patchedEmployee = applyPatch(patchPayload, existingEmployee);

        // Save and return updated employee
        EmployeeDTO updatedEmployee = employeeService.save(patchedEmployee);
        return ResponseEntity.ok(updatedEmployee);
    }

    // DELETE /employees/{employeeId} - delete employee
    @DeleteMapping("/employees/{employeeId}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable int employeeId) {
        employeeService.deleteById(employeeId);
        return ResponseEntity.noContent().build();
    }

    private EmployeeDTO applyPatch(Map<String, Object> patchPayload, EmployeeDTO existingEmployee) {
        try {
            // Convert employee DTO to ObjectNode
            ObjectNode employeeNode = objectMapper.convertValue(existingEmployee, ObjectNode.class);

            // Convert patch payload to ObjectNode
            ObjectNode patchNode = objectMapper.convertValue(patchPayload, ObjectNode.class);

            // Merge patch into employee node
            employeeNode.setAll(patchNode);

            // Convert back to EmployeeDTO
            return objectMapper.convertValue(employeeNode, EmployeeDTO.class);
        } catch (Exception e) {
            throw new BadRequestException("Invalid patch payload: " + e.getMessage());
        }
    }
}
