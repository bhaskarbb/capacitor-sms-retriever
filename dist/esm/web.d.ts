import { WebPlugin } from '@capacitor/core';
import type { SmsRetrieverPlugin } from './definitions';
export declare class SmsRetrieverWeb extends WebPlugin implements SmsRetrieverPlugin {
    echo(options: {
        value: string;
    }): Promise<{
        value: string;
    }>;
}
