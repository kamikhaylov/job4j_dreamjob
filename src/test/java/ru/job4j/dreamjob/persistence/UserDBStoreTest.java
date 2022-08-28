package ru.job4j.dreamjob.persistence;

import org.apache.commons.dbcp2.BasicDataSource;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.job4j.dreamjob.Main;
import ru.job4j.dreamjob.dream.model.User;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class UserDBStoreTest {
    private static final String EMAIL = "test@email.ru";
    private static final String PASSWORD = "123";

    private static BasicDataSource pool;

    @BeforeClass
    public static void before() {
        pool = new Main().loadPool();
    }

    @AfterMethod
    public void after() throws SQLException {
        try (PreparedStatement st = pool.getConnection().prepareStatement(
                "DELETE FROM users;"
                     + "ALTER TABLE users ALTER COLUMN id RESTART WITH 1;")) {
            st.execute();
        }
    }

    //@Test()
    public void whenCreateUser() {
        UserDBStore store = new UserDBStore(pool);
        User user1 = new User(0, "test1@email.ru", "user1", "123");
        User user2 = new User(0, "test2@email.ru", "user2", "123");

        Optional<User> addResult1 = store.add(user1);
        Optional<User> addResult2 = store.add(user2);
        List<User> users = store.findAll();

        assertNotNull(addResult1);
        assertNotNull(addResult2);
        assertEquals(users.size(), 2);
        assertEquals(addResult1.get().getEmail(), user1.getEmail());
        assertEquals(addResult1.get().getName(), user1.getName());
        assertEquals(addResult1.get().getPassword(), user1.getPassword());
        assertEquals(addResult2.get().getEmail(), user2.getEmail());
        assertEquals(addResult2.get().getName(), user2.getName());
        assertEquals(addResult2.get().getPassword(), user2.getPassword());
    }

    @Test()
    public void whenCreateUserThenFail() {
        UserDBStore store = new UserDBStore(pool);
        User user1 = new User(0, EMAIL, "user1", PASSWORD);
        User user2 = new User(0, EMAIL, "user2", PASSWORD);

        Optional<User> addResult1 = store.add(user1);
        Optional<User> addResult2 = store.add(user2);
        List<User> users = store.findAll();

        assertNotNull(addResult1);
        assertEquals(addResult2, Optional.empty());
        assertEquals(users.size(), 1);
        assertEquals(addResult1.get().getEmail(), EMAIL);
        assertEquals(addResult1.get().getName(), user1.getName());
        assertEquals(addResult1.get().getPassword(), PASSWORD);
    }

    @Test()
    public void whenLogin() {
        UserDBStore store = new UserDBStore(pool);
        User user = new User(0, EMAIL, "user", PASSWORD);
        store.add(user);

        Optional<User> result = store.findUserByEmailAndPwd(EMAIL, PASSWORD);

        assertNotNull(result);
        assertEquals(result.get().getEmail(), EMAIL);
        assertEquals(result.get().getName(), user.getName());
        assertEquals(result.get().getPassword(), PASSWORD);
    }

    @DataProvider(name = "loginProvider")
    private Object[][] loginData() {
        return new Object[][] {
                {
                        "not", PASSWORD
                },
                {
                        EMAIL, "321"
                },
                {
                        EMAIL, null
                },
                {
                        null, PASSWORD
                },
                {
                        null, null
                }
        };
    }

    @Test(dataProvider = "loginProvider")
    public void whenLoginFail(String email, String password) {
        UserDBStore store = new UserDBStore(pool);
        User user = new User(0, EMAIL, "user", PASSWORD);
        store.add(user);

        Optional<User> result = store.findUserByEmailAndPwd(email, password);

        assertEquals(result, Optional.empty());
    }
}