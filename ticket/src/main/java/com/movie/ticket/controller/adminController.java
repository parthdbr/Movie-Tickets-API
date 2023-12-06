package com.movie.ticket.controller;

import com.movie.ticket.DTO.CategoryDTO;
import com.movie.ticket.DTO.UserDTO;
import com.movie.ticket.decorator.*;
import com.movie.ticket.exception.CategoryExistsException;
import com.movie.ticket.exception.UserExistsException;
import com.movie.ticket.model.Category;
import com.movie.ticket.model.User;
import com.movie.ticket.service.AdminService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/admin")
@SecurityRequirement(name = "BearerAuth")
public class adminController {

    @Autowired
    AdminService adminService;


    @PostMapping("/add_user")
    public DataResponse<User> addUserData(@RequestBody UserDTO userDTO) throws UserExistsException {
        DataResponse<User> response = new DataResponse<>();

        try {
                response.setData(adminService.addUser(userDTO));
                response.setStatus(new Response(HttpStatus.CREATED, "Data Created", "201"));

        }catch(Exception e) {

            response.setStatus(new Response(HttpStatus.NO_CONTENT, "Data Not Created", "204"));
        }
        return response;
    }

    @GetMapping("/get_users")
    public ListDataResponse<User> getAllUser() {
        ListDataResponse<User> response = new ListDataResponse<>();

        try {
            response.setData(adminService.getAllUser());
            response.setStatus(new Response(HttpStatus.OK, "Data Available", "201"));

        }catch(Exception e) {

            response.setStatus(new Response(HttpStatus.NO_CONTENT, "Data Not Available", "404"));
        }
        return response;
    }

    @PostMapping("/add_category")
    public DataResponse<Category> addCategory(@RequestBody CategoryDTO categoryDTO) {
        DataResponse<Category> response = new DataResponse<>();

        try {
            response.setData(adminService.addCategory(categoryDTO));
            response.setStatus(new Response(HttpStatus.CREATED, "Data Created", "201"));

        }catch(Exception | CategoryExistsException e) {

            response.setStatus(new Response(HttpStatus.NO_CONTENT, "Category exists with this name", "204"));
        }
        return response;
    }

    @GetMapping("/get_categories")
    public ListDataResponse<Category> getAllCategory() throws CategoryExistsException {
        ListDataResponse<Category> response = new ListDataResponse<>();

        try {
            response.setData(adminService.getAllCategory());
            response.setStatus(new Response(HttpStatus.OK, "Data Available", "200"));

        }catch(Exception e) {

            response.setStatus(new Response(HttpStatus.NO_CONTENT, "Data Not Available", "204"));
        }
        return response;
    }

    @PutMapping("/update_category")
    public DataResponse<Category> updateCategory(@RequestParam String name , @RequestBody CategoryDTO categoryDTO) throws CategoryExistsException {
        DataResponse<Category> response = new DataResponse<>();

        try {
            response.setData(adminService.updateCategory(name, categoryDTO));
            response.setStatus(new Response(HttpStatus.ACCEPTED, "Data Updated", "200"));

        }catch(Exception e) {

            response.setStatus(new Response(HttpStatus.NOT_FOUND, "Data Not Found with this Name", "404"));
        }
        return response;
    }

    @DeleteMapping("/delete_category")
    public DataResponse<Category> deleteCategory(@RequestParam String name) throws CategoryExistsException {
        DataResponse<Category> response = new DataResponse<>();

        try{
            if (adminService.getCategoryByName(name) != null) {
                adminService.deleteCategory(name);
                response.setStatus(new Response(HttpStatus.OK, "Data deleted", "200"));
            } else {
                response.setStatus(new Response(HttpStatus.NOT_FOUND, "Data not found", "404"));
            }
        } catch (Exception e) {
            response.setStatus(new Response(HttpStatus.NOT_FOUND, "Data not found", "404"));
        }
        return response;
    }

    @GetMapping("/get_category_bookedSeats")
    public DataResponse<categoryBookedSeats> categoryBookedSeats(@RequestParam String category) {
        DataResponse<categoryBookedSeats> response = new DataResponse<>();

        try{
            response.setData(adminService.categoryBookedSeats(category));
            response.setStatus(new Response(HttpStatus.OK, "Category Found", "200"));
        }catch (Exception e) {
            response.setStatus(new Response(HttpStatus.NOT_FOUND, "Category Not Found", "404"));

        }

        return response;
    }

}
