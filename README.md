
# react-native-zebra-scanner

WIP - only android so far

## Getting started

`$ npm install react-native-zebra-scanner --save`

### Mostly automatic installation

`$ react-native link react-native-zebra-scanner`

### Manual installation

#### Android

1. Open up `android/app/src/main/java/[...]/MainActivity.java`
  - Add `import com.zebrascanner.ZebraScannerPackage;` to the imports at the top of the file
  - Add `new ZebraScannerPackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':react-native-zebra-scanner'
  	project(':react-native-zebra-scanner').projectDir = new File(rootProject.projectDir, 	'../node_modules/react-native-zebra-scanner/android')
  	```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
      compile project(':react-native-zebra-scanner')
  	```

## Usage
```javascript
import Zebra from 'react-native-zebra-scanner';

// TODO: What to do with the module?
Zebra;
```
  