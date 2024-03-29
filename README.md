# Web-Club-Recruitment-Task
Android-Mobile-Vision

**Application Name : Kill Bill (Android-Mobile-Vision Task)**
<br>
Language used : Kotlin
<br>
Minimum API Level : API 21, Android 5.0 (Lollipop)
<br>
Android Studio Version : 3.4
<br>
Application Size : 4MB

---

***How to set up:***
- Clone the repository and then connect an Android phone/Emulator of the minimum given SDK level and then press 'Run' in Android Studio
- It is mandatory for the Android device to have camera hardware and internal storage for images.
- The app can also be installed from the unsigned APK, however some phone settings need to be enabled to allow download from unknows source. (Path : /KillBill/app/build/outputs/apk/debug/apk-debug)

---

***Usage Instructions:***
<br>
*App Workflow:*
- The app will open up a screen which will show the user his/her list of previously captured bills
- There is an option to delete any desired records as well
- A button at the bottom will open up a new activity which allows the user to capture an image via any camera application or upload an image from gallery.
- Once analyzed and if any amount is recoginzed, a dialog will pop-up which allows the use to fill in the bill description.
- The data will be stored along with the current system date and can be viewed in the home page as mentioned above
- Appropriate Toast messages will guide the user during usage.

---

***Technical details:***
<br>
*Libraries*
- Android Jetpack
- Android X
- Material Design
- Android Vision - OCR Text Reader for Image Analysis 

---

*Plugins*
- Material Design Icon Generator

---

*Database*
- Used SQLite for storing the records

---



