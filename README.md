# Clover Universal SDK 
## Overview
The Clover Universal Android SDK enables your custom mobile point-of-sale (POS) to accept card present transactions by connecting to EMV compliant Clover Go Card Readers.

Clover Go supports two types of card readers; a magnetic stripe, EMV chip-and-signature card reader and an all-in-one card reader that supports Swipe, EMV Dip, and NFC Contactless payments. The SDK is designed to allow merchants to take payments on Android smartphones and Android tablets.  

Integrating with the Universal SDK enables merchants to take advantage of the Clover platform’s low credit card processing fees, as well as all of the other services Clover provides.

# Getting Started for Hackathon Developers

**Android Guide**

***System Requirements:***

-   Android Studio 2.3.3

-   Android 4.2 (API 17) or above on the device

-   A First Data-provided test reader

-   Test cards (you may also use your live cards for certain tests)

***Integration:***

To integrate our Android Universal SDK in your project, you have two
options:

1)  Clone the remote-pay-android-hackathon repository and add a new
    module for your app into the project --- EASIER WAY

    a.  Clone remote-pay-android-hackathon

    b.  Create a new module in the project

    c.  When prompted, select Phone & Tablet Module

    d.  Android Studio should automatically add your new module in the
        settings.gradle file. If not, add the module that you created

    e.  In your module’s build.gradle file, add the following line under
        dependencies

        i.  compile project(':remote-pay-android-connector')

2)  Clone the remote-pay-android-hackathon repository and copy the
    necessary modules into your own android project

    a.  Create or open your own project

    b.  Clone remote-pay-android-hackathon into a separate project

    c.  Using Finder (Finder/Explorer) copy the following folders/module
        into your own project

        i.  clover-aroid-sdk

        ii. clover-remote-interface

        iii. data

        iv. domain

        v.  reader

        vi. remote-pay-android-connector

        vii. roam

    d.  Update your settings.gradle file to include the newly added
        modules. It will look something like the following

        i.  include ':remote-pay-android-connector', ':reader', ':data',
            ':domain', ':roam', ':clover-android-sdk',
            ':clover-remote-interface',
            ':<your\_app\_module\_here>'

    e.  In your project’s build.gradle file under buildscript, make the
        following changes

        buildscript {

        repositories {

        mavenCentral()

        jcenter()

        }

        def mavenPlugin =
        “com.github.dcendents:android-maven-gradle-plugin:1.5”

        dependencies {

        classpath ‘com.android.tools.build:gradle:2.3.3’

        classpath “io.realm:realm-gradle-plugin:3.3.1"

        classpath mavenPlugin

        classpath
        ‘io.codearte.gradle.nexus:gradle-nexus-staging-plugin:0.10.0’

        }

        }

    f.  In your app module’s build.gradle file, add the following line
        under dependencies

        i.  compile project(':remote-pay-android-connector')

***Use Clover UniversalSDK in your app***

Use the following in your app

ICloverGoConnector cloverGo450Connector;

ICloverGoConnectorListener ccGoListener;

***Create and implement** **ICloverGoConnectorListener***

ccGoListener = new new ICloverGoConnectorListener() {

public void…

…

}

*\*\*\*\* Below are the most useful and important \*\*\*\**

ccGoListener = new ICloverGoConnectorListener() {

public void onDeviceDisconnected(ReaderInfo readerInfo) {

}

public void onDeviceConnected() {

}

public void onCloverGoDeviceActivity(final CloverDeviceEvent
deviceEvent) {

switch (deviceEvent.getEventState()) {

case CARD\_SWIPED:

break;

case CARD\_TAPPED:

break;

case CANCEL\_CARD\_READ:

break;

case EMV\_COMPLETE\_DATA:

break;

case CARD\_INSERTED\_MSG:

break;

case CARD\_REMOVED\_MSG:

break;

case PLEASE\_SEE\_PHONE\_MSG:

break;

case READER\_READY:

break;

}

}

public void onDeviceDiscovered(ReaderInfo readerInfo) {

}

public void onAidMatch(final List&lt;CardApplicationIdentifier&gt;
applicationIdentifiers, final AidSelection aidSelection) {

}

public void onDeviceReady(final MerchantInfo merchantInfo) {

}

public void onDeviceError(CloverDeviceErrorEvent deviceErrorEvent) {

switch (deviceErrorEvent.getErrorType()) {

case READER\_ERROR:

case CARD\_ERROR:

case READER\_TIMEOUT:

case COMMUNICATION\_ERROR:

case LOW\_BATTERY:

case PARTIAL\_AUTH\_REJECTED:

case DUPLICATE\_TRANSACTION\_REJECTED:

// notify user

break;

case MULTIPLE\_CONTACT\_LESS\_CARD\_DETECTED\_ERROR:

case CONTACT\_LESS\_FAILED\_TRY\_CONTACT\_ERROR:

case EMV\_CARD\_SWIPED\_ERROR:

case DIP\_FAILED\_ALL\_ATTEMPTS\_ERROR:

case DIP\_FAILED\_ERROR:

case SWIPE\_FAILED\_ERROR:

// show progress to user

break;

}

}

public void onAuthResponse(final AuthResponse response) {

}

public void onPreAuthResponse(final PreAuthResponse response) {

}

public void onTipAdjustAuthResponse(TipAdjustAuthResponse response) {

}

public void onCapturePreAuthResponse(CapturePreAuthResponse response) {

}

public void onConfirmPaymentRequest(ConfirmPaymentRequest request) {

}

public void onSaleResponse(final SaleResponse response) {

}

public void onRefundPaymentResponse(final RefundPaymentResponse
response) {

}

public void onTipAdded(TipAddedMessage message) {

}

public void onVoidPaymentResponse(VoidPaymentResponse response) {

}

};

