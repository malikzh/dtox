package io.github.malikzh.dtox.unit

import io.github.malikzh.dtox.Dtox
import io.github.malikzh.dtox.dto.SimpleDto
import spock.lang.Specification

class DtoxSpec extends Specification {
    def 'test for simple object'() {
        when:
        def result = Dtox.generate(SimpleDto, nullable: true) {
            field1 'a', 'b', nullable: true
            field2 'c', 'd'
            field3 {
                field4 'e', 'f'
                field5 'g', 'h'
            }
        }

        then:
        noExceptionThrown()

        and: 'check size'
        result.size() == 16
    }
}
