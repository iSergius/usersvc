package name.isergius.tasks.codemark.usersvc.ui;

import name.isergius.tasks.codemark.usersvc.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;

/**
 * Sergey Kondratyev
 */
@RestController
public class UserController {

    @PostMapping("/add")
    public ResponseEntity add(@RequestBody User user) {
        return new ResponseEntity(HttpStatus.OK);
    }
}
