package io.github.malikzh.dtox

final class DtoxDelegate {
    private Map<String, Field> fields = [:]

    def methodMissing(String name, def args) {
        assert !(args.size() < 1 || (args as List)[0] instanceof Map && args.size() < 2): "You need pass possible data for '$name' field"

        fields[name] = new Field().tap { field ->
            field.attributes = Dtox.defaultAttributes() + extractAttributes(args as List)
            field.variants = extractVariants(args as List)
        }
    }

    def getFields() {
        return fields;
    }

    private static List extractVariants(List<Object> args) {
        def variants = (args[0] instanceof Map ? args[1..-1] : args);

        if (variants[0] instanceof Closure) {
            def delegate = new DtoxDelegate()
            def closure = variants[0] as Closure

            closure.setResolveStrategy(Closure.DELEGATE_ONLY)
            closure.setDelegate(delegate)
            closure()

            return [delegate]
        }

        return variants
    }

    private static Map<String, Object> extractAttributes(List<Object> args) {
        return args[0] instanceof Map ? (args[0] as Map<String, Object>) : [:]
    }
}
