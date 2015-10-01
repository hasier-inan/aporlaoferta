package com.aporlaoferta.dao;

import com.aporlaoferta.model.TheUser;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by hasiermetal on 26/07/14.
 */
public interface UserDAO extends CrudRepository<TheUser, String> {
    TheUser findByUserNickname(String userNickname);
}
