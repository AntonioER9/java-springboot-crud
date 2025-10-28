package com.springboot.cruddemo.service;

import com.springboot.cruddemo.dto.EmployeeDTO;

import java.util.List;

public interface EmployeeService {

    List<EmployeeDTO> findAll();

    EmployeeDTO findById(int theId);

    EmployeeDTO save(EmployeeDTO theEmployeeDTO);

    void deleteById(int theId);

}
