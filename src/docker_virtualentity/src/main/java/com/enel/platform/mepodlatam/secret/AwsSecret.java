package com.enel.platform.mepodlatam.secret;

/**
 * Describe the AWS secret content for the virtaul entity
 * with a RDBMS as backend.
 *
 * @author Giuseppe Caliendo
 */
public class AwsSecret {

    static final String HOST = "host";
    static final String PORT = "port";
    static final String DB_NAME = "sid";
    static final String USERNAME = "username";
    static final String PASSWORD = "password";

    private final String host;
    private final String port;
    private final String dbname;
    private final String username;
    private final String password;

    /**
     * Describe the AWS secret content for the virtaul entity
     * with a RDBMS as backend.
     *
     * @param host     RDBMS host
     * @param port     server port
     * @param dbname   database schema name
     * @param username username
     * @param password password
     */
    public AwsSecret(String host,
                     String port,
                     String dbname,
                     String username,
                     String password) {
        this.host = host;
        this.port = port;
        this.dbname = dbname;
        this.username = username;
        this.password = password;
    }

    public String getHost() {
        return host;
    }

    public String getPort() {
        return port;
    }

    public String getDbname() {
        return dbname;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "AwsSecret{" +
                "host='" + host + '\'' +
                ", port='" + port + '\'' +
                ", dbname='" + dbname + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
