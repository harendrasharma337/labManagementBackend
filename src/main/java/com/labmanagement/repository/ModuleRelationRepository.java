package com.labmanagement.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.labmanagement.domain.ModuleRelation;
import com.labmanagement.domain.Modules;
import com.labmanagement.domain.User;

public interface ModuleRelationRepository extends JpaRepository<ModuleRelation, Long> {

	ModuleRelation findByUser(User user);

	List<ModuleRelation> findAllByModules(Modules m);

	Optional<ModuleRelation> findByUserAndModules(User user, Modules module);

}
