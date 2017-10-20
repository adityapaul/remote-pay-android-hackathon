package com.clover.remote.client.clovergo;

/**
 * Created by Akhani, Avdhesh on 4/18/17.
 */

import android.content.Context;

import com.clover.remote.client.CloverDeviceConfiguration;
import com.clover.remote.client.transport.CloverTransport;
import com.firstdata.clovergo.domain.model.ReaderInfo;

/**
 * Builder for building CloverGo instance
 */
public class CloverGoDeviceConfiguration implements CloverDeviceConfiguration {

    private final String apiKey;
    private final String secret;
    private final Context context;
    private final String accessToken;
    private final ENV env;
    private final String appId;
    private long scanPeriod = 30000;
    private boolean allowDuplicate = false;
    private boolean autoConnect = false;
    private ReaderInfo.ReaderType readerType = ReaderInfo.ReaderType.RP450;

    private CloverGoDeviceConfiguration(Context context, String accessToken, ENV env, String apiKey, String secret, String appId) {
        this.context = context;
        this.accessToken = accessToken;
        this.env = env;
        this.apiKey = apiKey;
        this.secret = secret;
        this.appId = appId;
    }

    @Override
    public String getCloverDeviceTypeName() {
        return "Clover Go";
    }

    @Override
    public String getMessagePackageName() {
        return "com.clover.remote.client.clovergo";
    }

    @Override
    public String getName() {
        return "Clover Go Device Configuration";
    }

    @Override
    public int getMaxMessageCharacters() {
        return 0;
    }

    @Override
    public CloverTransport getCloverTransport() {
        return null;
    }

    @Override
    public String getApplicationId() {
        return appId;
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getSecret() {
        return secret;
    }

    public Context getContext() {
        return context;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public ENV getEnv() {
        return env;
    }

    public long getScanPeriod() {
        return scanPeriod;
    }

    public boolean isAllowDuplicate() {
        return allowDuplicate;
    }

    public boolean isAutoConnect() {
        return autoConnect;
    }

    public ReaderInfo.ReaderType getReaderType() {
        return readerType;
    }

    public static class Builder {

        CloverGoDeviceConfiguration mCLoCloverGoConfiguration;

        /**
         * Build CloverGo Object with AccessToken(mandatory parameter to initialize Clover Go Object)
         *
         * @param accessToken access token received from the OAuth request
         * @param apiKey      API Key
         * @param secret      API Secret
         * @return CloverGoBuilder instance
         */
        public Builder(Context context, String accessToken, ENV env, String apiKey, String secret, String appId) {
            mCLoCloverGoConfiguration = new CloverGoDeviceConfiguration(context, accessToken, env, apiKey, secret, appId);
        }

        /**
         * Toggles switch to allow duplicate transactions. Optional setting. Default is false (no duplicates allowed.)
         *
         * @param allowDuplicate Boolean field whether to allow duplicates
         * @return CloverGoBuilder instance
         */
        public Builder overrideDuplicateTransaction(boolean allowDuplicate) {
            mCLoCloverGoConfiguration.allowDuplicate = allowDuplicate;
            return this;
        }

        /**
         * Sets the scan timeout (how long the phone will look for the RP450 bluetooth card reader.) Optional setting.  Default is 30 second scan timeout.
         *
         * @param timeOut Scan period in milliseconds to scan for RP450 readers.
         * @return CloverGoBuilder instance
         */
        public Builder scanTimeOut(long timeOut) {
            mCLoCloverGoConfiguration.scanPeriod = timeOut;
            return this;
        }


        /**
         * Toggles setting to allow auto-connect to last connected RP450 card reader when app comes to foreground. Optional setting.  Default is false (disabled.)
         *
         * @param autoConnect Boolean field to enable auto connect to last connected RP450
         * @return CloverGoBuilder instance
         */
        public Builder allowAutoConnect(boolean autoConnect) {
            mCLoCloverGoConfiguration.autoConnect = autoConnect;
            //if (autoConnect)
            //   cloverGoConnector.registerActivityCallbacks();
            return this;
        }

        /**
         * Sets the ReaderInfo.ReaderType of the device being configured
         * @param readerType
         * @return CloverGoBuilder instance
         */
        public Builder deviceType(ReaderInfo.ReaderType readerType) {
            mCLoCloverGoConfiguration.readerType = readerType;
            //if (autoConnect)
            //   cloverGoConnector.registerActivityCallbacks();
            return this;
        }

        /**
         * Returns the configured (built) CloverGoDeviceConfiguration instance
         *
         * @return CloverGoDeviceConfiguration instance
         */
        public CloverGoDeviceConfiguration build() {
            return mCLoCloverGoConfiguration;
        }
    }

    public enum ENV {
        LIVE, DEMO, SANDBOX, SANDBOX_DEV
    }
}