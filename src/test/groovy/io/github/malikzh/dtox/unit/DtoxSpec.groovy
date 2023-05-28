package io.github.malikzh.dtox.unit

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
}
