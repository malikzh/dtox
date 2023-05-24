package io.github.malikzh.dtox.unit

import org.codehaus.groovy.classgen.asm.DelegatingController

class DtoxDelegate {
    private def context = null
    private def fields = [:]

    DtoxDelegate(Object context) {
        this.context = context
    }

    void methodMissing(String name, Object args) {
        this.processClosure(name, args as List)
    }

    private void processClosure(String fieldName, List args) {
        assert !args.isEmpty(): 'Field possible values not specified'
        assert !(args[0] instanceof Map && args.size() == 1): 'Field values not passed'

        // Attributes
        def attributes = defaultFieldAttributes() + extractAttributesFromArgs(args)

        // Extract possible values
        def possibleValues = extractPossibleValues(args)
        println(fieldName + ': ' + possibleValues)
    }

    private static Map extractAttributesFromArgs(List args) {
        return (args[0] instanceof Map) ? args[0] : [:]
    }

    private static Object extractPossibleValues(List args) {
        def begin = 0
        def end = -1

        if ((args[0] instanceof Map)) {
            begin = 1;
        }

        if (args[begin] instanceof Closure) {
            return args[begin]
        }

        return args[begin..end]
    }

    /**
     * Default attributes for each field
     * @return
     */
    private static Map defaultFieldAttributes() {
        return [
                nullable: false,
                excludeIf: null
        ]
    }
}
