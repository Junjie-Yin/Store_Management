package upm.app.data.repositories.sql;

import upm.app.data.models.user.Role;
import upm.app.data.models.user.User;
import upm.app.data.repositories.UserRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepositorySql implements UserRepository {
    private final Connection connection;

    public UserRepositorySql() {
        this.connection = MySqlTables.getConnection();
    }

    @Override
    public User create(User user) {
        String sql = "INSERT INTO user (email, name, first_surname, second_surname, dni, role) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getName());
            stmt.setString(3, user.getFirstSurname());
            stmt.setString(4, user.getSecondSurname());
            stmt.setString(5, user.getDni());
            stmt.setString(6, user.getRole().toString());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                user.setId(rs.getInt(1));
            }
            return user;
        } catch (SQLException e) {
            throw new RuntimeException("Error creating user", e);
        }
    }

    @Override
    public Optional<User> read(Integer id) {
        try (PreparedStatement stmt = connection.prepareStatement("SELECT * FROM user WHERE id = ?")) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return Optional.of(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error reading user by id", e);
        }
        return Optional.empty();
    }

    private User mapRow(ResultSet rs) throws SQLException {
        return User.builder(
                        rs.getString("email"),
                        rs.getString("name"),
                        rs.getString("first_surname"),
                        rs.getString("dni"),
                        Role.valueOf(rs.getString("role"))
                )
                .id(rs.getInt("id"))
                .secondSurname(rs.getString("second_surname"))
                .build();
    }

    @Override
    public User update(Integer id, User entity) {
        String sql = "UPDATE user SET email=?, name=?, first_surname=?, second_surname=?, dni=?, role=? " +
                "WHERE id=?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, entity.getEmail());
            stmt.setString(2, entity.getName());
            stmt.setString(3, entity.getFirstSurname());
            stmt.setString(4, entity.getSecondSurname());
            stmt.setString(5, entity.getDni());
            stmt.setString(6, entity.getRole().toString());
            stmt.setInt(7, id);

            int rows = stmt.executeUpdate();

            if (rows == 0) {
                throw new upm.app.data.repositories.exception.EntityNotFoundException("User with ID " + id + " not found for update.");
            }

            entity.setId(id);
            return entity;

        } catch (SQLException e) {
            throw new RuntimeException("Error updating user", e);
        }
    }

    @Override
    public void delete(Integer id) {
        try {
            try (PreparedStatement deleteCartStmt = connection.prepareStatement("DELETE FROM shopping_cart WHERE user_id = ?")) {
                deleteCartStmt.setInt(1, id);
                deleteCartStmt.executeUpdate();
            }
            try (PreparedStatement deleteUserStmt = connection.prepareStatement("DELETE FROM user WHERE id = ?")) {
                deleteUserStmt.setInt(1, id);
                deleteUserStmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting user", e);
        }
    }

    @Override
    public List<User> findAll() {
        List<User> list = new ArrayList<>();
        String sql = "SELECT * FROM user";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) list.add(mapRow(rs));

        } catch (SQLException e) {
            throw new RuntimeException("Error reading all users", e);
        }

        return list;
    }

    @Override
    public int size() {
        String sql = "SELECT COUNT(*) FROM user";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            return rs.next() ? rs.getInt(1) : 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error getting user count", e);
        }
    }

    @Override
    public void clear() {
        try (PreparedStatement stmt = connection.prepareStatement("DELETE FROM user")) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error clearing users", e);
        }
    }

    @Override
    public Optional<User> findByEmail(String email) {
        String sql = "SELECT * FROM user WHERE email = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) return Optional.of(mapRow(rs));

        } catch (SQLException e) {
            throw new RuntimeException("Error searching user by email", e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findByDni(String dni) {
        String sql = "SELECT * FROM user WHERE dni = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, dni);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) return Optional.of(mapRow(rs));

        } catch (SQLException e) {
            throw new RuntimeException("Error searching user by DNI", e);
        }
        return Optional.empty();
    }

    @Override
    public List<User> findByRole(Role role) {
        String sql = "SELECT * FROM user WHERE role = ?";
        List<User> users = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, role.toString());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) users.add(mapRow(rs));

        } catch (SQLException e) {
            throw new RuntimeException("Error searching users by role", e);
        }
        return users;
    }
}