package org.acme.resource;

import org.acme.entity.Company;
import org.acme.service.CompanyService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/companies")
public class CompanyResource {

    @Inject
    CompanyService service;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Company> findAll() { return service.findAll(); }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Company get(@PathParam("id") Integer id) { return service.find(id); }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Company create(Company company) { return service.create(company.getName()); }

    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public void delete(@PathParam("id") Integer id) { service.delete(id); }


}
