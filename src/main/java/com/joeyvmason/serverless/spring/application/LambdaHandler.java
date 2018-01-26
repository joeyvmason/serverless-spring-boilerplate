package com.joeyvmason.serverless.spring.application;

import com.amazonaws.serverless.exceptions.ContainerInitializationException;
import com.amazonaws.serverless.proxy.internal.model.AwsProxyRequest;
import com.amazonaws.serverless.proxy.internal.model.AwsProxyResponse;
import com.amazonaws.serverless.proxy.internal.testutils.MockLambdaContext;
import com.amazonaws.serverless.proxy.spring.SpringLambdaContainerHandler;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

public class LambdaHandler implements RequestHandler<AwsProxyRequest, AwsProxyResponse> {
    private static final Logger LOG = LoggerFactory.getLogger(LambdaHandler.class);

    private static SpringLambdaContainerHandler<AwsProxyRequest, AwsProxyResponse> handler;

    @Override
    public AwsProxyResponse handleRequest(AwsProxyRequest awsProxyRequest, Context context) {
        if (handler == null) {

            try {
                handler = SpringLambdaContainerHandler.getAwsProxyHandler(MvcConfig.class);
            } catch (ContainerInitializationException e) {
                LOG.warn("Unable to create handler", e);
                return null;
            }
        }

        return handler.proxy(awsProxyRequest, context);
    }

}
