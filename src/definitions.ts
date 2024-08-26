export interface PluginListenerHandle {
  remove: () => void;
}

export interface SmsRetrieverPlugin {
  startSmsRetriever(): Promise<{ value: string }>;
  addListener(
    eventName: 'otpReceived',
    listenerFunc: (info: { otp: string }) => void
  ): PluginListenerHandle;
  addListener(
    eventName: 'otpTimeout',
    listenerFunc: () => void
  ): PluginListenerHandle;
}
