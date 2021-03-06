## Overview

Note is a sample to demonstrate how to use AndroidMvc.

There are 2 modules:
* android: includes views, dependencies of controllers relying on Android Framework
* core: includes controllers, models, services. Core module is pure JVM that can run unit tests without Android SDK at all.

## What's included
1. How to do dependency injection - see fragments and controllers
2. How a controller save data into preferences with a [PreferenceService](https://github.com/kejunxia/AndroidMvc/blob/master/samples/note/core/src/main/java/com/shipdream/lib/android/mvc/samples/note/service/android/PreferenceService.java) independent from Android SDK - [NoteControllerImpl](https://github.com/kejunxia/AndroidMvc/blob/master/samples/note/core/src/main/java/com/shipdream/lib/android/mvc/samples/note/controller/internal/NoteControllerImpl.java) needs to access preferences. But it's only talk to its [abstract interface](https://github.com/kejunxia/AndroidMvc/blob/master/samples/note/core/src/main/java/com/shipdream/lib/android/mvc/samples/note/service/android/PreferenceService.java)
3. How to satisfy [NoteControllerImpl](https://github.com/kejunxia/AndroidMvc/blob/master/samples/note/core/src/main/java/com/shipdream/lib/android/mvc/samples/note/controller/internal/NoteControllerImpl.java) with real Android Preference - The real implementation is [PreferenceServiceImpl](https://github.com/kejunxia/AndroidMvc/blob/master/samples/note/android/src/main/java/com/shipdream/lib/android/mvc/samples/note/view/internal/PreferenceServiceImpl.java) in module [android](https://github.com/kejunxia/AndroidMvc/tree/master/samples/note/android).
4. How to navigate - [AppControllerImpl](https://github.com/kejunxia/AndroidMvc/blob/master/samples/note/core/src/main/java/com/shipdream/lib/android/mvc/samples/note/controller/internal/AppControllerImpl.java) and [NoteControllerImpl](https://github.com/kejunxia/AndroidMvc/blob/master/samples/note/core/src/main/java/com/shipdream/lib/android/mvc/samples/note/controller/internal/NoteControllerImpl.java) use injected [NavigationController](https://github.com/kejunxia/AndroidMvc/blob/master/library/android-mvc-controller/src/main/java/com/shipdream/lib/android/mvc/controller/NavigationController.java) to navigate
5. How to integrate drawer menu - [MainFragment](https://github.com/kejunxia/AndroidMvc/blob/master/samples/note/android/src/main/java/com/shipdream/lib/android/mvc/samples/note/view/fragment/MainFragment.java)
6. How to support tablet with alternative view - [NoteTabletLandscape](https://github.com/kejunxia/AndroidMvc/blob/master/samples/note/android/src/main/java/com/shipdream/lib/android/mvc/samples/note/view/fragment/NoteTabletLandscape.java) and [AppControllerImpl](https://github.com/kejunxia/AndroidMvc/blob/master/samples/note/core/src/main/java/com/shipdream/lib/android/mvc/samples/note/controller/internal/AppControllerImpl.java) controllers what view to show
7. How to consume http service - [WeatherControllerImpl](https://github.com/kejunxia/AndroidMvc/blob/master/samples/note/core/src/main/java/com/shipdream/lib/android/mvc/samples/note/controller/internal/WeatherControllerImpl.java)
8. How to do unit tests - [TestNoteController](https://github.com/kejunxia/AndroidMvc/blob/master/samples/note/core/src/test/java/com/shipdream/lib/android/mvc/samples/note/controller/internal/TestNoteController.java)
9. How to mock http service without real network in unit tests - [TestWeatherController](https://github.com/kejunxia/AndroidMvc/blob/master/samples/note/core/src/test/java/com/shipdream/lib/android/mvc/samples/note/controller/internal/TestWeatherController.java)

## Download APK
[Click to download sample APK](https://docs.google.com/uc?authuser=0&id=0BwcZml9gnwoZOHcxZFI3Z0ZGUUk&export=download)