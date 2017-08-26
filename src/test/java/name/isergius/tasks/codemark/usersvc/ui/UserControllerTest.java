package name.isergius.tasks.codemark.usersvc.ui;

import name.isergius.tasks.codemark.usersvc.UsersvcApplication;
import name.isergius.tasks.codemark.usersvc.data.RoleRepository;
import name.isergius.tasks.codemark.usersvc.domain.UserInteractor;
import name.isergius.tasks.codemark.usersvc.model.Role;
import name.isergius.tasks.codemark.usersvc.model.User;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static java.util.Arrays.asList;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    public static final String PATH_ADD = "/add";
    public static final String PATH_GET = "/get/{id}";

    @Autowired
    private WebApplicationContext wac;
    @InjectMocks
    @Autowired
    private UserController controller;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UsersvcApplication application;
    @Mock
    private UserInteractor userInteractor;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
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
    }

    @Test
    public void testAdd_savingUserInInteractor() throws Exception {
        Role role = new Role(1, "ADMIN");
        roleRepository.save(role);
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
                .content(content));

        Mockito.verify(userInteractor)
                .add(eq(new User(0, VALUE_NAME, VALUE_LOGIN, VALUE_PASSWORD, asList(role))));
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
        when(userInteractor.get(1))
                .thenReturn(new User(1, VALUE_NAME, VALUE_LOGIN, VALUE_PASSWORD, asList(new Role(1, "ADMIN"))));

        mockMvc.perform(get("/get/{id}", 1).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json(content));
    }

    @Test
    public void testGet_notExistUser() throws Exception {
        mockMvc.perform(get(PATH_GET, 1).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
