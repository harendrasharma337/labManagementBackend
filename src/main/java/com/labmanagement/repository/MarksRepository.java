package com.labmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.labmanagement.domain.Marks;
import com.labmanagement.domain.User;

public interface MarksRepository extends JpaRepository<Marks, Long> {

	Marks findByUser(User user);

}
