package com.saveurmarche.saveurmarche.data.matcher.impl

import com.saveurmarche.saveurmarche.data.database.entity.Market
import com.saveurmarche.saveurmarche.data.matcher.Matcher

class MarketMatcher {

    companion object {
        fun withDes(data: String, ignoreCase: Boolean = true) = DescMatcher(data, ignoreCase)
        fun withName(data: String, ignoreCase: Boolean = true) = NameMatcher(data, ignoreCase)
        fun withProductDescription(data: String, ignoreCase: Boolean) = ProductDescriptionMatcher(data, ignoreCase)
        fun withCity(data: String, ignoreCase: Boolean = true) = CityMatcher(data, ignoreCase)
        fun withCountry(data: String, ignoreCase: Boolean = true) = CountryMatcher(data, ignoreCase)

        fun all(data: String, ignoreCase: Boolean = true) = Matcher.AnyOfMatcher(listOf(
                withDes(data, ignoreCase),
                withName(data, ignoreCase),
                withProductDescription(data, ignoreCase),
                withCity(data, ignoreCase),
                withCountry(data, ignoreCase)))
    }

    class DescMatcher(val data: String, val ignoreCase: Boolean = true) : Matcher<Market> {
        override fun invoke(actual: Market): Boolean = actual.description.contains(data, ignoreCase)
    }

    class NameMatcher(val data: String, val ignoreCase: Boolean = true) : Matcher<Market> {
        override fun invoke(actual: Market): Boolean = actual.name.contains(data, ignoreCase)
    }

    class ProductDescriptionMatcher(val data: String, val ignoreCase: Boolean = true) : Matcher<Market> {
        override fun invoke(actual: Market): Boolean = actual.productDescription.contains(data, ignoreCase)
    }

    class CityMatcher(val data: String, val ignoreCase: Boolean = true) : Matcher<Market> {
        override fun invoke(actual: Market): Boolean = actual.address!!.city.contains(data, ignoreCase)
    }

    class CountryMatcher(val data: String, val ignoreCase: Boolean = true) : Matcher<Market> {
        override fun invoke(actual: Market): Boolean = actual.address!!.country.contains(data, ignoreCase)
    }
}
