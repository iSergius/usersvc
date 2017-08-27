package name.isergius.tasks.codemark.usersvc.model;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import name.isergius.tasks.codemark.usersvc.ui.util.DeserializerRoleJsonConverter;
import name.isergius.tasks.codemark.usersvc.ui.util.SerializerRoleJsonConverter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Collection;
import java.util.Objects;

/**
 * Sergey Kondratyev
 */
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @NotNull
    @Basic
    private String name;

    @NotNull
    @Basic
    private String login;

    @NotNull
    @Pattern(regexp = "(\\S*\\p{Upper}+\\d+\\S*)|(\\S*\\d+\\p{Upper}+\\S*)", message = "Password must contain min one latter and one digit")
    @Basic
    private String password;

    @JsonDeserialize(contentConverter = DeserializerRoleJsonConverter.class)
    @JsonSerialize(contentConverter = SerializerRoleJsonConverter.class)
    @ManyToMany(targetEntity = Role.class, fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Collection<Role> roles;

    public User() {
    }

    public User(String name, String login, String password, Collection<Role> roles) {
        this(0, name, login, password, roles);
    }

    public User(long id, String name, String login, String password, Collection<Role> roles) {
        this.id = id;
        this.name = name;
        this.login = login;
        this.password = password;
        this.roles = roles;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public Collection<Role> getRoles() {
        return roles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id &&
                Objects.equals(name, user.name) &&
                Objects.equals(login, user.login) &&
                Objects.equals(password, user.password) &&
                Objects.equals(roles, user.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, login, password, roles);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", login='" + login + '\'' +
                ", password='" + "secret" + '\'' +
                ", roles=" + roles +
                '}';
    }
}
