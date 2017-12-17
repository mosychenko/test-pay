Get Token - curl -v -k https://localhost:8443/oauth2/token -H "Accept: application/json" -H "Accept-Language: en_US" -u "111:password" -d "grant_type=client_credentials"

// send request by RestTemplate to webhook endpoint not working - SSL Certificate verification error. Not Fixed.
Send pay - curl -v -k https://localhost:8443/payments/payment -H "Content-Type:application/json" -H "Authorization: Bearer cRJ5XHgVljF6osYa" -d '{"intent":"order","notification_url":"https://localhost:8443/webhook/test","payer":{"email":"test@example.com"},"transaction":{"external_id":"123456789","amount":{"value":"300","currency":"USD"},"description":"The payment transaction description"}}'

