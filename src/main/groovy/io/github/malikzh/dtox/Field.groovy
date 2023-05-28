package io.github.malikzh.dtox

import groovy.transform.EqualsAndHashCode

@EqualsAndHashCode
final class Field {
    Map<String, Object> attributes = [:]
    List<Object> variants = []
}
