# Firebase Setup Instructions

## Important: Firebase Configuration Required

This app uses Firebase Firestore to store order data. To enable Firebase functionality, you need to:

### 1. Create a Firebase Project
1. Go to [Firebase Console](https://console.firebase.google.com/)
2. Click "Add project" or select an existing project
3. Follow the setup wizard

### 2. Add Android App to Firebase
1. In Firebase Console, click "Add app" and select Android
2. Enter your package name: `com.example.myapplication_mainmenu1`
3. Download the `google-services.json` file
4. Place it in `app/` directory (same level as `build.gradle.kts`)

### 3. Enable Firestore Database
1. In Firebase Console, go to "Firestore Database"
2. Click "Create database"
3. Start in test mode (for development)
4. Choose a location for your database

### 4. Set Firestore Rules (Optional for testing)
For development, you can use these rules in Firestore Console:

```
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    match /orders/{document=**} {
      allow read, write: if true;
    }
  }
}
```

**Warning:** These rules allow anyone to read/write. Update them for production!

### 5. Enable Anonymous Authentication (Optional)
If you want user-specific orders:
1. Go to Authentication in Firebase Console
2. Enable "Anonymous" sign-in method

### Current Implementation
The app will:
- Use device ID as user ID if Firebase Auth is not configured
- Store orders in Firestore under the `orders` collection
- Display success message after order placement

If Firebase is not configured, the app will show an error when placing orders. Make sure to complete the setup above.

