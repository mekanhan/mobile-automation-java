mobile-automation/
├── src/
│   ├── main/java/
│   │   └── automation/
│   │       ├── pages/
│   │       │   ├── app1/
│   │       │   │   ├── LoginPage.java         # Interface/Abstract
│   │       │   │   ├── AndroidLoginPage.java
│   │       │   │   └── IOSLoginPage.java
│   │       │   └── app2/
│   │       │       ├── HomePage.java
│   │       │       ├── AndroidHomePage.java
│   │       │       └── IOSHomePage.java
│   │       └── utils/
│   │           ├── DriverManager.java
│   │           └── PlatformUtils.java
│   └── test/
│       ├── java/
│       │   └── steps/
│       │       ├── CommonSteps.java
│       │       ├── App1Steps.java
│       │       └── App2Steps.java
│       └── resources/
│           └── features/
│               ├── app1/
│               └── app2/
├── pom.xml
└── README.md
