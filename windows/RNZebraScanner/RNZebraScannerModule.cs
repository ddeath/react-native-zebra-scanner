using ReactNative.Bridge;
using System;
using System.Collections.Generic;
using Windows.ApplicationModel.Core;
using Windows.UI.Core;

namespace Zebra.Scanner.RNZebraScanner
{
    /// <summary>
    /// A module that allows JS to share data.
    /// </summary>
    class RNZebraScannerModule : NativeModuleBase
    {
        /// <summary>
        /// Instantiates the <see cref="RNZebraScannerModule"/>.
        /// </summary>
        internal RNZebraScannerModule()
        {

        }

        /// <summary>
        /// The name of the native module.
        /// </summary>
        public override string Name
        {
            get
            {
                return "RNZebraScanner";
            }
        }
    }
}
