package com.clover.remote.client.clovergo;

import android.graphics.Bitmap;

import com.clover.remote.Challenge;
import com.clover.remote.InputOption;
import com.clover.remote.client.messages.AuthRequest;
import com.clover.remote.client.messages.CapturePreAuthRequest;
import com.clover.remote.client.messages.CloseoutRequest;
import com.clover.remote.client.messages.CustomActivityRequest;
import com.clover.remote.client.messages.ManualRefundRequest;
import com.clover.remote.client.messages.PreAuthRequest;
import com.clover.remote.client.messages.ReadCardDataRequest;
import com.clover.remote.client.messages.RefundPaymentRequest;
import com.clover.remote.client.messages.SaleRequest;
import com.clover.remote.client.messages.TipAdjustAuthRequest;
import com.clover.remote.client.messages.VerifySignatureRequest;
import com.clover.remote.client.messages.VoidPaymentRequest;
import com.clover.remote.order.DisplayOrder;
import com.firstdata.clovergo.domain.model.ReaderInfo;

import java.util.List;

/**
 * Created by Akhani, Avdhesh on 4/18/17.
 */

public class CloverGoConnector extends DefaultCloverGoConnector{

    private CloverGoDeviceConfiguration mCloverGoConfiguration;
    private static CloverGoConnectorImpl cloverGoConnectorImpl;

    public CloverGoConnector(CloverGoDeviceConfiguration mCloverGoConfiguration){
        this.mCloverGoConfiguration = mCloverGoConfiguration;
        if (cloverGoConnectorImpl == null)
                cloverGoConnectorImpl = new CloverGoConnectorImpl(broadcaster,mCloverGoConfiguration);
    }

    /**
     * Based on ReaderType, if Bluetooth, begins scanning for a card reader.  If audio jack card reader,
     * attempts to connect with the reader.
     */
    @Override
    public void initializeConnection() {
        cloverGoConnectorImpl.initializeConnection(mCloverGoConfiguration.getReaderType());
    }

    /**
     * Connects to BlueTooth card reader.
     *
     * @param readerInfo
     */
    @Override
    public void connectToBluetoothDevice(ReaderInfo readerInfo) {
        cloverGoConnectorImpl.connectToDevice(readerInfo);
    }

    /**
     * Disconnect the card reader.  For Bluetooth, frees up the card reader.  For audio jack, performs software disconnect
     */
    @Override
    public void disconnectDevice() {
        cloverGoConnectorImpl.disconnectDevice(mCloverGoConfiguration.getReaderType());
    }

    /**
     * Stop scanning for a bluetooth card reader
     */
    @Override
    public void stopDeviceScan() {
        cloverGoConnectorImpl.stopDeviceScan();
    }

    /**
     * Stubbed out for Clover Go
     */
    @Override
    public void dispose() {
        broadcaster.clear();
    }

    /**
     * Starts a card reader transaction
     * @param saleRequest
     */
    @Override
    public void sale(SaleRequest saleRequest) {
        cloverGoConnectorImpl.sale(saleRequest,mCloverGoConfiguration.getReaderType(),mCloverGoConfiguration.isAllowDuplicate());
    }


    @Override
    public void acceptPayment(com.clover.sdk.v3.payments.Payment payment) {
        cloverGoConnectorImpl.acceptPayment(payment);
    }

    @Override
    public void rejectPayment(com.clover.sdk.v3.payments.Payment payment, Challenge challenge) {
        cloverGoConnectorImpl.rejectPayment(payment,challenge);
    }

    @Override
    public void auth(AuthRequest authRequest) {
        cloverGoConnectorImpl.auth(authRequest, mCloverGoConfiguration.getReaderType(),mCloverGoConfiguration.isAllowDuplicate());
    }

    @Override
    public void preAuth(PreAuthRequest preAuthRequest) {
        cloverGoConnectorImpl.preAuth(preAuthRequest, mCloverGoConfiguration.getReaderType(),mCloverGoConfiguration.isAllowDuplicate());
    }

    @Override
    public void tipAdjustAuth(final TipAdjustAuthRequest authTipAdjustRequest) {
        cloverGoConnectorImpl.tipAdjustAuth(authTipAdjustRequest);
    }

    @Override
    public void capturePreAuth(final CapturePreAuthRequest capturePreAuthRequest) {
        cloverGoConnectorImpl.capturePreAuth(capturePreAuthRequest);
    }

    @Override
    public void voidPayment(final VoidPaymentRequest voidPaymentRequest) {
        cloverGoConnectorImpl.voidPayment(voidPaymentRequest);
    }

    @Override
    public void refundPayment(final RefundPaymentRequest refundPaymentRequest) {
        cloverGoConnectorImpl.refundPayment(refundPaymentRequest);
    }

    @Override
    public void closeout(CloseoutRequest closeoutRequest){
        cloverGoConnectorImpl.closeout(closeoutRequest);
    }

