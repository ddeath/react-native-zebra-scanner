import React, { Component } from 'react';
import { NativeModules, View, Text } from 'react-native';

const { ZebraScanner } = NativeModules;

export default class Zebra extends Component {
  render() {
    return (
      <View>
        <Text>Hey!</Text>
      </View>
    );
  }
}
