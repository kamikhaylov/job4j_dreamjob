package ru.job4j.dreamjob.persistence;

import org.apache.commons.dbcp2.BasicDataSource;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.job4j.dreamjob.Main;
import ru.job4j.dreamjob.dream.model.City;
import ru.job4j.dreamjob.dream.model.Post;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

public class PostDBStoreTest {
    private static BasicDataSource pool;

    @BeforeClass
    public static void before() {
        pool = new Main().loadPool();
    }

    @AfterMethod
    public void after() throws SQLException {
        try (PreparedStatement st = pool.getConnection().prepareStatement(
                "DELETE FROM post;"
                     + "ALTER TABLE post ALTER COLUMN id RESTART WITH 1;")) {
            st.execute();
        }
    }

    @DataProvider(name = "postProvider")
    private Object[][] dataPost() {
        return new Object[][] {
                {
                    new Post(1, "Java Job", "desc", LocalDateTime.now(), true,
                        new City(1, "Москва"))
                },
                {
                    new Post(2, "Java", "Dev", LocalDateTime.now(), false,
                        new City(2, "Спб"))
                }
        };
    }

    @Test(dataProvider = "postProvider")
    public void whenCreatePost(Post post) {
        PostDBStore store = new PostDBStore(pool);

        boolean addResult = store.add(post);
        Post postInDb = store.findById(post.getId());
        List<Post> posts = store.findAll();

        assertTrue(addResult);
        assertNotNull(postInDb);
        assertEquals(posts.size(), 1);
        assertEquals(postInDb.getId(), post.getId());
        assertEquals(postInDb.getName(), post.getName());
        assertEquals(postInDb.getDescription(), post.getDescription());
        assertEquals(postInDb.getCreated(), post.getCreated());
        assertEquals(postInDb.getCity().getId(), post.getCity().getId());
    }

    @Test()
    public void whenUpdatePost() {
        PostDBStore store = new PostDBStore(pool);
        Post post = new Post(1, "Java Job", "desc", LocalDateTime.now(), true,
                new City(1, "Москва"));
        boolean addResult = store.add(post);
        Post updatedPost = new Post(post.getId(), "Java", "Dev", LocalDateTime.now(), false,
                new City(2, "Спб"));

        boolean updateResult = store.update(updatedPost);
        Post postInDb = store.findById(post.getId());
        List<Post> posts = store.findAll();

        assertTrue(addResult);
        assertTrue(updateResult);
        assertNotNull(postInDb);
        assertEquals(posts.size(), 1);
        assertEquals(postInDb.getId(), post.getId());
        assertEquals(postInDb.getId(), updatedPost.getId());
        assertEquals(postInDb.getName(), updatedPost.getName());
        assertEquals(postInDb.getDescription(), updatedPost.getDescription());
        assertEquals(postInDb.getCreated(), updatedPost.getCreated());
        assertEquals(postInDb.getCity().getId(), updatedPost.getCity().getId());
    }
}