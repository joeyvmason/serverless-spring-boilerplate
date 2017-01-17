# serverless-spring-boilerplate [ ![Codeship Status for joeyvmason/serverless-spring-boilerplate](https://codeship.com/projects/40846020-bccc-0134-bc23-4ec7667e0bdc/status?branch=master)](https://codeship.com/projects/195929)

Serverless Boilerplate for Spring Framework for AWS Lambda



### Known Issues
- If Content-Type is "application/json" instead of "application/json;charset=UTF", then providing Accept-Encoding values other than "UTF-8" (e.g. "gzip, deflate, sdch") will result in 500 errors when running on AWS Lambda.
