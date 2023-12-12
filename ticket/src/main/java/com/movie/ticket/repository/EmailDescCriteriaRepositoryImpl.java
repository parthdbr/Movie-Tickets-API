package com.movie.ticket.repository;

import com.movie.ticket.model.AdminConfig;
import com.movie.ticket.model.Templates;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@Slf4j
public class EmailDescCriteriaRepositoryImpl implements EmailDescCriteriaRepository {

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public String findTemplateBySubject(String subject) {

        Query query = new Query();
        List<AdminConfig> adminConfig = mongoTemplate.findAll(AdminConfig.class);

        int index=0;

        for (AdminConfig config : adminConfig) {
            List<Templates> temp = config.getTemplates();
            for (Templates templates : temp)
                if (subject.equalsIgnoreCase(templates.getSubject()))
                    return templates.getTemplate();
        }

        return null;
    }
}
