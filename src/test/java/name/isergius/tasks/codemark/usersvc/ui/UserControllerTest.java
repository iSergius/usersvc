package name.isergius.tasks.codemark.usersvc.ui;

import name.isergius.tasks.codemark.usersvc.domain.UserInteractor;
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

import static org.mockito.Matchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @Mock
    private UserInteractor userInteractor;

    @InjectMocks
    @Autowired
    private UserController controller;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void testAdd_success() throws Exception {
        JSONArray roles = new JSONArray()
                .put(1)
                .put(3)
                .put(5);
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
        JSONArray roles = new JSONArray()
                .put(1)
                .put(3)
                .put(5);
        String content = new JSONObject()
                .put(PROPERTY_NAME, VALUE_NAME)
                .put(PROPERTY_LOGIN, VALUE_LOGIN)
                .put(PROPERTY_PASSWORD, VALUE_PASSWORD)
                .put(PROPERTY_ROLES, roles)
                .toString();
        mockMvc.perform(post(PATH_ADD)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content));

        Mockito.verify(userInteractor).add(eq(new User(0, VALUE_NAME, VALUE_LOGIN, VALUE_PASSWORD)));
    }
}
