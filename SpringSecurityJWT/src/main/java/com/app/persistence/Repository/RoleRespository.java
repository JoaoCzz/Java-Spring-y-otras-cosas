package com.app.persistence.Repository;

import com.app.persistence.Entity.RoleEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RoleRespository extends CrudRepository<RoleEntity,Long> {
    List<RoleEntity> findRoleEntItiesByRoleEnumIn(List <String> roleNames);
}
