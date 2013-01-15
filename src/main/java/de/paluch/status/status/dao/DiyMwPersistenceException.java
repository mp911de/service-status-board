package de.paluch.status.status.dao;

/**
 * @author <a href="mailto:mpaluch@paluch.biz">Mark Paluch</a>
 */
public class DiyMwPersistenceException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 3002677656994408885L;

    /**
     * 
     */
    public DiyMwPersistenceException() {
        super();

    }

    /**
     * @param message
     * @param cause
     */
    public DiyMwPersistenceException(String message, Throwable cause) {
        super(message, cause);

    }

    /**
     * @param message
     */
    public DiyMwPersistenceException(String message) {
        super(message);

    }

    /**
     * @param cause
     */
    public DiyMwPersistenceException(Throwable cause) {
        super(cause);

    }

}
