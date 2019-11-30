package NewServer.auth;


import javax.annotation.Nullable;

public interface AuthService {

    void start();
    void stop();

    /**
     *
     * @param login
     * @param pass
     * @return nick or null
     */
    //либо javadoc либо @Nullablle (обозначается всегда когда может вернуться Null)
    @Nullable
    String getNickByLoginPass(String login, String pass);

}
