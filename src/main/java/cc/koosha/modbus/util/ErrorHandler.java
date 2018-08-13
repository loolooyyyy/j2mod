package cc.koosha.modbus.util;


/**
 * Handle the error (implementation specific, for instance just log it)
 */
public interface ErrorHandler {

    /**
     * Handle the error (implementation specific, for instance just log it)
     *
     * @param error thr error to handle.
     * @return true if the operation must continue, false otherwise.
     */
    boolean handle(Throwable error);

}
