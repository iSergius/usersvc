package name.isergius.tasks.codemark.usersvc.model;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import name.isergius.tasks.codemark.usersvc.ui.util.DeserializerRoleJsonConverter;
import name.isergius.tasks.codemark.usersvc.ui.util.SerializerRoleJsonConverter;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

/**
 * Sergey Kondratyev
 */
@Entity
@Table(name = "users")
public class User {

    @Id
    private long id;
    private String name;
    private String login;
    private String password;
    @JsonDeserialize(contentConverter = DeserializerRoleJsonConverter.class)
    @JsonSerialize(contentConverter = SerializerRoleJsonConverter.class)
    @JoinTable(name = "user_roles")
    @OneToMany(targetEntity = Role.class, fetch = FetchType.EAGER)
    private Collection<Role> roles;

    public User() {
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
