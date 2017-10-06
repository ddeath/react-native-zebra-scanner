import React, { Component } from 'react';
import {
  DeviceEventEmitter,
  NativeModules,
  requireNativeComponent,
} from 'react-native';

const { ZebraScannerModule } = NativeModules;

const Scanner = requireNativeComponent('ZebraScanner', Zebra, {nativeOnly: {
  onLayout: true
}});

export default class Zebra extends Component {
  componentWillMount() {
    this.addOnCodeReadListener(this.props.onCodeRead);
  }

  componentWillUnmount() {
    this.removeOnCodeReadListener();
  }

  componentWillReceiveProps(newProps) {
    const { onCodeRead } = this.props
    if (onCodeRead !== newProps.onCodeRead) {
      this.addOnCodeReadListener(newProps.onCodeRead);
    }
  }

  addOnCodeReadListener(callback) {
    this.removeOnCodeReadListener();
    if (callback) {
      this.onCodeRead = DeviceEventEmitter.addListener('onCodeReadAndroid', callback);
    }
  }

  removeOnCodeReadListener() {
    if (this.onCodeRead) {
      this.onCodeRead.remove();
      this.onCodeRead = null;
    }
  }

  render() {
    return (
      <Scanner style={{ flex: 1 }}>
        {children ? children : null}
      </Scanner>
    );
  }
}
