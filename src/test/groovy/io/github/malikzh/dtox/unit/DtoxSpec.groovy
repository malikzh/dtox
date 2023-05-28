package io.github.malikzh.dtox.unit

import io.github.malikzh.dtox.dto.ComplexDto
import io.github.malikzh.dtox.dto.SimpleDto
import io.github.malikzh.dtox.enums.SampleEnum
import spock.lang.Specification

import static io.github.malikzh.dtox.Dtox.dtox

class DtoxSpec extends Specification {
    def 'simple object'() {

        when: 'call main method'
        List<SimpleDto> result = dtox(SimpleDto) {
            field1 'str1', 'str2'
            field2 4, 5
            field3 SampleEnum.VALUE_1, SampleEnum.VALUE_2
        }

        then: 'check for exception'
        noExceptionThrown()

        and: 'check size'
        result.size() == 8

        and: 'check data'
        result[0].field1 == 'str1'
        result[0].field2 == 4
        result[0].field3 == SampleEnum.VALUE_1

        result[1].field1 == 'str1'
        result[1].field2 == 4
        result[1].field3 == SampleEnum.VALUE_2

        result[2].field1 == 'str1'
        result[2].field2 == 5
        result[2].field3 == SampleEnum.VALUE_1

        result[3].field1 == 'str1'
        result[3].field2 == 5
        result[3].field3 == SampleEnum.VALUE_2

        result[4].field1 == 'str2'
        result[4].field2 == 4
        result[4].field3 == SampleEnum.VALUE_1

        result[5].field1 == 'str2'
        result[5].field2 == 4
        result[5].field3 == SampleEnum.VALUE_2

        result[6].field1 == 'str2'
        result[6].field2 == 5
        result[6].field3 == SampleEnum.VALUE_1

        result[7].field1 == 'str2'
        result[7].field2 == 5
        result[7].field3 == SampleEnum.VALUE_2
    }

    def 'complex dto object'() {
        when:
        List<ComplexDto> result = dtox(ComplexDto) {
            field1 'a', 'b'
            field2 {
                field2 4, 5
            }
        }

        then:
        noExceptionThrown()

        and: 'check size'
        result.size() == 4

        and: 'check data'
        result[0].field1 == 'a'
        result[0].field2.field2 == 4

        result[1].field1 == 'a'
        result[1].field2.field2 == 5

        result[2].field1 == 'b'
        result[2].field2.field2 == 4

        result[3].field1 == 'b'
        result[3].field2.field2 == 5
    }

    def 'check with empty fields'() {
        when:
        List<ComplexDto> result = dtox(ComplexDto) {
            field1 'a', 'b'
            field2 {
                // none
            }
        }

        then:
        noExceptionThrown()

        and: 'check size'
        result.size() == 2

        and: 'check data'
        result[0].field1 == 'a'
        result[0].field2 != null

        result[1].field1 == 'b'
        result[1].field2 != null
    }

    // check nullable
    def 'nullable fields with simple dto'() {
        when:
        List<SimpleDto> result = dtox(SimpleDto, nullable: true) {
            field1 'str1', 'str2',
                    nullable: true

            field2 4,
                    nullable: true
        }

        then:
        noExceptionThrown()

        and: 'check size'
        result.size() == 7

        and: 'check data'
        result[0] == null

        result[1].field1 == null
        result[1].field2 == null

        result[2].field1 == null
        result[2].field2 == 4

        result[3].field1 == 'str1'
        result[3].field2 == null

        result[4].field1 == 'str1'
        result[4].field2 == 4

        result[5].field1 == 'str2'
        result[5].field2 == null

        result[6].field1 == 'str2'
        result[6].field2 == 4
    }

    def 'check nullable for complex dto'() {
        when:
        List<ComplexDto> result = dtox(ComplexDto) {
            field1 'a', 'b'
            field2 (nullable: true) {
                field2 4
            }
        }

        then:
        noExceptionThrown()

        and: 'check size'
        result.size() == 4

        and: 'check data'
        result[0].field1 == 'a'
        result[0].field2 == null

        result[1].field1 == 'a'
        result[1].field2 != null && result[1].field2.field2 == 4

        result[2].field1 == 'b'
        result[2].field2 == null

        result[3].field1 == 'b'
        result[3].field2 != null && result[1].field2.field2 == 4
    }

    // Check excludeIf
    def 'check excludeIf with simple dto'() {
        when:
        List<SimpleDto> result = dtox(SimpleDto, excludeIf: { SimpleDto it -> it.field2 == 6 }) {
            field1 'str1', 'str2'
            field2 4, 5, 6
        }

        then:
        noExceptionThrown()

        and:
        result.size() == 4

        and: 'check data'
        result[0].field1 == 'str1'
        result[0].field2 == 4

        result[1].field1 == 'str1'
        result[1].field2 == 5

        result[2].field1 == 'str2'
        result[2].field2 == 4

        result[3].field1 == 'str2'
        result[3].field2 == 5
    }

    def 'check excludeIf with complex dto'() {
        when:
        List<ComplexDto> result = dtox(ComplexDto) {
            field1 'a', 'b'
            field2 (excludeIf: { SimpleDto dto -> dto.field2 == 8 }) {
                field2 4, 5, 8
            }
        }

        then:
        noExceptionThrown()

        and:
        result.size() == 4

        and: 'check data'
        result[0].field1 == 'a'
        result[0].field2.field2 == 4

        result[1].field1 == 'a'
        result[1].field2.field2 == 5

        result[2].field1 == 'b'
        result[2].field2.field2 == 4

        result[3].field1 == 'b'
        result[3].field2.field2 == 5
    }
}
