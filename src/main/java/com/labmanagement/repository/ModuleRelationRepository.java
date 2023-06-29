package com.labmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.labmanagement.domain.ModuleRelation;
import com.labmanagement.domain.User;

public interface ModuleRelationRepository extends JpaRepository<ModuleRelation, Long> {

	ModuleRelation findByUser(User user);

}
