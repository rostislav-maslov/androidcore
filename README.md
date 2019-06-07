# androidcore

[ ![Download](https://api.bintray.com/packages/unitbean/AndroidCore/com.unitbean.core/images/download.svg) ](https://bintray.com/unitbean/AndroidCore/com.unitbean.core/_latestVersion)

Библиотека включает в себя базовый набор средств и инструментов для Android-разработчика

Ориентирована на проекты, основным языком которых является [**Kotlin**](https://github.com/JetBrains/kotlin)

Основная архитектура базируется на MVP, и частной её реализации, предоставляемой библиотекой [**Moxy**](https://github.com/moxy-community/Moxy)

## Что содержит

- UbNotify (средство показа пуш-уведомлений, инкапсулирующий в себе весь boilerplate-код)
- Extension-функции: 
```kotlin
View.dpToPx(dp : Int): Float
View.visible
View.invisible
View.gone
AlertDialog.isNotShowing(): Boolean
Disposable.isNotDisposed(): Boolean
<T>ArrayList<T>.renew(list: Collection<T>): ArrayList<T>
Collection<String>.containsIgnoreCase(value: String): Boolean
View.animator(property Property<T, Float>, vararg values Float): ObjectAnimator
View.animator(property String, vararg values Float): ObjectAnimator
```
- Удобные Extension-функции для работы с `java.util.Calendar`:
```kotlin
Calendar.dayOfWeek
Calendar.day
Calendar.hours
Calendar.minutes
Calendar.seconds
Calendar.milliseconds
Calendar.addHours(hours: Int)
Calendar.addMinutes(minutes: Int)
Calendar.dayRoll(amount: Int)
```
- LogUtils с поддержкой кастомного обработчика ошибок (напр. Crashlytics)
- TextAdapter абстрактый класс TextWatcherAdapter
- UbUtils (методы с * требуют вызова UbUtils.init(context) в Application):
```kotlin
* getString(@StringRes id: Int, vararg parameters: Any) : String
* getResources(): Resources
* copyTextToClipboard(text: String): Boolean
* getStatusBarHeight(): Int
isValidPhoneNumber(number: String): Boolean
isValidEmail(email: String): Boolean
getIPAddress(useIPv4: Boolean): String
isNetworkException(error: Throwable): Boolea
isBrokenSamsungDevice(): Boolean
hideSoftKeyboard(context: Context
openSoftKeyboard(context: Context, view: View)
isGpsIsEnabled(context: Context)
```

Библиотека предоставляет конечному проекту следующие зависимости.
```gradle
def verMoxy = '1.0.13'
def verKotlin = '1.3.31'
def verRetrofit = '2.5.0'
def verDagger = '2.23'
def verCoroutines = '1.2.1'

  // app compat
  api 'androidx.appcompat:appcompat:1.0.2'
  api 'androidx.legacy:legacy-support-v4:1.0.0'
  api 'androidx.recyclerview:recyclerview:1.0.0'
  api 'androidx.cardview:cardview:1.0.0'
  api 'com.google.android.material:material:1.0.0'
  api 'androidx.vectordrawable:vectordrawable:1.0.1'
  api 'androidx.constraintlayout:constraintlayout:1.1.3'

  //moxy
  api "com.github.moxy-community:moxy:$verMoxy"
  api "com.github.moxy-community:moxy-androidx:$verMoxy"
  api "com.github.moxy-community:moxy-material:$verMoxy"

  //retrofit 2
  api "com.squareup.retrofit2:retrofit:$verRetrofit"
  api "com.squareup.retrofit2:converter-gson:$verRetrofit"
  api "com.squareup.retrofit2:adapter-rxjava2:$verRetrofit"

  //logging interceptor
  api 'com.squareup.okhttp3:logging-interceptor:3.12.1'

  //rx android
  api 'io.reactivex.rxjava2:rxandroid:2.1.1'
  api 'io.reactivex.rxjava2:rxjava:2.2.8'

  //dagger 2
  api "com.google.dagger:dagger:$verDagger"
  
  // kotlin coroutines
  api "org.jetbrains.kotlinx:kotlinx-coroutines-core:$verCoroutines"
  api "org.jetbrains.kotlinx:kotlinx-coroutines-android:$verCoroutines"
```

Следует учесть, что аттрибуты **kapt** необходимо подключать отдельно в gradle-файле финального проекта
```gradle
def verMoxy = '1.0.13'
def verDagger = '2.23'

  // kapts
  kapt "com.github.moxy-community:moxy-compiler:$verMoxy"
  kapt "com.google.dagger:dagger-compiler:$verDagger"
```

## Как подключить

Библиотека опубликована в JCenter и доступна напрямую.
Добавить в локальный модуль следующее

```gradle
dependencies {
    implementation 'com.unitbean.core:android:$latest_version'
}
```

## Пример

Расположен в модуле `app` проекта

## Разработчик 

* Виктор Лиханов

Yandex: [volixanov@unitbean.com](volixanov@unitbean.com)

## Соавторы

* Денис Капустин

Yandex: [denis.kapustin@unitbean.com](denis.kapustin@unitbean.com)



* Умалт Исакхаджиев

Yandex: [uisakxazhiev@unitbean.com](uisakxazhiev@unitbean.com)
