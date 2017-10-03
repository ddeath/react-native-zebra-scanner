import React, { Component } from 'react';
import { NativeModules, View, Text, requireNativeComponent } from 'react-native';

const { ZebraScanner } = NativeModules;

const Scanner = requireNativeComponent('ZebraScanner', Zebra, {});

export default class Zebra extends Component {
  render() {
    return (
      <View>
        <Text>Hey!</Text>
        <Scanner />
      </View>
    );
  }
}
