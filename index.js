import React, { Component } from 'react';
import { NativeModules, View, Text, requireNativeComponent } from 'react-native';

const { ZebraScannerModule } = NativeModules;

const Scanner = requireNativeComponent('ZebraScanner', Zebra, {nativeOnly: {
  testID: true,
  renderToHardwareTextureAndroid: true,
  accessibilityLabel: true,
  importantForAccessibility: true,
  accessibilityLiveRegion: true,
  accessibilityComponentType: true,
  onLayout: true
}});

export default class Zebra extends Component {
  render() {
    return (
      <View style={{ flex: 1, flexDirection: 'row' }}>
        <Scanner style={{ flex: 1, height: 200}}/>
        <Text>Hey!</Text>
      </View>
    );
  }
}
