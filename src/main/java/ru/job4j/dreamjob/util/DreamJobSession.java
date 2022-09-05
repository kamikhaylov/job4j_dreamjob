package ru.job4j.dreamjob.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import ru.job4j.dreamjob.dream.model.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public final class DreamJobSession {
    private static final Logger LOGGER =
            LoggerFactory.getLogger(DreamJobSession.class.getName());
    private static final String USER = "user";
    private static final String GUEST = "guest";

    private DreamJobSession() {
    }

    public static void create(User user, HttpServletRequest req) {
        LOGGER.info("DreamJobSession.create");

        HttpSession session = req.getSession();
        session.setAttribute(USER, user);
    }

    public static void check(Model model, HttpSession session) {
        LOGGER.info("DreamJobSession.check");

        User user = (User) session.getAttribute(USER);
        if (user == null) {
            user = new User();
            user.setName(GUEST);
        }
        model.addAttribute(USER, user);
    }

    public static void invalidate(HttpSession session) {
        LOGGER.info("DreamJobSession.invalidate");

        session.invalidate();
    }
}
