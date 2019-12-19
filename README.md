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
OkHttpClient.download(url: String, crossinline objectMapper: (byteStream: InputStream?) -> T?)
Drawable.colorize(colorInt: Int)
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
- BundleExtractorDelegate средство для работы с передаваемыми в `Fragment.arguments` и `Activity.intent.extras` значениями с помощью Kotlin Delegates:
```kotlin
Fragment.<T>argument(key: String, defValue: T? = null)
Activity.<T>extra(key: String, defValue: T? = null)
```
- BeautySpan средство для удобной работы со Spannable-строками:
```kotlin
Context.spannableBuilder(builder: (SpannableStringCreator.() -> Unit)): SpannableString
```
- UbUtils (методы с * требуют вызова UbUtils.init(context) в Application):
```kotlin
* getString(@StringRes id: Int, vararg parameters: Any) : String
* getResources(): Resources
* copyTextToClipboard(text: String): Boolean
* getStatusBarHeight(): Int
timer: Sequence<Int>
isValidPhoneNumber(number: String): Boolean
isValidEmail(email: String): Boolean
getIPAddress(useIPv4: Boolean): String
isNetworkException(error: Throwable): Boolea
isBrokenSamsungDevice(): Boolean
hideSoftKeyboard(context: Context
openSoftKeyboard(context: Context, view: View)
isGpsIsEnabled(context: Context)
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
