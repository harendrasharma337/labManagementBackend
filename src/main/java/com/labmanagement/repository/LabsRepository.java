package com.labmanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.labmanagement.domain.Labs;
import com.labmanagement.domain.Modules;

public interface LabsRepository extends JpaRepository<Labs, Long> {

	List<Labs> findByModules(Modules module);

}
