package com.labmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.labmanagement.domain.Modules;

public interface ModuleRepository extends JpaRepository<Modules, Long> {

}
