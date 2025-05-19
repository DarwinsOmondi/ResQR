# ResQR
This is an android app that allows the user to instantly send emergency alerts and securely share their medical data via QR codes, even from the lock screen.

🔑 Key Features
🔐 Role-Based Access: Choose between Victim or Responder roles at signup

🆘 Instant Emergency Alerts: Victims can send GPS-based SOS alerts with a description and image

🧬 Secure Medical Profile: Users store vital data like blood type, medications, and allergies

📲 QR Code Access: Responders scan QR codes (shown on lock screen) to instantly retrieve medical data

🔔 Real-Time Alerts for Responders: View incoming alerts with location and status updates

🎨 Role-Based Theming: Color-coded UI — red for victims, blue for responders

🔒 Lock Screen Overlay: Display QR code and essential info even when the device is locked

☁️ Supabase Backend: Uses Supabase Auth, Realtime, and Postgres for scalable, secure data handling

🚧 Tech Stack
Frontend: Kotlin + Jetpack Compose

Backend: Supabase (Auth, Realtime, Storage, Postgres)

Architecture: MVVM (Model-View-ViewModel)

Others: QR Code Generator, Location Services, Realtime Sync

🎯 Use Case
Ideal for:

Individuals with medical conditions

Emergency responders and health professionals

Crisis reporting and disaster zones

