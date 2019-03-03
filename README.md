# Recast-Kotlin
A lightweight hash map mutation library that can be used in Android, Java, and Kotlin projects
<br/>

## To include this library in your Android or Gradle project

<br/>

**Step 1 - Include the ShoutOut Software libraries repo**

Add the following line in the ```repositories``` block inside the ```allProjects``` block in your project's build.gradle file

```
maven { url "https://dl.bintray.com/shoutoutsoftware/libraries" }
```

<br/>


**Sample**

Here's a sample ```build.gradle``` file that shows where to include the above line in your gradle file. 

<pre>
// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    ext.kotlin_version = '1.1.51'
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath 'com.android.tools.build:gradle:3.1.0-alpha02'
}

allprojects {
    repositories {
        google()
        jcenter()
        <b>maven {   url "https://dl.bintray.com/shoutoutsoftware/libraries" }</b>
    }
}

task clean(type: Delete) {
    delete rootProject.buildDir
}
</pre>

<br/>


**Step 2 - Add the library to your project**

Add the following line in the ```dependencies``` block of your module's ```build.gradle``` file.

```
compile 'com.shoutoutsoftware:recast:0.0.1'
```

<br/>


**Sample**

Here's a sample ```build.gradle``` file that shows where to include the above line in your module's gradle file. 

<pre>
apply plugin: 'com.android.application'
apply plugin: 'kotlin-android'
apply plugin: 'kotlin-android-extensions'

android {
    defaultConfig {
       ...
    }
    buildTypes {
       ...
    }
}

dependencies {
    <b>compile 'com.shoutoutsoftware:recast:0.0.1'</b>
    implementation 'com.android.support:appcompat-v7:26.1.0'
    implementation 'com.android.support.constraint:constraint-layout:1.0.2'
    ...
}
</pre>
