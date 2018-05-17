# androidcore

[![](https://jitpack.io/v/unitbean/androidcore.svg)](https://jitpack.io/#unitbean/androidcore)

Библиотека включает в себя базовый набор средств и инструментов для Android-разработчика

Ориентирована на проекты, основным языком которых является [**Kotlin**](https://github.com/JetBrains/kotlin)

Основная архитектура базируется на MVP, и частной её реализации, предоставляемой библиотекой [**Moxy**](https://github.com/Arello-Mobile/Moxy)

## Что содержит

- BaseActivity, BaseFragment, BaseDialogFragment, BasePresenter, BaseView
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
- LogUtils с поддержкой кастомного обработчика ошибок (напр. Crashlytics)
- TextAdapter абстрактый класс TextWatcherAdapter
- UbUtils (требует инициализации в Application):
```kotlin
getString(@StringRes id: Int, vararg parameters: Any) : String
isValidPhoneNumber(number: String): Boolean
getIPAddress(useIPv4: Boolean): String
isNetworkException(error: Throwable): Boolea
isBrokenSamsungDevice(): Boolean
hideSoftKeyboard(context: Context
openSoftKeyboard(context: Context, view: View)
openMarket(context: Context)
```

Библиотека предоставляет конечному проекту следующие зависимости.
```gradle
def verMoxy = '1.5.3'
def verKotlin = '1.2.41'
def verSupport = '27.1.1'
def verRetrofit = '2.4.0'
def verDagger = '2.16'

  // app compat
  api "com.android.support:appcompat-v7:$verSupport"
  api "com.android.support:support-v4:$verSupport"
  api "com.android.support:appcompat-v7:$verSupport"
  api "com.android.support:recyclerview-v7:$verSupport"
  api "com.android.support:cardview-v7:$verSupport"
  api "com.android.support:design:$verSupport"
  api "com.android.support:support-vector-drawable:$verSupport"
  api 'com.android.support.constraint:constraint-layout:1.1.0'

  //moxy
  api "com.arello-mobile:moxy:$verMoxy"
  api "com.arello-mobile:moxy-app-compat:$verMoxy"

  //retrofit 2
  api "com.squareup.retrofit2:retrofit:$verRetrofit"
  api "com.squareup.retrofit2:converter-gson:$verRetrofit"
  api "com.squareup.retrofit2:adapter-rxjava2:$verRetrofit"

  //logging interceptor
  api 'com.squareup.okhttp3:logging-interceptor:3.10.0'

  //rx android
  api 'io.reactivex.rxjava2:rxandroid:2.0.2'
  api 'io.reactivex.rxjava2:rxjava:2.1.13'

  //dagger 2
  api "com.google.dagger:dagger:$verDagger"
```

Следует учесть, что аттрибуты **kapt** необходимо подключать отдельно в gradle-файле финального проекта
```gradle
def verMoxy = '1.5.3'
def verDagger = '2.16'

  // kapts
  kapt "com.arello-mobile:moxy-compiler:$verMoxy"
  kapt "com.google.dagger:dagger-compiler:$verDagger"
```

## Как подключить

Необходимо добавить jitpack-репо в корневой уровень зависимостей __build.gradle__
```gradle
allprojects {
	repositories {
		...
		maven { url "https://jitpack.io" }
	}
}
```
и после этого добавить в локальный модуль следующее

```gradle
dependencies {
	implementation 'com.github.unitbean:androidcore:$latest_version'
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
