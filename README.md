# GRAMA - Grama-Suvidha Portal

**Grama-Suvidha Portal** is a specialized Android application designed to bridge the transparency gap between village panchayats and citizens. It acts as a **Digital Village Notice Board**, allowing residents to track the progress of local development projects, provide feedback, and report issues in real-time.

---

## 📱 App Overview

The app provides a centralized platform for managing and viewing village-level infrastructure works. It is built to work seamlessly in rural environments where internet connectivity may be intermittent.

### Key Highlights
- **Dual Language Support**: Full localization in **English** and **Kannada** to ensure accessibility for all villagers.
- **Role-Based Experience**: Tailored interfaces for **Panchayat Admins** and **General Users**.
- **Offline First**: Powered by a local Room database, ensuring the app is fully functional without an internet connection.
- **Transparency**: Clear breakdown of project budgets, components used, and real-time progress percentages.

---

## 🚀 Features

### For Users
- **Registration & Login**: Secure account creation for village residents.
- **Project Dashboard**: View ongoing, completed, and planned projects with visual progress bars.
- **Detailed Insights**: Access project-specific data including "Work Amount Splits" and "Components Used".
- **Interactive Feedback**: Rate project quality using stars and submit detailed feedback or report issues directly on project photos.

### For Panchayat Admins
- **Project Management**: Add new projects to the village notice board using the floating action button.
- **Data Control**: Ability to delete or update project information.
- **Administrative Badge**: Clear visual distinction to prevent role confusion.
- **Audit View**: Access to internal audit tags and detailed financial splits.

---

## 🛠 Tech Stack

- **Language**: Kotlin
- **UI Framework**: XML Layouts with **Data Binding** and **Material Design 3**.
- **Architecture**: MVVM (Model-View-ViewModel).
- **Database**: **Room Database** for persistent local storage and offline support.
- **Navigation**: Jetpack Navigation Component.
- **Image Loading**: Coil for optimized image rendering.
- **Annotation Processing**: KAPT & KSP.

---

## 📦 Getting Started

### Prerequisites
- Android Studio Ladybug or newer.
- JDK 11 or 17.
- Android SDK 35 (Compile SDK).

### Default Credentials (Admin)
To explore the administrative features, use:
- **Email**: `admin`
- **Password**: `admin123`
- **Role**: Select "As Panchayat Admin"

---

## 📸 Screen Highlights
1. **Login**: Language toggle and role selection.
2. **Dashboard**: Project summary cards (Ongoing/Completed/Planned).
3. **Project Details**: Image overlays for quick feedback and rating.
4. **Registration**: Simple form for new users to join the portal.

---

## ⚖️ License
This project is developed for the Grama-Suvidha initiative to improve rural digital infrastructure.
