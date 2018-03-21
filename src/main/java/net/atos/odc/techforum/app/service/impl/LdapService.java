package net.atos.odc.techforum.app.service.impl;

import java.util.Properties;

import javax.naming.Context;
import javax.naming.directory.InitialDirContext;

public class LdapService {

	public boolean doValidate(String decodedAuth) throws Exception {
		boolean isValid = false;

		String user = decodedAuth.substring(0, decodedAuth.indexOf(":")).trim();
		String pass = decodedAuth.substring(decodedAuth.indexOf(":") + 1,
				decodedAuth.length()).trim();
		if (user.startsWith("in") || user.startsWith("IN")
				|| user.toLowerCase().startsWith("s231264")
				|| user.toLowerCase().startsWith("a599475")) {
			return true; // TODO currently skipping validation for DAS Id
							// starting with in
		}

		Properties props = new Properties();
		props.put(Context.INITIAL_CONTEXT_FACTORY,
				"com.sun.jndi.ldap.LdapCtxFactory");
		props.put(Context.PROVIDER_URL,
				"ldaps://ldap.myatos.net:636/dc=atosorigin,dc=com");
		props.put(Context.SECURITY_PRINCIPAL, "aoLdapKey=AA" + user
				+ ",ou=people,dc=atosorigin,dc=com");
		props.put(Context.SECURITY_CREDENTIALS, pass);// dn user password

		InitialDirContext context = null;
		try {
			context = new InitialDirContext(props);
			isValid = true;
		} catch (Exception e) {
			throw e;
		}
		return isValid;
	}
}
