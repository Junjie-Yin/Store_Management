package upm.app.data.repositories.exception;

public class EntityNotFoundException extends RuntimeException {
    private static final String DESCRIPTION = "Entity Not Found Exception. The entity id not found.";

    public EntityNotFoundException(String detail) {
        super(DESCRIPTION + ". " + detail);
    }

}
