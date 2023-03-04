package dev.johnoreilly.confetti

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.cache.normalized.*
import com.apollographql.apollo3.cache.normalized.api.MemoryCacheFactory
import com.apollographql.apollo3.cache.normalized.sql.SqlNormalizedCacheFactory
import dev.johnoreilly.confetti.fragment.SessionDetails
import kotlinx.coroutines.flow.*
import kotlinx.datetime.LocalDate



class ConfettiRepository {
    val conferenceName = "kotlinconf2023"

    // Create Apollo client with normalized cache
    private val apolloClient = ApolloClient.Builder()
        .serverUrl("http://10.0.2.2:8080/graphql?conference=$conferenceName")
        .normalizedCache(MemoryCacheFactory(10_000_000).chain(SqlNormalizedCacheFactory(getDatabaseName(conferenceName))))
        .build()


    // Gets list of sessions from backend and then observes the cache for any changes
    val sessions = apolloClient.query(GetSessionsQuery()).watch().map {
        it.dataAssertNoErrors.sessions.map { it.sessionDetails }
    }


    // Group sessions by date
    val sessionsByDateMap: Flow<Map<LocalDate, List<SessionDetails>>> = sessions.map {
        it.groupBy { it.start.date }
    }

}





















