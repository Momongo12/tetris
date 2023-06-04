package tetris.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import tetris.model.dto.LoginRequestDto;
import tetris.model.dto.RegistrationRequestDto;
import tetris.service.AuthService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

@Log4j2
public class AuthServiceImpl implements AuthService {

    private static final String AUTHORIZATION_SERVER_BASE_URL = "http://localhost:8080/api/auth";

    public boolean authenticate(LoginRequestDto loginRequestDto) {
        HttpURLConnection connection = null;
        try {
            String loginUrl = AUTHORIZATION_SERVER_BASE_URL + "/login";

            connection = createConnection(loginUrl);

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

            connection = createConnection(loginUrl);

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

    private static HttpURLConnection createConnection(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);
        return connection;
    }

    private static void sendRequest(HttpURLConnection connection, Object request) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String requestBody = objectMapper.writeValueAsString(request);

        OutputStream outputStream = connection.getOutputStream();
        outputStream.write(requestBody.getBytes());
        outputStream.flush();
    }

    private static String readResponse(InputStream inputStream) throws IOException {
        StringBuilder responseBuilder = new StringBuilder();
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            responseBuilder.append(new String(buffer, 0, bytesRead));
        }
        return responseBuilder.toString();
    }
}
