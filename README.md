# serverless-spring-boilerplate [ ![Codeship Status for joeyvmason/serverless-spring-boilerplate](https://codeship.com/projects/40846020-bccc-0134-bc23-4ec7667e0bdc/status?branch=master)](https://codeship.com/projects/195929)

Serverless Boilerplate for Spring Framework and AWS Lambda

### About

Do you love Spring MVC? Are you interested in developing serverless applications with AWS Lambda? Well, then this is the boilerplate for you! This project demonstrates how to run a Spring MVC application on AWS Lambda, and how to write tests for said appplication. For the data store, this uses a simple in-memory Map to store `Article` objects by ID. For a real-world serverless application, I would suggest using [DynamoDB](https://aws.amazon.com/dynamodb/).

This project would not be possible without [aws-serverless-java-container](https://github.com/awslabs/aws-serverless-java-container).

### Build

`$ gradle shadowJar`

### Deploy

1. Create Lambda function
	- Triggers: None
	- Runtime: `Java 8 Runtime`
	- Handler: `com.joeyvmason.serverless.spring.application.LambdaHandler`
	- Role: `Create new Role from Template`
	- Policy: `Simple Microservices permissions`
	- Jar: `build/libs/serverless-spring-boilerplate-all.jar`
	- Memory: Set to max

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
		
3. Test
	1. Create an article
		- Endpoint: `/articles`
		- Method: `POST`
		- Example: `curl -H "Content-Type: application/json" -X POST -d '{"title": "some title", "body": "some body"}'  {invoke_url}/{stage}/articles`
	
	2. Get all articles
		- Endpoint: `/articles`
		- Method: `GET`
		- Example: `curl {invoke_url}/{stage}/articles`
		
	3. Get article by id
		- Endpoint: `/articles/{article_id}`
		- Method: `GET`
		- Example: `curl {invoke_url}/{stage}/articles/{article_id}`
	
	4. Update an article
		- Endpoint: `/articles/{article_id}`
		- Method: `PUT`
		- Example: `curl -H "Content-Type: application/json" -X PUT -d '{"title": "new title", "body": "new body"}'  {invoke_url}/{stage}/articles/{article_id}`

