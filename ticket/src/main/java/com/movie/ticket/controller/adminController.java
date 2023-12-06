package com.movie.ticket.controller;

import com.movie.ticket.DTO.CategoryDTO;
import com.movie.ticket.DTO.UserDTO;
import com.movie.ticket.decorator.*;
import com.movie.ticket.exception.CategoryExistsException;
import com.movie.ticket.exception.SeatNotAvailable;
import com.movie.ticket.exception.UserExistsException;
import com.movie.ticket.exception.UserNotExistsException;
import com.movie.ticket.model.Category;
import com.movie.ticket.model.User;
import com.movie.ticket.repository.AdminCriteriaRepository;
import com.movie.ticket.service.AdminService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/admin")
@SecurityRequirement(name = "BearerAuth")
public class adminController {

    @Autowired
    AdminService adminService;

    @Autowired
    AdminCriteriaRepository adminCriteriaRepository;


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
    public PageResponse<User> getAllUser(@RequestParam(defaultValue = "0") int page,@RequestParam(defaultValue = "3") int size ) {
        PageResponse<User> response = new PageResponse<>();

        Page<User> users = adminService.getAllUser(page, size);

        try {
            if(!users.isEmpty()) {
                response.setUsers(users.getContent());
                response.setPage_number((long) page);
                response.setSize_of_page((long) size);
                response.setTotal_page((long) users.getTotalPages());
                response.setTotal_count(users.getTotalElements());
                response.setStatus(new Response(HttpStatus.OK, "Data Available", "200"));
            }else
                response.setStatus(new Response(HttpStatus.NOT_FOUND, "No Data Available", "404"));

        }catch(Exception e) {

            response.setStatus(new Response(HttpStatus.NO_CONTENT, "Data Not Available", "404"));
        }
        return response;
    }

    @PostMapping("/add_category")
    public DataResponse<Category> addCategory(@RequestBody CategoryDTO categoryDTO) {
        DataResponse<Category> response = new DataResponse<>();
        String seatsAvailable = adminCriteriaRepository.checkSeatsAvailable(categoryDTO);

        try {
            if (seatsAvailable == null) {
            response.setData(adminService.addCategory(categoryDTO));
            response.setStatus(new Response(HttpStatus.CREATED, "Data Created", "201"));
            }else{
                response.setStatus(new Response(HttpStatus.NO_CONTENT, "Seats are occupied by "+seatsAvailable+" category", "204"));
            }

        }catch(Exception | CategoryExistsException e) {

            response.setStatus(new Response(HttpStatus.NO_CONTENT, "Category exists with this name", "204"));
        } catch (SeatNotAvailable e) {

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
    public DataResponse<Category> updateCategory(@RequestParam String id , @RequestBody CategoryDTO categoryDTO) throws CategoryExistsException {
        DataResponse<Category> response = new DataResponse<>();
        String seatsAvailable = adminCriteriaRepository.checkSeatsAvailabletoUpdate(categoryDTO, id);


        try {
            if (seatsAvailable == null) {
                response.setData(adminService.updateCategory(id, categoryDTO));
                response.setStatus(new Response(HttpStatus.ACCEPTED, "Data Updated", "200"));
            }else{
                response.setStatus(new Response(HttpStatus.NO_CONTENT, "Seats are occupied by "+seatsAvailable+" category", "204"));
            }
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

    @GetMapping("/get_user_by_seats_booked")
    public DataResponse<User> getUserBySeatsBooked(@RequestParam int seatNumber) {
        DataResponse<User> response = new DataResponse<>();

        try{
            response.setData(adminService.getUserBySeatsBooked(seatNumber));
            response.setStatus(new Response(HttpStatus.OK, "Booked Seat Found", "200"));
        }catch (Exception | UserNotExistsException e) {
            response.setStatus(new Response(HttpStatus.NOT_FOUND, "Seat is available for booking", "404"));

        }

        return response;
    }

    @GetMapping(value = "/find_By_Email", produces = "application/json")
    public DataResponse<User> getUserByEmail(String email) {

        User users = adminService.getUserByEmail(email);
        DataResponse<User> response = new DataResponse<>();
        try {
            if (users != null) {
                if (!ObjectUtils.isEmpty(users)) {
                    response.setData(users);
                    response.setStatus(new Response(HttpStatus.OK, "Data Fetched", "200"));
                } else {
                    response.setStatus(new Response(HttpStatus.NOT_FOUND, "No Data Available", "404"));
                }
            } else
                response.setStatus(new Response(HttpStatus.NOT_FOUND, "No Data Available", "404"));

        } catch (Exception e) {
            response.setStatus(new Response(HttpStatus.NOT_FOUND, "No Data Available", "404"));
        }
        return response;

    }

}
