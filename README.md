# NextStop
NextStop is an android app built using Kotlin and Jetpack Compose. It allows tracking of [WPT](https://winnipegtransit.com/) 
buses so you can see exactly how long you need to wait.

<img src="https://github.com/user-attachments/assets/58099aa1-8842-4f34-adde-f89b80b04e67" width="250">
<img src="https://github.com/user-attachments/assets/76cf67ef-1266-4c0a-bc47-dd8d39ee6eb2" width="250">
<img src="https://github.com/user-attachments/assets/fe7c1738-df7a-4cc4-9473-3b1b2bc9714c" width="250">

# Running
To run the app locally:
1. [Sign up](https://api.winnipegtransit.com/) to access WPT's API.
2. Create a `gradle.properties` file in the root directory.
3. Clone the repo.
4. Add the following to the `gradle.properties`
```
android.useAndroidX=true
API_KEY=YOUR_API_KEY_HERE
```
6. Run the app in Android Studio :)
