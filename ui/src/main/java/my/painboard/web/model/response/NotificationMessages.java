package my.painboard.web.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NotificationMessages {

    USER_SUCCESSFULLY_REGISTERED("User was registered.");

    private String notificationMessage;
}
