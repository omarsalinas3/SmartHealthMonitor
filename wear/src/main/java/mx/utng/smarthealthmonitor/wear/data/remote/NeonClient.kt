package mx.utng.smarthealthmonitor.wear.data.remote
import mx.utng.smarthealthmonitor.wear.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NeonClient {
    // Pooler endpoint — el que funciona con HTTP SQL API
    private const val BASE_URL = "https://ep-jolly-bonus-ajoj7vt8-pooler.c-3.us-east-2.aws.neon.tech/"

    val CONN_STRING = "postgresql://neondb_owner:npg_XARsI7iJcUC8@ep-jolly-bonus-ajoj7vt8-pooler.c-3.us-east-2.aws.neon.tech/neondb?sslmode=require"

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
