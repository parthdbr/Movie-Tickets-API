package com.movie.ticket.repository;

import com.movie.ticket.model.AdminConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
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
    public String findTemplateBySubject(String subject, String key) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {

        List<AdminConfig> adminConfig = mongoTemplate.findAll(AdminConfig.class);
        AdminConfig config = adminConfig.get(0);

        for (Field field : config.getEmailConfiguration().getClass().getDeclaredFields()) {
            field.setAccessible(true);
            if (key.equals(field.getName())) {
                Method getter = config.getEmailConfiguration().getClass().getMethod("get" + StringUtils.capitalize(field.getName()));
                Object value = getter.invoke(config.getEmailConfiguration());
                if (subject.equalsIgnoreCase((String) Class.forName(value.getClass().getName()).getMethod("getSubject").invoke(value))) {
                    return (String) Class.forName(value.getClass().getName()).getMethod("getTemplate").invoke(value);
                }
            }
        }

        return  null;
    }

}
