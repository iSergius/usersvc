package name.isergius.tasks.codemark.usersvc.ui;

import name.isergius.tasks.codemark.usersvc.domain.UserInteractor;
import name.isergius.tasks.codemark.usersvc.model.User;
import name.isergius.tasks.codemark.usersvc.ui.dto.ResponseMessage;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;

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
    public ResponseEntity<ResponseMessage> add(@RequestBody User user) {
        try {
            long id = userInteractor.add(user);
            URI location = ServletUriComponentsBuilder.fromPath(PATH_GET)
                    .buildAndExpand(id)
                    .toUri();
            return ResponseEntity.created(location)
                    .body(new ResponseMessage());
        } catch (ConstraintViolationException e) {
            Set<String> errors = e.getConstraintViolations().stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.toSet());
            return new ResponseEntity<>(new ResponseMessage(errors), HttpStatus.BAD_REQUEST);
        }
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
    public ResponseEntity<ResponseMessage> edit(@PathVariable("id") long id,
                                                @RequestBody User user) {
        if (user.getId() != id) {
            return ResponseEntity.badRequest()
                    .body(new ResponseMessage(asList("Path id and user id is not equals")));
        }
        try {
            userInteractor.edit(user);
            return ResponseEntity.ok(new ResponseMessage());
        } catch (ConstraintViolationException e) {
            return ResponseEntity.badRequest()
                    .body(new ResponseMessage(e.getConstraintViolations()
                            .stream()
                            .map(ConstraintViolation::getMessage)
                            .collect(Collectors.toSet())));
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
