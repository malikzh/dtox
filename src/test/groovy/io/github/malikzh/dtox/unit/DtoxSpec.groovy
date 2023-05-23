package io.github.malikzh.dtox.unit

import io.github.malikzh.dtox.dto.SimpleDto
import spock.lang.Specification

class DtoxSpec extends Specification {
    def 'test for simple object'() {
        when:
        def result = Dtox.generate(SimpleDto) {
            field1 'a'
            field2 'b'
        }
        def d = 1
        then:
        noExceptionThrown()
    }
}
