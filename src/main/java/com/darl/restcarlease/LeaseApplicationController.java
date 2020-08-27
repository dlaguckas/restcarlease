package com.darl.restcarlease;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
class LeaseApplicationController {

  private final LeaseApplicationRepository repository;
  private final LeaseApplicationModelAssembler assembler;


  LeaseApplicationController(LeaseApplicationRepository repository, LeaseApplicationModelAssembler assembler) {
    this.repository = repository;
    this.assembler = assembler;
  }

  @GetMapping("/leaseApplications")
  CollectionModel<EntityModel<LeaseApplication>> all() {
	  
	  List<EntityModel<LeaseApplication>> leaseApplications = repository.findAll().stream()
			  .map(assembler::toModel)
			  .collect(Collectors.toList());
	  return CollectionModel.of(leaseApplications, linkTo(methodOn(LeaseApplicationController.class).all()).withSelfRel());
  }

  @PostMapping("/leaseApplications")
  EntityModel<LeaseApplication> newLeaseApplication(@RequestBody LeaseApplication newLeaseApplication) {
	  newLeaseApplication.setApplicationStatus();
	  return assembler.toModel(repository.save(newLeaseApplication));
  }

  @GetMapping("/leaseApplications/{id}")
  EntityModel<LeaseApplication> one(@PathVariable Long id) {

    LeaseApplication leaseApplication = repository.findById(id)
      .orElseThrow(() -> new LeaseApplicationNotFoundException(id));
    
    return assembler.toModel(leaseApplication);
  }

  @PutMapping("/leaseApplications/{id}")
  EntityModel<LeaseApplication> replaceLeaseApplication(@RequestBody LeaseApplication newLeaseApplication, @PathVariable Long id) {

    return repository.findById(id)
      .map(leaseApplication -> {
        leaseApplication.setVehicleData(newLeaseApplication.getVehicleData());
        leaseApplication.setPersonData(newLeaseApplication.getPersonData());
        leaseApplication.setPersonIncome(newLeaseApplication.getPersonIncome());
        leaseApplication.setRequestedAmount(newLeaseApplication.getRequestedAmount());
        leaseApplication.setApplicationStatus();
        return assembler.toModel(repository.save(leaseApplication));
      })
      .orElseGet(() -> {
        newLeaseApplication.setApplicationId(id);
        return assembler.toModel(repository.save(newLeaseApplication));
      });
  }

  @DeleteMapping("/leaseApplications/{id}")
  void deleteLeaseApplication(@PathVariable Long id) {
    repository.deleteById(id);
  }
}

@Component
class LeaseApplicationModelAssembler implements RepresentationModelAssembler<LeaseApplication, EntityModel<LeaseApplication>> {

  @Override
  public EntityModel<LeaseApplication> toModel(LeaseApplication leaseApplication) {

    return EntityModel.of(leaseApplication,
        linkTo(methodOn(LeaseApplicationController.class).one(leaseApplication.getApplicationId())).withSelfRel(),
        linkTo(methodOn(LeaseApplicationController.class).all()).withRel("leaseApplications"));
  }
}

class LeaseApplicationNotFoundException extends RuntimeException {

	LeaseApplicationNotFoundException(Long id) {
	    super("Could not find lease application " + id);
	  }
}

@ControllerAdvice
class LeaseApplicationNotFoundAdvice {

  @ResponseBody
  @ExceptionHandler(LeaseApplicationNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  String LeaseApplicationNotFoundHandler(LeaseApplicationNotFoundException ex) {
    return ex.getMessage();
  }
}