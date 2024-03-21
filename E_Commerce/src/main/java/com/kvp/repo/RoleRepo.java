package com.kvp.repo;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kvp.entities.Role;
import com.kvp.entities.User;


@Repository
public interface RoleRepo extends JpaRepository<Role, Long> {
	
	Role findByRoleName(String roleName);

}
