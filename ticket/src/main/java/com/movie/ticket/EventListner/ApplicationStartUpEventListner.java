package com.movie.ticket.EventListner;

import com.movie.ticket.Util.Utils;
import com.movie.ticket.controller.adminController;
import com.movie.ticket.controller.userController;
import com.movie.ticket.model.AdminConfig;
import com.movie.ticket.model.RestAPIs;
import com.movie.ticket.repository.AdminConfigRepository;
import com.movie.ticket.repository.RestAPIRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class ApplicationStartUpEventListner {
    @Autowired
    RestAPIRepository restAPIRepository;


    @EventListener()
    @Async
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {

        saveIfNotExits(Utils.getAllMethodNames(adminController.class));
        saveIfNotExits(Utils.getAllMethodNames(userController.class));
    }

    private void saveIfNotExits(List<RestAPIs> apis){
        for (RestAPIs restAPIs : apis) {
            if (!restAPIRepository.existsByName(restAPIs.getName())){
                restAPIRepository.insert(restAPIs);
            }
        }
    }
}
