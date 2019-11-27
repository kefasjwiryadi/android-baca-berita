# Baca Berita :construction:

Baca Berita is an Android application that fetches Indonesian headline news from [NewsAPI](https://newsapi.org/). It is still in its early stages of development.

# Language and Libraries
- Written in [Kotlin](https://kotlinlang.org/)
- [Kotlin Coroutine](https://kotlinlang.org/docs/reference/coroutines-overview.html)
- [Android Architecture Components](https://developer.android.com/topic/libraries/architecture): [LiveData](https://developer.android.com/topic/libraries/architecture/livedata) + [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) + [Room](https://developer.android.com/topic/libraries/architecture/room)
- [Navigation Component](https://developer.android.com/guide/navigation)
- [Retrofit](https://square.github.io/retrofit/)

# Architecture
- Single activity
- MVVM:
![MVVM](https://developer.android.com/topic/libraries/architecture/images/final-architecture.png)

# Development Setup
API Key is required to run this app.
- Get API Key from [NewsAPI](https://newsapi.org/)
- Create new file named `credentials.properties` in root folder
- Add the API key to `credentials.properties`:
```
NEWS_API_KEY = "put_api_key_here"
```
