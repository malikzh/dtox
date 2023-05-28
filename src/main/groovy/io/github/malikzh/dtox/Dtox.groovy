package io.github.malikzh.dtox

import java.util.Map.Entry

final class Dtox {
    static List<Object> generate(Class clazz, Closure definition) {
        Objects.requireNonNull(clazz, 'Class must be not null!')

        def delegate = new DtoxDelegate()
        definition.setDelegate(delegate)
        definition.setResolveStrategy(Closure.DELEGATE_ONLY)
        definition()

        generateCombinations(delegate.fields.entrySet().toList(), builderFunction())

        return []
    }

    private static void generateCombinations(List<Entry<String, Field>> fields, Closure builder, data = [:]) {
        if (fields.isEmpty()) {
            return
        }

        def field = fields[0]

        // Closure case
        if (field.value.variants.size() == 1 && field.value.variants[0] instanceof DtoxDelegate) {
            DtoxDelegate delegate = field.value.variants[0] as DtoxDelegate
            generateCombinations(delegate.fields.entrySet().toList(), { clazz, map ->
                data[field.key] = builder(null, data)
                println('subvar: ' + map)
            })
            return
        }

        // Handle variants
        for (variant in field.value.variants) {
            data[field.key] = variant

            if (fields.size() > 1) {
                generateCombinations(fields[1..-1], builder, data)
            } else {
                builder(null, data)
            }
        }
    }

    static def ii = 1

    static Closure builderFunction() {
        return { Class clazz, Map<String, Object> data ->
            println('var: ' + (ii++) + ' ' + data)
            new Object()
//            clazz.getDeclaredConstructor().newInstance().tap { obj ->
//                for (entry in data) {
//                    obj[entry.key] = entry.value
//                }
//            }
        }
    }
}
