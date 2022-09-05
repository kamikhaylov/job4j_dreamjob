package ru.job4j.dreamjob.dream.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

import static java.util.Objects.isNull;

@Component
public class AuthFilter implements Filter {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthFilter.class.getName());
    private static final String USER = "user";
    private static final String LOGIN_PAGE = "/loginPage";

    private final Set<String> addresses = Set.of(
            "formAddUser",
            "registration",
            "success",
            "fail",
            "loginPage",
            "login"
    );

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        LOGGER.info("AuthFilter.doFilter");

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String uri = req.getRequestURI();
        if (addressPresent(uri)) {
            chain.doFilter(req, res);
            return;
        }
        if (isNull(req.getSession().getAttribute(USER))) {
            res.sendRedirect(req.getContextPath() + LOGIN_PAGE);
            return;
        }
        chain.doFilter(req, res);
    }

    private boolean addressPresent(String uri) {
        boolean result = false;
        for (String address : addresses) {
            if (uri.endsWith(address)) {
                result = true;
                break;
            }
        }
        return result;
    }
}
