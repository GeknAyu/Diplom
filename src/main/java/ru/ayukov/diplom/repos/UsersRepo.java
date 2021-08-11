package ru.ayukov.diplom.repos;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.ayukov.diplom.domain.Users;

import java.util.Optional;


public interface UsersRepo extends JpaRepository<Users, Long> {
    Optional<Users> findByUserName(String userName);
    Optional<Users> findById(Long id);
}
