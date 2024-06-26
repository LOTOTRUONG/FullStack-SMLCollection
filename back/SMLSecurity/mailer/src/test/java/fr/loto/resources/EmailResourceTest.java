package fr.loto.resources;

import fr.loto.dto.Email;
import fr.loto.entities.ClientEntity;
import fr.loto.repository.ClientRepository;
import fr.loto.service.ApiKeyService;
import io.quarkus.mailer.Mailer;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

class EmailResourceTest {
    @Mock
    ApiKeyService apiKeyService;

    @Mock
    Mailer mailer;

    @Mock
    ClientRepository clientRepository;


    @InjectMocks
    EmailResource emailResource;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @Order(1)
    @Transactional
    void testValidApiKey() {

        // Test with a valid API key
        Email email = new Email();
        email.setSendTo("pelovejeily@gmail.com");
        email.setSubject("TEST SEND EMAIL");
        email.setText("Here is email to test valid apikey");
        String apiKey = "ZJlrcQJcgQUFjUwR";
        ClientEntity clientEntity = new ClientEntity();
        clientEntity.setMonthlyQuota(100);

        when(clientRepository.findByApiKey(eq(apiKey))).thenReturn(clientEntity);
        when(apiKeyService.getCountEmail(eq(apiKey))).thenReturn(Response.ok("{\"emailCountByMonth\": 50}").build());


        Response response = emailResource.sendEmail(email, apiKey);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals("Success sending email to pelovejeily@gmail.com", response.getEntity());
        verify(mailer, times(1)).send(any());
    }
    @Test
    @Order(2)
    void testInsufficientQuota(){
        Email email = new Email();
        email.setSendTo("pelovejeily@gmail.com");
        email.setSubject("TEST SEND EMAIL");
        email.setText("Here is email to test valid apikey");
        String apiKey = "k81yAZKGyyvfRDyM";
        ClientEntity clientEntity = new ClientEntity();
        clientEntity.setMonthlyQuota(1);
        when(clientRepository.findByApiKey(eq(apiKey))).thenReturn(clientEntity);
        when(apiKeyService.getCountEmail(eq(apiKey))).thenReturn(Response.ok("{\"emailCountByMonth\": 50}").build());
        Response response = emailResource.sendEmail(email, apiKey);
        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.getStatus());
    }
    @Test
    @Order(3)
    void testNoApiKey(){
        // Test with a valid API key
        Email email = new Email();
        email.setSendTo("pelovejeily@gmail.com");
        email.setSubject("TEST SEND EMAIL");
        email.setText("Here is email to test valid apikey");

        Response response = emailResource.sendEmail(email, null);
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        assertEquals("Invalid api key provided", response.getEntity());
    }

    @Test
    @Order(4)
    void testInvalidApiKey() {
        // Test with an invalid API key
        Email email = new Email();
        email.setSendTo("pelovejeily@gmail.com");
        email.setSubject("TEST SEND EMAIL");
        email.setText("Here is a test for invalid apikey");
        String apiKey = "invalid_api_key";

        // Call the method and assert the response
        Response response = emailResource.sendEmail(email, apiKey);
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        assertEquals("Invalid API key provided", response.getEntity());

    }
}