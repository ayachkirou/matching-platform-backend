package net.java.guides.matching.repositories;

import net.java.guides.matching.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<Users,Long> {
}
