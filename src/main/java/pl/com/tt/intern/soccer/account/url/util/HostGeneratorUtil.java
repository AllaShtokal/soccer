package pl.com.tt.intern.soccer.account.url.util;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static lombok.AccessLevel.PRIVATE;

@Component
@NoArgsConstructor(access = PRIVATE)
public final class HostGeneratorUtil {

    private static String serverAddress;
    private static String serverPort;

    public static String generate() {
        return serverAddress + ":" + serverPort;
    }

    @Value("${frontend.server.address}")
    public void setServerAddress(String serverAddress) {
        HostGeneratorUtil.serverAddress = serverAddress;
    }

    @Value("${frontend.server.port}")
    public void setServerPort(String serverPort) {
        HostGeneratorUtil.serverPort = serverPort;
    }
}
