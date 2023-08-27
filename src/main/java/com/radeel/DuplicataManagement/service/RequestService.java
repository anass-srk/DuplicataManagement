package com.radeel.DuplicataManagement.service;

import java.time.LocalDate;
import java.util.List;

import com.radeel.DuplicataManagement.model.Client;
import com.radeel.DuplicataManagement.model.Gerance;
import com.radeel.DuplicataManagement.model.RequestState;
import com.radeel.DuplicataManagement.util.RequestResponse;

public interface RequestService {

  List<RequestResponse> getAllRequests();
  boolean answerRequest(long id,RequestState state);
  List<RequestResponse> getClientRequests(Client client);
  void addRequest(Client client,short localite,Gerance gerance,long police,LocalDate date);

}
