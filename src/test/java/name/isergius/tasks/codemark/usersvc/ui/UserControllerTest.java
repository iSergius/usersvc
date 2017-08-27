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

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static name.isergius.tasks.codemark.usersvc.ui.UserController.*;
import static org.junit.Assert.assertEquals;
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
    public static final String VALUE_PASSWORD = "P123123";
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
                .put(role.getId());
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
        User user = createUser();
        String content = new JSONObject()
                .put(PROPERTY_ID, user.getId())
                .put(PROPERTY_NAME, user.getName())
                .put(PROPERTY_LOGIN, user.getLogin())
                .put(PROPERTY_PASSWORD, user.getPassword())
                .put(PROPERTY_ROLES, new JSONArray()
                        .put(role.getId()))
                .toString();

        mockMvc.perform(get(PATH_GET, user.getId()).accept(MediaType.APPLICATION_JSON))
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
        User user = createUser();
        mockMvc.perform(delete(PATH_DELETE, user.getId()))
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
        User user1 = userRepository.save(new User("Петя", "petr", "P321321", asList(role)));
        User user2 = userRepository.save(new User("Иван", "ivan", "P456456", asList(role)));

        String array = new JSONArray()
                .put(new JSONObject()
                        .put(PROPERTY_ID, user1.getId())
                        .put(PROPERTY_NAME, user1.getName())
                        .put(PROPERTY_LOGIN, user1.getLogin())
                        .put(PROPERTY_PASSWORD, user1.getPassword())
                        .put(PROPERTY_ROLES, new JSONArray()
                                .put(role.getId())))
                .put(new JSONObject()
                        .put(PROPERTY_ID, user2.getId())
                        .put(PROPERTY_NAME, user2.getName())
                        .put(PROPERTY_LOGIN, user2.getLogin())
                        .put(PROPERTY_PASSWORD, user2.getPassword())
                        .put(PROPERTY_ROLES, new JSONArray()
                                .put(role.getId())))
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

    @Test
    public void testEdit_success() throws Exception {
        User user = createUser();
        Role role = roleRepository.save(new Role("USER"));
        String content = new JSONObject()
                .put(PROPERTY_ID, user.getId())
                .put(PROPERTY_NAME, user.getName())
                .put(PROPERTY_LOGIN, user.getLogin())
                .put(PROPERTY_PASSWORD, user.getPassword())
                .put(PROPERTY_ROLES, new JSONArray().put(role.getId()))
                .toString();

        mockMvc.perform(put(PATH_EDIT, user.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().isOk());

        User actualUser = userRepository.findOne(user.getId());
        List<Role> roles = new ArrayList<>(actualUser.getRoles());
        assertTrue(roles.size() == 1);
        Role r = roles.get(0);
        assertEquals(role.getId(), r.getId());
    }

    @Test
    public void testEdit_editIsNotExistUser() throws Exception {
        JSONArray roles = new JSONArray()
                .put(1);
        String content = new JSONObject()
                .put(PROPERTY_ID, VALUE_ID)
                .put(PROPERTY_NAME, VALUE_NAME)
                .put(PROPERTY_LOGIN, VALUE_LOGIN)
                .put(PROPERTY_PASSWORD, VALUE_PASSWORD)
                .put(PROPERTY_ROLES, roles)
                .toString();

        mockMvc.perform(put(PATH_EDIT, VALUE_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testEdit_editUserByWrongPath() throws Exception {
        User user = createUser();
        JSONArray roles = new JSONArray()
                .put(1);
        String content = new JSONObject()
                .put(PROPERTY_ID, user.getId())
                .put(PROPERTY_NAME, VALUE_NAME)
                .put(PROPERTY_LOGIN, VALUE_LOGIN)
                .put(PROPERTY_PASSWORD, VALUE_PASSWORD)
                .put(PROPERTY_ROLES, roles)
                .toString();

        mockMvc.perform(put(PATH_EDIT, user.getId() + 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().isBadRequest());
    }

    private User createUser() {
        List<Role> roles = asList(role);
        return userRepository.save(new User(VALUE_ID, VALUE_NAME, VALUE_LOGIN, VALUE_PASSWORD, roles));
    }

    private Role createRole() {
        return roleRepository.save(new Role(VALUE_ID, VALUE_ROLE));
    }
}
