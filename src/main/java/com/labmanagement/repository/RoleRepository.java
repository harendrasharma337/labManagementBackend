package com.labmanagement.repository;

import org.springframework.data.repository.CrudRepository;

import com.labmanagement.domain.Role;

public interface RoleRepository extends CrudRepository<Role, Long> {
}
