package com.tms.finalproject_autoshop.repository;

import com.tms.finalproject_autoshop.model.Security;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface SecurityRepository extends JpaRepository<Security, Long> {
    @Query(nativeQuery = true, value = "SELECT * FROM security WHERE role = :roleParam")
    List<Security> customFindByRole(String roleParam);

    Security getByUsername(String username);

    boolean existsByUsername(String username);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE security SET role = 'ADMIN' WHERE user_id = :userId")
    int setAdminRoleByUserId(Long userId);

    Optional<Object> findByUsername(String username);
}
