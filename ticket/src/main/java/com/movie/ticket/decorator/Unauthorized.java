package com.movie.ticket.decorator;

import com.movie.ticket.model.User;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Unauthorized {
    User data;
    Response status;

}
