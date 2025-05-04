package com.example.gymapp.api
import com.example.gymapp.data.model.*
import retrofit2.http.*
import java.time.LocalDate

// Definir como la app va a comunicarse con Spring Boot backend
interface ApiService {
    @GET("gym_user/{id_user}")
    suspend fun getUserProfile(@Path("id_user") id: Int): User

    @PUT("gym_user/{id_user}")
    suspend fun updateUserProfile(@Path("id_user") id: Int, @Body user: User): User

    @GET("session/{session_day}")
    suspend fun getSessionsForDay(@Path("session_day") day: String): List<SessionDetails>

    @GET("session_instance/{session_instance_date}")
    suspend fun getSessionDate(@Path("session_instance_date") date: LocalDate): SessionInstance
/*

    @GET("api/sessions")
    suspend fun getSessionsByDate(@Query("date") date: Date): List<SessionDetails>

    // Endpoints para ver las reservas
    @POST("api/registrations")
    suspend fun registerForSession(@Body registration: UserSessionRegistration): UserSessionRegistration

    @DELETE("api/registrations/{registrationId}")
    suspend fun cancelRegistration(@Path("registrationId") registrationId: Int): Boolean

    @GET("api/users/{userId}/registrations")
    suspend fun getUserRegistrations(@Path("userId") userId: Int): List<RegistrationDetails>

    @GET("api/users/{userId}/registrations")
    suspend fun getUserRegistrationsByType(
        @Path("userId") userId: Int,
        @Query("type") type: String
    ): List<RegistrationDetails>

     */
}