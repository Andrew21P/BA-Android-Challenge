
# Android - Bliss Challenge
Application demo for recruitment process in Bliss Applications. 

![Bliss Challenge](https://i.ibb.co/Xj0Zv81/smartmockups-jzjp5trv.jpg)

## Getting Started
For compile and Run this project, it's mandatory to have Android Studio (last version available recommended), or directly install the application APK and run on a device.

 To get more details about project, you can [check the specification document.](https://docs.google.com/document/d/1qpQDsO03DT9TIqg4oFqtrokr4gXkHF4dlrHgOEXRQ9w/edit#heading=h.xv5gmb3t855w)

### Compile and Run:

 1. Download or clone repository;
 2. Open Android Studio
 3. Open BA-Android-Challenge project
 4. Click Run

### Direct install:

 1. Download APK from repository: /app/release/app-release.apk or from https://bit.ly/2HfWQEO
 2. Copy APK to device
 3. Enable "Unkown Sources" option in device Settings -> Security
 4. Open APK and Install

## Testing deep links: 
In Android Studio terminal, copy and paste the following instructions depending on the test case:

 

 -  Filter (with content) 

```java
adb shell am start -W -a android.intent.action.VIEW -d "blissrecruitment://questions?question_filter=FILTER" pt.andrew.blisschallenge
```
	 
 - Filter (no content): 
 ```java
adb shell am start -W -a android.intent.action.VIEW -d "blissrecruitment://questions?question_filter=" pt.andrew.blisschallenge
```
 
 - Question detail: 
  ```java
adb shell am start -W -a android.intent.action.VIEW -d "blissrecruitment://questions?question_id=1" pt.andrew.blisschallenge
```

## Author:
Andrew Fernandes. 


	 
