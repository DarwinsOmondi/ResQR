# 🚨 ResQR

**ResQR** is a life-saving Android application that enables users to instantly broadcast emergency alerts and share critical medical information through QR codes — even when their device is locked.

---

## 🔑 Key Features

* ### 🔐 **Role-Based Access**

  Users select a role at sign-up:

  * **👤 Victim** – someone who may need help during an emergency.
  * **🧑‍🚒 Responder** – trained personnel or nearby civilians who can respond to alerts.

* ### 🆘 **Instant Emergency Alerts**

  Victims can:

  * Trigger **real-time SOS alerts**.
  * Share **GPS coordinates**, a **short message**, and an **optional image**.

* ### 🧬 **Medical Profile Management**

  Securely store medical data such as:

  * Blood type
  * Allergies
  * Chronic conditions
  * Medications

* ### 📲 **QR Code-Based Medical Access**

  Victims generate a unique QR code that:

  * Allows responders to access their medical profile.
  * Works even from the **lock screen**.

* ### 🔔 **Real-Time Alert Dashboard**

  Responders can:

  * View incoming emergency alerts.
  * Get live updates with location, alert type, and patient info.

* ### 🎨 **Role-Based Theming**

  Distinct visual UI based on role:

  * 🔴 **Red UI** for Victims
  * 🔵 **Blue UI** for Responders

* ### 🔒 **Lock Screen Overlay**

  Victims can show QR code and selected medical details **without unlocking their device** — critical for unconscious or disabled individuals.

---

## 🧱 Tech Stack

| Layer            | Technology                             |
| ---------------- | -------------------------------------- |
| **Frontend**     | Kotlin, Jetpack Compose                |
| **Architecture** | MVVM (Model-View-ViewModel)            |
| **Backend**      | [Supabase](https://supabase.com/)      |
|                  | - Supabase Auth (Authentication)       |
|                  | - Supabase Database (PostgreSQL)       |
|                  | - Supabase Realtime (Live alerts)      |
|                  | - Supabase Storage (Media files)       |
| **Others**       | QR Code Generator, Google Location API |

---

## 🧩 Features by Role

### 👤 **Victim**

* Register and update a personal **medical profile**.
* Send **emergency alerts** with:

  * 📍 GPS location (real-time)
  * 📝 Short description
  * 📸 Optional image
* Display a **QR Code overlay** from the lock screen.

### 🧑‍🚒 **Responder**

* Receive **live emergency alerts**.
* View alert details: victim's name, location, status, medical data.
* **Scan victim's QR code** to instantly retrieve medical data.
* Access embedded **maps, images**, and quick contact options.

---

## 📁 Project Structure

```bash
ResQR/
├── app/
│   ├── ui/                # UI screens and Jetpack Compose elements
│   │   ├── victim/        # Victim-specific UI screens
│   │   ├── responder/     # Responder-specific UI screens
│   │   └── common/        # Shared composables
│
│   ├── viewmodel/         # ViewModel classes per feature/module
│   │   ├── AuthViewModel.kt
│   │   ├── VictimViewModel.kt
│   │   └── ResponderViewModel.kt
│
│   ├── model/             # Kotlin data classes
│   │   ├── User.kt
│   │   ├── MedicalProfile.kt
│   │   └── Alert.kt
│
│   ├── repository/        # Logic for interacting with Supabase
│   │   ├── AuthRepository.kt
│   │   ├── AlertRepository.kt
│   │   └── MedicalDataRepository.kt
│
│   ├── utils/             # Helpers: QR generation, location, etc.
│   │   ├── QRCodeUtils.kt
│   │   ├── LocationUtils.kt
│   │   └── Permissions.kt
│
│   └── theme/             # Colors, themes, typography
│
├── supabase/              # Supabase keys, tables, API config
│   ├── SupabaseClient.kt
│   └── SupabaseConstants.kt
│
├── README.md              # This file
└── build.gradle.kts       # Kotlin DSL build configuration
```

---

## ⚙️ Setup Instructions

### 📦 Clone the Project

```bash
git clone https://github.com/DarwinsOmondi/ResQR.git
cd ResQR
```

### 🛠 Open in Android Studio

1. Launch **Android Studio**.
2. Click **File > Open** and select the `ResQR` folder.
3. Let Gradle sync and dependencies resolve.

### 🔐 Supabase Setup

1. Go to [https://supabase.com/](https://supabase.com/).
2. Create a new project and get:

   * `SUPABASE_URL`
   * `SUPABASE_ANON_KEY`
3. Add credentials in:

   ```kotlin
   // SupabaseConstants.kt
   const val SUPABASE_URL = "https://<your-project>.supabase.co"
   const val SUPABASE_KEY = "your-anon-key"
   ```

### 🧮 Setup Tables

Run the provided SQL in your Supabase project's SQL editor:

```sql
-- users table
create table users (
  id uuid primary key default uuid_generate_v4(),
  email text unique not null,
  role text check (role in ('victim', 'responder')),
  full_name text,
  blood_type text,
  allergies text[],
  medication text[],
  conditions text[],
  created_at timestamptz default now()
);

-- alerts table
create table alerts (
  id uuid primary key default uuid_generate_v4(),
  user_id uuid references users(id),
  location geography(Point, 4326),
  description text,
  timestamp timestamptz default now()
);
```

---

## ▶️ Run the App

1. Select a device or emulator.
2. Hit the ▶️ **Run** button.
3. Login or sign up with role selection (Victim or Responder).

---

## 💡 Contributions

Pull requests and feature suggestions are welcome!
Please:

* Follow the MVVM structure.
* Use Jetpack Compose idioms.
* Maintain clean commit history and code readability.

---

## 🛡️ License

This project is licensed under the MIT License — see the `LICENSE` file for details.

---
