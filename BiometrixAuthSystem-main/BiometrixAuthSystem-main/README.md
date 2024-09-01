# BiometrixAuthSystem
## Biometric Login System with Android App
### This project is a secure login system for web applications using face recognition technology along with an Android app for biometric authentication.

### Overview
The project consists of two main components:

#### Flask Web Application: The web application provides a user interface for login using face recognition. It allows users to register their face and login securely to access protected resources.
#### Android App: The Android app serves as an additional layer of security for user authentication. It utilizes biometric authentication (such as fingerprint or face unlock) on the user's device to verify their identity before granting access to the web application.
### Features
+ Face recognition login system for web applications.
+ Registration of user faces for secure authentication.
+ Biometric authentication using the Android app for an added layer of security.
+ Integration with Firebase for user data management and push notifications.
### Technologies Used
+ Flask: Python web framework used for developing the web application.
+ Face Recognition Library: Utilized for face detection and recognition.
+ Android Studio: Development environment for building the Android app.
+ Firebase: Backend service used for user authentication, data storage, and push notifications.
### Usage
### 1. Web Application:
* Users can register their faces using the registration page.
* After registration, users can log in using face recognition on the login page.
* Access to protected resources is granted upon successful authentication.
### 2. Android App:
* Users can authenticate using biometric authentication (fingerprint, face unlock) on their Android device.
* Once authenticated, users can select an email ID and other personal information associated with their account.
* The app securely communicates with the web application to facilitate login and access to protected resources.
### Setup
+ Clone the repository to your local machine.
+ Follow the setup instructions provided in the README files of the Flask web application and Android app directories.
+ Ensure you have the necessary dependencies installed and configured (e.g., Python, Flask, Firebase, Android SDK).
### Contributions
Contributions to the project are welcome! Feel free to submit bug reports, feature requests, or pull requests via GitHub.


License  
This project is licensed under the MIT License.
