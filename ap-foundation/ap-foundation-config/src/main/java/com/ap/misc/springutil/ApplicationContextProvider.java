package com.ap.misc.springutil;

import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

@SuppressWarnings("unchecked")
public class ApplicationContextProvider implements ApplicationContextAware {

    private static ApplicationContext applicationContext = null;

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
       ApplicationContextProvider.applicationContext = applicationContext;
    }

    public static <T> T getBeanByName(String beanName) {
        return (T) getContext().getBean(beanName);
    }

    public static <T> Map<String, T> getBeansOfType(Class<T> type) {
        return getContext().getBeansOfType(type);
    }

    public static ApplicationContext getContext() {
        if (applicationContext == null) {
            throw new IllegalStateException("The applicationContext member has not been injected (yet).");
        }
        return applicationContext;
    }
}


