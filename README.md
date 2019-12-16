
# Baca Berita :construction:

Baca Berita is an Android application that fetches Indonesian headline news from [News API](https://newsapi.org/). It is still in its early stages of development.
## Language and Libraries
- Language: [Kotlin](https://kotlinlang.org/)
- Architecture: [MVVM](https://developer.android.com/jetpack/docs/guide), [Android Architecture Components](https://developer.android.com/topic/libraries/architecture) ([LiveData](https://developer.android.com/topic/libraries/architecture/livedata) + [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) + [Room](https://developer.android.com/topic/libraries/architecture/room) + [Data Binding](https://developer.android.com/topic/libraries/data-binding/))
- Networking: [Retrofit](https://square.github.io/retrofit/), [Gson](https://github.com/google/gson)
- Dependency Injection: [Manual Dependency Injection](https://developer.android.com/training/dependency-injection/manual)
- Persistence: [SQLite](https://www.sqlite.org/index.html), [Room](https://developer.android.com/topic/libraries/architecture/room)
- Logging: [Timber](https://github.com/JakeWharton/timber)
- Navigation: Single Activity, [Navigation Component](https://developer.android.com/guide/navigation)
- Asynchronous: [Kotlin Coroutine](https://kotlinlang.org/docs/reference/coroutines-overview.html)
- Image: [Glide](https://bumptech.github.io/glide/)
## Development Setup
API Key is required to run this app.
- Get API Key from [NewsAPI](https://newsapi.org/)
- Create new file named `credentials.properties` in root folder
- Add the API key to `credentials.properties`:
```
NEWS_API_KEY = "put_api_key_here"
```
