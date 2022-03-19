## Payment Service

* This is a microservice running on http://localhost:8089.
* We are using this application in Ecommerce front end https://github.com/Aasifali1/Ecommerce-angular-app 

## To access the rest api
* clone this repo.
* run all order-service, product, customer microservices.
* run kafka and create a topic "order-status"

## Stripe
* Create an account on stripe
* get stripe secret key and replace the values of "stripe.secret.key" in application.properties file.
* get stripe public key and replace the values of "stripe.public.key" in properties file.
* similarly create a webhook secret key and replace the values of "stripe.webhook.secret" in the same file.