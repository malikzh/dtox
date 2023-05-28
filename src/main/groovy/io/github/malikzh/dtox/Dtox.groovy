package io.github.malikzh.dtox

import java.util.Map.Entry

final class Dtox {
    static List<Object> generate(Class clazz, Closure definition) {
        Objects.requireNonNull(clazz, 'Class must be not null!')

        def delegate = new DtoxDelegate()
        definition.setDelegate(delegate)
        definition.setResolveStrategy(Closure.DELEGATE_ONLY)
        definition()

        return generateCombinations(delegate.fields.entrySet().toList(), builderFunction())
    }

    private static List<Object> generateCombinations(List<Entry<String, Field>> fields, Closure builder, data = [:]) {
        List<Object> combinations = []

        if (fields.isEmpty()) {
            return combinations
        }

        def field = fields[0]

        // Handle variants
        for (variant in field.value.variants) {
            data[field.key] = variant

            if (fields.size() > 1) {
                combinations += generateCombinations(fields[1..-1], builder, data)
            } else {
                combinations.push(builder(null, data))
            }
        }

        return combinations
    }

    static Closure builderFunction() {
        return { Class clazz, Map<String, Object> data ->
            println(data)
            new Object()
//            clazz.getDeclaredConstructor().newInstance().tap { obj ->
//                for (entry in data) {
//                    obj[entry.key] = entry.value
//                }
//            }
        }
    }
}
