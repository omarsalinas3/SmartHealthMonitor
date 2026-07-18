package mx.utng.smarthealthmonitor.tv.data.remote
import mx.utng.smarthealthmonitor.tv.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
 
object NeonClient {
    private const val BASE_URL = "https://${BuildConfig.NEON_HOST}/"
 
    // Usaremos la API KEY del local.properties como la cadena de conexión
    val CONN_STRING  = BuildConfig.NEON_API_KEY
 
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
