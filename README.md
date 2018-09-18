# Background Timer
This is an Android library implementation of a `background countdown timer`.

It uses the Android service component, running on the *main thread*, to execute tick and finish callbacks.

### Caveat
Do not use this timer for precise timing as there can be a significant delay between the timer firing and the execution of the respective callback due to as the invocations rely on Android Broadcasts.

### Limitation
Only one instance of the timer can be running at a time. This restriction might be lifted in future releases.

### Usage

Gradle:

Root buld.gradle:
```
allprojects {
   	repositories {
   	    ...
   	    maven { url 'https://jitpack.io' }
   	}
}
```

And in your application module:
```
dependencies {
    ...
    implementation 'com.github.charlesmuchene:backgroundtimer:0.0.1'
}
```

### Contributions
---
Contributions, PRs are welcome :D

### License

```
Copyright (C) 2018 Charles Muchene

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```