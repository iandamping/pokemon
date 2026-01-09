# Android Jetpack Compose Showcase - Clean Architecture & Static Analysis

This repository is a modern Android development lab focused on implementing Declarative UI and Automated Code Quality Control. This project demonstrates how to build scalable applications that meet industry standards.

### Tech stack
* UI: Jetpack Compose (Material 3).
* Extract Color: Palette
* Architecture: MVVM with Clean Architecture.
* Dependency Injection: Hilt.
* Networking: Retrofit with OkHTTP & Moshi.
* Local Database: Room.
* Unit-testing: Mockk.
* Coroutine Flow Unit-Testing: Turbine

### Code Quality Assurance:
* Detekt: Used for static code analysis to ensure compliance with Kotlin coding standards.
* Reviewdog: Integrated with GitHub Actions to provide automated code reviews directly on Pull Requests, ensuring every line of code meets standards before being merged.


### Architecture
**Pokemon** is based on MVVM architecture and Repository pattern whose architecture follows [Google's official architecture guidelines.](https://developer.android.com/topic/architecture).
The architecture consists of two layers: Data layer and UI layer.<br/>
### 💾 Data Layer (Offline-First)
**Pokemon** implement an **Offline-first** strategy. This means the application remains fully functional without an internet connection by using the local database as the primary source.

### 📱 UI Layer
This layer handles the display. We use a **State-driven UI** where the UI will react to changes in data emitted by the ViewModel.
