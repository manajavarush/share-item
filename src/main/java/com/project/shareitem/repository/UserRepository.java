package com.project.shareitem.repository;

import com.project.shareitem.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Optional;

@Repository
@Slf4j
public class UserRepository {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<User> userRowMapper = (rs, rowNum) -> new User(
            rs.getLong("id"),
            rs.getString("name"),
            rs.getString("email")
    );

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public User save(User user) {
        String sql = "INSERT INTO users (name, email) VALUES(?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getEmail());

            return preparedStatement;
        }, keyHolder);

        Long userId = keyHolder.getKey().longValue();
        user.setId(userId);

        return user;
    }

    public Optional<User> findById(Long userId) {
        String sql = "SELECT id, name, email from users WHERE id = ?";

        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, userRowMapper, userId));
        } catch (EmptyResultDataAccessException exception) {
            return Optional.empty();
        }
    }

    public void update(User user) {
        String sql = "UPDATE users SET name = ?, email = ? WHERE id = ?";
        jdbcTemplate.update(sql, user.getName(), user.getEmail(), user.getId());
    }

    public boolean delete(Long userId) {
        String sql = "DELETE FROM users WHERE id = ?";
        int affectedRows = jdbcTemplate.update(sql, userId);

        return affectedRows > 0;
    }

    public boolean existsByEmail(String userEmail) {
        String sql = "SELECT EXISTS(SELECT 1 FROM users WHERE email = ?)";
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class, userEmail));
    }
}
