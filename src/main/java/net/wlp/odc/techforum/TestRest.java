/**
 * 
 */
package net.wlp.odc.techforum;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.inject.Inject;

import net.wlp.odc.techforum.model.Foo;
import net.wlp.odc.techforum.service.FooService;

@Path("/api")
public class TestRest {
	
	
	@Inject
    private FooService fooService;

	@GET
	@Path("/json")
	@Produces({ "application/json" })
	public String getHelloWorldJSON() {
		Foo foo = new Foo();
		foo.setFoo("Hello");
		fooService.create(foo);
		return "{\"result\":\"" + " Hello World"
				+ "\"}";
	}
	
	

}
