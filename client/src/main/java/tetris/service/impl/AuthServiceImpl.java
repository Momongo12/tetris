package tetris.service.impl;


import lombok.extern.log4j.Log4j2;
import tetris.model.dto.LoginRequestDto;
import tetris.model.dto.RegistrationRequestDto;
import tetris.service.AuthService;
import static tetris.util.ServiceUtil.*;

import java.io.IOException;
import java.net.HttpURLConnection;


/**
 * @author denMoskvin
 * @version 1.0
 */
@Log4j2
public class AuthServiceImpl implements AuthService {

    private static final String AUTHORIZATION_SERVER_BASE_URL = "http://localhost:8080/api/auth";

    public boolean authenticate(LoginRequestDto loginRequestDto) {
        HttpURLConnection connection = null;
        try {
            String loginUrl = AUTHORIZATION_SERVER_BASE_URL + "/login";

            connection = createConnection(loginUrl, "POST");

            sendRequest(connection, loginRequestDto);

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                log.info("Authorization successful");
                return true;
            } else {
                log.error("Authorization failed\nstatus:" + responseCode);
            }

        } catch (IOException e) {
            log.error("Authorization error", e);
        }finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return false;
    }

    public boolean registerPlayer(RegistrationRequestDto registrationRequestDto) {
        HttpURLConnection connection = null;
        try {
            String loginUrl = AUTHORIZATION_SERVER_BASE_URL + "/register";

            connection = createConnection(loginUrl, "POST");

            sendRequest(connection, registrationRequestDto);

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                log.info("Registration successful");
                return true;
            } else {
                log.error("Registration failed status:" + responseCode);
            }

        } catch (IOException e) {
            log.error("Registration error", e);
        }finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return false;
    }
}
