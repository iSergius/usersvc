package name.isergius.tasks.codemark.usersvc.model;

/**
 * Sergey Kondratyev
 */
public class User {

    private long id;
    private String name;
    private String login;
    private String password;

    public User() {}

    public User(long id, String name, String login, String password) {
        this.id = id;
        this.name = name;
        this.login = login;
        this.password = password;
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
}
