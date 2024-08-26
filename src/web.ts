import { WebPlugin } from '@capacitor/core';

import type { SmsRetrieverPlugin } from './definitions';

export class SmsRetrieverWeb extends WebPlugin implements SmsRetrieverPlugin {
  async startSmsRetriever(): Promise<{ value: string }> {
    throw new Error('Method not implmented');
  }
}
