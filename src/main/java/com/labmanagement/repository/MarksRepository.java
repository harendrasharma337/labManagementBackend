package com.labmanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.labmanagement.domain.Labs;
import com.labmanagement.domain.Marks;
import com.labmanagement.domain.Modules;
import com.labmanagement.domain.User;

public interface MarksRepository extends JpaRepository<Marks, Long> {

	List<Marks> findByUser(User user);

	List<Marks> findByModules(Modules modules);

	List<Marks> findByLabs(Labs lab);

}
