## bKash-Tokenized-Android-Kotlin

Hello there. Welcome to unofficial guide about integrating bKash payment gateway in your application. If you already have had enough with the documentation and API references provided officially by bKash, then this documentation is just for you. Below, I will try to explain the integration steps in detail pointing all the difficulties I've faced and how I came across them. 

## Official Links 
Before digging in to the main part, lets just share the current official API documentation links available for all. These links are subjected to change without any prior notice. I will try my best to keep them updated. 

* [bKash API Reference]( https://developer.bka.sh/v1.2.0-beta/reference)
* [bKash Payment Demo](https://merchantdemo.sandbox.bka.sh/frontend/checkout)

To generate access token and perform the necessary steps, you will need the following credentials provided directly by bKash -

- Sandbox username
- Sandbox password
- App key and
- App secret

## Needed Info
     {
        var bkashSandboxUsername = "your sandbox user name"
        var bkashSandboxPassword = "your sandbox password name"
        var bkashSandboxAppKey = "your sandbox app key"
        var bkashSandboxAppSecret = "your sandbox app secret"
        var sessionIdToken = ""
        var mode = "0011"
        var payerReference = "01770618575"
        var callbackURL = "your callback yourDomain.com"
        var merchantAssociationInfo = ""
        var amount = "30"
        var currency = "BDT"
        var intents = "sale"
        var merchantInvoiceNumber = "Inv0124"
    }

## Screenshots 
![Downloads3](https://user-images.githubusercontent.com/32567035/221949020-77786de8-ddac-4b6a-a356-f4255ab1460a.jpg)
![Collages1](https://user-images.githubusercontent.com/32567035/221949046-35eacb19-3afc-4d71-b523-868497ef3275.jpg)

## Screen-Record 
https://user-images.githubusercontent.com/32567035/221950345-d1d24c91-d41e-4831-857b-c015c2d14010.mp4



