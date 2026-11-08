package upm.app.data.models.user;

import upm.app.data.models.Entity;
import upm.app.data.models.exceptions.InvalidAttributeException;

public class User extends Entity {
    private String email;
    private String name;
    private String firstSurname;
    private String secondSurname;
    private String dni;
    private Role role;

    private User(Builder builder) {
        super(builder.id);
        setEmail(builder.email);
        setName(builder.name);
        setFirstSurname(builder.firstSurname);
        setSecondSurname(builder.secondSurname);
        setDni(builder.dni);
        setRole(builder.role);
    }

    public static Builder builder(String email, String name, String firstSurname, String dni, Role role) {
        return new Builder(email, name, firstSurname, dni, role);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email == null || !isValidEmail(email)) {
            throw new InvalidAttributeException("El email es inválido." + email);
        }
        this.email = email;
    }

    private boolean isValidEmail(String email) {
        return email.contains("@") && email.contains(".");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidAttributeException("El nombre es inválido." + name);
        }
        this.name = name;
    }

    public String getFirstSurname() {
        return firstSurname;
    }

    public void setFirstSurname(String firstSurname) {
        if (firstSurname == null || firstSurname.trim().isEmpty()) {
            throw new InvalidAttributeException("El primer apellido es inválido." + firstSurname);
        }
        this.firstSurname = firstSurname;
    }

    public String getSecondSurname() {
        return secondSurname;
    }

    public void setSecondSurname(String secondSurname) {
        if (secondSurname != null && secondSurname.trim().isEmpty()) {
            throw new InvalidAttributeException("El segundo apellido es inválido." + firstSurname);
        }
        this.secondSurname = secondSurname;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        if (dni == null || dni.trim().isEmpty()) {
            throw new InvalidAttributeException("El DNI es inválido." + dni);
        }
        this.dni = dni;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        if (role == null) {
            throw new InvalidAttributeException("El rol es inválido: null");
        }
        this.role = role;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + getId() + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", firstSurname='" + firstSurname + '\'' +
                (secondSurname != null ? ", secondSurname='" + secondSurname + '\'' : "") +
                ", dni='" + dni + '\'' +
                ", role=" + role +
                '}';
    }

    public static class Builder {
        private final String email;
        private final String name;
        private final String firstSurname;
        private final String dni;
        private final Role role;
        private Integer id;
        private String secondSurname;

        public Builder(String email, String name, String firstSurname, String dni, Role role) {
            this.email = email;
            this.name = name;
            this.firstSurname = firstSurname;
            this.dni = dni;
            this.role = role;
        }

        public Builder id(Integer id) {
            this.id = id;
            return this;
        }

        public Builder secondSurname(String secondSurname) {
            this.secondSurname = secondSurname;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }
}
