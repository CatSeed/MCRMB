package com.mcrmb.util;

/**
 * 由于该项目运行在Java 7环境下，因此需要使用较为古老的语言特性和API
 * <p>
 * Optional是一个泛型类，用于表示可能为null的值。
 *
 * @param <T> 泛型类型参数，表示值的类型。
 */
public final class Optional<T> {
    private final T value;

    /**
     * 构造方法，创建一个Optional实例
     *
     * @param value 可能为null的值
     */
    public Optional(T value) {
        this.value = value;
    }

    /**
     * 创建一个包含非null值的Optional实例
     *
     * @param value 非null的值
     * @return 包含指定值的Optional实例
     * @throws NullPointerException 如果指定值为null则抛出空指针异常
     */
    public static <T> Optional<T> of(T value) {
        if (value == null) {
            throw new NullPointerException("value is null");
        }
        return new Optional<>(value);
    }

    /**
     * 创建一个空的Optional实例
     *
     * @param <T> 泛型类型参数
     * @return 空的Optional实例
     */
    public static <T> Optional<T> empty() {
        return new Optional<>(null);
    }

    /**
     * 判断Optional实例是否包含非null值
     *
     * @return 如果Optional实例包含非null值则返回true，否则返回false
     */
    public boolean isPresent() {
        return value != null;
    }

    /**
     * 获取Optional实例中的值
     *
     * @return Optional实例中的值
     * @throws IllegalStateException 如果Optional实例中的值为null则抛出非法状态异常
     */
    public T get() {
        if (value == null) {
            throw new IllegalStateException("No value present");
        }
        return value;
    }

    /**
     * 获取Optional实例中的值，如果该值为null，则返回指定的默认值
     *
     * @param other 指定的默认值
     * @return Optional实例中的值，如果该值为null，则返回指定的默认值
     */
    public T orElse(T other) {
        return value != null ? value : other;
    }
}