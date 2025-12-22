# RahulDemo - Portfolio Management App

A modern, robust Android application built with Jetpack Compose that demonstrates professional software engineering practices, including Clean Architecture, MVVM, and reactive programming.

## 📸 Screenshots

| Light Theme | Dark Theme | Error / Offline State |
| :---: | :---: | :---: |
| <img src="https://github.com/user-attachments/assets/0e4c7a23-fa8c-4d7c-a0dc-69c561e9042c" width="250"/> | <img src="https://github.com/user-attachments/assets/ef1c7aad-c03c-46e8-91e0-a13799c2181b" width="250"/> | <img src="https://github.com/user-attachments/assets/8cd949fb-1f73-4585-8113-0d087c3e16f4" width="250"/> |

<p>
  <img src="https://github.com/user-attachments/assets/bee06a10-ea60-4c7d-b373-58f36df56bd9" width="250" />
  <img src="https://github.com/user-attachments/assets/08f6dd79-4293-4298-9054-f14da53e8efc" width="250" />
</p>




## 📱 Features

- **Portfolio Overview**: View a detailed list of stock holdings with real-time calculations.
- **Dynamic Portfolio Summary**: Interactive bottom sheet that calculates and displays:
  - Total Current Value
  - Total Investment
  - Total Profit & Loss (PNL)
  - Today's PNL
- **Interactive UI**: The summary view is collapsible and expandable with a smooth animation on tap.
- **Tab Navigation**: Seamlessly switch between **Holdings** and **Positions** with sliding animations.
- **Theme Support**: Full support for **Light and Dark themes**, ensuring accessibility and visual comfort.
- **Pull-to-Refresh**: Intuitive gesture to refresh portfolio data.
- **Robust Error Handling**:
  - **Network Monitoring**: Centralized monitoring that prompts the user with a Snackbar when offline and auto-recovers when connectivity returns.
  - **Empty States**: Professional "No orders yet" placeholder with Lottie animations for empty tabs.
  - **Error Views**: Specific full-screen error states with Lottie animations when initial data load fails.
  - **Automatic Retry**: if network fails the app tries to automatically call holdings api

## 🛠 Tech Stack

- **UI**: Jetpack Compose (Material 3)
- **Dependency Injection**: Hilt
- **Networking**: Retrofit & OkHttp
- **Asynchronous Flow**: Kotlin Coroutines & StateFlow
- **Animations**: Lottie Compose
- **Architecture**: MVVM + Clean Architecture (Data, Domain, Presentation)
- **Testing**: JUnit, MockK (Unit testing for ViewModels, UseCases, Repositories, and Mappers)

## 🏗 Architecture

The project follows **Clean Architecture** principles to ensure scalability and testability:
- **Presentation Layer**: Compose screens, ViewModels (MVI-ish `onAction` pattern), and UI state management.
- **Domain Layer**: Pure Kotlin business logic containing Models and UseCases (e.g., `CalculatePortfolioSummaryUseCase`).
- **Data Layer**: API definitions, Repository implementations, and Data Transfer Objects (DTOs) with Mappers.



## 🧪 Testing

The app is backed by a comprehensive suite of unit tests:
- **Repository Tests**: Verifies data fetching and mapping logic using mock responses.
- **UseCase Tests**: Ensures business logic for financial calculations is 100% accurate.
- **ViewModel Tests**: Validates UI state transitions, action handling, and automatic network recovery logic.

## 🚀 Getting Started

1. Clone the repository.
2. Open in Android Studio (Ladybug or newer recommended).
3. Sync the project with Gradle files.
4. Run the `:app` module on an emulator or physical device.
5. To run tests: Right-click `app/src/test` folder and select **Run 'Tests in...'**
