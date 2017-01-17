# serverless-spring-boilerplate [ ![Codeship Status for joeyvmason/serverless-spring-boilerplate](https://codeship.com/projects/40846020-bccc-0134-bc23-4ec7667e0bdc/status?branch=master)](https://codeship.com/projects/195929)

Serverless Boilerplate for Spring Framework and AWS Lambda

### About

Do you love Spring MVC? Are you interested in developing serverless applications with AWS Lambda? Well, then this is the boilerplate for you! This project demonstrates how to run a Spring MVC application on AWS Lambda, and how to write tests for said appplication. 

This project would not be possible without [aws-serverless-java-container](https://github.com/awslabs/aws-serverless-java-container). The Spring branch for this aws-serverless-java-container is not yet complete, so for now, I've included libs from my personal fork until it's up on Maven Central.

### Build

`$ gradle shadowJar`

### Deploy

1. Create Lambda function
	- Triggers: None
	- Runtime: `Java 8 Runtime`
	- Handler: `com.joeyvmason.serverless.spring.application.LambdaHandler`
	- Role: `Create new Role from Template`
	- Policy: `Simple Microservices permissions`

2. Create API Gateway API
	1. Create Resource
		- Configure as proxy resource: `True`
		- Resource name: `proxy`
		- Resource Path: `{proxy+}`
	2. Map resource to Lambda function
		- Select `Integration Request`
		- Integration Type: `Lambda Function`
		- Use Lambda Proxy Integration: `True`
		- Select region and Lambda function
		- `Actions` -> `Deploy API`

### Known Issues
- If `Content-Type` request header is `application/json` instead of `application/json;charset=UTF`, then providing `Accept-Encoding` values other than `UTF-8` (e.g. `gzip, deflate, sdch`) will result in 500 errors when running on AWS Lambda. Have not been able to reproduce this locally, but it happens without fail when run on AWS Lambda.
