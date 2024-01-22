package com.example.demo.repositories;

import com.example.demo.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepository {

    private final JdbcTemplate jdbc;

    @Value("${sql.findAll}")
    private String sqlFindAll;

    @Value("${sql.save}")
    private String sqlSave;

    @Value("${sql.deleteById}")
    private String sqlDeleteById;

    @Value("${sql.updateUser}")
    private String sqlUpdateUser;

    @Value("${sql.getUserById}")
    private String sqlGetUserById;

    public UserRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<User> findAll() {
        RowMapper<User> userRowMapper = (r, i) -> {
            User rowObject = new User();
            rowObject.setId(r.getInt("id"));
            rowObject.setFirstName(r.getString("firstName"));
            rowObject.setLastName(r.getString("lastName"));
            return rowObject;
        };

        return jdbc.query(sqlFindAll, userRowMapper);
    }

    public User save(User user) {
        jdbc.update(sqlSave, user.getFirstName(), user.getLastName());
        return user;
    }

    public void deleteById(int id) {
        jdbc.update(sqlDeleteById, id);
    }

    public User updateUser(User user) {
        String checkSql = "SELECT COUNT(*) FROM userTable WHERE id = ?";
        int count = jdbc.queryForObject(checkSql, Integer.class, user.getId());

        if (count == 0) {
            throw new IllegalArgumentException("User with id " + user.getId() + " does not exist");
        }

        jdbc.update(sqlUpdateUser, user.getFirstName(), user.getLastName(), user.getId());

        return user;
    }

    public User getUserById(int id) {
        return jdbc.queryForObject(sqlGetUserById, new Object[]{id}, new UserRowMapper());
    }
}
