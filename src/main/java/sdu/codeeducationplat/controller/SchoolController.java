package sdu.codeeducationplat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sdu.codeeducationplat.model.School;
import sdu.codeeducationplat.service.SchoolService;

import java.util.List;

@RestController
@RequestMapping("/api/schools")
public class SchoolController {

    @Autowired
    private SchoolService schoolService;

    @GetMapping
    public ResponseEntity<List<School>> getAllSchools() {
        List<School> schools = schoolService.getAllSchools();
        return ResponseEntity.ok(schools);
    }
}