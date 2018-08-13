package cc.koosha.modbus.xinternal;

import cc.koosha.modbus.util.ErrorHandler;
import cc.koosha.modbus.util.Callback;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.concurrent.NotThreadSafe;
import javax.annotation.concurrent.ThreadSafe;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;


/**
 * Runs a loop as long as possible, stops when the loop is stopped by calling
 * pause() method or similar, or if an error occurs (that is, an exception
 * occurs).
 * <p>
 * Just as the name suggests, it loops over a task (or callback) as long as
 * {@link #isRunning()} returns true {@link #pause()} is not called.
 * <p>
 * <b>IMPORTANT</b>: If the callback blocks, so will the looper. If it returns
 * immediately, another loop will be ran again without any delay <i>unless</i>
 * {@link #delayMillis} is set to greater than zero in which case, there will be
 * {@link #delayMillis} milliseconds delay before executing next loop. If {@link
 * #delayMillis} is set to lower than zero, it is simply skipped.
 *
 * <b>IMPORTANT</b>: {@link #delayMillis} is simply used as an argument of
 * {@link Thread#sleep(long)}, so the delay mechanism is not necessarily cpu
 * friendly or power consumption friendly. As it is expected that {@link #loop}
 * will block, and {@link #delayMillis} is set to zero or less, in which case
 * this delay is simply skipped and {@link Thread#sleep(long)} is <b>NOT</b>
 * called.
 *
 * @deprecated make internal
 */
@Slf4j
@RequiredArgsConstructor
@ThreadSafe
@NotThreadSafe
@Deprecated
public final class Looper implements Task {

    @NonNull
    private final ExecutorService executorService;

    /**
     * Callback that is called when an error occurs. If it returns {@code false}
     * when called, {@link Looper} will stop looping, and {@link #stop()} is
     * called.
     */
    @NonNull
    private final ErrorHandler errorHandler;

    /**
     * Called when Looper is stopped. This callback might be called many times,
     * just as {@link #stop()} may be called many times.
     */
    @NonNull
    private final Callback onStopped;

    /**
     * The callback being called in a loop. It's better to be a blocking
     * callback. When {@link Callback#invoke()} is finished and returns, it is
     * called again after {@link #delayMillis} milliseconds delay unless an
     * error occurs.
     */
    @NonNull
    private final Callback loop;

    /**
     * When shutting down the executor, how long we should wait for the Executor
     * to suspend?
     */
    private final long awaitTerminationMillis;

    /**
     * The amount of delay between each loop iteration. See the documentation of
     * {@link Looper} itself.
     */
    private final long delayMillis;

    private final Runnable loopWrapper = new Runnable() {
        @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted())
                if (isRunning())
                    loop.invoke();
                else if (delayMillis > 0) {
                    try {
                        Thread.sleep(delayMillis);
                    }
                    catch (InterruptedException e) {
                        pause();
                        break;
                    }
                }
        }
    };

    private volatile boolean ran = false;
    private volatile boolean isRunning = false;

    private void error(Throwable error) {
        if (!errorHandler.handle(error))
            stop();
    }

    @Override
    @Synchronized
    public boolean isRunning() {
        return isRunning;
    }

    /**
     * It is an error calling this method if the Looper is already running.
     */
    @Override
    @Synchronized
    public void resume() {
        if (isRunning())
            throw new IllegalStateException("already running");
        this.isRunning = true;
    }

    /**
     * It is an error calling this method if the Looper is not running.
     */
    @Override
    @Synchronized
    public void pause() {
        if (!isRunning())
            throw new IllegalStateException("not running");
        this.isRunning = false;
    }

    /**
     * Starts the looper.
     * <p>
     * It is an error calling this method on an already running Looper or on a
     * looper that has been previously stopped, and it will raise an {@link
     * IllegalStateException}.
     */
    @Override
    @Synchronized
    public void start() {
        if (ran)
            throw new IllegalStateException("can not start again");
        ran = true;

        log.trace("starting");
        resume();
        try {
            while (isRunning()) {
                loop.invoke();
            }
        }
        catch (Exception e) {
            error(e);
            log.error("stopping on error", e);
        }
        finally {
            stop();
        }
    }

    /**
     * Stop the Looper.
     */
    @Override
    @Synchronized
    public void stop() {
        log.info("stopping");
        ran = true;
        isRunning = false;

        try {
            executorService.awaitTermination(awaitTerminationMillis, TimeUnit.MILLISECONDS);
        }
        catch (InterruptedException e) {
            log.warn("executor termination interrupted, shutdown immediately", e);
            executorService.shutdownNow();
            log.trace("calling stop callback");
            this.onStopped.invoke();
            Thread.currentThread().interrupt();
            return;
        }

        log.trace("calling stop callback");
        this.onStopped.invoke();
    }


    /**
     * A default {@link ErrorHandler}, it just logs the error (to login api) and
     * returns false to {@link Looper} so it will stop (as a result, {@link
     * Looper#stop()} will be called.
     */
    public static ErrorHandler onErrorStopLogingHandler() {
        return LoginErrorHandler;
    }

    private static final ErrorHandler LoginErrorHandler = new ErrorHandler() {
        @Override
        public boolean handle(Throwable error) {
            log.error("looper error", error);
            return false;
        }
    };

}
