package com.abhi.student_service.service;

import com.abhi.student_service.dto.School;
import com.abhi.student_service.dto.StudentResponse;
import com.abhi.student_service.model.Student;
import com.abhi.student_service.repository.StudentRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final RestTemplate restTemplate;

    @Autowired
    public StudentService(StudentRepository studentRepository, RestTemplate restTemplate) {
        this.studentRepository = studentRepository;
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<?> createStudent(Student student){
        try {
            return new ResponseEntity<Student>(studentRepository.save(student), HttpStatus.CREATED);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    public ResponseEntity<?> fetchStudentById(String id) throws JsonProcessingException {
        Optional<Student> studentOptional =  studentRepository.findById(id);

        Student student = studentOptional.orElseThrow(() -> new RuntimeException("Student Not Found"));

        // Fetch School details
        String schoolResponse = restTemplate.getForObject("http://SCHOOL-SERVICE/school/" + student.getSchoolId(), String.class);
        School school = new Gson().fromJson(schoolResponse,School.class);
        // Create Response
        StudentResponse studentResponse = new StudentResponse(
                student.getId(),
                student.getName(),
                String.valueOf(student.getAge()),
                student.getGender(),
                school

        );

        return new ResponseEntity<>(new Gson().toJson(studentResponse), HttpStatus.OK);
    }

    public ResponseEntity<?> fetchStudents(){
        List<Student> students = studentRepository.findAll();
        if(!students.isEmpty()){
            return new ResponseEntity<List<Student>>(students, HttpStatus.OK);
        }else {
            return new ResponseEntity<>("No Students",HttpStatus.NOT_FOUND);
        }
    }
}
