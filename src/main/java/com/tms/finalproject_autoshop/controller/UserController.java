package com.tms.finalproject_autoshop.controller;

import com.tms.finalproject_autoshop.model.User;
import com.tms.finalproject_autoshop.model.dto.UserCreateDto;
import com.tms.finalproject_autoshop.model.dto.UserUpdateDto;
import com.tms.finalproject_autoshop.security.CustomUserDetailService;
import com.tms.finalproject_autoshop.security.CustomUserDetails;
import com.tms.finalproject_autoshop.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@SecurityRequirement(name = "Bearer Authentication")
@RestController
@RequestMapping("/user")
@Tag(name = "User", description = "Operations for managing users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(
            summary = "Create a new user",
            description = "Creates a new user (ADMIN only)",
            responses = {
                    @ApiResponse(responseCode = "201", description = "User created"),
                    @ApiResponse(responseCode = "409", description = "User already exists")
            }
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<User> createUser(
            @Parameter(description = "User DTO") @RequestBody UserCreateDto user) {

        Boolean result = userService.createUser(user);
        if (!result) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(
            summary = "Get user by ID",
            description = "Returns a user by ID (ADMIN only)",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User found",
                            content = @Content(schema = @Schema(implementation = User.class))),
                    @ApiResponse(responseCode = "404", description = "User not found")
            }
    )
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(
            @Parameter(description = "User ID") @PathVariable("id") Long id) {

        Optional<User> user = userService.getUserById(id);
        return user.map(value -> ResponseEntity.ok().body(value))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Update user",
            description = "Updates an existing user (ADMIN only)",
            responses = {
                    @ApiResponse(responseCode = "200", description = "User updated"),
                    @ApiResponse(responseCode = "404", description = "User not found")
            }
    )
    @PreAuthorize("hasRole('USER')")
    @PutMapping()
    public ResponseEntity<User> updateUser(
            @Parameter(description = "Updated user object") @RequestBody UserUpdateDto userUpdateDto) {
        Long userId = CustomUserDetailService.getUserId();
        Boolean result = userService.updateUser(userUpdateDto, userId);
        if (!result) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(
            summary = "Get all users",
            description = "Returns a list of all users (ADMIN only)",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List returned"),
                    @ApiResponse(responseCode = "404", description = "No users found")
            }
    )
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> user = userService.getAllUsers();
        if (user.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    @Operation(
            summary = "Delete user",
            description = "Deletes a user by ID (ADMIN only)",
            responses = {
                    @ApiResponse(responseCode = "204", description = "User deleted"),
                    @ApiResponse(responseCode = "404", description = "User not found")
            }
    )
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteUser(
            @Parameter(description = "User ID") @PathVariable("id") Long id) {

        Boolean result = userService.deleteUser(id);
        if (!result) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
    @Operation(
            summary = "Get sorted users",
            description = "Returns a list of users sorted by specified field and order (ADMIN only)",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List returned"),
                    @ApiResponse(responseCode = "204", description = "No users found")
            }
    )
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/sort/{field}")
    public ResponseEntity<List<User>> getSortedUsersByField(@PathVariable("field") String field, @RequestParam("order") String order) {
        List<User> users = userService.getSortedUsersByField(field, order);
        if (users.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(users);
    }
    @Operation(
            summary = "Get paginated users",
            description = "Returns a paginated list of users (ADMIN only)",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Page returned"),
                    @ApiResponse(responseCode = "204", description = "No users found")
            }
    )
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/pagination/{page}/{size}")
    public ResponseEntity<Page<User>> getAllUsersWithPagination(@PathVariable("page") int page, @PathVariable("size") int size) {
        Page<User> users = userService.getAllUsersWithPagination(page, size);
        if (users.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(users);
    }
}
