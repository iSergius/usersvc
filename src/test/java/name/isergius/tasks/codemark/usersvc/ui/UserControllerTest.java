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
import org.springframework.cache.CacheManager;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static name.isergius.tasks.codemark.usersvc.ui.UserController.*;
import static org.hamcrest.Matchers.any;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
    @Autowired
    private CacheManager cacheManager;

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
        cacheManager.getCache("usersCache").clear();
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

        mockMvc.perform(post(BASE_PATH + PATH_ADD)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().isCreated());

        assertEquals(1, userRepository.count());
    }

    @Test
    public void testAdd_successResponse() throws Exception {
        JSONArray roles = new JSONArray()
                .put(role.getId());
        String requestBody = new JSONObject()
                .put(PROPERTY_NAME, VALUE_NAME)
                .put(PROPERTY_LOGIN, VALUE_LOGIN)
                .put(PROPERTY_PASSWORD, VALUE_PASSWORD)
                .put(PROPERTY_ROLES, roles)
                .toString();
        String responseBody = new JSONObject()
                .put("success", true)
                .toString();

        mockMvc.perform(post(BASE_PATH + PATH_ADD)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(content().json(responseBody));

        assertEquals(1, userRepository.count());
    }

    @Test
    public void testAdd_successLocation() throws Exception {
        JSONArray roles = new JSONArray()
                .put(role.getId());
        String requestBody = new JSONObject()
                .put(PROPERTY_NAME, VALUE_NAME)
                .put(PROPERTY_LOGIN, VALUE_LOGIN)
                .put(PROPERTY_PASSWORD, VALUE_PASSWORD)
                .put(PROPERTY_ROLES, roles)
                .toString();

        mockMvc.perform(post(BASE_PATH + PATH_ADD)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", any(String.class)));

        assertEquals(1, userRepository.count());
    }

    @Test
    public void testAdd_additionWrongUser() throws Exception {
        JSONArray roles = new JSONArray()
                .put(role.getId() + 1);
        String requestBody = new JSONObject()
                .put(PROPERTY_NAME, VALUE_NAME)
                .put(PROPERTY_LOGIN, VALUE_LOGIN)
                .put(PROPERTY_PASSWORD, VALUE_PASSWORD)
                .put(PROPERTY_ROLES, roles)
                .toString();
        String responseBody = new JSONObject()
                .put("success", false)
                .put("errors", new JSONArray()
                        .put(User.ROLE_CONSTRAINT_MESSAGE))
                .toString();

        mockMvc.perform(post(BASE_PATH + PATH_ADD)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(responseBody));
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

        mockMvc.perform(get(BASE_PATH + PATH_GET, user.getId()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json(content));
    }

    @Test
    public void testGet_notExistUser() throws Exception {
        mockMvc.perform(get(BASE_PATH + PATH_GET, VALUE_ID).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDelete_success() throws Exception {
        User user = createUser();
        mockMvc.perform(delete(BASE_PATH + PATH_DELETE, user.getId()))
                .andExpect(status().isOk());
        assertTrue(userRepository.count() == 0);
    }

    @Test
    public void testDelete_tryDeleteIsNotExistUser() throws Exception {
        long id = 1;
        mockMvc.perform(delete(BASE_PATH + PATH_DELETE, id))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testList_success() throws Exception {
        User user1 = userRepository.save(new User("Петя", "petr", "P321321", asList(role)));
        User user2 = userRepository.save(new User("Иван", "ivan", "P456456", asList(role)));

        String responseBody = new JSONObject()
                .put("content", new JSONArray()
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
                                        .put(role.getId()))))
                .put("last", true)
                .put("totalElements", 2)
                .put("totalPages", 1)
                .put("sort", null)
                .put("first", true)
                .put("numberOfElements", 2)
                .put("size", 10)
                .put("number", 0)
                .toString();

        mockMvc.perform(get(BASE_PATH + PATH_LIST).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(responseBody));
    }

    @Test
    public void testList_empty() throws Exception {
        String responseBody = new JSONObject()
                .put("content", new JSONArray())
                .put("last", true)
                .put("totalElements", 0)
                .put("totalPages", 0)
                .put("sort", null)
                .put("first", true)
                .put("numberOfElements", 0)
                .put("size", 10)
                .put("number", 0)
                .toString();

        mockMvc.perform(get(BASE_PATH + PATH_LIST).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(responseBody));
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

        mockMvc.perform(put(BASE_PATH + PATH_EDIT, user.getId())
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

        mockMvc.perform(put(BASE_PATH + PATH_EDIT, VALUE_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testEdit_errorResponse() throws Exception {
        JSONArray roles = new JSONArray()
                .put(1);
        String requestBody = new JSONObject()
                .put(PROPERTY_ID, VALUE_ID)
                .put(PROPERTY_NAME, VALUE_NAME)
                .put(PROPERTY_LOGIN, VALUE_LOGIN)
                .put(PROPERTY_PASSWORD, VALUE_PASSWORD)
                .put(PROPERTY_ROLES, roles)
                .toString();
        String responseBody = new JSONObject()
                .put("success", false)
                .put("errors", new JSONArray().put("User is not exist"))
                .toString();

        mockMvc.perform(put(BASE_PATH + PATH_EDIT, VALUE_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(responseBody));
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

        mockMvc.perform(put(BASE_PATH + PATH_EDIT, user.getId() + 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testEdit_responseOnEditUserByWrongPath() throws Exception {
        User user = createUser();
        JSONArray roles = new JSONArray()
                .put(1);
        String requestBody = new JSONObject()
                .put(PROPERTY_ID, user.getId())
                .put(PROPERTY_NAME, VALUE_NAME)
                .put(PROPERTY_LOGIN, VALUE_LOGIN)
                .put(PROPERTY_PASSWORD, VALUE_PASSWORD)
                .put(PROPERTY_ROLES, roles)
                .toString();
        String responseBody = new JSONObject()
                .put("success", false)
                .put("errors", new JSONArray().put("Path id and user id is not equals"))
                .toString();

        mockMvc.perform(put(BASE_PATH + PATH_EDIT, user.getId() + 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(responseBody));
    }

    @Test
    public void testEdit_successResponse() throws Exception {
        User user = createUser();
        Role role = roleRepository.save(new Role("USER"));
        String responseBody = new JSONObject()
                .put("success", true)
                .toString();

        String requestBody = new JSONObject()
                .put(PROPERTY_ID, user.getId())
                .put(PROPERTY_NAME, user.getName())
                .put(PROPERTY_LOGIN, user.getLogin())
                .put(PROPERTY_PASSWORD, user.getPassword())
                .put(PROPERTY_ROLES, new JSONArray().put(role.getId()))
                .toString();

        mockMvc.perform(put(BASE_PATH + PATH_EDIT, user.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(content().json(responseBody));
    }

    private User createUser() {
        List<Role> roles = asList(role);
        return userRepository.save(new User(VALUE_ID, VALUE_NAME, VALUE_LOGIN, VALUE_PASSWORD, roles));
    }

    private Role createRole() {
        return roleRepository.save(new Role(VALUE_ID, VALUE_ROLE));
    }
}
