package ru.digitalhabits.homework3.builder;

public interface Builder<C,F,S,V> {

    V buildFromRequest(C c);

    F buildResponseFull(V v);

    S buildResponseShort(V v);

}
