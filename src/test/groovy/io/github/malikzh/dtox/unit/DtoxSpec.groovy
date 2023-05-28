package io.github.malikzh.dtox.unit

import io.github.malikzh.dtox.Dtox
import io.github.malikzh.dtox.dto.SimpleDto
import spock.lang.Specification

class DtoxSpec extends Specification {
    def 'test for simple object'() {
        when:
        def result = Dtox.generate(SimpleDto) {
            field1 'a', 'b'
            field2 'c', 'd'
        }
        def d = 1
        then:
        noExceptionThrown()
    }
}
