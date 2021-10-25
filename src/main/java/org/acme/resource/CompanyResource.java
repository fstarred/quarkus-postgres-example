package org.acme.resource;

import org.acme.entity.Company;
import org.acme.service.CompanyService;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Optional;

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
    public Company get(@PathParam("id") Integer id) { return Optional.ofNullable(service.find(id)).orElseThrow(NotFoundException::new) ; }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Company create(@Valid Company company) {
        return service.create(company.getName());
    }

    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public void delete(@PathParam("id") Integer id) {
        Optional.ofNullable(service.find(id)).ifPresentOrElse(i -> service.delete(i), () -> {
            throw new NotFoundException();
        });
    }


}
