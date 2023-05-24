package io.github.malikzh.dtox.unit

class Dtox {
    static List generate(Class clazz, Closure definition) {
        Objects.requireNonNull(clazz, 'Class must be not null!')

        def delegate = new DtoxDelegate(clazz.getDeclaredConstructor().newInstance())
        definition.setDelegate(delegate)
        definition.setResolveStrategy(Closure.DELEGATE_ONLY)
        definition.call()
        //print(delegate.fields)
    }
}
