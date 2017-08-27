package name.isergius.tasks.codemark.usersvc.ui.dto;

import java.util.Collection;

/**
 * Sergey Kondratyev
 */
public class ResponseMessage {

    private boolean success;
    private Collection<String> errors;

    public ResponseMessage() {
        this.success = true;
    }

    public ResponseMessage(Collection<String> errors) {
        this.errors = errors;
    }

    public boolean isSuccess() {
        return success;
    }

    public Collection<String> getErrors() {
        return errors;
    }

}
