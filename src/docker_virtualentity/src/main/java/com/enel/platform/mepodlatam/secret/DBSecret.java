package com.enel.platform.mepodlatam.secret;

import lombok.Data;


@Data
public class DBSecret {	

	private String host;
	private String port;
	private String dbName;
	private String username;
	private String password;

}
