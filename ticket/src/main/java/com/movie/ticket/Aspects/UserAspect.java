package com.movie.ticket.Aspects;

import com.movie.ticket.config.NullAwareBeanUtilsBean;
import com.movie.ticket.model.Category;
import com.movie.ticket.model.CreationUpdation;
import com.movie.ticket.model.User;
import com.movie.ticket.repository.CategoryRepository;
import com.movie.ticket.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.ObjectUtils;

import java.util.Date;


@Slf4j
@Aspect
@Configuration
public class UserAspect {

    @Autowired
    UserRepository userRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    NullAwareBeanUtilsBean nullAwareBeanUtilsBean;

    @Around("execution(* com.movie.ticket.repository.*.save(..))")
    public Object beforeSaveOnRepository(ProceedingJoinPoint joinPoint) throws Throwable {

//        log.info("Before save method calls ");
        Object[] args = joinPoint.getArgs();
        CreationUpdation creationUpdation = new CreationUpdation();


       if (args.length > 0) {
            if (args[0] instanceof User) {
                User user = userRepository.findByEmailContainingAndSoftDeleteIsFalse(((User) args[0]).getEmail());
                if (SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")) {
                    if (!ObjectUtils.isEmpty(user)) {
                        nullAwareBeanUtilsBean.copyProperties(creationUpdation, ((User) args[0]).getCreationUpdation());
                        creationUpdation.setUpdatedBy(((User) args[0]).getId());
                        creationUpdation.setUpdatedAt(new Date());
                        ((User) args[0]).setCreationUpdation(creationUpdation);
                        return joinPoint.proceed(args);
                    } else {
                        Object obj = joinPoint.proceed();
                        User newUser = userRepository.findByEmailContainingAndSoftDeleteIsFalse(((User) args[0]).getEmail());
                        creationUpdation.setCreatedBy(newUser.getId());
                        creationUpdation.setCreatedAt(new Date());
                        ((User) args[0]).setCreationUpdation(creationUpdation);
                        return joinPoint.proceed(args);
                    }
                }else{
                    String username = ((UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
                    User newUser = userRepository.findByEmailContainingAndSoftDeleteIsFalse(username);
                    if (!ObjectUtils.isEmpty(user)) {
                        nullAwareBeanUtilsBean.copyProperties(creationUpdation, ((User) args[0]).getCreationUpdation());
                        creationUpdation.setUpdatedBy(newUser.getId());
                        creationUpdation.setUpdatedAt(new Date());
                        ((User) args[0]).setCreationUpdation(creationUpdation);
                        return joinPoint.proceed(args);
                    } else {
                        Object obj = joinPoint.proceed();
                        creationUpdation.setCreatedBy(newUser.getId());
                        creationUpdation.setCreatedAt(new Date());
                        ((User) args[0]).setCreationUpdation(creationUpdation);
                        return joinPoint.proceed(args);
                    }
                }

            } else if (args[0] instanceof Category) {
                String username = ((UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
                Category category = categoryRepository.findByNameContainingAndSoftDeleteIsFalse(((Category) args[0]).getName());
                User user = userRepository.findByEmailContainingAndSoftDeleteIsFalse(username);
                if (!ObjectUtils.isEmpty(category)){
                    nullAwareBeanUtilsBean.copyProperties(creationUpdation, ((Category) args[0]).getCreationUpdation());
                    creationUpdation.setUpdatedBy(user.getId());
                    creationUpdation.setUpdatedAt(new Date());
                    ((Category) args[0]).setCreationUpdation(creationUpdation);
                    return joinPoint.proceed(args);
                }else{
                    Object obj = joinPoint.proceed();
                    creationUpdation.setCreatedBy(user.getId());
                    creationUpdation.setCreatedAt(new Date());
                    ((Category) args[0]).setCreationUpdation(creationUpdation);
                    return joinPoint.proceed(args);
                }

            }
       }

       return args[0];

    }
}
