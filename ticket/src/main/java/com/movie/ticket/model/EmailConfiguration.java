package com.movie.ticket.model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailConfiguration {
    MailConfig userRegistration;
    MailConfig categoryCreation;
    MailConfig categoryUpdation;
    MailConfig categoryDeletion;
}
