package mx.utng.smarthealthmonitor.tv.data.remote
import mx.utng.smarthealthmonitor.tv.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NeonClient {
    // Endpoint directo de Neon (sin pooler) para el HTTP SQL API
    private const val BASE_URL = "https://ep-jolly-bonus-ajoj7vt8.us-east-2.aws.neon.tech/"

    val AUTH_HEADER = "Bearer ${BuildConfig.NEON_API_KEY}"

    val api: NeonApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }).build())
            .build()
            .create(NeonApiService::class.java)
    }
}
