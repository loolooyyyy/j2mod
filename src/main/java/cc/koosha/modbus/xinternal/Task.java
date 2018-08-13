package cc.koosha.modbus.xinternal;


// TODO rename
public interface Task {

    void start();

    /**
     * A task is stopped when its runner is stopped. This method is called to
     * let task cleanup any resources it uses.
     */
    void stop();

    // -----------------

    /**
     * Temporarily pause task.
     */
    void pause();

    /**
     * Resume a task previously paused.
     */
    void resume();

    /**
     * Check and see if task is paused.
     *
     * @return true if task is running false otherwise.
     */
    boolean isRunning();

}
