import React, { Component } from 'react';
import { NativeModules, View, Text, requireNativeComponent } from 'react-native';

const { ZebraScanner } = NativeModules;

const Scanner = requireNativeComponent('ZebraScanner', Zebra, {});

export default class Zebra extends Component {
  render() {
    return (
      <View style={{ flex: 1, flexDirection: 'row' }}>
        <Scanner />
        <Text>Hey!</Text>
      </View>
    );
  }
}
