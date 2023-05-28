package io.github.malikzh.dtox

import java.util.Map.Entry

final class Dtox {
    static List<Object> dtox(Map<String, Object> attributes, Class clazz, Closure definition) {
        Objects.requireNonNull(clazz, 'Class must be not null!')

        def delegate = new DtoxDelegate()
        definition.setDelegate(delegate)
        definition.setResolveStrategy(Closure.DELEGATE_ONLY)

        definition()

        def attrs = defaultAttributes() + attributes

        def builder = attrs['builder']

        def result = []

        generateCombinations(clazz, delegate.fields.entrySet().toList()) { cl, data ->
            builder(cl, data).tap {
                if (!attrs['excludeIf'](it)) {
                    result.add(it)
                }
            }
        }

        if (attrs['nullable']) {
            result.push(null)
        }

        return result
    }

    static List<Object> dtox(Class clazz, Closure definition) {
        return dtox([:], clazz, definition)
    }

    private static void generateCombinations(Class clazz, List<Entry<String, Field>> fields, Closure builder, Map<String, Object> data = [:]) {
        if (fields.isEmpty()) {
            builder(clazz, data)
            return
        }

        def field = fields[0]

        // Closure case
        if (field.value.variants.size() == 1 && field.value.variants[0] instanceof DtoxDelegate) {
            DtoxDelegate delegate = field.value.variants[0] as DtoxDelegate

            // Get class from property
            def fieldClass = getFieldType(clazz, field.key)
            assert fieldClass != null: "Class '${clazz.name}' doesn't have field '$field.key'"

            def fieldBuilder = field.value.attributes['builder']
            def excludeIf = field.value.attributes['excludeIf']

            if (field.value.attributes['nullable']) {
                data[field.key] = null
                builder(clazz, data)
            }

            generateCombinations(fieldClass, delegate.fields.entrySet().toList(), { c, map ->
                def fieldDto = fieldBuilder(c, map)

                if (!excludeIf(fieldDto)) {
                    data[field.key] = fieldDto
                    builder(clazz, data)
                }
            })
            return
        }

        // Handle variants
        if (field.value.attributes['nullable'] && !field.value.variants.any { it == null }) {
            field.value.variants.push(null)
        }

        for (variant in field.value.variants) {
            data[field.key] = variant

            if (fields.size() > 1) {
                generateCombinations(clazz, fields[1..-1], builder, data)
            } else {
                builder(clazz, data)
            }
        }
    }

    static Closure builderFunction() {
        return { Class clazz, Map<String, Object> data ->
            clazz.getDeclaredConstructor().newInstance().tap { obj ->
                for (entry in data) {
                    obj."$entry.key" = entry.value
                }
            }
        }
    }

    static Map<String, Object> defaultAttributes() {
        return [
                nullable: false,
                excludeIf: { obj -> false },
                builder: builderFunction()
        ]
    }

    private static Class getFieldType(Class clazz, String name) {
        return [*clazz.properties['declaredFields']].find { field ->
            field.getName() == name
        }?.with {
            it.properties['type']
        } as Class
    }
}
