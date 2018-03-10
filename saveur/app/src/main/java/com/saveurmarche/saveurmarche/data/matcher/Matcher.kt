package com.saveurmarche.saveurmarche.data.matcher

interface Matcher<in T> : (T) -> Boolean {
    /**
     * Reports whether the [actual] value meets the criteria and, if not, why it does not match.
     */
    override fun invoke(actual: T): Boolean

    class AnyOfMatcher<in T>(private val matchers: Iterable<Matcher<T>>) : Matcher<T> {
        override fun invoke(actual: T) = matchers.any({ it(actual) })
    }
}