***Initialize SDK with 450 (Bluetooth) Reader***

Parameters (Required) to initialize SDK:

1.  access Token

2.  environment

3.  api key

4.  secret

5.  app ID

CloverGoDeviceConfiguration config = new
CloverGoDeviceConfiguration.Builder(getApplicationContext(),
accessToken, goEnv, apiKey, secret, appId).
deviceType(ReaderInfo.ReaderType.RP450). allowAutoConnect(false).
build();

ICloverGoConnector cloverGo450Connector = (CloverGoConnector)
ConnectorFactory.createCloverConnector(config);

cloverGo450Connector.addCloverGoConnectorListener(ccGoListener);

cloverGo450Connector.initializeConnection();

***Making Sale transaction***

SaleRequest request = new
SaleRequest(store.getCurrentOrder().getTotal(), externalPaymentID);

request.setCardEntryMethods(store.getCardEntryMethods());

request.setAllowOfflinePayment(store.getAllowOfflinePayment());

request.setForceOfflinePayment(store.getForceOfflinePayment());

request.setApproveOfflinePaymentWithoutPrompt(store.getApproveOfflinePaymentWithoutPrompt());

request.setTippableAmount(store.getCurrentOrder().getTippableAmount());

request.setTaxAmount(store.getCurrentOrder().getTaxAmount());

request.setDisablePrinting(store.getDisablePrinting());

request.setTipMode(store.getTipMode());

request.setSignatureEntryLocation(store.getSignatureEntryLocation());

request.setSignatureThreshold(store.getSignatureThreshold());

request.setDisableReceiptSelection(store.getDisableReceiptOptions());

request.setDisableDuplicateChecking(store.getDisableDuplicateChecking());

request.setTipAmount(store.getTipAmount());

request.setAutoAcceptPaymentConfirmations(store.getAutomaticPaymentConfirmation());

request.setAutoAcceptSignature(store.getAutomaticSignatureConfirmation());

cloverGo450Connector.sale(request);

Required parameters for sale transaction:

1.  amount – which will be total amount you want to make a transaction

2.  externalPaymentID – random unique number for this transaction

Optional parameters:-

1.  allowOfflinePayment

2.  approveOfflinePaymentWithoutPrompt

3.  autoAcceptSignature

4.  autoAcceptPaymentConfirmations

5.  cardEntryMethods

6.  disableCashback

7.  disableDuplicateChecking

8.  disablePrinting

9.  disableReceiptSelection

10. disableRestartTransactionOnFail

11. disableTipOnScreen

12. forceOfflinePayment

13. cardNotPresent

14. tipAmount

15. tippableAmount

16. tipMode

***Making Auth transaction***

AuthRequest request = new
AuthRequest(store.getCurrentOrder().getTotal(), externalPaymentID);

request.setCardEntryMethods(store.getCardEntryMethods());

request.setAllowOfflinePayment(store.getAllowOfflinePayment());

request.setForceOfflinePayment(store.getForceOfflinePayment());

request.setApproveOfflinePaymentWithoutPrompt(store.getApproveOfflinePaymentWithoutPrompt());

request.setTippableAmount(store.getCurrentOrder().getTippableAmount());

request.setTaxAmount(store.getCurrentOrder().getTaxAmount());

request.setDisablePrinting(store.getDisablePrinting());

request.setSignatureEntryLocation(store.getSignatureEntryLocation());

request.setSignatureThreshold(store.getSignatureThreshold());

request.setDisableReceiptSelection(store.getDisableReceiptOptions());

request.setDisableDuplicateChecking(store.getDisableDuplicateChecking());

request.setAutoAcceptPaymentConfirmations(store.getAutomaticPaymentConfirmation());

request.setAutoAcceptSignature(store.getAutomaticSignatureConfirmation());

cloverConnector.auth(request);

Required parameters for auth transaction:

1.  amount – which will be total amount you want to make a transaction

2.  externalPaymentID – random unique number for this transaction

Optional parameters:

1.  allowOfflinePayment

2.  approveOfflinePaymentWithoutPrompt

3.  autoAcceptSignature

4.  autoAcceptPaymentConfirmations

5.  cardEntryMethods

6.  disableCashback

7.  disableDuplicateChecking

8.  disablePrinting

9.  disableReceiptSelection

10. disableRestartTransactionOnFail

11. disableTipOnScreen

12. forceOfflinePayment

13. cardNotPresent

14. tipAmount

15. tippableAmount

16. tipMode

