package de.atron.randomtime.filter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.annotation.Priority;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

@Provider
@Priority(0)
public class CustomLoggingFilter implements ContainerRequestFilter, ContainerResponseFilter {
	private final Logger log = LoggerFactory.getLogger(CustomLoggingFilter.class);

	@Context
	private ResourceInfo resourceInfo;

	@Context
	ServletConfig servletConfig;

	@Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        MDC.put("start-time", String.valueOf(System.currentTimeMillis()));
        MDC.put("request_id", UUID.randomUUID().toString());
        MDC.put("REQUEST_ID", UUID.randomUUID().toString());
		MDC.put("port",  requestContext.getUriInfo().getBaseUri().toString());

        log.warn("Method Name : {} ", resourceInfo.getResourceMethod().getName());
        log.warn("Class : %s ", resourceInfo.getResourceClass().getCanonicalName());
        logQueryParameters(requestContext);
        logMethodAnnotations();
        logRequestHeader(requestContext);

        //log entity stream...
        String entity = readEntityStream(requestContext);
        if(null != entity && entity.trim().length() > 0) {
            log.warn("Entity Stream : {}",entity);
        }
    }

	private void logQueryParameters(ContainerRequestContext requestContext) {
		Iterator<String> iterator = requestContext.getUriInfo().getPathParameters().keySet().iterator();
		while (iterator.hasNext()) {
			String name = iterator.next();
			List<String> obj = requestContext.getUriInfo().getPathParameters().get(name);
			String value = null;
			if (null != obj && obj.size() > 0) {
				value = obj.get(0);
			}
			log.debug("Query Parameter Name: {}, Value :{}", name, value);
		}
	}

	private void logMethodAnnotations() {
		Annotation[] annotations = resourceInfo.getResourceMethod().getDeclaredAnnotations();
		if (annotations != null && annotations.length > 0) {
			log.debug("----Start Annotations of resource ----");
			for (Annotation annotation : annotations) {
				log.debug(annotation.toString());
			}
			log.debug("----End Annotations of resource----");
		}
	}

	private void logRequestHeader(ContainerRequestContext requestContext) {
		Iterator<String> iterator;
		log.debug("----Start Header Section of request ----");
		log.debug("Method Type : {}", requestContext.getMethod());
		iterator = requestContext.getHeaders().keySet().iterator();
		while (iterator.hasNext()) {
			String headerName = iterator.next();
			String headerValue = requestContext.getHeaderString(headerName);
			log.debug("Header Name: {}, Header Value :{} ", headerName, headerValue);
		}
		log.debug("----End Header Section of request ----");
	}

	private String readEntityStream(ContainerRequestContext requestContext) {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		final InputStream inputStream = requestContext.getEntityStream();
		final StringBuilder builder = new StringBuilder();
		try {
			ReaderWriter.writeTo(inputStream, outStream);
			byte[] requestEntity = outStream.toByteArray();
			if (requestEntity.length == 0) {
				builder.append("");
			} else {
				builder.append(new String(requestEntity));
			}
			requestContext.setEntityStream(new ByteArrayInputStream(requestEntity));
		} catch (IOException ex) {
			log.debug("----Exception occurred while reading entity stream :{}", ex.getMessage());
		}
		return builder.toString();
	}

	@Override
	public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext)
			throws IOException {
		String stTime = MDC.get("start-time");
		if (null == stTime || stTime.length() == 0) {
			return;
		}
		long startTime = Long.parseLong(stTime);
		long executionTime = System.currentTimeMillis() - startTime;
		log.debug("Total request execution time : {} milliseconds", executionTime);
		// clear the context on exit
		MDC.clear();
	}
}