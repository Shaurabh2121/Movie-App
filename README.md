# 🎬 Movie App

[![Kotlin](https://img.shields.io/badge/Kotlin-1.9.x-blue.svg?logo=kotlin)](http://kotlinlang.org)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-1.6.x-brightgreen.svg?logo=jetpackcompose)](https://developer.android.com/jetpack/compose)
[![Gradle](https://img.shields.io/badge/Gradle-8.x-yellow.svg?logo=gradle)](https://gradle.org)


**Movie App** is a modern Android application built with cutting-edge technologies, showcasing best practices in Android development. It allows users to effortlessly explore a vast library of movies, discover popular and upcoming titles, delve into detailed information, and keep track of their personal watchlist.

## ✨ App Showcase

<img width="200" alt="Screenshot_20250921_115442" src="https://github.com/user-attachments/assets/aee30cc6-f5d4-41a9-9509-fc48c15a74ca" />
<img width="200"  alt="Screenshot_20250921_114128" src="https://github.com/user-attachments/assets/22beab41-4f14-45c6-bfb1-345844da571a" />
<img width="200" alt="Screenshot_20250921_114007" src="https://github.com/user-attachments/assets/e6de1120-d637-4aeb-b491-b98e43222612" />


## 🚀 Key Features

*   **Explore & Discover:** Browse curated lists of popular, top-rated, and upcoming movies.
*   **Intuitive Sorting:** Easily sort movie lists (e.g., by popularity, release date) through a user-friendly dialog.
*   **Powerful Search:** Quickly find specific movies by title.
*   **Comprehensive Details:** Access rich information for each movie, including synopsis, ratings, cast, and more.
*   **Personal Watchlist:** Bookmark your favorite movies for easy access and tracking.
*   **Sleek & Modern UI:** Built entirely with Jetpack Compose for a declarative, reactive, and visually appealing user interface.
*   **Robust & Resilient:** Graceful error handling for network issues and unexpected API responses, ensuring a consistent user experience.

## 🏗️ Architectural Blueprint

This project is built with a strong emphasis on a **Clean, Modular, and Testable Architecture**, promoting separation of concerns, scalability, and maintainability.

*   **Clean Architecture:** Adheres to principles that separate the application into distinct layers (Presentation, Domain, Data). This ensures that business logic (`:core-domain`) is independent of UI (`:feature:*` modules) and framework specifics, making the codebase easier to understand, test, and evolve.
*   **Modular Design:** The application is broken down into several independent or loosely coupled Gradle modules (`:app`, `:core-domain`, `:core-database`, `:core-network`, and various `:feature:*` modules). This approach:
    *   Improves build times significantly.
    *   Enhances code organization, encapsulation, and reusability.
    *   Facilitates parallel development and team collaboration.
*   **Reactive Programming:** Leverages Kotlin Coroutines and Flow for managing asynchronous operations and data streams efficiently, leading to more responsive UIs and cleaner asynchronous code.
*   **Dependency Injection with Hilt:** Utilizes Hilt for managing dependencies throughout the application, reducing boilerplate, simplifying setup, and improving testability by making it easy to provide mock dependencies.

## 🛠️ Technology Stack & Libraries

*   **Kotlin**: The primary, modern programming language for Android development, chosen for its conciseness and safety features.
*   **Jetpack Compose**: For building the UI declaratively, allowing for faster development cycles, more interactive UIs, and less boilerplate compared to XML layouts.
*   **Kotlin Coroutines & Flow**: For asynchronous programming and reactive data streams, essential for handling network requests and database operations without blocking the main thread.
*   **Android Jetpack Suite**:
    *   **ViewModel**: Manages UI-related data in a lifecycle-aware manner, surviving configuration changes.
    *   **Navigation Compose**: Handles in-app navigation seamlessly within a Compose-based UI.
    *   **Room Persistence Library**: Provides an abstraction layer over SQLite for robust local data storage (e.g., for bookmarks), offering compile-time query validation.
*   **Hilt**: For dependency injection, simplifying dependency management across the app and especially useful for testing.
*   **Retrofit & OkHttp**: For efficient, type-safe, and flexible networking to fetch movie data from remote APIs.
*   **Coil**: An image loading library for Kotlin, built on Coroutines, for efficiently loading and displaying movie posters and backdrops.
*   **Gradle Kotlin DSL**: For writing clear, maintainable, and type-safe build scripts.

## 📖 **Project Structure**

| Module | Responsibility | Key Features |
|--------|----------------|--------------|
| **:app** | Application integration & dependency coordination | Navigation orchestration, DI setup |
| **:core-database** | Local data persistence & caching | Room database, migration handling |
| **:core-domain** | Business logic & use case orchestration | Clean architecture compliance |
| **:core-network** | Remote data integration & API management | Retrofit services, error handling |
| **:feature-movie-list** | Movie discovery & browsing interface | Advanced sorting, filtering capabilities |
| **:feature-movie-detail** | Detailed movie information presentation | Rich media integration |
| **:feature-bookmarks** | User preference & favorite management | Local data persistence |

## 🧩 Module Breakdown

The project follows a multi-module strategy to enforce separation and promote scalability:

*   `**:app**`: The main application module. It orchestrates the different modules, handles top-level navigation, and assembles the final application.
*   `**:core-domain**`: Contains the heart of the business logic. This includes use cases (interactors), domain models (entities), and repository interfaces. It is a pure Kotlin module, independent of the Android framework.
*   `**:core-database**`: Encapsulates all database-related logic, including Room entities, Data Access Objects (DAOs), and the database instance definition.
*   `**:core-network**`: Manages all remote API interactions, including Retrofit service definitions, Data Transfer Objects (DTOs), and network request/response handling.
*   `**:feature:movie-list**`: A self-contained feature module responsible for displaying lists of movies (e.g., popular, upcoming). It includes its own UI (Composables), ViewModel, the sort dialog functionality, and depends on `:core-domain` for data and logic.
*   `**:feature:movie-detail**`: Dedicated to showing detailed information about a selected movie. Contains its UI, ViewModel, and interacts with `:core-domain`.
*   `**:feature:bookmarks**`: Manages the display and interaction with user-bookmarked movies. Includes its UI, ViewModel, and depends on `:core-domain` and `:core-database` (via repositories).
*   **Test Modules** (e.g., `app.unitTest`, `core-domain.unitTest`, `feature:movie-list.unitTest`): Each source module is accompanied by corresponding unit test modules.

## 🧪 Commitment to Quality: Testing Strategy

Quality is a cornerstone of this project. A comprehensive testing strategy is employed to ensure robustness, reliability, and maintainability:

*   **Unit Tests (`*.unitTest` modules)**:
    *   **Use Cases/Interactors (`:core-domain`)**: Thoroughly tested to verify the correctness of all business logic.
    *   **Repository Implementations**: Tested to ensure accurate data handling, transformations, and interactions with (mocked) data sources (network services, database DAOs).
    *   **ViewModels (`:feature:*` modules)**: Extensively tested to validate UI logic, state management, data transformations, and interactions with use cases.
    *   **Error Handling Paths**: Specific test cases cover various error scenarios (e.g., network failures, API errors) to ensure the app handles them gracefully and provides appropriate user feedback.
*   **Coverage**: The project aims for high test coverage across all critical components to catch regressions and ensure stability.

