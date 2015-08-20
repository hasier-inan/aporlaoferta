package com.aporlaoferta.dao;

import com.aporlaoferta.model.EmailTemplate;
import org.springframework.data.repository.CrudRepository;

/**
 * Created with IntelliJ IDEA.
 * User: hasiermetal
 * Date: 11/08/15
 * Time: 16:14
 */
public interface EmailTemplateDAO extends CrudRepository<EmailTemplate, String> {
    EmailTemplate findByName(String name);
}
