# Admin Role System - Setup Guide

## Overview

This SmartShop application now includes a role-based user system where:
1. **New users** automatically register as regular **users**
2. Users can **request admin access** by entering a valid **admin code**
3. Once verified, users are promoted to **admin** role
4. **Admins** can access the Admin Panel to manage products, categories, and orders

## How It Works

### User Registration Flow
1. User registers with email, password, display name (optional), and phone number
2. User is automatically created as a regular **user** (role: 'user')
3. User data is stored in Firestore under `users/<uid>` collection

### Admin Access Request Flow
1. User navigates to Profile Screen
2. If not already an admin, user sees "Request Admin Access" button
3. User enters the admin code
4. The app verifies the code against Firebase
5. If valid, user's role is updated to 'admin'
6. User sees admin badge and "Admin Panel" button becomes available

## Firebase Setup Steps

### 1. Create Admin Codes Collection

In your Firebase Firestore console:

1. Go to your Firestore Database
2. Create a new collection named **`settings`**
3. Create a new document with ID: **`admin_codes`**
4. Add a field:
   - **Field name:** `codes` (Array type)
   - **Value:** Add your admin codes as array items (strings)
   
Example:
```
codes: Array
  [0]: "ADMIN_SECRET_CODE_001"
  [1]: "ADMIN_CODE_2024"
  [2]: "YOUR_ADMIN_KEY"
```

### 2. Set Firestore Security Rules

Update your Firestore Security Rules to protect admin codes:

```firestore
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    
    // Allow authenticated users to read/write their own profile
    match /users/{uid} {
      allow read, write: if request.auth.uid == uid;
    }
    
    // Only admins can read/write settings
    match /settings/{document=**} {
      allow read, write: if request.auth != null && 
        get(/databases/$(database)/documents/users/$(request.auth.uid)).data.role == 'admin';
    }
    
    // Allow anyone to read public collections
    match /{document=**} {
      allow read: if request.auth != null;
    }
  }
}
```

### 3. User Data Structure in Firestore

Each user document includes:

```json
{
  "uid": "firebase_user_id",
  "email": "user@example.com",
  "displayName": "User Name",
  "phoneNumber": "+88017xxxxxxxx",
  "role": "user",  // or "admin"
  "createdAt": "timestamp",
  "adminVerifiedAt": "timestamp"  // Only for admins
}
```

## API/Code Reference

### AuthService Methods

#### Get User Data from Firestore
```dart
Future<UserModel?> getUserData(String uid) async
```

#### Verify Admin Credentials
```dart
Future<bool> verifyAdminCredentials(String uid, String adminCode) async
```
- Checks if admin code is valid
- Updates user role to 'admin' if valid
- Returns `true` if successful, `false` otherwise

#### Update User Role Manually
```dart
Future<void> updateUserRole(String uid, String role) async
```

### AuthViewModel Methods

#### Request Admin Access
```dart
Future<bool> requestAdminAccess(String adminCode) async
```
- Verifies the admin code
- Updates user model if successful
- Returns `true` if successful

#### Check if User is Admin
```dart
bool get isAdmin => _userModel?.role == 'admin';
```

## Routes

### Admin Verification Screen
- **Route:** `AppRoutes.adminVerification`
- **Path:** `/admin-verification`
- **Component:** `AdminVerificationScreen`
- **Description:** Screen where users can enter admin code

### Admin Panel
- **Route:** Accessible from Dashboard > Admin Panel (if user is admin)
- **Component:** `AdminDashboardScreen`
- **Description:** Admin management interface

## Testing the System

### Test Scenario 1: Regular User Registration
1. Install and run the app
2. Register new user with email and password
3. Verify user is created in Firestore with role: 'user'
4. Check profile screen shows "Request Admin Access" option

### Test Scenario 2: Request Admin Access with Invalid Code
1. Click "Request Admin Access"
2. Enter invalid code
3. Should see error message
4. User role remains 'user'

### Test Scenario 3: Request Admin Access with Valid Code
1. Click "Request Admin Access"
2. Enter valid admin code (from Firebase settings)
3. Should see success message
4. Profile now shows admin badge
5. Admin Panel appears in drawer menu
6. User role in Firestore updated to 'admin'

## Troubleshooting

### Admin Code Not Working
- Verify the admin code is added to `settings/admin_codes` collection
- Check the exact spelling and spacing
- Ensure codes array is properly formatted in Firebase

### User Can't Access Admin Panel After Verification
- Try logging out and logging back in
- Check user document in Firestore has role: 'admin'
- Ensure Firestore rules allow viewing admin content

### Admin Access Screen Shows Error
- Check internet connection
- Verify user is logged in
- Check Firebase project is initialized correctly

## Security Notes

- **Admin codes** should be secure and not shared publicly
- Consider rotating admin codes periodically
- Use Firestore security rules to restrict sensitive data
- Log admin verification attempts for audit trail
- Consider adding email verification for admin accounts

## Future Enhancements

- Two-factor authentication for admin access
- Admin activity logging
- Role-based access control (RBAC) for specific admin functions
- Admin code expiration dates
- Multiple admin levels (super admin, manager, editor)
- Email notification on admin verification

