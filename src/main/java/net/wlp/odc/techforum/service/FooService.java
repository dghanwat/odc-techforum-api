package net.wlp.odc.techforum.service;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import net.wlp.odc.techforum.model.Foo;
 

@Stateless 
public class FooService {
	
	@PersistenceContext
    private EntityManager entityManager;
 
 
    public Foo create(Foo foo) {
        entityManager.persist(foo);
        return foo;
    }
 
    public Foo find(Long id) {
        Foo foo = entityManager.find(Foo.class, id);
        return foo;
    }

}
