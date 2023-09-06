package com.radeel.DuplicataManagement.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.radeel.DuplicataManagement.model.Admin;
import com.radeel.DuplicataManagement.model.Client;
import com.radeel.DuplicataManagement.model.Gerance;
import com.radeel.DuplicataManagement.model.Request;
import com.radeel.DuplicataManagement.repository.DuplicataTypeRepository;
import com.radeel.DuplicataManagement.model.RequestState;
import com.radeel.DuplicataManagement.repository.MonthRepository;
import com.radeel.DuplicataManagement.repository.RequestRepository;
import com.radeel.DuplicataManagement.repository.RequestStatusRepository;
import com.radeel.DuplicataManagement.util.RequestResponse;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class RequestManager implements RequestService {

  @Autowired
  private RequestRepository repository;

  @Autowired
  private DuplicataTypeRepository duplicataTypeRepository;

  @Autowired
  private MonthRepository monthRepository;

  @Autowired
  private RequestStatusRepository requestStatusRepository;


  @Override
  public List<RequestResponse> getAllRequests() {
    return repository.findAll().stream()
    .map(RequestResponse::fromRequest).toList();
  }

  @Override
  public boolean answerRequest(Admin admin,long id, RequestState state) {
    var request = repository.findById(id);
    if(
      !request.isPresent() ||
      !request.get().getStatus().getName().equals(RequestState.PENDING.name())
    ){
      return false;
    }
    var re = request.get();
    re.setStatus(requestStatusRepository.findByName(state.name()).get());
    re.setAdmin(admin);
    re.setResponseDate(LocalDate.now());
    repository.save(re);
    return true;
  }

  @Override
  public List<RequestResponse> getClientRequests(Client client) {
    return repository.findByClient(client).stream()
    .map(RequestResponse::fromRequest).toList();
  }

  @Override
  public void addRequest(Client client, short localite, Gerance gerance, long police, LocalDate date) {
    var type = duplicataTypeRepository.findByName(gerance.name()).get();
    var month = monthRepository.findById((short)date.getMonthValue()).get();
    if(repository.existsByLocaliteAndPoliceAndTypeAndClientAndMonthAndYear(
      localite,police,type,
      client,month,(short)date.getYear()
    )){
      throw new IllegalStateException("A similar demand was already sent !");
    }
    repository.save(new Request(
      0,localite,police,null,client,month,(short)date.getYear(),LocalDate.now(),
      null,requestStatusRepository.findByName(RequestState.PENDING.name()).get(),type
    ));
  }

}
