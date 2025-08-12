package com.madiest.moapin.category.repository;

import com.madiest.moapin.auth.model.User;
import com.madiest.moapin.category.model.Category;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByIdAndUser(Long id, User user);

    List<Category> findAllByUser(User user);

    List<Category> findAllByUser(User user, Sort sort);
}
