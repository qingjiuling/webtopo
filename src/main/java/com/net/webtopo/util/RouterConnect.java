package com.net.webtopo.util;

import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;


import javax.servlet.ServletContext;

@Component
public class RouterConnect implements ServletContextAware {

    public static TelnetClient instance;

    @Override
    public void setServletContext(ServletContext servletContext) {
        instance = new TelnetClient("VT220","#");
    }
}
