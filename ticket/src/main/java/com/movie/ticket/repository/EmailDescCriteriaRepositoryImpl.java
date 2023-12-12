package com.movie.ticket.repository;

import com.movie.ticket.model.AdminConfig;
import com.movie.ticket.model.Templates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class EmailDescCriteriaRepositoryImpl implements EmailDescCriteriaRepository {

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public String findTemplateBySubject(String subject) {

        Query query = new Query();
        List<AdminConfig> adminConfig = mongoTemplate.findAll(AdminConfig.class);

        int index=0;

        for (AdminConfig adminConfig1 : adminConfig) {
            for (Templates templates: adminConfig1.getTemplates()) {
                if (subject.equals(templates.getSubject())){
                  index = templates.getSubject().indexOf(subject);
                  break;
                }
            }
        }

        return adminConfig.get(0).getTemplates().get(index).getTemplate();

//        query.addCriteria(Criteria.where("templates.subject").is("New User Registered"));
//        query.addCriteria(Criteria.where("templates").elemMatch(Criteria.where("subject").is(subject)));
//        query.fields().include("templates.$");

//
    }
}
