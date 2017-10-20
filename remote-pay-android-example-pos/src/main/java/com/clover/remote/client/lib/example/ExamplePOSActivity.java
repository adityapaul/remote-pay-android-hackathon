/*
 * Copyright (C) 2016 Clover Network, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.clover.remote.client.lib.example;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.clover.remote.CardData;
import com.clover.remote.Challenge;
import com.clover.remote.InputOption;
import com.clover.remote.client.CloverDeviceConfiguration;
import com.clover.remote.client.ConnectorFactory;
import com.clover.remote.client.ICloverConnector;
import com.clover.remote.client.ICloverConnectorListener;
import com.clover.remote.client.MerchantInfo;
import com.clover.remote.client.USBCloverDeviceConfiguration;
import com.clover.remote.client.WebSocketCloverDeviceConfiguration;
import com.clover.remote.client.clovergo.CloverGoConnector;
import com.clover.remote.client.clovergo.CloverGoDeviceConfiguration;
import com.clover.remote.client.clovergo.ICloverGoConnector;
import com.clover.remote.client.clovergo.ICloverGoConnectorListener;
import com.clover.remote.client.clovergo.messages.GoPayment;
import com.clover.remote.client.lib.example.messages.ConversationQuestionMessage;
import com.clover.remote.client.lib.example.messages.ConversationResponseMessage;
import com.clover.remote.client.lib.example.messages.CustomerInfo;
import com.clover.remote.client.lib.example.messages.CustomerInfoMessage;
import com.clover.remote.client.lib.example.messages.PayloadMessage;
import com.clover.remote.client.lib.example.messages.PhoneNumberMessage;
import com.clover.remote.client.lib.example.messages.Rating;
import com.clover.remote.client.lib.example.messages.RatingsMessage;
import com.clover.remote.client.lib.example.model.POSCard;
import com.clover.remote.client.lib.example.model.POSDiscount;
import com.clover.remote.client.lib.example.model.POSExchange;
import com.clover.remote.client.lib.example.model.POSItem;
import com.clover.remote.client.lib.example.model.POSNakedRefund;
import com.clover.remote.client.lib.example.model.POSOrder;
import com.clover.remote.client.lib.example.model.POSPayment;
import com.clover.remote.client.lib.example.model.POSRefund;
import com.clover.remote.client.lib.example.model.POSStore;
import com.clover.remote.client.lib.example.utils.CurrencyUtils;
import com.clover.remote.client.lib.example.utils.DialogHelper;
import com.clover.remote.client.lib.example.utils.IdUtils;
import com.clover.remote.client.messages.AuthResponse;
import com.clover.remote.client.messages.CapturePreAuthResponse;
import com.clover.remote.client.messages.CardApplicationIdentifier;
import com.clover.remote.client.messages.CloseoutRequest;
import com.clover.remote.client.messages.CloseoutResponse;
import com.clover.remote.client.messages.CloverDeviceErrorEvent;
import com.clover.remote.client.messages.CloverDeviceEvent;
import com.clover.remote.client.messages.ConfirmPaymentRequest;
import com.clover.remote.client.messages.CustomActivityRequest;
import com.clover.remote.client.messages.CustomActivityResponse;
import com.clover.remote.client.messages.ManualRefundRequest;
import com.clover.remote.client.messages.ManualRefundResponse;
import com.clover.remote.client.messages.MessageFromActivity;
import com.clover.remote.client.messages.MessageToActivity;
import com.clover.remote.client.messages.OpenCashDrawerRequest;
import com.clover.remote.client.messages.PaymentResponse;
import com.clover.remote.client.messages.PreAuthRequest;
import com.clover.remote.client.messages.PreAuthResponse;
import com.clover.remote.client.messages.PrintJobStatusRequest;
import com.clover.remote.client.messages.PrintJobStatusResponse;
import com.clover.remote.client.messages.PrintManualRefundDeclineReceiptMessage;
import com.clover.remote.client.messages.PrintManualRefundReceiptMessage;
import com.clover.remote.client.messages.PrintPaymentDeclineReceiptMessage;
import com.clover.remote.client.messages.PrintPaymentMerchantCopyReceiptMessage;
import com.clover.remote.client.messages.PrintPaymentReceiptMessage;
import com.clover.remote.client.messages.PrintRefundPaymentReceiptMessage;
import com.clover.remote.client.messages.PrintRequest;
import com.clover.remote.client.messages.ReadCardDataRequest;
import com.clover.remote.client.messages.ReadCardDataResponse;
import com.clover.remote.client.messages.RefundPaymentResponse;
import com.clover.remote.client.messages.ResetDeviceResponse;
import com.clover.remote.client.messages.ResultCode;
import com.clover.remote.client.messages.RetrieveDeviceStatusRequest;
import com.clover.remote.client.messages.RetrieveDeviceStatusResponse;
import com.clover.remote.client.messages.RetrievePaymentRequest;
import com.clover.remote.client.messages.RetrievePaymentResponse;
import com.clover.remote.client.messages.RetrievePendingPaymentsResponse;
import com.clover.remote.client.messages.RetrievePrintersRequest;
import com.clover.remote.client.messages.RetrievePrintersResponse;
import com.clover.remote.client.messages.SaleResponse;
import com.clover.remote.client.messages.TipAdjustAuthResponse;
import com.clover.remote.client.messages.VaultCardResponse;
import com.clover.remote.client.messages.VerifySignatureRequest;
import com.clover.remote.client.messages.VoidPaymentResponse;
import com.clover.remote.message.TipAddedMessage;
import com.clover.sdk.v3.payments.Credit;
import com.clover.sdk.v3.payments.Payment;
import com.clover.sdk.v3.printer.Printer;
import com.crashlytics.android.Crashlytics;
import com.firstdata.clovergo.domain.model.ReaderInfo;
import com.google.gson.Gson;

import java.io.InputStream;
import java.net.URI;
import java.net.URLDecoder;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.prefs.Preferences;

import io.fabric.sdk.android.Fabric;

public class ExamplePOSActivity extends Activity implements CurrentOrderFragment.OnFragmentInteractionListener,
        AvailableItem.OnFragmentInteractionListener, OrdersFragment.OnFragmentInteractionListener,
        RegisterFragment.OnFragmentInteractionListener, SignatureFragment.OnFragmentInteractionListener,
        CardsFragment.OnFragmentInteractionListener, ManualRefundsFragment.OnFragmentInteractionListener, MiscellaneousFragment.OnFragmentInteractionListener,
        ProcessingFragment.OnFragmentInteractionListener, PreAuthFragment.OnFragmentInteractionListener, GoSignatureFragment.OnFragmentInteractionListener {

  private static final String TAG = "ExamplePOSActivity";
  public static final String EXAMPLE_POS_SERVER_KEY = "clover_device_endpoint";
  public static final int WS_ENDPOINT_ACTIVITY = 123;
  public static final int SVR_ACTIVITY = 456;
  public static final String EXTRA_CLOVER_CONNECTOR_CONFIG = "EXTRA_CLOVER_CONNECTOR_CONFIG";
  public static final String EXTRA_WS_ENDPOINT = "WS_ENDPOINT";
  public static final String EXTRA_CLEAR_TOKEN = "CLEAR_TOKEN";
  private static final String DEFAULT_EID = "DFLTEMPLYEE";
  private static int RESULT_LOAD_IMG = 1;
  public static List<Printer> printers;
  private Printer printer;
  public static String lastPrintRequestId;
  private int printRequestId = 0;

  // Package name for example custom activities
  public static final String CUSTOM_ACTIVITY_PACKAGE = "com.clover.cfp.examples.";

  private Dialog ratingsDialog;
  private ListView ratingsList;
  private ArrayAdapter<String> ratingsAdapter;

  public static final String EXTRA_CLOVER_GO_CONNECTOR_APP_ID = "EXTRA_CLOVER_GO_CONNECTOR_APP_ID";
  public static final String EXTRA_CLOVER_GO_CONNECTOR_ACCESS_TOKEN = "EXTRA_CLOVER_GO_CONNECTOR_CONFIG_ACCESS_TOKEN";
  public static final String EXTRA_CLOVER_GO_CONNECTOR_API_KEY = "EXTRA_CLOVER_GO_CONNECTOR_CONFIG_API_KEY";
  public static final String EXTRA_CLOVER_GO_CONNECTOR_SECRET = "EXTRA_CLOVER_GO_CONNECTOR_CONFIG_SECRET";
  public static final String EXTRA_CLOVER_GO_CONNECTOR_READER_TYPE = "EXTRA_CLOVER_GO_CONNECTOR_READER_TYPE";
  public static final String EXTRA_CLOVER_GO_CONNECTOR_ENV = "EXTRA_CLOVER_GO_CONNECTOR_ENV";
  private String paymentType;

  Payment currentPayment = null;
  Challenge[] currentChallenges = null;
  PaymentConfirmationListener paymentConfirmationListener = new PaymentConfirmationListener() {
    @Override
    public void onRejectClicked(Challenge challenge) { // Reject payment and send the challenge along for logging/reason
      getCloverConnector().rejectPayment(currentPayment, challenge);
      currentChallenges = null;
      currentPayment = null;
    }

    @Override
    public void onAcceptClicked(final int challengeIndex) {

      if (challengeIndex == currentChallenges.length - 1) { // no more challenges, so accept the payment
        getCloverConnector().acceptPayment(currentPayment);
        currentChallenges = null;
        currentPayment = null;

      } else { // show the next challenge
        runOnUiThread(new Runnable() {
          @Override
          public void run() {

            Challenge theChallenge = currentChallenges[challengeIndex + 1];

            switch(theChallenge.type) {
              case DUPLICATE_CHALLENGE:
                showPaymentConfirmation(paymentConfirmationListener, theChallenge,  challengeIndex + 1);
                break;

            }
          }
        });
      }
    }
  };

  boolean usb = true;

  HashMap<ReaderInfo.ReaderType, ICloverGoConnector> cloverGoConnectorMap = new HashMap<>();
  HashMap<ReaderInfo.ReaderType, MerchantInfo> merchantInfoMap = new HashMap<>();
  ICloverConnector cloverConnector;

  POSStore store = new POSStore();
  private AlertDialog pairingCodeDialog;

  private transient CloverDeviceEvent.DeviceEventState lastDeviceEvent;
  private SharedPreferences sharedPreferences;

  private ICloverConnectorListener ccListener;
  private ICloverGoConnectorListener ccGoListener;
  private String apiKey;
  private String secret;
  private String accessToken;
  private String appId;
  private CloverGoDeviceConfiguration.ENV goEnv;

  private ProgressDialog progressDialog;
  private Dialog alertDialog;

  private ArrayList<ReaderInfo> mArrayListReadersList;
  private ArrayList<String> mArrayListReaderString;
  private ArrayAdapter<String> mReaderArrayAdapter;
  private ReaderInfo.ReaderType currentGoConfig;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Fabric.with(this, new Crashlytics());
    setContentView(R.layout.activity_example_pos);

    if (null != getActionBar()) {
      getActionBar().hide();
    }

    sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    initStore();

    String posName = "Clover Example POS";
    String applicationId = posName + ":1.4";
    CloverDeviceConfiguration config;
    CloverGoDeviceConfiguration goConfig;

    String configType = getIntent().getStringExtra(EXTRA_CLOVER_CONNECTOR_CONFIG);

    if ("GO".equals(configType)) {
      findViewById(R.id.RefundButton).setVisibility(View.GONE);
      findViewById(R.id.CardsButton).setVisibility(View.GONE);
      findViewById(R.id.PendingButton).setVisibility(View.GONE);

      apiKey = getIntent().getStringExtra(EXTRA_CLOVER_GO_CONNECTOR_API_KEY);
      secret = getIntent().getStringExtra(EXTRA_CLOVER_GO_CONNECTOR_SECRET);
      accessToken = getIntent().getStringExtra(EXTRA_CLOVER_GO_CONNECTOR_ACCESS_TOKEN);
      appId = getIntent().getStringExtra(EXTRA_CLOVER_GO_CONNECTOR_APP_ID);
      goEnv = (CloverGoDeviceConfiguration.ENV) getIntent().getSerializableExtra(EXTRA_CLOVER_GO_CONNECTOR_ENV);
      currentGoConfig = (ReaderInfo.ReaderType) getIntent().getExtras().get(EXTRA_CLOVER_GO_CONNECTOR_READER_TYPE);

      final Spinner paymentTypeSpinner = (Spinner) findViewById(R.id.selectPaymentSpinner);
      paymentTypeSpinner.setVisibility(View.VISIBLE);

      paymentTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
          switch (paymentTypeSpinner.getSelectedItem().toString()) {
            case "RP450":
              paymentType = "";
              currentGoConfig = ReaderInfo.ReaderType.RP450;
              if (cloverGoConnectorMap.get(ReaderInfo.ReaderType.RP450) == null) {
                CloverGoDeviceConfiguration config = new CloverGoDeviceConfiguration.Builder(getApplicationContext(), accessToken, goEnv, apiKey, secret, appId).deviceType(ReaderInfo.ReaderType.RP450).allowAutoConnect(false).build();
                ICloverGoConnector cloverGo450Connector = (CloverGoConnector) ConnectorFactory.createCloverConnector(config);
                cloverGoConnectorMap.put(ReaderInfo.ReaderType.RP450, cloverGo450Connector);
                cloverGo450Connector.addCloverGoConnectorListener(ccGoListener);
              }
              if (merchantInfoMap.get(ReaderInfo.ReaderType.RP450) == null) {
                ((TextView) findViewById(R.id.ConnectionStatusLabel)).setText("Disconnected");
              } else {
                MerchantInfo merchantInfo = merchantInfoMap.get(ReaderInfo.ReaderType.RP450);
                ((TextView) findViewById(R.id.ConnectionStatusLabel)).setText(String.format(merchantInfo.getDeviceInfo().getModel() + " Connected: %s (%s)", merchantInfo.getDeviceInfo().getSerial(), merchantInfo.getMerchantName()));
              }
              break;
            case "RP350":
              paymentType = "";
              currentGoConfig = ReaderInfo.ReaderType.RP350;
              if (cloverGoConnectorMap.get(ReaderInfo.ReaderType.RP350) == null) {
                CloverGoDeviceConfiguration config = new CloverGoDeviceConfiguration.Builder(getApplicationContext(), accessToken, goEnv, apiKey, secret, appId).deviceType(ReaderInfo.ReaderType.RP350).allowAutoConnect(false).build();
                ICloverGoConnector cloverGo350Connector = (CloverGoConnector) ConnectorFactory.createCloverConnector(config);
                cloverGoConnectorMap.put(ReaderInfo.ReaderType.RP350, cloverGo350Connector);
                cloverGo350Connector.addCloverGoConnectorListener(ccGoListener);
              }

              if (merchantInfoMap.get(ReaderInfo.ReaderType.RP350) == null) {
                ((TextView) findViewById(R.id.ConnectionStatusLabel)).setText("Disconnected");
              } else {
                MerchantInfo merchantInfo = merchantInfoMap.get(ReaderInfo.ReaderType.RP350);
                ((TextView) findViewById(R.id.ConnectionStatusLabel)).setText(String.format(merchantInfo.getDeviceInfo().getModel() + " Connected: %s (%s)", merchantInfo.getDeviceInfo().getSerial(), merchantInfo.getMerchantName()));
              }
              break;
            case "KEYED":
              paymentType = "KEYED";
              break;
          }
          updateComponentsWithNewCloverConnector();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
      });

      goConfig = new CloverGoDeviceConfiguration.Builder(getApplicationContext(), accessToken, goEnv, apiKey, secret, appId).deviceType(currentGoConfig).allowAutoConnect(false).build();

      ICloverGoConnector cloverGoConnector = (CloverGoConnector) ConnectorFactory.createCloverConnector(goConfig);
      cloverGoConnectorMap.put(currentGoConfig, cloverGoConnector);

    } else if ("USB".equals(configType)) {
      config = new USBCloverDeviceConfiguration(this, applicationId);
    } else if ("WS".equals(configType)) {

      String serialNumber = "Aisle 3";
      String authToken = null;

      URI uri = (URI) getIntent().getSerializableExtra(EXTRA_WS_ENDPOINT);

      String query = uri.getRawQuery();
      if (query != null) {
        try {
          String[] nameValuePairs = query.split("&");
          for (String nameValuePair : nameValuePairs) {
            String[] nameAndValue = nameValuePair.split("=", 2);
            String name = URLDecoder.decode(nameAndValue[0], "UTF-8");
            String value = URLDecoder.decode(nameAndValue[1], "UTF-8");

            if ("authenticationToken".equals(name)) {
              authToken = value;
            } else {
              Log.w(TAG, String.format("Found query parameter \"%s\" with value \"%s\"",
                      name, value));
            }
          }
          uri = new URI(uri.getScheme(), uri.getUserInfo(), uri.getHost(), uri.getPort(), uri.getPath(), null, uri.getFragment());
        } catch (Exception e) {
          Log.e(TAG, "Error extracting query information from uri.", e);
          setResult(RESULT_CANCELED);
          finish();
          return;
        }
      }
      KeyStore trustStore = createTrustStore();

      if (authToken == null) {
        boolean clearToken = getIntent().getBooleanExtra(EXTRA_CLEAR_TOKEN, false);
        if (!clearToken) {
          authToken = sharedPreferences.getString("AUTH_TOKEN", null);
        }
      }
      config = new WebSocketCloverDeviceConfiguration(uri, applicationId, trustStore, posName, serialNumber, authToken) {
        @Override
        public int getMaxMessageCharacters() {
          return 0;
        }

        @Override
        public void onPairingCode(final String pairingCode) {
          runOnUiThread(new Runnable() {
            @Override
            public void run() {
              // If we previously created a dialog and the pairing failed, reuse
              // the dialog previously created so that we don't get a stack of dialogs
              if (pairingCodeDialog != null) {
                pairingCodeDialog.setMessage("Enter pairing code: " + pairingCode);
              } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(ExamplePOSActivity.this);
                builder.setTitle("Pairing Code");
                builder.setMessage("Enter pairing code: " + pairingCode);
                pairingCodeDialog = builder.create();
              }
              pairingCodeDialog.show();
            }
          });
        }

        @Override
        public void onPairingSuccess(String authToken) {
          Preferences.userNodeForPackage(ExamplePOSActivity.class).put("AUTH_TOKEN", authToken);
          sharedPreferences.edit().putString("AUTH_TOKEN", authToken).apply();
          runOnUiThread(new Runnable() {
            @Override
            public void run() {
              if (pairingCodeDialog != null && pairingCodeDialog.isShowing()) {
                pairingCodeDialog.dismiss();
                pairingCodeDialog = null;
              }
            }
          });
        }
      };
      cloverConnector = ConnectorFactory.createCloverConnector(config);
    } else {
      finish();
      return;
    }

    initialize();

    FrameLayout frameLayout = (FrameLayout) findViewById(R.id.contentContainer);

    FragmentManager fragmentManager = getFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

    RegisterFragment register = RegisterFragment.newInstance(store, getCloverConnector());

    fragmentTransaction.add(R.id.contentContainer, register, "REGISTER");
    fragmentTransaction.commit();

    ratingsDialog = new Dialog(ExamplePOSActivity.this);
    ratingsDialog.setContentView(R.layout.finalratings_layout);
    ratingsDialog.setCancelable(true);
    ratingsDialog.setCanceledOnTouchOutside(true);
    ratingsList = (ListView) ratingsDialog.findViewById(R.id.ratingsList);
    ratingsAdapter = new ArrayAdapter<>(ExamplePOSActivity.this, android.R.layout.simple_list_item_1, new String[0]);

  }

  public List<Printer> getPrinters() {
    return this.printers;
  }


  private KeyStore createTrustStore() {
    try {

      String STORETYPE = "PKCS12";
      KeyStore trustStore = KeyStore.getInstance(STORETYPE);
      InputStream trustStoreStream = getClass().getResourceAsStream("/certs/clover_cacerts.p12");
      String TRUST_STORE_PASSWORD = "clover";

      trustStore.load(trustStoreStream, TRUST_STORE_PASSWORD.toCharArray());

      return trustStore;
    } catch (Throwable t) {
      Log.e(getClass().getSimpleName(), "Error loading trust store", t);
      t.printStackTrace();
      return null;
    }

  }

  private void initStore() {
    // initialize store...
    store.addAvailableItem(new POSItem("0", "Chicken Nuggets", 539, true, true));
    store.addAvailableItem(new POSItem("1", "Hamburger", 699, true, true));
    store.addAvailableItem(new POSItem("2", "Cheeseburger", 759, true, true));
    store.addAvailableItem(new POSItem("3", "Double Hamburger", 819, true, true));
    store.addAvailableItem(new POSItem("4", "Double Cheeseburger", 899, true, true));
    store.addAvailableItem(new POSItem("5", "Bacon Cheeseburger", 999, true, true));
    store.addAvailableItem(new POSItem("6", "Small French Fries", 239, true, true));
    store.addAvailableItem(new POSItem("7", "Medium French Fries", 259, true, true));
    store.addAvailableItem(new POSItem("8", "Large French Fries", 279, true, true));
    store.addAvailableItem(new POSItem("9", "Small Fountain Drink", 169, true, true));
    store.addAvailableItem(new POSItem("10", "Medium Fountain Drink", 189, true, true));
    store.addAvailableItem(new POSItem("11", "Large Fountain Drink", 229, true, true));
    store.addAvailableItem(new POSItem("12", "Chocolate Milkshake", 449, true, true));
    store.addAvailableItem(new POSItem("13", "Vanilla Milkshake", 419, true, true));
    store.addAvailableItem(new POSItem("14", "Strawberry Milkshake", 439, true, true));
    store.addAvailableItem(new POSItem("15", "Ice Cream Cone", 189, true, true));
    store.addAvailableItem(new POSItem("16", "$25 Gift Card", 2500, false, false));
    store.addAvailableItem(new POSItem("17", "$50 Gift Card", 5000, false, false));

    store.addAvailableDiscount(new POSDiscount("10% Off", 0.1f));
    store.addAvailableDiscount(new POSDiscount("$5 Off", 500));
    store.addAvailableDiscount(new POSDiscount("None", 0));

    store.createOrder(false);
    // Per Transaction Settings defaults
    //store.setTipMode(SaleRequest.TipMode.ON_SCREEN_BEFORE_PAYMENT);
    //store.setSignatureEntryLocation(DataEntryLocation.ON_PAPER);
    //store.setDisablePrinting(false);
    //store.setDisableReceiptOptions(false);
    //store.setDisableDuplicateChecking(false);
    //store.setAllowOfflinePayment(false);
    //store.setForceOfflinePayment(false);
    //store.setApproveOfflinePaymentWithoutPrompt(true);
    //store.setAutomaticSignatureConfirmation(true);
    //store.setAutomaticPaymentConfirmation(true);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == WS_ENDPOINT_ACTIVITY) {
      if (!usb) {
        initialize();
      }
    }
    if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
            && null != data) {
      // Get the Image from data

      Uri selectedImage = data.getData();
      String[] filePathColumn = {MediaStore.Images.Media.DATA};

      // Get the cursor
      Cursor cursor = getContentResolver().query(selectedImage,
              filePathColumn, null, null, null);
      // Move to first row
      cursor.moveToFirst();

      int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
      String imgDecodableString = cursor.getString(columnIndex);
      printImage(imgDecodableString);
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_parent, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    int id = item.getItemId();

    if (id == R.id.action_settings) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  public void onClickCancel(View view) {
    getCloverConnector().cancel();
  }


  public void initialize() {

    if (getCloverConnector() != null) {
      getCloverConnector().dispose();
    }


    ICloverConnectorListener ccListener = new ICloverConnectorListener() {
      public void onDeviceDisconnected() {
        runOnUiThread(new Runnable() {
          @Override
          public void run() {
            Toast.makeText(ExamplePOSActivity.this, "Disconnected", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "disconnected");
            ((TextView) findViewById(R.id.ConnectionStatusLabel)).setText("Disconnected");
          }
        });

      }

      public void onDeviceConnected() {

        runOnUiThread(new Runnable() {
          @Override
          public void run() {
            showMessage("Connecting...", Toast.LENGTH_SHORT);
            ((TextView) findViewById(R.id.ConnectionStatusLabel)).setText("Connecting");
          }
        });
      }

      public void onDeviceReady(final MerchantInfo merchantInfo) {
        runOnUiThread(new Runnable() {
          public void run() {
            if (pairingCodeDialog != null && pairingCodeDialog.isShowing()) {
              pairingCodeDialog.dismiss();
              pairingCodeDialog = null;
            }
            showMessage("Ready!", Toast.LENGTH_SHORT);
            ((TextView) findViewById(R.id.ConnectionStatusLabel)).setText(String.format("Connected: %s (%s)", merchantInfo.getDeviceInfo().getSerial(), merchantInfo.getMerchantName()));
          }
        });
        RetrievePrintersRequest rpr = new RetrievePrintersRequest();
        getCloverConnector().retrievePrinters(rpr);
      }

      public void onError(final Exception e) {
        runOnUiThread(new Runnable() {
          @Override
          public void run() {
            showMessage("Error: " + e.getMessage(), Toast.LENGTH_LONG);
          }
        });
      }

      public void onDebug(final String s) {
        runOnUiThread(new Runnable() {
          @Override
          public void run() {
            showMessage("Debug: " + s, Toast.LENGTH_LONG);
          }
        });
      }

      @Override
      public void onDeviceActivityStart(final CloverDeviceEvent deviceEvent) {

        lastDeviceEvent = deviceEvent.getEventState();
        runOnUiThread(new Runnable() {
          @Override
          public void run() {
            ((TextView) findViewById(R.id.DeviceStatus)).setText(deviceEvent.getMessage());
            Toast.makeText(ExamplePOSActivity.this, deviceEvent.getMessage(), Toast.LENGTH_SHORT).show();
            LinearLayout ll = (LinearLayout) findViewById(R.id.DeviceOptionsPanel);
            ll.removeAllViews();

            for (final InputOption io : deviceEvent.getInputOptions()) {
              Button btn = new Button(ExamplePOSActivity.this);
              btn.setText(io.description);
              btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  getCloverConnector().invokeInputOption(io);
                }
              });
              ll.addView(btn);
            }
          }
        });
      }

      @Override
      public void onReadCardDataResponse(final ReadCardDataResponse response) {
        runOnUiThread(new Runnable() {
          @Override
          public void run() {
            AlertDialog.Builder builder = new AlertDialog.Builder(ExamplePOSActivity.this);
            builder.setTitle("Read Card Data Response");
            if (response.isSuccess()) {

              LayoutInflater inflater = ExamplePOSActivity.this.getLayoutInflater();

              View view = inflater.inflate(R.layout.card_data_table, null);
              ListView listView = (ListView) view.findViewById(R.id.cardDataListView);


              if (listView != null) {
                class RowData {
                  RowData(String label, String value) {
                    this.text1 = label;
                    this.text2 = value;
                  }

                  String text1;
                  String text2;
                }

                ArrayAdapter<RowData> data = new ArrayAdapter<RowData>(getBaseContext(), android.R.layout.simple_list_item_2) {
                  @Override
                  public View getView(int position, View convertView, ViewGroup parent) {
                    View v = convertView;

                    if (v == null) {
                      LayoutInflater vi;
                      vi = LayoutInflater.from(getContext());
                      v = vi.inflate(android.R.layout.simple_list_item_2, null);
                    }

                    RowData rowData = getItem(position);

                    if (rowData != null) {
                      TextView primaryColumn = (TextView) v.findViewById(android.R.id.text1);
                      TextView secondaryColumn = (TextView) v.findViewById(android.R.id.text2);

                      primaryColumn.setText(rowData.text2);
                      secondaryColumn.setText(rowData.text1);
                    }

                    return v;
                  }
                };
                listView.setAdapter(data);
                CardData cardData = response.getCardData();
                data.addAll(new RowData("Encrypted", cardData.encrypted + ""));
                data.addAll(new RowData("Cardholder Name", cardData.cardholderName));
                data.addAll(new RowData("First Name", cardData.firstName));
                data.addAll(new RowData("Last Name", cardData.lastName));
                data.addAll(new RowData("Expiration", cardData.exp));
                data.addAll(new RowData("First 6", cardData.first6));
                data.addAll(new RowData("Last 4", cardData.last4));
                data.addAll(new RowData("Track 1", cardData.track1));
                data.addAll(new RowData("Track 2", cardData.track2));
                data.addAll(new RowData("Track 3", cardData.track3));
                data.addAll(new RowData("Masked Track 1", cardData.maskedTrack1));
                data.addAll(new RowData("Masked Track 2", cardData.maskedTrack2));
                data.addAll(new RowData("Masked Track 3", cardData.maskedTrack3));
                data.addAll(new RowData("Pan", cardData.pan));

              }
              builder.setView(view);

            } else if (response.getResult() == ResultCode.CANCEL) {
              builder.setMessage("Get card data canceled.");
            } else {
              builder.setMessage("Error getting card data. " + response.getReason() + ": " + response.getMessage());
            }

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
              @Override
              public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
              }
            });
            AlertDialog dialog = builder.create();
            dialog.show();

          }
        });
      }

      @Override
      public void onDeviceActivityEnd(final CloverDeviceEvent deviceEvent) {
        if (deviceEvent.getEventState() == lastDeviceEvent) {
          runOnUiThread(new Runnable() {
            @Override
            public void run() {
              ((TextView) findViewById(R.id.DeviceStatus)).setText("");
              LinearLayout ll = (LinearLayout) findViewById(R.id.DeviceOptionsPanel);
              ll.removeAllViews();
            }
          });
        }
      }

      @Override
      public void onDeviceError(CloverDeviceErrorEvent deviceErrorEvent) {
        showMessage("DeviceError: " + deviceErrorEvent.getMessage(), Toast.LENGTH_LONG);
      }

      @Override
      public void onAuthResponse(final AuthResponse response) {
        if (response.isSuccess()) {
          runOnUiThread(new Runnable() {
            @Override
            public void run() {
              Payment _payment = response.getPayment();
              if (_payment.getExternalPaymentId().equals(store.getCurrentOrder().getPendingPaymentId())) {
                long cashback = _payment.getCashbackAmount() == null ? 0 : _payment.getCashbackAmount();
                long tip = _payment.getTipAmount() == null ? 0 : _payment.getTipAmount();
                POSPayment payment = new POSPayment(_payment.getId(), _payment.getExternalPaymentId(), _payment.getOrder().getId(), DEFAULT_EID, _payment.getAmount(), tip, cashback);
                setPaymentStatus(payment, response);
                store.addPaymentToOrder(payment, store.getCurrentOrder());
                showMessage("Auth successfully processed.", Toast.LENGTH_SHORT);

                store.createOrder(false);
                CurrentOrderFragment currentOrderFragment = (CurrentOrderFragment) getFragmentManager().findFragmentById(R.id.PendingOrder);
                currentOrderFragment.setOrder(store.getCurrentOrder());

                showRegister(null);
                SystemClock.sleep(3000);
                getCloverConnector().showWelcomeScreen();
              } else {
                externalMismatch();
              }
            }
          });
        } else {
          showMessage("Auth error:" + response.getResult(), Toast.LENGTH_LONG);
          getCloverConnector().showMessage("There was a problem processing the transaction");
          SystemClock.sleep(3000);
        }
      }

      @Override
      public void onPreAuthResponse(final PreAuthResponse response) {
        runOnUiThread(new Runnable() {
          @Override
          public void run() {
            if (response.isSuccess()) {
              Payment _payment = response.getPayment();
              if (_payment.getExternalPaymentId().equals(store.getCurrentOrder().getPendingPaymentId())) {
                long cashback = _payment.getCashbackAmount() == null ? 0 : _payment.getCashbackAmount();
                long tip = _payment.getTipAmount() == null ? 0 : _payment.getTipAmount();
                POSPayment payment = new POSPayment(_payment.getId(), _payment.getExternalPaymentId(), _payment.getOrder().getId(), DEFAULT_EID, _payment.getAmount(), tip, cashback);
                setPaymentStatus(payment, response);
                store.addPreAuth(payment);
                showMessage("PreAuth successfully processed.", Toast.LENGTH_SHORT);
                showPreAuths(null);
              } else {
                externalMismatch();
              }
            } else {
              showMessage("PreAuth: " + response.getResult(), Toast.LENGTH_LONG);
            }
          }
        });
        SystemClock.sleep(3000);
        getCloverConnector().showWelcomeScreen();
      }

      @Override
      public void onRetrievePendingPaymentsResponse(RetrievePendingPaymentsResponse response) {
        if (!response.isSuccess()) {
          store.setPendingPayments(null);
          showMessage("Retrieve Pending Payments: " + response.getResult(), Toast.LENGTH_LONG);
        } else {
          store.setPendingPayments(response.getPendingPayments());
        }
      }

      @Override
      public void onTipAdjustAuthResponse(TipAdjustAuthResponse response) {
        if (response.isSuccess()) {

          boolean updatedTip = false;
          for (POSOrder order : store.getOrders()) {
            for (POSExchange exchange : order.getPayments()) {
              if (exchange instanceof POSPayment) {
                POSPayment posPayment = (POSPayment) exchange;
                if (exchange.getPaymentID().equals(response.getPaymentId())) {
                  posPayment.setTipAmount(response.getTipAmount());
                  updatedTip = true;
                  break;
                }
              }
            }
            if (updatedTip) {
              showMessage("Tip successfully adjusted", Toast.LENGTH_LONG);
              break;
            }
          }
        } else {
          showMessage("Tip adjust failed", Toast.LENGTH_LONG);
        }
      }

      @Override
      public void onCapturePreAuthResponse(CapturePreAuthResponse response) {

        if (response.isSuccess()) {
          for (final POSPayment payment : store.getPreAuths()) {
            if (payment.getPaymentID().equals(response.getPaymentID())) {
              final long paymentAmount = response.getAmount();
              runOnUiThread(new Runnable() {
                @Override
                public void run() {
                  store.removePreAuth(payment);
                  store.addPaymentToOrder(payment, store.getCurrentOrder());
                  payment.setPaymentStatus(POSPayment.Status.AUTHORIZED);
                  payment.amount = paymentAmount;
                  showMessage("Sale successfully processing using Pre Authorization", Toast.LENGTH_LONG);

                  store.createOrder(false);
                  CurrentOrderFragment currentOrderFragment = (CurrentOrderFragment) getFragmentManager().findFragmentById(R.id.PendingOrder);
                  currentOrderFragment.setOrder(store.getCurrentOrder());
                  showRegister(null);
                }
              });
              break;
            } else {
              showMessage("PreAuth Capture: Payment received does not match any of the stored PreAuth records", Toast.LENGTH_LONG);
            }
          }
        } else {
          showMessage("PreAuth Capture Error: Payment failed with response code = " + response.getResult() + " and reason: " + response.getReason(), Toast.LENGTH_LONG);
        }
      }

      @Override
      public void onVerifySignatureRequest(VerifySignatureRequest request) {

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        hideFragments(fragmentManager, fragmentTransaction);

        Fragment fragment = fragmentManager.findFragmentByTag("SIGNATURE");
        if (fragment == null) {
          fragment = SignatureFragment.newInstance(request, getCloverConnector());
          fragmentTransaction.add(R.id.contentContainer, fragment, "SIGNATURE");
        } else {
          ((SignatureFragment) fragment).setVerifySignatureRequest(request);
          fragmentTransaction.show(fragment);
        }

        fragmentTransaction.commit();
      }

      @Override
      public void onMessageFromActivity(MessageFromActivity message) {
        //showMessage("Custom Activity Message Received for actionId: " + message.actionId + " with payload: " + message.payload, Toast.LENGTH_LONG);
        PayloadMessage payloadMessage = new Gson().fromJson(message.getPayload(), PayloadMessage.class);
        switch (payloadMessage.messageType) {
          case REQUEST_RATINGS:
            handleRequestRatings();
            break;
          case RATINGS:
            handleRatings(message.getPayload());
            break;
          case PHONE_NUMBER:
            handleCustomerLookup(message.getPayload());
            break;
          case CONVERSATION_RESPONSE:
            handleJokeResponse(message.getPayload());
            break;
          default:
            Toast.makeText(getApplicationContext(), R.string.unknown_payload + payloadMessage.messageType.name(), Toast.LENGTH_LONG).show();
        }
      }

      @Override
      public void onConfirmPaymentRequest(ConfirmPaymentRequest request) {
        if (request.getPayment() == null || request.getChallenges() == null) {
          showMessage("Error: The ConfirmPaymentRequest was missing the payment and/or challenges.", Toast.LENGTH_LONG);
        } else {
          currentPayment = request.getPayment();
          currentChallenges = request.getChallenges();
          runOnUiThread(new Runnable() {
            @Override
            public void run() {
              showPaymentConfirmation(paymentConfirmationListener, currentChallenges[0], 0);
            }
          });
        }
      }

      @Override
      public void onCloseoutResponse(CloseoutResponse response) {
        if (response.isSuccess()) {
          showMessage("Closeout is scheduled.", Toast.LENGTH_SHORT);
        } else {
          showMessage("Error scheduling closeout: " + response.getResult(), Toast.LENGTH_LONG);
        }
      }

      @Override
      public void onSaleResponse(final SaleResponse response) {
        if (response != null) {
          if (response.isSuccess()) { // Handle cancel response
            if (response.getPayment() != null) {
              Payment _payment = response.getPayment();
              Log.d(TAG, "payment external: " + _payment.getExternalPaymentId());
              if (_payment.getExternalPaymentId().equals(store.getCurrentOrder().getPendingPaymentId())) {
                POSPayment payment = new POSPayment(_payment.getId(), _payment.getExternalPaymentId(), _payment.getOrder().getId(), "DFLTEMPLYEE", _payment.getAmount(), _payment.getTipAmount() != null ? _payment.getTipAmount() : 0, _payment.getCashbackAmount() != null ? _payment.getCashbackAmount() : 0);
                setPaymentStatus(payment, response);

                store.addPaymentToOrder(payment, store.getCurrentOrder());
                showMessage("Sale successfully processed", Toast.LENGTH_SHORT);
                runOnUiThread(new Runnable() {
                  @Override
                  public void run() {
                    store.createOrder(false);
                    CurrentOrderFragment currentOrderFragment = (CurrentOrderFragment) getFragmentManager().findFragmentById(R.id.PendingOrder);
                    currentOrderFragment.setOrder(store.getCurrentOrder());
                    showRegister(null);
                  }
                });
              } else {
                externalMismatch();
              }
            } else { // Handle null payment
              showMessage("Error: Sale response was missing the payment", Toast.LENGTH_LONG);
            }
          } else {
            showMessage(response.getResult().toString() + ":" + response.getReason() + "  " + response.getMessage(), Toast.LENGTH_LONG);
          }
        } else { //Handle null payment response
          showMessage("Error: Null SaleResponse", Toast.LENGTH_LONG);
        }
        SystemClock.sleep(3000);
        getCloverConnector().showWelcomeScreen();
      }

      @Override
      public void onManualRefundResponse(final ManualRefundResponse response) {
        if (response.isSuccess()) {
          Credit credit = response.getCredit();
          final POSNakedRefund nakedRefund = new POSNakedRefund(null, credit.getAmount());
          runOnUiThread(new Runnable() {
            @Override
            public void run() {
              store.addRefund(nakedRefund);
              showMessage("Manual Refund successfully processed", Toast.LENGTH_SHORT);
            }
          });
        } else if (response.getResult() == ResultCode.CANCEL) {
          showMessage("User canceled the Manual Refund", Toast.LENGTH_SHORT);
        } else {
          showMessage("Manual Refund Failed with code: " + response.getResult() + " - " + response.getMessage(), Toast.LENGTH_LONG);
        }
      }

      @Override
      public void onRefundPaymentResponse(final RefundPaymentResponse response) {
        if (response.isSuccess()) {
          POSRefund refund = new POSRefund(response.getRefund().getId(), response.getPaymentId(), response.getOrderId(), "DEFAULT", response.getRefund().getAmount());
          boolean done = false;
          for (POSOrder order : store.getOrders()) {
            for (POSExchange payment : order.getPayments()) {
              if (payment instanceof POSPayment) {
                if (payment.getPaymentID().equals(response.getRefund().getPayment().getId())) {
                  ((POSPayment) payment).setPaymentStatus(POSPayment.Status.REFUNDED);
                  store.addRefundToOrder(refund, order);
                  showMessage("Payment successfully refunded", Toast.LENGTH_SHORT);
                  done = true;
                  break;
                }
              }
            }
            if (done) {
              break;
            }
          }
        } else {
          runOnUiThread(new Runnable() {
            @Override
            public void run() {
              AlertDialog.Builder builder = new AlertDialog.Builder(ExamplePOSActivity.this);
              builder.setTitle("Refund Error").setMessage("There was an error refunding the payment");
              builder.create().show();
              Log.d(getClass().getName(), "Got refund response of " + response.getReason());
            }
          });
        }
      }


      @Override
      public void onTipAdded(TipAddedMessage message) {
        if (message.tipAmount > 0) {
          showMessage("Tip successfully added: " + CurrencyUtils.format(message.tipAmount, Locale.getDefault()), Toast.LENGTH_SHORT);
        }
      }

      @Override
      public void onVoidPaymentResponse(VoidPaymentResponse response) {
        if (response.isSuccess()) {
          boolean done = false;
          for (POSOrder order : store.getOrders()) {
            for (POSExchange payment : order.getPayments()) {
              if (payment instanceof POSPayment) {
                if (payment.getPaymentID().equals(response.getPaymentId())) {
                  ((POSPayment) payment).setPaymentStatus(POSPayment.Status.VOIDED);
                  showMessage("Payment was voided", Toast.LENGTH_SHORT);
                  done = true;
                  break;
                }
              }
            }
            if (done) {
              break;
            }
          }
        } else {
          showMessage(getClass().getName() + ":Got VoidPaymentResponse of " + response.getResult(), Toast.LENGTH_LONG);
        }
      }

      @Override
      public void onVaultCardResponse(final VaultCardResponse response) {
        if (response.isSuccess()) {
          POSCard card = new POSCard();
          card.setFirst6(response.getCard().getFirst6());
          card.setLast4(response.getCard().getLast4());
          card.setName(response.getCard().getCardholderName());
          card.setMonth(response.getCard().getExpirationDate().substring(0, 2));
          card.setYear(response.getCard().getExpirationDate().substring(2, 4));
          card.setToken(response.getCard().getToken());
          store.addCard(card);
          showMessage("Card successfully vaulted", Toast.LENGTH_SHORT);
        } else {
          if (response.getResult() == ResultCode.CANCEL) {
            showMessage("User canceled the operation", Toast.LENGTH_SHORT);
            getCloverConnector().showWelcomeScreen();
          } else {
            showMessage("Error capturing card: " + response.getResult(), Toast.LENGTH_LONG);
            getCloverConnector().showMessage("Card was not saved");
            SystemClock.sleep(4000); //wait 4 seconds
            getCloverConnector().showWelcomeScreen();
          }
        }
      }

      @Override
      public void onPrintJobStatusResponse(PrintJobStatusResponse response) {
        showMessage("PrintJobStatus: " + response.getStatus(), Toast.LENGTH_SHORT);
      }

      @Override
      public void onRetrievePrintersResponse(RetrievePrintersResponse response) {
        printers = response.getPrinters();
      }

      @Override
      public void onPrintManualRefundReceipt(PrintManualRefundReceiptMessage pcm) {
        showMessage("Print Request for ManualRefund", Toast.LENGTH_SHORT);
      }

      @Override
      public void onPrintManualRefundDeclineReceipt(PrintManualRefundDeclineReceiptMessage pcdrm) {
        showMessage("Print Request for Declined ManualRefund", Toast.LENGTH_SHORT);
      }

      @Override
      public void onPrintPaymentReceipt(PrintPaymentReceiptMessage pprm) {
        showMessage("Print Request for Payment Receipt", Toast.LENGTH_SHORT);
      }

      @Override
      public void onPrintPaymentDeclineReceipt(PrintPaymentDeclineReceiptMessage ppdrm) {
        showMessage("Print Request for DeclinedPayment Receipt", Toast.LENGTH_SHORT);
      }

      @Override
      public void onPrintPaymentMerchantCopyReceipt(PrintPaymentMerchantCopyReceiptMessage ppmcrm) {
        showMessage("Print Request for MerchantCopy of a Payment Receipt", Toast.LENGTH_SHORT);
      }

      @Override
      public void onPrintRefundPaymentReceipt(PrintRefundPaymentReceiptMessage pprrm) {
        showMessage("Print Request for RefundPayment Receipt", Toast.LENGTH_SHORT);
      }

      @Override
      public void onCustomActivityResponse(CustomActivityResponse response) {
        boolean success = response.isSuccess();
        if (success) {
          showMessage("Success! Got: " + response.getPayload() + " from CustomActivity: " + response.getAction(), 5000);
        } else {
          if (response.getResult().equals(ResultCode.CANCEL)) {
            showMessage("Custom activity: " + response.getAction() + " was canceled.  Reason: " + response.getReason(), 5000);
          } else {
            showMessage("Failure! Custom activity: " + response.getAction() + " failed.  Reason: " + response.getReason(), 5000);
          }
        }
      }

      @Override
      public void onRetrieveDeviceStatusResponse(RetrieveDeviceStatusResponse response) {
        showMessage((response.isSuccess() ? "Success!" : "Failed!") + " State: " + response.getState()
                + " ExternalActivityId: " + response.getData().toString()
                + " reason: " + response.getReason(), Toast.LENGTH_LONG);
      }

      @Override
      public void onResetDeviceResponse(ResetDeviceResponse response) {
        showMessage((response.isSuccess() ? "Success!" : "Failed!") + " State: " + response.getState()
                + " reason: " + response.getReason(), Toast.LENGTH_LONG);
      }

      @Override
      public void onRetrievePaymentResponse(RetrievePaymentResponse response) {
        showMessage("RetrievePayment: " + (response.isSuccess() ? "Success!" : "Failed!")
                + " QueryStatus: " + response.getQueryStatus() + " for id " + response.getExternalPaymentId()
                + " Payment: " + response.getPayment()
                + " reason: " + response.getReason(), Toast.LENGTH_LONG);
      }

    };


    ccGoListener = new ICloverGoConnectorListener() {
      @Override
      public void onSignatureNeeded() {

      }

      public void onDeviceDisconnected() {
      }

      @Override
      public void onDeviceDisconnected(ReaderInfo readerInfo) {
        merchantInfoMap.put(readerInfo.getReaderType(), null);
        Toast.makeText(ExamplePOSActivity.this, "Disconnected", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "disconnected");
        if (currentGoConfig == readerInfo.getReaderType()) {
          ((TextView) findViewById(R.id.ConnectionStatusLabel)).setText("Disconnected");
        }

      }

      public void onDeviceConnected() {

        runOnUiThread(new Runnable() {
          @Override
          public void run() {
            showMessage("Connecting...", Toast.LENGTH_SHORT);
            ((TextView) findViewById(R.id.ConnectionStatusLabel)).setText("Connecting");
          }
        });
      }

      @Override
      public void onCloverGoDeviceActivity(final CloverDeviceEvent deviceEvent) {
        switch (deviceEvent.getEventState()) {

          case CARD_SWIPED:
            showProgressDialog(deviceEvent.getEventState().name(), deviceEvent.getMessage(), false);
            break;
          case CARD_TAPPED:
            showProgressDialog(deviceEvent.getEventState().name(), deviceEvent.getMessage(), false);
            break;
          case CANCEL_CARD_READ:
            showMessage(deviceEvent.getMessage(), Toast.LENGTH_LONG);
            break;
          case EMV_COMPLETE_DATA:
            showProgressDialog(deviceEvent.getEventState().name(), deviceEvent.getMessage(), false);
            break;
          case CARD_INSERTED_MSG:
            showProgressDialog(deviceEvent.getEventState().name(), deviceEvent.getMessage(), false);
            break;
          case CARD_REMOVED_MSG:
            showMessage(deviceEvent.getMessage(), Toast.LENGTH_LONG);
            break;
          case PLEASE_SEE_PHONE_MSG:
            showMessage(deviceEvent.getMessage(), Toast.LENGTH_LONG);
            break;
          case READER_READY:
            showMessage(deviceEvent.getMessage(), Toast.LENGTH_LONG);
            break;
        }
      }

      @Override
      public void onGetMerchantInfo() {
        showProgressDialog("Getting Merchant Info", "Please wait", false);
      }

      @Override
      public void onGetMerchantInfoResponse(boolean isSuccess) {
        if (isSuccess) {
          dismissDialog();
        } else {
          showAlertDialog("Merchant Info Error", "Could not retrieve merchant info. Please try again later.");
        }
      }

      @Override
      public void onDeviceDiscovered(ReaderInfo readerInfo) {
        boolean isSelected = false;
        for (ReaderInfo _readerInfo : mArrayListReadersList) {
          if (_readerInfo.getBluetoothIdentifier().contentEquals(readerInfo.getBluetoothIdentifier())) {
            isSelected = true;
            break;
          }
        }
        if (!isSelected) {
          mArrayListReadersList.add(readerInfo);
          mArrayListReaderString.add(readerInfo.getBluetoothName());
          if (mReaderArrayAdapter != null) {
            mReaderArrayAdapter.notifyDataSetChanged();
          }
        }
      }

      @Override
      public void onAidMatch(final List<CardApplicationIdentifier> applicationIdentifiers, final AidSelection aidSelection) {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(ExamplePOSActivity.this, android.R.layout.simple_list_item_1);
        for (CardApplicationIdentifier applicationIdentifier : (List<CardApplicationIdentifier>) applicationIdentifiers) {
          arrayAdapter.add(applicationIdentifier.getApplicationLabel());
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(ExamplePOSActivity.this);
        builder.setSingleChoiceItems(arrayAdapter, 0, new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialogInterface, int i) {
            dialogInterface.dismiss();
            aidSelection.selectApplicationIdentifier((CardApplicationIdentifier) applicationIdentifiers.get(i));
          }
        });
        builder.setCancelable(false);
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialogInterface, int i) {
            dismissDialog();
            dialogInterface.cancel();
            Toast.makeText(ExamplePOSActivity.this, "Transaction cancelled Card is not charged", Toast.LENGTH_LONG).show();
          }
        });
        builder.setTitle("Please choose card");
        builder.create().show();
      }

      public void onDeviceReady(final MerchantInfo merchantInfo) {
        runOnUiThread(new Runnable() {
          public void run() {

            if (currentGoConfig.name().equalsIgnoreCase(merchantInfo.getDeviceInfo().getModel())) {
              ((TextView) findViewById(R.id.ConnectionStatusLabel)).setText(String.format(merchantInfo.getDeviceInfo().getModel() + " Connected: %s (%s)", merchantInfo.getDeviceInfo().getSerial(), merchantInfo.getMerchantName()));
            }
            if ("RP450".equals(merchantInfo.getDeviceInfo().getModel())) {
              merchantInfoMap.put(ReaderInfo.ReaderType.RP450, merchantInfo);
            } else if ("RP350".equals(merchantInfo.getDeviceInfo().getModel())) {
              merchantInfoMap.put(ReaderInfo.ReaderType.RP350, merchantInfo);
            }
            if (pairingCodeDialog != null && pairingCodeDialog.isShowing()) {
              pairingCodeDialog.dismiss();
              pairingCodeDialog = null;
            }
            showMessage("Ready!", Toast.LENGTH_LONG);
          }
        });
      }

      public void onError(final Exception e) {

      }

      public void onDebug(final String s) {

      }

      @Override
      public void onDeviceActivityStart(final CloverDeviceEvent deviceEvent) {
      }

      @Override
      public void onReadCardDataResponse(final ReadCardDataResponse response) {
      }

      @Override
      public void onMessageFromActivity(MessageFromActivity message) {

      }

      @Override
      public void onDeviceActivityEnd(final CloverDeviceEvent deviceEvent) {
      }

      @Override
      public void onDeviceError(CloverDeviceErrorEvent deviceErrorEvent) {
        switch (deviceErrorEvent.getErrorType()) {
          case READER_ERROR:
          case CARD_ERROR:
          case READER_TIMEOUT:
          case COMMUNICATION_ERROR:
          case LOW_BATTERY:
          case PARTIAL_AUTH_REJECTED:
          case DUPLICATE_TRANSACTION_REJECTED:
            showAlertDialog(deviceErrorEvent.getErrorType().name(), deviceErrorEvent.getMessage());
            break;
          case MULTIPLE_CONTACT_LESS_CARD_DETECTED_ERROR:
          case CONTACT_LESS_FAILED_TRY_CONTACT_ERROR:
          case EMV_CARD_SWIPED_ERROR:
          case DIP_FAILED_ALL_ATTEMPTS_ERROR:
          case DIP_FAILED_ERROR:
          case SWIPE_FAILED_ERROR:
            showProgressDialog(deviceErrorEvent.getErrorType().name(), deviceErrorEvent.getMessage(), true);
            break;
        }
      }

      @Override
      public void onAuthResponse(final AuthResponse response) {
        dismissDialog();
        if (response.isSuccess()) {
          runOnUiThread(new Runnable() {
            @Override
            public void run() {
              GoPayment _payment = (GoPayment) response.getPayment();
              POSPayment payment = new POSPayment(_payment.getId(), _payment.getExternalPaymentId(), _payment.getOrder().getId(), "DFLTEMPLYEE", _payment.getAmount(), _payment.getTipAmount() != null ? _payment.getTipAmount() : 0, _payment.getCashbackAmount() != null ? _payment.getCashbackAmount() : 0);
              setPaymentStatus(payment, response);
              store.addPaymentToOrder(payment, store.getCurrentOrder());
              showMessage("Auth successfully processed.", Toast.LENGTH_SHORT);

              store.createOrder(false);
              CurrentOrderFragment currentOrderFragment = (CurrentOrderFragment) getFragmentManager().findFragmentById(R.id.PendingOrder);
              currentOrderFragment.setOrder(store.getCurrentOrder());

              if (_payment.isSignatureRequired()) {
                captureSignature(_payment.getId());
              }

              showRegister(null);
              SystemClock.sleep(3000);
              showStatus("");
              try {
                // Operation Not Supported in Clover Go
                getCloverConnector().showWelcomeScreen();
              } catch (UnsupportedOperationException e) {
                Log.e("Example POS", e.getMessage());
              }
            }
          });
        } else {
          showAlertDialog(response.getReason(), response.getMessage());
          // showMessage("Auth error:" + response.getResult(), Toast.LENGTH_LONG);
          try {
            getCloverConnector().showMessage("There was a problem processing the transaction");
          } catch (UnsupportedOperationException e) {
            Log.e("EXAMPLE POS", e.getMessage());
          }
          SystemClock.sleep(3000);
        }
      }

      @Override
      public void onPreAuthResponse(final PreAuthResponse response) {
        dismissDialog();

        if (response.isSuccess()) {
          GoPayment _payment = (GoPayment) response.getPayment();
          POSPayment payment = new POSPayment(_payment.getId(), _payment.getExternalPaymentId(), _payment.getOrder().getId(), "DFLTEMPLYEE", _payment.getAmount(), _payment.getTipAmount() != null ? _payment.getTipAmount() : 0,
                  _payment.getCashbackAmount() != null ? _payment.getCashbackAmount() : 0);
          setPaymentStatus(payment, response);
          store.addPreAuth(payment);
          showMessage("PreAuth successfully processed.", Toast.LENGTH_SHORT);
          showPreAuths(null);

          if (_payment.isSignatureRequired()) {
            captureSignature(_payment.getId());
          }

        } else {
          showAlertDialog(response.getReason(), response.getMessage());
//              showMessage("PreAuth: " + response.getResult(), Toast.LENGTH_LONG);
        }

        SystemClock.sleep(3000);
        showStatus("");
        try {
          // Operation Not Supported in Clove Go
          getCloverConnector().showWelcomeScreen();
        } catch (UnsupportedOperationException e) {
          Log.e("Example POS", e.getMessage());
        }
      }

      @Override
      public void onRetrievePendingPaymentsResponse(RetrievePendingPaymentsResponse retrievePendingPaymentResponse) {
      }

      @Override
      public void onTipAdjustAuthResponse(TipAdjustAuthResponse response) {
        if (response.isSuccess()) {

          boolean updatedTip = false;
          for (POSOrder order : store.getOrders()) {
            for (POSExchange exchange : order.getPayments()) {
              if (exchange instanceof POSPayment) {
                POSPayment posPayment = (POSPayment) exchange;
                if (exchange.getPaymentID().equals(response.getPaymentId())) {
                  posPayment.setTipAmount(response.getTipAmount());
                  updatedTip = true;
                  break;
                }
              }
            }
            if (updatedTip) {
              showMessage("Tip successfully adjusted", Toast.LENGTH_LONG);
              break;
            }
          }
        } else {
          showMessage("Tip adjust failed", Toast.LENGTH_LONG);
        }
      }

      @Override
      public void onCapturePreAuthResponse(CapturePreAuthResponse response) {

        if (response.isSuccess()) {
          for (final POSPayment payment : store.getPreAuths()) {
            if (payment.getPaymentID().equals(response.getPaymentID())) {
              final long paymentAmount = response.getAmount();
              runOnUiThread(new Runnable() {
                @Override
                public void run() {
                  store.removePreAuth(payment);
                  store.addPaymentToOrder(payment, store.getCurrentOrder());
                  payment.setPaymentStatus(POSPayment.Status.AUTHORIZED);
                  payment.amount = paymentAmount;
                  showMessage("Sale successfully processing using Pre Authorization", Toast.LENGTH_LONG);

                  store.createOrder(false);
                  CurrentOrderFragment currentOrderFragment = (CurrentOrderFragment) getFragmentManager().findFragmentById(R.id.PendingOrder);
                  currentOrderFragment.setOrder(store.getCurrentOrder());
                  showRegister(null);
                }
              });
              break;
            } else {
              showMessage("PreAuth Capture: Payment received does not match any of the stored PreAuth records", Toast.LENGTH_LONG);
            }
          }
        } else {
          showMessage("PreAuth Capture Error: Payment failed with response code = " + response.getResult() + " and reason: " + response.getReason(), Toast.LENGTH_LONG);
        }
      }

      @Override
      public void onVerifySignatureRequest(VerifySignatureRequest request) {
      }

      @Override
      public void onConfirmPaymentRequest(ConfirmPaymentRequest request) {
        if (request.getChallenges() == null) {
          showMessage("Error: The ConfirmPaymentRequest was missing the payment and/or challenges.", Toast.LENGTH_LONG);
        } else {
          currentPayment = request.getPayment();
          currentChallenges = request.getChallenges();
          final Challenge theChallenge = currentChallenges[0];

          runOnUiThread(new Runnable() {
            @Override
            public void run() {

              switch(theChallenge.type) {
                case DUPLICATE_CHALLENGE:
                  showPaymentConfirmation(paymentConfirmationListener, theChallenge, 0);
                  break;

              }
            }
          });
        }
      }

      @Override
      public void onCloseoutResponse(CloseoutResponse response) {
        if (response.isSuccess()) {
          showMessage("Closeout is scheduled.", Toast.LENGTH_SHORT);
        } else {
          showMessage("Error scheduling closeout: " + response.getResult(), Toast.LENGTH_LONG);
        }
      }

      @Override
      public void onSaleResponse(final SaleResponse response) {
        dismissDialog();
        if (response != null) {
          if (response.isSuccess()) { // Handle cancel response
            if (response.getPayment() != null) {
              GoPayment _payment = (GoPayment) response.getPayment();
              POSPayment payment = new POSPayment(_payment.getId(), _payment.getExternalPaymentId(), _payment.getOrder().getId(), "DFLTEMPLYEE", _payment.getAmount(), _payment.getTipAmount() != null ? _payment.getTipAmount() : 0, _payment.getCashbackAmount() != null ? _payment.getCashbackAmount() : 0);
              setPaymentStatus(payment, response);

              store.addPaymentToOrder(payment, store.getCurrentOrder());
              showMessage("Sale successfully processed", Toast.LENGTH_SHORT);
              store.createOrder(false);
              CurrentOrderFragment currentOrderFragment = (CurrentOrderFragment) getFragmentManager().findFragmentById(R.id.PendingOrder);
              currentOrderFragment.setOrder(store.getCurrentOrder());
              if (_payment.isSignatureRequired()) {
                captureSignature(_payment.getId());
              }
              showRegister(null);
              showStatus("");
            } else { // Handle null payment
              showMessage("Error: Sale response was missing the payment", Toast.LENGTH_LONG);
            }
          } else {
            showAlertDialog(response.getReason(), response.getMessage());
          }
        } else { //Handle null payment response
          showMessage("Error: Null SaleResponse", Toast.LENGTH_LONG);
        }
        SystemClock.sleep(3000);
        try {
          // Operation Not Supported in Clove Go
          getCloverConnector().showWelcomeScreen();
        } catch (UnsupportedOperationException e) {
          Log.e("Example POS", e.getMessage());
        }
      }

      @Override
      public void onManualRefundResponse(final ManualRefundResponse response) {
      }

      @Override
      public void onRefundPaymentResponse(final RefundPaymentResponse response) {
        if (response.isSuccess()) {
          POSRefund refund = new POSRefund(response.getRefund().getId(), response.getPaymentId(), response.getOrderId(), "DEFAULT", response.getRefund().getAmount());
          boolean done = false;
          for (POSOrder order : store.getOrders()) {
            for (POSExchange payment : order.getPayments()) {
              if (payment instanceof POSPayment) {
                if (payment.getPaymentID().equals(response.getPaymentId())) {
                  ((POSPayment) payment).setPaymentStatus(POSPayment.Status.REFUNDED);
                  store.addRefundToOrder(refund, order);
                  showMessage("Payment successfully refunded", Toast.LENGTH_SHORT);
                  done = true;
                  break;
                }
              }
            }
            if (done) {
              break;
            }
          }
        } else {
          runOnUiThread(new Runnable() {
            @Override
            public void run() {
              AlertDialog.Builder builder = new AlertDialog.Builder(ExamplePOSActivity.this);
              builder.setTitle("Refund Error").setMessage("There was an error refunding the payment");
              builder.create().show();
              Log.d(getClass().getName(), "Got refund response of " + response.getReason());
            }
          });
        }
      }

      @Override
      public void onTipAdded(TipAddedMessage message) {
        if (message.tipAmount > 0) {
          showMessage("Tip successfully added: " + CurrencyUtils.format(message.tipAmount, Locale.getDefault()), Toast.LENGTH_SHORT);
        }
      }

      @Override
      public void onVoidPaymentResponse(VoidPaymentResponse response) {
        if (response.isSuccess()) {
          boolean done = false;
          for (POSOrder order : store.getOrders()) {
            for (POSExchange payment : order.getPayments()) {
              if (payment instanceof POSPayment) {
                if (payment.getPaymentID().equals(response.getPaymentId())) {
                  ((POSPayment) payment).setPaymentStatus(POSPayment.Status.VOIDED);
                  showMessage("Payment was voided", Toast.LENGTH_SHORT);
                  done = true;
                  break;
                }
              }
            }
            if (done) {
              break;
            }
          }
        } else {
          showMessage(getClass().getName() + ":Got VoidPaymentResponse of " + response.getResult(), Toast.LENGTH_LONG);
        }
      }

      @Override
      public void onVaultCardResponse(final VaultCardResponse response) {
      }

      @Override
      public void onPrintJobStatusResponse(PrintJobStatusResponse response) {

      }

      @Override
      public void onRetrievePrintersResponse(RetrievePrintersResponse response) {

      }

      @Override
      public void onPrintManualRefundReceipt(PrintManualRefundReceiptMessage pcm) {
      }

      @Override
      public void onPrintManualRefundDeclineReceipt(PrintManualRefundDeclineReceiptMessage pcdrm) {
      }

      @Override
      public void onPrintPaymentReceipt(PrintPaymentReceiptMessage pprm) {
      }

      @Override
      public void onPrintPaymentDeclineReceipt(PrintPaymentDeclineReceiptMessage ppdrm) {
      }

      @Override
      public void onPrintPaymentMerchantCopyReceipt(PrintPaymentMerchantCopyReceiptMessage ppmcrm) {
      }

      @Override
      public void onPrintRefundPaymentReceipt(PrintRefundPaymentReceiptMessage pprrm) {
      }

      @Override
      public void onCustomActivityResponse(CustomActivityResponse response) {
      }

      @Override
      public void onRetrieveDeviceStatusResponse(RetrieveDeviceStatusResponse response) {

      }

      @Override
      public void onResetDeviceResponse(ResetDeviceResponse response) {

      }

      @Override
      public void onRetrievePaymentResponse(RetrievePaymentResponse response) {

      }

    };

    if (currentGoConfig == ReaderInfo.ReaderType.RP450 || currentGoConfig == ReaderInfo.ReaderType.RP350)
      ((ICloverGoConnector) getCloverConnector()).addCloverGoConnectorListener(ccGoListener);
    else {
      getCloverConnector().addCloverConnectorListener(ccListener);

      getCloverConnector().addCloverConnectorListener(ccListener);
      getCloverConnector().initializeConnection();
    }

    updateComponentsWithNewCloverConnector();
  }


  private void captureSignature(String paymentID) {

    FragmentManager fragmentManager = getFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

    GoSignatureFragment fragment = GoSignatureFragment.newInstance(paymentID, getCloverConnector());
    fragmentTransaction.add(R.id.contentContainer, fragment);

    fragmentTransaction.commit();
  }

  private void showStatus(String msg) {
    ((TextView) findViewById(R.id.DeviceStatus)).setText(msg);
  }

  private void showRatingsDialog(final Rating[] ratings) {
    ExamplePOSActivity.this.runOnUiThread(new Runnable() {
      @Override
      public void run() {
        final String[] ratingsStrings = new String[ratings.length];
        for (int x = 0; x < ratings.length; x++) {
          ratingsStrings[x] = ratings[x].id + ": " + ((Integer) ratings[x].value).toString() + " Stars";
        }
        ratingsAdapter = new ArrayAdapter<>(ExamplePOSActivity.this, android.R.layout.simple_list_item_1, ratingsStrings);
        ratingsList.setAdapter(ratingsAdapter);
        ratingsDialog.show();
      }
    });

  }

  private void setPaymentStatus(POSPayment payment, PaymentResponse response) {
    if (response.isSale()) {
      payment.setPaymentStatus(POSPayment.Status.PAID);
    } else if (response.isAuth()) {
      payment.setPaymentStatus(POSPayment.Status.AUTHORIZED);
    } else if (response.isPreAuth()) {
      payment.setPaymentStatus(POSPayment.Status.PREAUTHORIZED);
    }
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    if (cloverGoConnectorMap.get(ReaderInfo.ReaderType.RP350) != null) {
      cloverGoConnectorMap.get(ReaderInfo.ReaderType.RP350).disconnectDevice();
    }
    if (cloverGoConnectorMap.get(ReaderInfo.ReaderType.RP450) != null) {
      cloverGoConnectorMap.get(ReaderInfo.ReaderType.RP450).disconnectDevice();
    }
    if (getCloverConnector() != null) {
      getCloverConnector().dispose();
    }
  }

  @Override
  public void onFragmentInteraction(Uri uri) {

  }

  private void showPaymentConfirmation(PaymentConfirmationListener listenerIn, Challenge challengeIn, int challengeIndexIn) {
    showChallengeDialog(R.string.payment_confirmation, R.string.reject, R.string.accept, listenerIn, challengeIn, challengeIndexIn);
  }

  private void showPartialAuthChallenge(PaymentConfirmationListener listenerIn, Challenge challengeIn, int challengeIndexIn) {
    showChallengeDialog(R.string.partial_auth_error_lbl, R.string.no, R.string.yes, listenerIn, challengeIn, challengeIndexIn);
  }

  private void showChallengeDialog(int titleResId, int negativeButtonResId, int positiveButtonResId,
                                   PaymentConfirmationListener listenerIn, Challenge challengeIn,
                                   int challengeIndexIn) {

    final int challengeIndex = challengeIndexIn;
    final Challenge challenge = challengeIn;
    final PaymentConfirmationListener listener = listenerIn;
    AlertDialog.Builder confirmationDialog = new AlertDialog.Builder(this);
    confirmationDialog.setTitle(titleResId);
    confirmationDialog.setCancelable(false);
    confirmationDialog.setMessage(challenge.message);
    confirmationDialog.setNegativeButton(negativeButtonResId, new DialogInterface.OnClickListener() {

      @Override
      public void onClick(DialogInterface dialog, int which) {
        listener.onRejectClicked(challenge);
        dialog.dismiss();
      }
    });
    confirmationDialog.setPositiveButton(positiveButtonResId, new DialogInterface.OnClickListener() {

      @Override
      public void onClick(DialogInterface dialog, int which) {
        listener.onAcceptClicked(challengeIndex);
        dialog.dismiss();
      }
    });
    confirmationDialog.show();
  }

  private void handleRequestRatings() {
    Rating rating1 = new Rating();
    rating1.id = "Quality";
    rating1.question = "How would you rate the overall quality of your entree?";
    rating1.value = 0;
    Rating rating2 = new Rating();
    rating2.id = "Server";
    rating2.question = "How would you rate the overall performance of your server?";
    rating2.value = 0;
    Rating rating3 = new Rating();
    rating3.id = "Value";
    rating3.question = "How would you rate the overall value of your dining experience?";
    rating3.value = 0;
    Rating rating4 = new Rating();
    rating4.id = "RepeatBusiness";
    rating4.question = "How likely are you to dine at this establishment again in the near future?";
    rating4.value = 0;
    Rating[] ratings = new Rating[]{rating1, rating2, rating3, rating4};
    RatingsMessage ratingsMessage = new RatingsMessage(ratings);
    String ratingsListJson = ratingsMessage.toJsonString();
    sendMessageToActivity("com.clover.cfp.examples.RatingsExample", ratingsListJson);
  }

  private void handleRatings(String payload) {
    //showMessage(payload, Toast.LENGTH_SHORT);
    RatingsMessage ratingsMessage = (RatingsMessage) PayloadMessage.fromJsonString(payload);
    Rating[] ratingsPayload = ratingsMessage.ratings;
    showRatingsDialog(ratingsPayload);
    //for (Rating rating:ratingsPayload
    //     ) {
    //  String ratingString = "Rating ID: " + rating.id + " - " + rating.question + " Rating value: " + Integer.toString(rating.value);
    //  showMessage(ratingString, Toast.LENGTH_SHORT);
    //}
  }

  private void handleCustomerLookup(String payload) {
    PhoneNumberMessage phoneNumberMessage = new Gson().fromJson(payload, PhoneNumberMessage.class);
    String phoneNumber = phoneNumberMessage.phoneNumber;
    showMessage("Just received phone number " + phoneNumber + " from the Ratings remote application.", 3000);
    showMessage("Sending customer name Ron Burgundy to the Ratings remote application for phone number " + phoneNumber, 3000);
    CustomerInfo customerInfo = new CustomerInfo();
    customerInfo.customerName = "Ron Burgundy";
    customerInfo.phoneNumber = phoneNumber;
    CustomerInfoMessage customerInfoMessage = new CustomerInfoMessage(customerInfo);
    String customerInfoJson = customerInfoMessage.toJsonString();
    sendMessageToActivity("com.clover.cfp.examples.RatingsExample", customerInfoJson);
  }

  private void handleJokeResponse(String payload) {
    ConversationResponseMessage jokeResponseMessage = (ConversationResponseMessage) PayloadMessage.fromJsonString(payload);
    showMessage("Received JokeResponse of: " + jokeResponseMessage.message, Toast.LENGTH_SHORT);
  }

  private void showMessage(final String msg, final int duration) {
    runOnUiThread(new Runnable() {
      @Override
      public void run() {
        Toast.makeText(ExamplePOSActivity.this, msg, duration).show();
      }
    });
  }

  public void showSettings(MenuItem item) {
    if (!usb) {
      Intent intent = new Intent(this, ExamplePOSSettingsActivity.class);
      startActivityForResult(intent, WS_ENDPOINT_ACTIVITY);
    }
  }

  private void updateComponentsWithNewCloverConnector() {
    FragmentManager fragmentManager = getFragmentManager();

    RegisterFragment refFragment = (RegisterFragment) fragmentManager.findFragmentByTag("REGISTER");
    if (refFragment != null) {
      refFragment.setCloverConnector(getCloverConnector());
      refFragment.setPaymentType(paymentType);
    }
    OrdersFragment ordersFragment = (OrdersFragment) fragmentManager.findFragmentByTag("ORDERS");
    if (ordersFragment != null) {
      ordersFragment.setCloverConnector(getCloverConnector());
    }
    ManualRefundsFragment manualRefundsFragment = (ManualRefundsFragment) fragmentManager.findFragmentByTag("REFUNDS");
    if (manualRefundsFragment != null) {
      manualRefundsFragment.setCloverConnector(getCloverConnector());
    }
    CardsFragment cardsFragment = (CardsFragment) fragmentManager.findFragmentByTag("CARDS");
    if (cardsFragment != null) {
      cardsFragment.setCloverConnector(getCloverConnector());
    }
    MiscellaneousFragment miscFragment = (MiscellaneousFragment) fragmentManager.findFragmentByTag("MISC");
    if (miscFragment != null) {
      miscFragment.setCloverConnector(getCloverConnector());
    }
    PendingPaymentsFragment ppFragment = (PendingPaymentsFragment) fragmentManager.findFragmentByTag("PENDING");
    if (ppFragment != null) {
      ppFragment.setCloverConnector(getCloverConnector());
    }
  }

  public void showPending(View view) {
    FragmentManager fragmentManager = getFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

    hideFragments(fragmentManager, fragmentTransaction);

    Fragment fragment = fragmentManager.findFragmentByTag("PENDING");

    if (fragment == null) {
      fragment = PendingPaymentsFragment.newInstance(store, getCloverConnector());
      fragmentTransaction.add(R.id.contentContainer, fragment, "PENDING");
    } else {
      ((PendingPaymentsFragment) fragment).setCloverConnector(getCloverConnector());
      fragmentTransaction.show(fragment);
    }

    fragmentTransaction.commit();
  }

  public void showOrders(View view) {
    FragmentManager fragmentManager = getFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

    hideFragments(fragmentManager, fragmentTransaction);

    Fragment fragment = fragmentManager.findFragmentByTag("ORDERS");
    if (fragment == null) {
      fragment = OrdersFragment.newInstance(store, getCloverConnector());
      ((OrdersFragment) fragment).setCloverConnector(getCloverConnector());
      fragmentTransaction.add(R.id.contentContainer, fragment, "ORDERS");
    } else {
      ((OrdersFragment) fragment).setCloverConnector(getCloverConnector());
      fragmentTransaction.show(fragment);
    }

    fragmentTransaction.commit();
  }

  public void showRegister(View view) {
    FragmentManager fragmentManager = getFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

    hideFragments(fragmentManager, fragmentTransaction);

    Fragment fragment = fragmentManager.findFragmentByTag("REGISTER");
    if (fragment == null) {
      fragment = RegisterFragment.newInstance(store, getCloverConnector());
      fragmentTransaction.add(R.id.contentContainer, fragment, "REGISTER");
    } else {
      ((RegisterFragment) fragment).setCloverConnector(getCloverConnector());
      fragmentTransaction.show(fragment);
    }

    fragmentTransaction.commit();
  }

  public void showRefunds(View view) {
    FragmentManager fragmentManager = getFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

    hideFragments(fragmentManager, fragmentTransaction);

    Fragment fragment = fragmentManager.findFragmentByTag("REFUNDS");
    if (fragment == null) {
      fragment = ManualRefundsFragment.newInstance(store, getCloverConnector());
      fragmentTransaction.add(R.id.contentContainer, fragment, "REFUNDS");
    } else {
      ((ManualRefundsFragment) fragment).setCloverConnector(getCloverConnector());
      fragmentTransaction.show(fragment);
    }

    fragmentTransaction.commit();
  }

  public void showCards(View view) {
    FragmentManager fragmentManager = getFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

    hideFragments(fragmentManager, fragmentTransaction);

    Fragment fragment = fragmentManager.findFragmentByTag("CARDS");
    if (fragment == null) {
      fragment = CardsFragment.newInstance(store, getCloverConnector());
      ((CardsFragment) fragment).setCloverConnector(getCloverConnector());
      fragmentTransaction.add(R.id.contentContainer, fragment, "CARDS");
    } else {
      ((CardsFragment) fragment).setCloverConnector(getCloverConnector());
      fragmentTransaction.show(fragment);
    }
    fragmentTransaction.commit();
  }

  public void showMisc(View view) {
    FragmentManager fragmentManager = getFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

    hideFragments(fragmentManager, fragmentTransaction);

    Fragment fragment = fragmentManager.findFragmentByTag("MISC");

    if (fragment == null) {
      fragment = MiscellaneousFragment.newInstance(store, getCloverConnector());
      fragmentTransaction.add(R.id.contentContainer, fragment, "MISC");
    } else {
      ((MiscellaneousFragment) fragment).setCloverConnector(getCloverConnector());
      fragmentTransaction.show(fragment);
    }

    fragmentTransaction.commit();
  }

  public void showPreAuths(View view) {
    FragmentManager fragmentManager = getFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

    hideFragments(fragmentManager, fragmentTransaction);

    Fragment fragment = fragmentManager.findFragmentByTag("PRE_AUTHS");

    if (fragment == null) {
      fragment = PreAuthFragment.newInstance(store, getCloverConnector());
      ((PreAuthFragment) fragment).setStore(store);
      fragmentTransaction.add(R.id.contentContainer, fragment, "PRE_AUTHS");
    } else {
      ((PreAuthFragment) fragment).setCloverConnector(getCloverConnector());
      fragmentTransaction.show(fragment);
    }

    fragmentTransaction.commit();
  }

  private void hideFragments(FragmentManager fragmentManager, FragmentTransaction fragmentTransaction) {
    Fragment fragment = fragmentManager.findFragmentByTag("ORDERS");
    if (fragment != null) {
      fragmentTransaction.hide(fragment);
    }
    fragment = fragmentManager.findFragmentByTag("REGISTER");
    if (fragment != null) {
      fragmentTransaction.hide(fragment);
    }
    fragment = fragmentManager.findFragmentByTag("SIGNATURE");
    if (fragment != null) {
      fragmentTransaction.hide(fragment);
    }
    fragment = fragmentManager.findFragmentByTag("CARDS");
    if (fragment != null) {
      fragmentTransaction.hide(fragment);
    }
    fragment = fragmentManager.findFragmentByTag("MISC");
    if (fragment != null) {
      fragmentTransaction.hide(fragment);
    }
    fragment = fragmentManager.findFragmentByTag("REFUNDS");
    if (fragment != null) {
      fragmentTransaction.hide(fragment);
    }
    fragment = fragmentManager.findFragmentByTag("PRE_AUTHS");
    if (fragment != null) {
      fragmentTransaction.hide(fragment);
    }
    fragment = fragmentManager.findFragmentByTag("PENDING");
    if (fragment != null) {
      fragmentTransaction.hide(fragment);
    }
  }

  private int getNextPrintRequestId() {
    return ++printRequestId;
  }

  public void captureCardClick(View view) {
    try {
      getCloverConnector().vaultCard(store.getCardEntryMethods());
    } catch (UnsupportedOperationException e) {
      Toast.makeText(ExamplePOSActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
    }

  }

  public void onManualRefundClick(View view) {
    CharSequence val = ((TextView) findViewById(R.id.ManualRefundTextView)).getText();
    try {
      long refundAmount = Long.parseLong(val.toString());
      ManualRefundRequest request = new ManualRefundRequest(refundAmount, IdUtils.getNextId());
      request.setAmount(refundAmount);
      request.setCardEntryMethods(store.getCardEntryMethods());
      request.setDisablePrinting(store.getDisablePrinting());
      request.setDisableReceiptSelection(store.getDisableReceiptOptions());
      getCloverConnector().manualRefund(request);
    } catch (NumberFormatException nfe) {
      showMessage("Invalid value. Must be an integer.", Toast.LENGTH_LONG);
    } catch (UnsupportedOperationException e) {
      Toast.makeText(ExamplePOSActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
    }
  }

  public void connect350Click(View view) {
    if (cloverGoConnectorMap.get(ReaderInfo.ReaderType.RP350) == null) {
      CloverGoDeviceConfiguration config = new CloverGoDeviceConfiguration.Builder(getApplicationContext(), accessToken, goEnv, apiKey, secret, appId).deviceType(ReaderInfo.ReaderType.RP350).allowAutoConnect(false).build();
      ICloverGoConnector cloverGo350Connector = (CloverGoConnector) ConnectorFactory.createCloverConnector(config);
      cloverGoConnectorMap.put(ReaderInfo.ReaderType.RP350, cloverGo350Connector);
      cloverGo350Connector.addCloverGoConnectorListener(ccGoListener);
    }
    if (merchantInfoMap.get(ReaderInfo.ReaderType.RP350) == null) {
      cloverGoConnectorMap.get(ReaderInfo.ReaderType.RP350).initializeConnection();
    } else {
      showMessage("Reader 450 Already Connected", Toast.LENGTH_LONG);
    }
  }

  public void connect450Click(View view) {

    if (isBluetoothEnabled() && isGPSEnabled()) {
      if (cloverGoConnectorMap.get(ReaderInfo.ReaderType.RP450) == null) {
        CloverGoDeviceConfiguration config = new CloverGoDeviceConfiguration.Builder(getApplicationContext(), accessToken, goEnv, apiKey, secret, appId).deviceType(ReaderInfo.ReaderType.RP450).allowAutoConnect(false).build();
        ICloverGoConnector cloverGo450Connector = (CloverGoConnector) ConnectorFactory.createCloverConnector(config);
        cloverGoConnectorMap.put(ReaderInfo.ReaderType.RP450, cloverGo450Connector);
        cloverGo450Connector.addCloverGoConnectorListener(ccGoListener);
      }
      if (merchantInfoMap.get(ReaderInfo.ReaderType.RP450) == null) {
        cloverGoConnectorMap.get(ReaderInfo.ReaderType.RP450).initializeConnection();
        mArrayListReadersList = new ArrayList<>();
        mArrayListReaderString = new ArrayList<>();
        showBluetoothReaders();
      } else {
        showMessage("Reader 450 Already Connected", Toast.LENGTH_LONG);
      }
    } else {
      showMessage("Enable GPS and Bluetooth", Toast.LENGTH_LONG);
    }

  }

  public void queryPaymentClick(View view) {
    String externalPaymentId = ((TextView) findViewById(R.id.QueryPaymentText)).getText().toString();
    getCloverConnector().retrievePayment(new RetrievePaymentRequest(externalPaymentId));
  }

  public void printTextClick(View view) {
    String[] textLines = ((TextView) findViewById(R.id.PrintTextText)).getText().toString().split("\n");
    List<String> lines = Arrays.asList(textLines);
    PrintRequest pr = new PrintRequest(lines);
    lastPrintRequestId = String.valueOf(getNextPrintRequestId());
    pr.setPrintRequestId(lastPrintRequestId);
    if (printer != null) {
      pr = new PrintRequest(lines, lastPrintRequestId, printer.getId());
    }

    try {
      // Operation Not Supported in Clove Go
//          getCloverConnector().printText(lines);
      getCloverConnector().print(pr);
    } catch (UnsupportedOperationException e) {
      Toast.makeText(ExamplePOSActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
      Log.e("Example POS", e.getMessage());
    }

//    getCloverConnector().print(pr);
    updatePrintStatusText();
  }

  public void printImageURLClick(View view) {
    String URL = ((TextView) findViewById(R.id.PrintImageURLText)).getText().toString();
    PrintRequest pr = new PrintRequest(URL);
    lastPrintRequestId = String.valueOf(getNextPrintRequestId());
    pr.setPrintRequestId(lastPrintRequestId);
    if (printer != null) {
      pr = new PrintRequest(URL, lastPrintRequestId, printer.getId());
    }
    getCloverConnector().print(pr);
    updatePrintStatusText();
  }

  public void queryPrintStatusClick(View view) {
    PrintJobStatusRequest pjsr = new PrintJobStatusRequest(lastPrintRequestId);
    getCloverConnector().retrievePrintJobStatus(pjsr);
  }

  private void updatePrintStatusText() {
    EditText printStatusId = ((EditText) findViewById(R.id.QueryPrintStatusText));
    printStatusId.setText(lastPrintRequestId);
  }

  public void showMessageClick(View view) {


    try {
      // Operation not Supported in CLover GO
      getCloverConnector().showMessage(((TextView) findViewById(R.id.ShowMessageText)).getText().toString());
    } catch (UnsupportedOperationException e) {
      Log.e("EXAMPLE POS", e.getMessage());
    }
//    getCloverConnector().showMessage(((TextView) findViewById(R.id.ShowMessageText)).getText().toString());
  }

  public void showWelcomeMessageClick(View view) {
    try {
      // Operation Not Supported in Clove Go
      getCloverConnector().showWelcomeScreen();
    } catch (UnsupportedOperationException e) {
      Toast.makeText(ExamplePOSActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
      Log.e("Example POS", e.getMessage());
    }
  }

  public void showThankYouClick(View view) {
    try {
      // Operation not Supported in CloverGO
      getCloverConnector().showThankYouScreen();
    } catch (UnsupportedOperationException e) {
      Toast.makeText(ExamplePOSActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
    }

  }

  public void onOpenCashDrawerClick(View view) {
    OpenCashDrawerRequest ocdr = new OpenCashDrawerRequest("Test");
    if (printer != null) {
      ocdr.setDeviceId(printer.getId());
    }
    getCloverConnector().openCashDrawer(ocdr);
  }

  public void preauthCardClick(View view) {

    if ("KEYED".equals(paymentType)) {
      KeyedTransactionFragment keyedTransactionFragment = KeyedTransactionFragment.newInstance(store, getCloverConnector(), "preAuth");
      FragmentManager fm = getFragmentManager();
      keyedTransactionFragment.show(fm, "KEYED_FRAGMENT");
    } else {
      if (getCloverConnector() instanceof ICloverGoConnector)
        showProgressDialog("PreAuth Transaction", "Swipe, Tap or Dip card for Payment", true);

      String externalPaymentID = IdUtils.getNextId();
      Log.d(TAG, "ExternalPaymentID:" + externalPaymentID);
      store.getCurrentOrder().setPendingPaymentId(externalPaymentID);
      PreAuthRequest request = new PreAuthRequest(5000L, externalPaymentID);
      request.setCardEntryMethods(store.getCardEntryMethods());
      request.setDisablePrinting(store.getDisablePrinting());
      request.setSignatureEntryLocation(store.getSignatureEntryLocation());
      request.setSignatureThreshold(store.getSignatureThreshold());
      request.setDisableReceiptSelection(store.getDisableReceiptOptions());
      request.setDisableDuplicateChecking(store.getDisableDuplicateChecking());
      getCloverConnector().preAuth(request);
    }
  }

  public void onClickCloseout(View view) {
    CloseoutRequest request = new CloseoutRequest();
    request.setAllowOpenTabs(false);
    request.setBatchId(null);
    getCloverConnector().closeout(request);
  }


  public void printImageClick(View view) {
    Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
    startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
  }

  public void setPrinter(Printer printer) {
    this.printer = printer;
  }

  public void printImage(String imgDecodableString) {
    Bitmap bitmap = BitmapFactory.decodeFile(imgDecodableString);
    PrintRequest pr = new PrintRequest(bitmap);
    lastPrintRequestId = String.valueOf(getNextPrintRequestId());
    pr.setPrintRequestId(lastPrintRequestId);
    if (this.printer != null) {
      pr = new PrintRequest(bitmap, lastPrintRequestId, printer.getId());
    }
    getCloverConnector().print(pr);
    updatePrintStatusText();
  }


  public void onResetDeviceClick(View view) {
    runOnUiThread(new Runnable() {
      @Override
      public void run() {
        new AlertDialog.Builder(ExamplePOSActivity.this)
                .setTitle("Reset Device")
                .setMessage("Are you sure you want to reset the device? Warning: You may lose any pending transaction information.")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                  @Override
                  public void onClick(DialogInterface dialog, int which) {
                    getCloverConnector().resetDevice();
                  }
                })
                .setNegativeButton("No", null)
                .show();
      }
    });
  }

  public void onReadCardDataClick(View view) {
    getCloverConnector().readCardData(new ReadCardDataRequest(store.getCardEntryMethods()));
  }

  public void onGetDeviceStatusClick(View view) {
    getCloverConnector().retrieveDeviceStatus(new RetrieveDeviceStatusRequest(false));
  }

  public void onGetDeviceStatusCBClick(View view) {
    getCloverConnector().retrieveDeviceStatus(new RetrieveDeviceStatusRequest(true));
  }

  public void refreshPendingPayments(View view) {
    try {
      // Operation not Supported in CLover GO
      getCloverConnector().retrievePendingPayments();
    } catch (UnsupportedOperationException e) {
      Toast.makeText(ExamplePOSActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
    }

  }

  public void startActivity(View view) {
    String selectedItem = (String) ((Spinner) findViewById(R.id.activity_id)).getSelectedItem();
    String activityId = CUSTOM_ACTIVITY_PACKAGE + selectedItem;
    String payload = ((EditText) findViewById(R.id.activity_payload)).getText().toString();

    CustomActivityRequest car = new CustomActivityRequest(activityId);
    car.setPayload(payload);
    boolean nonBlocking = ((Switch) findViewById(R.id.customActivityBlocking)).isChecked();
    car.setNonBlocking(nonBlocking);

    //If the custom activity is conversational, pass in the messageTo and messageFrom action string arrays
    if (activityId.equals("com.clover.cfp.examples.BasicConversationalExample")) {
      Button messageButton = (Button) findViewById(R.id.sendMessageToActivityButton);
      if (messageButton != null) {
        messageButton.setVisibility(View.VISIBLE);
      }
    } else {
      Button messageButton = (Button) findViewById(R.id.sendMessageToActivityButton);
      if (messageButton != null) {
        messageButton.setVisibility(View.INVISIBLE);
      }
    }

    getCloverConnector().startCustomActivity(car);
  }

  public void sendMessageToActivity(View view) {
    String activityId = CUSTOM_ACTIVITY_PACKAGE + ((Spinner) findViewById(R.id.activity_id)).getSelectedItem().toString();
    ConversationQuestionMessage message = new ConversationQuestionMessage("Why did the Storm Trooper buy an iPhone?");
    String payload = message.toJsonString();
    MessageToActivity messageRequest = new MessageToActivity(activityId, payload);
    getCloverConnector().sendMessageToActivity(messageRequest);
    Button messageButton = (Button) findViewById(R.id.sendMessageToActivityButton);
    if (messageButton != null) {
      messageButton.setVisibility(View.INVISIBLE);
    }
  }

  public void sendMessageToActivity(String activityId, String payload) {
    MessageToActivity messageRequest = new MessageToActivity(activityId, payload);
    getCloverConnector().sendMessageToActivity(messageRequest);
  }

  public void externalMismatch() {
    runOnUiThread(new Runnable() {
      @Override
      public void run() {
        AlertDialog externalIDMismatch;
        AlertDialog.Builder builder = new AlertDialog.Builder(ExamplePOSActivity.this);
        builder.setTitle("Error");
        builder.setMessage("External Payment Id's do not match");
        externalIDMismatch = builder.create();
        externalIDMismatch.show();
      }
    });
  }

  /*
    Displays RP450 readers discovered while scanning. and select reader to connect.
     */
  private void showBluetoothReaders() {

    final Dialog mDialog = new Dialog(this, R.style.selectReaderDialog);
    mDialog.setContentView(R.layout.dialog_layout);
    ((TextView) mDialog.findViewById(R.id.dialogTitle)).setText("Select one Bluetooth Reader");
    mDialog.setCancelable(true);
    mDialog.setCanceledOnTouchOutside(false);
    mDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
      @Override
      public void onCancel(DialogInterface dialogInterface) {
        ((ICloverGoConnector) getCloverConnector()).stopDeviceScan();
        mDialog.dismiss();

      }
    });

    mDialog.findViewById(R.id.dialogBtn).setVisibility(View.VISIBLE);
    mDialog.findViewById(R.id.dialogBtn).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        ((ICloverGoConnector) getCloverConnector()).stopDeviceScan();
        mDialog.dismiss();
      }
    });


    mReaderArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1, mArrayListReaderString);
    ListView mBluetoothReadersListVw = (ListView) mDialog.findViewById(R.id.dialogList);
    mBluetoothReadersListVw.setAdapter(mReaderArrayAdapter);
    mBluetoothReadersListVw.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ReaderInfo readerInfo = mArrayListReadersList.get(position);
        ((ICloverGoConnector) getCloverConnector()).connectToBluetoothDevice(readerInfo);
        Toast.makeText(ExamplePOSActivity.this, readerInfo.getBluetoothName() + "\n" + readerInfo.getBluetoothIdentifier(), Toast.LENGTH_SHORT).show();
        mDialog.dismiss();
      }
    });
    mReaderArrayAdapter.notifyDataSetChanged();
    mDialog.show();
  }

  private ICloverConnector getCloverConnector() {
    if (currentGoConfig == ReaderInfo.ReaderType.RP450) {
      return cloverGoConnectorMap.get(ReaderInfo.ReaderType.RP450);
    } else if (currentGoConfig == ReaderInfo.ReaderType.RP350) {
      return cloverGoConnectorMap.get(ReaderInfo.ReaderType.RP350);
    } else {
      return cloverConnector;
    }
  }

  public void showProgressDialog(String title, String message, boolean isCancelable) {
    dismissDialog();
    progressDialog = DialogHelper.showProgressDialog(this, title, message, isCancelable, "Cancel", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        getCloverConnector().cancel();
      }
    });
    progressDialog.show();
  }

  public void showAlertDialog(String title, String message) {
    dismissDialog();
    alertDialog = DialogHelper.createAlertDialog(this, title, message, "OK", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        dialog.dismiss();
      }
    });
    alertDialog.show();
  }

  private void dismissDialog() {
    if (alertDialog != null && alertDialog.isShowing()) {
      alertDialog.dismiss();
    }
    if (progressDialog != null && progressDialog.isShowing()) {
      progressDialog.dismiss();
    }
  }

  private boolean isBluetoothEnabled() {
    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    if (mBluetoothAdapter != null && !mBluetoothAdapter.isEnabled()) {
      Toast.makeText(ExamplePOSActivity.this, "Turn on Bluetooth to connect 450 reader", Toast.LENGTH_SHORT).show();
      return false;
    } else {
      return true;
    }
  }

  private boolean isGPSEnabled() {
    LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
  }
}
