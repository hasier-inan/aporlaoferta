package com.aporlaoferta.dao;

import com.aporlaoferta.model.TheUserRoles;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by hasiermetal on 26/07/14.
 */
public interface UserRolesDAO extends CrudRepository<TheUserRoles, String> {

    List<TheUserRoles> findByUserNickname(String userNickname);
}
