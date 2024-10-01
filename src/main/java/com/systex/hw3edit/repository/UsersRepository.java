package com.systex.hw3edit.repository;

import com.systex.hw3edit.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<Users, Integer> {

    Users findByEmailAndPassword(String email, String password);

    Users findByEmail(String email);
}
