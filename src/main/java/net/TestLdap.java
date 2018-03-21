package net;

import java.util.Properties;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import net.atos.odc.techforum.app.model.UserDetailsDto;

public class TestLdap {

	public static void main(String[] args) {
		String adminUser = "A506826";
		String adminPass = "Nov@2016";

		Properties props = new Properties();
		props.put(Context.INITIAL_CONTEXT_FACTORY,
				"com.sun.jndi.ldap.LdapCtxFactory");
		props.put(Context.PROVIDER_URL,
				"ldaps://ldap.myatos.net:636/dc=atosorigin,dc=com");
		props.put(Context.SECURITY_PRINCIPAL, "aoLdapKey=AA" + adminUser
				+ ",ou=people,dc=atosorigin,dc=com");// adminuser - User with
														// special priviledge,
														// dn user
		props.put(Context.SECURITY_CREDENTIALS, adminPass);// dn user password

		String username = "a203833";// , "a508928"; a203833 // The User you want to find
		InitialDirContext context = null;
		try {
			context = new InitialDirContext(props);
		} catch (NamingException e) {
			e.printStackTrace();
		}
		SearchControls ctrls = new SearchControls();
		// ctrls.setReturningAttributes(new String[] { "givenName",
		// "sn","memberOf","mail" });
		ctrls.setReturningAttributes(new String[] { "*", "+" });
		ctrls.setSearchScope(SearchControls.SUBTREE_SCOPE);

		NamingEnumeration<SearchResult> answers = null;
		try {
			answers = context.search("ou=people", "(uid=" + username + ")",
					ctrls);
		} catch (NamingException e) {
			e.printStackTrace();
		}
		UserDetailsDto dto = new UserDetailsDto();
		try {
			javax.naming.directory.SearchResult result = answers.nextElement();
			NamingEnumeration e = result.getAttributes().getAll();
			while (e.hasMoreElements()) {
				Object name = (Object) e.nextElement();
				if (name.toString().startsWith("aoLegalGivenName:")) {
					dto.setFirstName(name.toString().replace("aoLegalGivenName:", ""));
				} else if (name.toString().startsWith("aoLegalSurname:")) {
					dto.setLastName(name.toString().replace("aoLegalSurname:", ""));
				} else if (name.toString().startsWith("l:")) {
					dto.setLocation(name.toString().replace("l:", ""));
				} else if (name.toString().startsWith("street:")) {
					dto.setLocation(name.toString().replace("street:", ""));
				}

				//System.out.println(name.toString());
			}
		} catch (NullPointerException ex) {

		}
		System.out.println(dto.getLocation() + dto.getFirstName() + dto.getLastName());

	}
}
