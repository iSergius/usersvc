package name.isergius.tasks.codemark.usersvc.ui;

import name.isergius.tasks.codemark.usersvc.domain.UserInteractor;
import name.isergius.tasks.codemark.usersvc.model.User;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

/**
 * Sergey Kondratyev
 */
@RestController
public class UserController {

    public static final String PATH_ADD = "/add";
    public static final String PATH_GET = "/get/{id}";
    public static final String PATH_DELETE = "/delete/{id}";
    public static final String PATH_LIST = "/list";
    public static final String PATH_EDIT = "/edit/{id}";

    private UserInteractor userInteractor;

    public UserController(UserInteractor userInteractor) {
        this.userInteractor = userInteractor;
    }

    @PostMapping(path = PATH_ADD)
    public ResponseEntity add(@RequestBody User user) {
        userInteractor.add(user);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @GetMapping(path = PATH_GET)
    public ResponseEntity<User> get(@PathVariable("id") long id) {
        User user = userInteractor.get(id);
        if (Objects.nonNull(user)) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(path = PATH_LIST)
    public ResponseEntity<List<User>> list() {
        List<User> users = userInteractor.list();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PutMapping(path = PATH_EDIT)
    public ResponseEntity edit(@PathVariable("id") long id, @RequestBody User user) {
        try {
            userInteractor.edit(user);
            return new ResponseEntity(HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(path = PATH_DELETE)
    public ResponseEntity delete(@PathVariable("id") long id) {
        try {
            userInteractor.delete(id);
            return new ResponseEntity(HttpStatus.OK);
        } catch (EmptyResultDataAccessException e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }
}
