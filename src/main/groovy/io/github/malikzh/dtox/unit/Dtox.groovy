package io.github.malikzh.dtox.unit

final class Dtox {
    static void generate(Class clazz, Closure definition) {
        Objects.requireNonNull(clazz, 'Class must be not null!')
    }
}
