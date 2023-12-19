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


       if (args.length > 0) {
            if (args[0] instanceof User) {
                User user = userRepository.findByEmailContainingAndSoftDeleteIsFalse(((User) args[0]).getEmail());

                if (SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")) {
                    if (!ObjectUtils.isEmpty(user)) {
                        ((User) args[0]).setUpdatedBy(((User) args[0]).getId());
                        ((User) args[0]).setUpdatedAt(new Date());
                    } else {
                        Object obj = joinPoint.proceed();
                        User user1 = userRepository.findByEmailContainingAndSoftDeleteIsFalse(((User) args[0]).getEmail());
                        user1.setCreatedBy(user1.getId());
                        user1.setCreatedAt(new Date());
                        nullAwareBeanUtilsBean.copyProperties(args[0], user1);
                    }
                }else{
                    String id = ((UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
                    User newUser = userRepository.findByIdAndSoftDeleteIsFalse(id);
                    if (!ObjectUtils.isEmpty(newUser)) {
                        ((User) args[0]).setUpdatedBy(newUser.getId());
                        ((User) args[0]).setUpdatedAt(new Date());

                    } else {
                        Object obj = joinPoint.proceed();
                        User user1 = userRepository.findByEmailContainingAndSoftDeleteIsFalse(((User) args[0]).getEmail());
                        user1.setCreatedBy(user1.getId());
                        user1.setCreatedAt(new Date());
                        nullAwareBeanUtilsBean.copyProperties(args[0], user1);
                    }
                }
                return joinPoint.proceed(args);

            } else if (args[0] instanceof Category) {
                String id = ((UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
                Category category = categoryRepository.findByNameContainingAndSoftDeleteIsFalse(((Category) args[0]).getName());
                User user = userRepository.findByIdAndSoftDeleteIsFalse(id);
                if (!ObjectUtils.isEmpty(category)){
                    ((Category)args[0]).setUpdatedBy(user.getId());
                    ((Category)args[0]).setUpdatedAt(new Date());
                }else{
                    Object obj = joinPoint.proceed();
                    Category category1 = categoryRepository.findByNameContainingAndSoftDeleteIsFalse(((Category) args[0]).getName());
                    category1.setCreatedBy(user.getId());
                    category1.setCreatedAt(new Date());
                    nullAwareBeanUtilsBean.copyProperties(args[0], category1);
                }
                return joinPoint.proceed(args);

            }
       }

       return args[0];

    }
}
