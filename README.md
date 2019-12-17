
# Baca Berita :construction:
Baca Berita is an Android application that fetches Indonesian headline news from [News API](https://newsapi.org/). It is still in its early stages of development.
## Screenshots
![Home screen](screenshots/home_framed.png "Home screen")
![Detail 1 screen](screenshots/detail_1_framed.png "Detail 1 screen")
![Detail 2 screen](screenshots/detail_2_framed.png "Detail 2 screen")
![Search screen](screenshots/search_framed.png "Search screen")
![Favorite screen](screenshots/favorite_framed.png "Favorite screen")
![Splash screen](screenshots/splash_framed.png "Splash screen")
## Demo
[https://youtu.be/hp51WvGVcpg](https://youtu.be/hp51WvGVcpg)
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
## License
    MIT License

    Copyright (c) 2019 Kefas J. Wiryadi

    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:

    The above copyright notice and this permission notice shall be included in all
    copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
    SOFTWARE.
