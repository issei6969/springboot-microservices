package com.abhi.school_serice.controller;

import com.abhi.school_serice.entity.School;
import com.abhi.school_serice.service.SchoolService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin("*")
@RequestMapping("/school")
@RestController
public class SchoolController {

    private final SchoolService schoolService;

    @Autowired
    public SchoolController(SchoolService schoolService) {
        this.schoolService = schoolService;
    }
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public School addSchool(@RequestBody String school){
        School data = new Gson().fromJson(school,School.class);
        return schoolService.addSchool(data);
    }
    @GetMapping
    public String fetchSchools(){
        return new Gson().toJson(schoolService.fetchSchools());
    }
    @GetMapping("/{id}")
    public String fetchSchoolById(@PathVariable int id){
        return new Gson().toJson(schoolService.fetchSchoolById(id));
    }

}
