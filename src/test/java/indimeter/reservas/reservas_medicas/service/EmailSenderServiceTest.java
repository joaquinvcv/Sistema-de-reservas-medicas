package indimeter.reservas.reservas_medicas.service;
import indimeter.reservas.reservas_medicas.service.EmailSenderService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EmailSenderServiceTest{
    



    @MockBean
    private JavaMailSender mailSender;

    @Autowired
    @Mock
    private EmailSenderService emailSenderService;

    @Test
    public void sendEmailTest() {
        // Arrange
        String toEmail = "eduardo.contreras1902@alumnos.ubiobio.cl";
        String subject = "Test Subject";
        String body = "Test Body";

        // Act

       emailSenderService.sendEmail(toEmail, subject, body);

        // Assert
        ArgumentCaptor<SimpleMailMessage> argumentCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender).send(argumentCaptor.capture());
        SimpleMailMessage message = argumentCaptor.getValue();
        assertEquals("correosporspring@gmail.com", message.getFrom());
        assertEquals(toEmail, message.getTo()[0]);
        assertEquals(body, message.getText());
        assertEquals(subject, message.getSubject());
    }

}