package com.madiest.moapin.content.repository;

import com.madiest.moapin.auth.model.User;
import com.madiest.moapin.content.model.Content;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContentRepository extends JpaRepository<Content, Long> {

  Optional<Content> findByIdAndUser(Long id, User user);

  List<Content> findAllByCategory_IdAndUser(Long categoryId, User user);
}
