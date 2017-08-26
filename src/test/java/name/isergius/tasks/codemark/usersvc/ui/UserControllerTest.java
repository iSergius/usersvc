package name.isergius.tasks.codemark.usersvc.ui;


import name.isergius.tasks.codemark.usersvc.data.RoleRepository;
import name.isergius.tasks.codemark.usersvc.data.UserRepository;
import name.isergius.tasks.codemark.usersvc.model.Role;
import name.isergius.tasks.codemark.usersvc.model.User;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static java.util.Arrays.asList;
import static name.isergius.tasks.codemark.usersvc.ui.UserController.*;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Sergey Kondratyev
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserControllerTest {

    public static final String PROPERTY_NAME = "name";
    public static final String PROPERTY_LOGIN = "login";
    public static final String PROPERTY_PASSWORD = "password";
    public static final String PROPERTY_ROLES = "roles";

    public static final String VALUE_NAME = "Вася";
    public static final String VALUE_LOGIN = "vasa";
    public static final String VALUE_PASSWORD = "123123";
    public static final String VALUE_ROLE = "ADMIN";
    public static final long VALUE_ID = 1;
    public static final String PROPERTY_ID = "id";

    @Autowired
    private WebApplicationContext wac;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    private MockMvc mockMvc;

    private Role role;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        role = createRole();
    }

    @After
    public void tearDown() throws Exception {
        userRepository.deleteAll();
        roleRepository.deleteAll();
    }

    @Test
    public void testAdd_success() throws Exception {
        JSONArray roles = new JSONArray()
                .put(1);
        String content = new JSONObject()
                .put(PROPERTY_NAME, VALUE_NAME)
                .put(PROPERTY_LOGIN, VALUE_LOGIN)
                .put(PROPERTY_PASSWORD, VALUE_PASSWORD)
                .put(PROPERTY_ROLES, roles)
                .toString();
        mockMvc.perform(post(PATH_ADD)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().isCreated());
        assertTrue(userRepository.count() == 1);
    }

    @Test
    public void testGet_success() throws Exception {
        JSONArray roles = new JSONArray()
                .put(1);
        String content = new JSONObject()
                .put(PROPERTY_NAME, VALUE_NAME)
                .put(PROPERTY_LOGIN, VALUE_LOGIN)
                .put(PROPERTY_PASSWORD, VALUE_PASSWORD)
                .put(PROPERTY_ROLES, roles)
                .toString();
        createUser();

        mockMvc.perform(get(PATH_GET, VALUE_ID).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json(content));
    }

    @Test
    public void testGet_notExistUser() throws Exception {
        mockMvc.perform(get(PATH_GET, VALUE_ID).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDelete_success() throws Exception {
        int id = 1;
        createUser();
        mockMvc.perform(delete(PATH_DELETE, id))
                .andExpect(status().isOk());
        assertTrue(userRepository.count() == 0);
    }

    @Test
    public void testDelete_tryDeleteIsNotExistUser() throws Exception {
        long id = 1;
        mockMvc.perform(delete(PATH_DELETE, id))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testList_success() throws Exception {
        int id1 = 2;
        String name1 = "Петя";
        String login1 = "petr";
        String password1 = "321321";
        User user1 = new User(id1, name1, login1, password1, asList(role));
        userRepository.save(user1);
        int id2 = 3;
        String name2 = "Иван";
        String login2 = "ivan";
        String password2 = "456456";
        User user2 = new User(id2, name2, login2, password2, asList(role));
        userRepository.save(user2);

        String array = new JSONArray()
                .put(new JSONObject()
                        .put(PROPERTY_ID, id1)
                        .put(PROPERTY_NAME, name1)
                        .put(PROPERTY_LOGIN, login1)
                        .put(PROPERTY_PASSWORD, password1)
                        .put(PROPERTY_ROLES, new JSONArray()
                                .put(1)))
                .put(new JSONObject()
                        .put(PROPERTY_ID, id2)
                        .put(PROPERTY_NAME, name2)
                        .put(PROPERTY_LOGIN, login2)
                        .put(PROPERTY_PASSWORD, password2)
                        .put(PROPERTY_ROLES, new JSONArray()
                                .put(1)))
                .toString();

        mockMvc.perform(get(PATH_LIST).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(array));
    }

    @Test
    public void testList_empty() throws Exception {
        mockMvc.perform(get(PATH_LIST).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    private User createUser() {
        List<Role> roles = asList(roleRepository.findOne(VALUE_ID));
        return userRepository.save(new User(VALUE_ID, VALUE_NAME, VALUE_LOGIN, VALUE_PASSWORD, roles));
    }

    private Role createRole() {
        return roleRepository.save(new Role(VALUE_ID, VALUE_ROLE));
    }
}
