package cc.koosha.modbus.util;


/**
 * Same as java.util.function.Function.
 */
public interface Function<T, R> {

    R apply(T t);

}
