package io.github.malikzh.dtox.unit

import io.github.malikzh.dtox.dto.SimpleDto
import spock.lang.Specification

class DtoxSpec extends Specification {
    def 'test for simple object'() {
        when:
        def result = Dtox.generate(SimpleDto) {
            field1 'a1', 'a2',
                    nullable: true, editable: false


            field2 'b', nullable: true, editable: false
//            field3 {
//                field4 'c'
//                field5 'd'
//            }
        }
        def d = 1
        then:
        noExceptionThrown()
    }
}
