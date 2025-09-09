package com.friendfinapp.dating;
data class Country(
    val name: String,
    var states: List<State> = listOf() // List of states for this country
)

data class State(
    val name: String,
    var cities: List<String> = listOf() // List of cities for this state
)

class CountryStateCityRepository(private val restCountriesApi: RestCountriesApi, private val geonamesApi: GeonamesApi) {

    // Get all countries
    suspend fun getCountries(): List<Country> {
        val countries = mutableListOf<Country>()
        val countryResponse = restCountriesApi.getAllCountries()

        for (countryData in countryResponse) {
            val country = Country(countryData.name.common)
            val states = getStatesForCountry(countryData.alpha2Code)
            country.states = states

            countries.add(country)
        }

        return countries
    }

    // Get states for a given country
    private suspend fun getStatesForCountry(countryCode: String): List<State> {
        val states = mutableListOf<State>()
        val stateResponse = geonamesApi.getStatesForCountry(countryCode)

        for (stateData in stateResponse) {
            val state = State(stateData.adminName1)
            val cities = getCitiesForState(stateData.adminName1)
            state.cities = cities

            states.add(state)
        }

        return states
    }

    // Get cities for a given state
    private suspend fun getCitiesForState(stateCode: String): List<String> {
        val cities = mutableListOf<String>()
        val cityResponse = geonamesApi.getCitiesForState(stateCode)

        for (cityData in cityResponse) {
            cities.add(cityData.name)
        }

        return cities
    }
}
