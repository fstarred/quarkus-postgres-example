package org.acme.service;

import org.acme.entity.Company;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;
import javax.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class CompanyService {

    @Inject
    EntityManager entityManager;

    public List<Company> findAll() {
        final CriteriaQuery<Company> criteria = entityManager
                .getCriteriaBuilder()
                .createQuery(Company.class);
        criteria.select(criteria.from(Company.class));
        return entityManager.createQuery(criteria).getResultList();
    }

    public Company find(Integer id) {
        return entityManager.find(Company.class, id);
    }

    @Transactional
    public Company create(String name) {
        var output = new Company();
        output.setName(name);
        entityManager.persist(output);
        return output;
    }

    @Transactional
    public void delete(Integer id) {
        entityManager.remove(entityManager.getReference(Company.class, id));
    }
}
