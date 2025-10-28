package com.springboot.cruddemo.service.impl;

import com.springboot.cruddemo.dto.EmployeeDTO;
import com.springboot.cruddemo.entity.Employee;
import com.springboot.cruddemo.exception.ResourceNotFoundException;
import com.springboot.cruddemo.repository.EmployeeRepository;
import com.springboot.cruddemo.service.EmployeeService;
import com.springboot.cruddemo.util.EmployeeMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

    public EmployeeServiceImpl(EmployeeRepository theEmployeeRepository, EmployeeMapper theEmployeeMapper) {
        this.employeeRepository = theEmployeeRepository;
        this.employeeMapper = theEmployeeMapper;
    }

    @Override
    public List<EmployeeDTO> findAll() {
        List<Employee> employees = employeeRepository.findAll();
        return employeeMapper.toDTOList(employees);
    }

    @Override
    public EmployeeDTO findById(int theId) {
        Optional<Employee> result = employeeRepository.findById(theId);

        if (result.isEmpty()) {
            throw new ResourceNotFoundException("Employee not found with id: " + theId);
        }

        return employeeMapper.toDTO(result.get());
    }

    @Override
    public EmployeeDTO save(EmployeeDTO theEmployeeDTO) {
        Employee employee = employeeMapper.toEntity(theEmployeeDTO);
        Employee savedEmployee = employeeRepository.save(employee);
        return employeeMapper.toDTO(savedEmployee);
    }

    @Override
    public void deleteById(int theId) {
        Optional<Employee> result = employeeRepository.findById(theId);

        if (result.isEmpty()) {
            throw new ResourceNotFoundException("Employee not found with id: " + theId);
        }

        employeeRepository.deleteById(theId);
    }
}






