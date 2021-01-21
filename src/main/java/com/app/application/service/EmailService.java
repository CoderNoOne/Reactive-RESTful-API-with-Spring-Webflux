package com.app.application.service;

import com.app.application.dto.CreateMailDto;
import com.app.application.dto.MailDto;
import com.app.application.exception.EmailServiceException;
import com.app.application.validator.CreateMailDtoValidator;
import com.app.application.validator.util.Validations;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final CreateMailDtoValidator createMailDtoValidator;


    public Mono<MailDto> sendSingleEmail(CreateMailDto createMailDto) {

        var errors = createMailDtoValidator.validate(createMailDto);

        if (Validations.hasErrors(errors)) {
            throw new EmailServiceException("Mail is not valid. Errors are: [%s]".formatted(Validations.createErrorMessage(errors)));
        }

        return Mono.fromCallable(() -> sendAsHtml(createMailDto))
                .subscribeOn(Schedulers.boundedElastic())
                .map(CreateMailDto::toMailDto);
    }


    private MimeMessage createMimeMessage(String to, String htmlContent, String title) {

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, false);
            messageHelper.setText(htmlContent, true);
            messageHelper.setTo(to);
            messageHelper.setSubject(title);
            return mimeMessage;
        } catch (MessagingException e) {
            log.info("Exception during message generation");
            log.error(e.getMessage(), e);
            throw new EmailServiceException(e.getMessage());
        }
    }

    private void sendBulk(MimeMessage... messages) {
        mailSender.send(messages);
    }

    private CreateMailDto sendAsHtml(CreateMailDto createMailDto) {

        try {
            MimeMessage mimeMessage = createMimeMessage(createMailDto.getTo(), createMailDto.getHtmlContent(), createMailDto.getTitle());
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, false);
            messageHelper.setText(createMailDto.getHtmlContent(), true);
            messageHelper.setFrom(Objects.requireNonNullElse(createMailDto.getFrom(), "noreply@domain.com"));
            messageHelper.setTo(createMailDto.getTo());
            messageHelper.setSubject(createMailDto.getTitle());
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.info("Exception during mail sending");
            log.error(e.getMessage(), e);
            throw new EmailServiceException(e.getMessage());
        }

        return createMailDto;
    }

}
