package vn.thanhtuanle.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "User Controller")
public class UserController {

    @Operation(summary = "Test API", description = "Users API")
    @GetMapping
    public String greeting(@RequestParam String name) {
        return "Hello, " + name;
    }
}
