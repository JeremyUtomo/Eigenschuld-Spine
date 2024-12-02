package ngo.spine.eigenschuldapi.Exception;

import org.apache.tomcat.websocket.*;

public class CustomAuthenticationException extends AuthenticationException {
    public CustomAuthenticationException(String message) {
        super(message);
    }
}
