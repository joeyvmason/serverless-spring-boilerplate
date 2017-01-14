package com.joeyvmason.serverless.spring.application;

import com.amazonaws.serverless.exceptions.ContainerInitializationException;
import com.amazonaws.serverless.proxy.internal.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.internal.model.AwsProxyResponse;
import com.amazonaws.serverless.proxy.spring.SpringLambdaContainerHandler;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

public class LambdaHandler implements RequestHandler<AwsProxyRequest, AwsProxyResponse> {

    SpringLambdaContainerHandler<AwsProxyRequest, AwsProxyResponse> handler;
    boolean initialized = false;

    public AwsProxyResponse handleRequest(AwsProxyRequest awsProxyRequest, Context context) {
        if (!initialized) {
            initialized = true;
            try {
                AnnotationConfigWebApplicationContext applicationContext = new AnnotationConfigWebApplicationContext();
                applicationContext.register(MvcConfig.class);
                handler = SpringLambdaContainerHandler.getAwsProxyHandler(applicationContext);
            } catch (ContainerInitializationException e) {
                e.printStackTrace();
                return null;
            }
        }

        return handler.proxy(awsProxyRequest, context);
    }
}
