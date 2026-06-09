# Firebase Configuration - Step by Step

## 🔥 Firebase Setup Guide

### ⚠️ Important
Before you start, make sure your Firebase project is already set up in the app.

---

## ✅ Step 1: Create Collection and Document

### In Firebase Console:

1. Go to: **Firestore Database** → **Data**
2. Click **Create Collection**
3. Name it: `settings`
4. Click **Next**
5. Create a new document:
   - **Document ID:** `admin_codes` (NOT auto-generated)
   - Click **Save**

---

## ✅ Step 2: Add Admin Codes Array

### In the Document You Just Created:

1. Click **Add Field**
2. **Field name:** `codes`
3. **Field type:** Choose **Array** from dropdown
4. Click on the empty field
5. For each admin code:
   - Click **Add element**
   - Choose type: **String**
   - Enter code value (e.g., "ADMIN_2024")
   - Click checkmark
6. Click **Save**

### Example Array:
```
codes: [
  "ADMIN_CODE_001",
  "ADMIN_CODE_2024",
  "MANAGER_ACCESS"
]
```

---

## ✅ Step 3: Verify Structure

Your Firestore should now look like:

```
Collections
├── settings/
│   └── admin_codes/
│       └── codes: ["ADMIN_CODE_001", "ADMIN_CODE_2024", ...]
├── users/
├── products/
├── categories/
└── orders/
```

---

## ✅ Step 4: Update Security Rules

### Go to: **Firestore Database** → **Rules**

1. **Delete** existing rules
2. **Paste** the content from `firestore.rules` file
3. Click **Publish**

### Rules will control:
- ✅ Users can only access their own profile
- ✅ Admins can see other admins' data
- ✅ Admin codes only visible to admins
- ✅ Users can't modify their own role

---

## 🧪 Step 5: Test the Setup

### Test Case 1: Check Collection
1. Open Firebase Console → Firestore
2. Verify you see `settings` collection
3. Verify you see `admin_codes` document
4. Verify you see `codes` array with your codes

### Test Case 2: Test in App
1. Open the SmartShop app
2. Register a new user account
3. Go to **Profile** → **Request Admin Access**
4. Enter one of your admin codes
5. Should see success message ✅

### Test Case 3: Verify Role Updated
1. Open Firebase Console → Firestore
2. Go to `users` collection
3. Find the user you just registered
4. Verify `role` field = `admin` ✅

---

## 📝 Common Mistakes to Avoid

### ❌ Wrong Collection Name
- WRONG: "setting" or "admin" or "codes"
- CORRECT: `settings` (plural!)

### ❌ Wrong Document ID
- WRONG: auto-generated ID
- CORRECT: `admin_codes` (specify this!)

### ❌ Wrong Field Type
- WRONG: String type with comma-separated values
- CORRECT: Array type with individual string elements

### ❌ Missing Array Brackets
- WRONG: codes: "ADMIN_001, ADMIN_002"
- CORRECT: codes: ["ADMIN_001", "ADMIN_002"]

### ❌ Forgot Security Rules
- Problem: Users can see each other's data
- Solution: Apply security rules from `firestore.rules`

---

## 🔐 Security Rules Explained

```firestore
match /settings/{document=**} {
  // Only admins can read settings
  allow read: if getUserRole(request.auth.uid) == 'admin';
  
  // Only admins can write settings
  allow write: if getUserRole(request.auth.uid) == 'admin';
}
```

This means:
- ✅ Only admin users can see admin codes
- ✅ Regular users cannot see this collection
- ✅ Anonymous users are completely blocked

---

## 🆘 Troubleshooting Firebase Setup

### ❌ "Error: settings is undefined"
**Solution:**
- Make sure you created the `settings` collection (not `setting`)
- Make sure you created the `admin_codes` document
- Refresh Firestore console

### ❌ "Code shows in Firebase but app says invalid"
**Solution:**
- Check exact spelling (case-sensitive!)
- Check for extra spaces
- Verify array format (should be ["CODE1", "CODE2"], not "CODE1,CODE2")
- Log out and log back into app

### ❌ "Admin codes not showing in array"
**Solution:**
- Click "Add element" to add each code
- Make sure each is a String type
- Make sure you clicked checkmark after entering code
- Click Save after completing array

### ❌ "Users can see each other's data"
**Solution:**
- Apply security rules from `firestore.rules`
- Verify rules are published
- Wait a few seconds for rules to take effect
- Test with incognito/private window if issues persist

---

## ✨ Firebase Console Quick Guide

### To Add a New Admin Code:
1. Firebase Console → Firestore → settings → admin_codes
2. Click the `codes` field
3. Click "Add element"
4. Choose String type
5. Enter code
6. Click checkmark
7. Click Save

### To Remove an Admin Code:
1. Firebase Console → Firestore → settings → admin_codes
2. Click the `codes` field
3. Click trash icon next to code to delete
4. Click Save

### Changes Take Effect Immediately!
- No app restart needed
- No rebuild needed
- Just save and users can use new codes right away

---

## 📊 Firebase Structure Checklist

Before moving to testing, verify:

- [ ] Collection `settings` created
- [ ] Document `admin_codes` created (with exact name)
- [ ] Field `codes` created with Array type
- [ ] At least one admin code in array
- [ ] Security rules applied and published
- [ ] Each element in array is String type
- [ ] Array uses quotes around each code
- [ ] No extra spaces in codes

---

## 🎯 Typical Firebase Setup (5 minutes)

```
Time 1:00 - Go to Firestore
Time 1:30 - Create "settings" collection
Time 2:00 - Create "admin_codes" document
Time 3:00 - Add "codes" array with values
Time 4:00 - Copy and paste security rules
Time 4:30 - Publish security rules
Time 5:00 - Done! ✅
```

---

## 📸 Visual Checklist

### You Should See This:

**In Firestore Data Tab:**
```
Collections
├── settings
│   └── admin_codes
│       └── codes: ["ADMIN_CODE_001", "ADMIN_CODE_2024"]
```

**In Rules Tab:**
```
Should see the security rules from firestore.rules
Status should show "Published" (green checkmark)
```

---

## 🔗 Quick Links

- Firebase Console: https://console.firebase.google.com/
- Firestore Docs: https://firebase.google.com/docs/firestore
- Security Rules Guide: https://firebase.google.com/docs/firestore/security/start

---

## ✅ When You're Done

After completing this setup:

1. ✅ Users can register as regular users
2. ✅ Users can request admin access
3. ✅ Admin codes verify from Firebase
4. ✅ Admins get special access
5. ✅ Security is enforced

---

## 🚀 Next Step

Once Firebase is set up:

1. Open the app
2. Register a test user
3. Go to Profile → "Request Admin Access"
4. Enter one of your admin codes
5. See success message
6. See admin badge in profile
7. Access Admin Panel ✅

---

**Firebase Setup Status:** Ready to Configure ✅  
**Estimated Time:** 5-10 minutes  
**Difficulty:** ⭐⭐☆☆☆ (Very Easy)

Start with Step 1 above! 👆

