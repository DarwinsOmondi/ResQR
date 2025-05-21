# 🚨 ResQR

**ResQR** is a life-saving Android application that empowers users to instantly send emergency alerts and securely share their medical data via QR codes — even from the lock screen.

## 🔑 Key Features

- **🔐 Role-Based Access Control**
  - Choose your role at signup: **Victim** (emergency reporter) or **Responder** (emergency personnel).
  
- **🆘 Instant Emergency Alerts**
  - Victims can send **real-time SOS alerts** with GPS coordinates, a short description, and an optional image.
  
- **🧬 Secure Medical Profile**
  - Store crucial medical data: blood type, medication, allergies, conditions — securely and privately.
  
- **📲 QR Code Medical Sharing**
  - Generate a QR code for responders to **instantly scan** and access your medical info — even from the lock screen.

- **🔔 Real-Time Alert Dashboard**
  - Responders receive instant updates about nearby SOS alerts, with user location, status, and medical data.

- **🎨 Role-Based Theming**
  - Custom UI themes:
    - 🔴 **Red** for Victims
    - 🔵 **Blue** for Responders

- **🔒 Lock Screen Overlay**
  - Show your QR code and critical data **without unlocking** the device — lifesaving during unconscious or restricted situations.

---

## 🚧 Tech Stack

| Layer       | Technology                                 |
|-------------|---------------------------------------------|
| **Frontend** | Kotlin, Jetpack Compose                    |
| **Architecture** | MVVM (Model-View-ViewModel)             |
| **Backend**  | [Supabase](https://supabase.com/)         |
|             | - Auth (user authentication)               |
|             | - Postgres (medical/user data)             |
|             | - Realtime (emergency alert updates)       |
|             | - Storage (image uploads)                  |
| **Others**   | QR Code Generator, Location Services      |

---

## 📦 Features by Role

### 👤 Victim
- Register and build a personal medical profile
- Send real-time emergency alerts with:
  - 📍 GPS location
  - 📝 Short description
  - 📸 Image of the situation
- Display medical QR code on the lock screen

### 🧑‍🚒 Responder
- Scan victim's QR code for instant medical access
- View real-time list of incoming emergency alerts
- Receive alerts with full context (map, image, details)

---

## 🧪 Project Structure
ResQR/
│
├── app/
│ ├── ui/ # Composables and screens
│ ├── viewmodel/ # ViewModel classes (MVVM)
│ ├── model/ # Data classes (User, MedicalData, etc.)
│ ├── repository/ # Data access and business logic
│ └── utils/ # QR code, location, permissions
│
├── supabase/ # Supabase client and config
└── README.md # 📄 You're here!

## ⚙️ Setup Instructions

1. **Clone the Repository**

   ```bash
   git clone https://github.com/yourusername/ResQR.git
   cd ResQR
2. **Open in Android Studio**

3. **File > Open > Choose the ResQR folder**

4.**Set up Supabase**

5.**Add your Supabase credentials (URL + Key) in the appropriate config file**

6.**Run the App**

Click ▶️ Run or use the emulator/physical device
