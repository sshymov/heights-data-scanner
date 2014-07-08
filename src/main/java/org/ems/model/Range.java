package org.ems.model;

/**
 * Created by stas on 7/4/14.
 */
public class Range<T> {
    private T from, to;

    public Range(T from, T to) {
        this.from = from;
        this.to = to;
    }

    public T getFrom() {
        return from;
    }

    public T getTo() {
        return to;
    }
}
