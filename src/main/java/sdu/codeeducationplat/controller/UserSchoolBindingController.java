package sdu.codeeducationplat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sdu.codeeducationplat.dto.UserSchoolBindingDTO;
import sdu.codeeducationplat.model.UserSchoolBinding;
import sdu.codeeducationplat.service.UserSchoolBindingService;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/user-school-bindings")
public class UserSchoolBindingController {

    @Autowired
    private UserSchoolBindingService userSchoolBindingService;

    @PostMapping("/bind")
    public ResponseEntity<String> bindSchool(@Valid @RequestBody UserSchoolBindingDTO dto) {
        userSchoolBindingService.bindSchool(dto);
        return ResponseEntity.ok("绑定成功");
    }

    @GetMapping("/user/{uid}")
    public ResponseEntity<List<UserSchoolBinding>> getUserBindings(@PathVariable String uid) {
        List<UserSchoolBinding> bindings = userSchoolBindingService.getUserBindings(uid);
        return ResponseEntity.ok(bindings);
    }
}