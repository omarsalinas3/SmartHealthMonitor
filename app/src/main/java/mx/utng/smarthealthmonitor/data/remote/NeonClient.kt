package mx.utng.smarthealthmonitor.data.remote
import mx.utng.smarthealthmonitor.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
 
object NeonClient {
    private const val BASE_URL = "https://${BuildConfig.NEON_HOST}/"
 
    // Header 1: Authorization con la Neon API Key
    val AUTH_HEADER  = "Bearer ${BuildConfig.NEON_API_KEY}"
    // Header 2: Connection String de la base de datos
    val CONN_STRING  = BuildConfig.NEON_CONN_STRING
 
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
