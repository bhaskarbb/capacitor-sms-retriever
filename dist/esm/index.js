import { registerPlugin } from '@capacitor/core';
const SmsRetriever = registerPlugin('SmsRetriever', {
    web: () => import('./web').then(m => new m.SmsRetrieverWeb()),
});
export * from './definitions';
export { SmsRetriever };
//# sourceMappingURL=index.js.map