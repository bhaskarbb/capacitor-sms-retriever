import { SmsRetriever } from 'sms-retriever';

window.testEcho = () => {
    const inputValue = document.getElementById("echoInput").value;
    SmsRetriever.echo({ value: inputValue })
}
