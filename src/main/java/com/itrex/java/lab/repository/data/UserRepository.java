package com.itrex.java.lab.repository.data;

import com.itrex.java.lab.entity.Role;
import com.itrex.java.lab.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);

    Iterable<User> findByRole(Role customer);
}
