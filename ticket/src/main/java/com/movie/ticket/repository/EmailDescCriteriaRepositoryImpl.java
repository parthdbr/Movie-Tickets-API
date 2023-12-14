package com.movie.ticket.repository;

import com.movie.ticket.model.AdminConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;



@Component
@Slf4j
public class EmailDescCriteriaRepositoryImpl implements EmailDescCriteriaRepository {

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public String findTemplateBySubject(String subject, String key) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {

        List<AdminConfig> adminConfig = mongoTemplate.findAll(AdminConfig.class);

        for (AdminConfig config : adminConfig) {
            for (Field field : config.getEmailConfiguration().getClass().getDeclaredFields()) {
                field.setAccessible(true);
                if (key.equals(field.getName()))
                    for (Method method : config.getEmailConfiguration().getClass().getMethods())
                        if (method.getName().equals("get" + StringUtils.capitalize( field.getName()))) {
                            Object res = Class.forName(
                                            config.getEmailConfiguration().getClass().getName())
                                            .getMethod("get" + StringUtils.capitalize(field.getName()))
                                            .invoke(config.getEmailConfiguration());
                            if(subject.equalsIgnoreCase((String) Class.forName(res.getClass().getName()).getMethod("getSubject").invoke(res)))
                                return (String) Class.forName(res.getClass().getName()).getMethod("getTemplate").invoke(res);
                        }

            }
        }

        return  null;
    }

}
