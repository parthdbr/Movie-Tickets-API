package com.movie.ticket.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreationUpdation {
    Date createdAt;
    String createdBy;
    Date updatedAt;
    String updatedBy;
}