    /**
     * Sends signature data to servers.
     * @param paymentId
     * @param xy  coordinates of signature vectors
     */
    @Override
    public void captureSignature(String paymentId, int[][] xy) {
        cloverGoConnectorImpl.captureSignature(paymentId,xy);
    }

    /**
     * Sends a receipt to the email and/or phone (SMS)
     * @param email
     * @param phoneNo
     * @param orderId
     */
    @Override
    public void sendReceipt(String email, String phoneNo, String orderId) {
        cloverGoConnectorImpl.sendReceipt(email,phoneNo,orderId);
    }

    @Override
    public void cancel() {
        cloverGoConnectorImpl.cancelReaderTransaction(mCloverGoConfiguration.getReaderType());
    }

    @Override
    public void resetDevice() {
    }

    /////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////

    /**
     *  Not supported in Clover Go
     */
    @Override
    public void acceptSignature(VerifySignatureRequest request) throws UnsupportedOperationException{
        throw new UnsupportedOperationException("Operation Not supported in Clover Go");
    }

    /**
     *  Not supported in Clover Go
     */
    @Override
    public void rejectSignature(VerifySignatureRequest request) throws UnsupportedOperationException{
        throw new UnsupportedOperationException("Operation Not supported in Clover Go");
    }

    /**
     *  Not supported in Clover Go
     */
    @Override
    public void manualRefund(ManualRefundRequest request) throws UnsupportedOperationException{
        throw new UnsupportedOperationException("Operation Not supported in Clover Go");
    }

    /**
     *  Not supported in Clover Go
     */
    @Override
    public void vaultCard(Integer cardEntryMethods) throws UnsupportedOperationException{
        throw new UnsupportedOperationException("Operation Not supported in Clover Go");
    }

    /**
     *  Not supported in Clover Go
     */
    @Override
    public void retrievePendingPayments() throws UnsupportedOperationException{
        throw new UnsupportedOperationException("Operation Not supported in Clover Go");
    }

    /**
     *  Not supported in Clover Go
     */
    @Override
    public void readCardData(ReadCardDataRequest request) throws UnsupportedOperationException{
        throw new UnsupportedOperationException("Operation Not supported in Clover Go");
    }

    /**
     *  Not supported in Clover Go
     */
    @Override
    public void startCustomActivity(CustomActivityRequest request) throws UnsupportedOperationException{
        throw new UnsupportedOperationException("Operation Not supported in Clover Go");
    }

    /**
     *  Not supported in Clover Go
     */
    @Override
    public void showMessage(String message) throws UnsupportedOperationException{
        throw new UnsupportedOperationException("Operation Not supported in Clover Go");
    }

    /**
     *  Not supported in Clover Go
     */
    @Override
    public void showWelcomeScreen() throws UnsupportedOperationException{
        throw new UnsupportedOperationException("Operation Not supported in Clover Go");
    }

    /**
     *  Not supported in Clover Go
     */
    @Override
    public void showThankYouScreen() throws UnsupportedOperationException{
        throw new UnsupportedOperationException("Operation Not supported in Clover Go");
    }

    /**
     *  Not supported in Clover Go
     */
    @Override
    public void displayPaymentReceiptOptions(String orderId, String paymentId) throws UnsupportedOperationException{
        throw new UnsupportedOperationException("Operation Not supported in Clover Go");
    }

    /**
     *  Not supported in Clover Go
     */
    @Override
    public void openCashDrawer(String reason) throws UnsupportedOperationException{
        throw new UnsupportedOperationException("Operation Not supported in Clover Go");
    }

    /**
     *  Not supported in Clover Go
     */
    @Override
    public void showDisplayOrder(DisplayOrder order) throws UnsupportedOperationException{
        throw new UnsupportedOperationException("Operation Not supported in Clover Go");
    }

    /**
     *  Not supported in Clover Go
     */
    @Override
    public void removeDisplayOrder(DisplayOrder order) throws UnsupportedOperationException{
        throw new UnsupportedOperationException("Operation Not supported in Clover Go");
    }

    /**
     *  Not supported in Clover Go
     */
    @Override
    public void invokeInputOption(final InputOption io) throws UnsupportedOperationException{
        throw new UnsupportedOperationException("Operation Not supported in Clover Go");
    }

    /**
     *  Not supported in Clover Go
     */
    @Override
    public void printText(List<String> messages) throws UnsupportedOperationException{
        throw new UnsupportedOperationException("Operation Not supported in Clover Go");
    }

    /**
     *  Not supported in Clover Go
     */
    @Override
    public void printImage(Bitmap image) throws UnsupportedOperationException{
        throw new UnsupportedOperationException("Operation Not supported in Clover Go");
    }

    /**
     *  Not supported in Clover Go
     */
    @Override
    public void printImageFromURL(String url) throws UnsupportedOperationException{
        throw new UnsupportedOperationException("Operation Not supported in Clover Go");
    }
}