package com.radeel.DuplicataManagement.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

import com.radeel.DuplicataManagement.model.Client;
import com.radeel.DuplicataManagement.model.ClientCategory;
import com.radeel.DuplicataManagement.model.Locality;
import com.radeel.DuplicataManagement.repository.ClientCategoryRepository;
import com.radeel.DuplicataManagement.repository.ClientRepository;
import com.radeel.DuplicataManagement.repository.ElectricityPoliceRepository;
import com.radeel.DuplicataManagement.repository.LocalityRepository;
import com.radeel.DuplicataManagement.repository.MonthRepository;
import com.radeel.DuplicataManagement.repository.PlaceRepository;
import com.radeel.DuplicataManagement.repository.WaterPoliceRepository;
import com.radeel.DuplicataManagement.util.DuplicataResponse;
import com.radeel.DuplicataManagement.util.Point;

import jakarta.transaction.Transactional;

@Transactional
public abstract class DuplicataService {

  @Autowired
  protected LocalityRepository locationRepository;

  @Autowired 
  protected MonthRepository monthRepository;

  @Autowired
  protected PlaceRepository placeRepository;

  @Autowired
  protected ClientCategoryRepository clientCategoryRepository;

  @Autowired
  protected ClientRepository clientRepository;

  @Autowired
  protected ElectricityPoliceRepository epoliceRepository;

  @Autowired
  protected WaterPoliceRepository wpoliceRepository;

  public abstract void saveDuplicata(String content);
  public abstract boolean saveDuplicata(String content,short localite,long police);
  public abstract void saveDuplicatas(String content);

  public void setPolices(Client client, List<Point> list1,List<Point> list2) {
    for (var p : list1) {
      var police = epoliceRepository.findByPolice(p.y())
      .stream().filter(pol -> pol.getPlace().getLocation().getId() == p.x()).toList();
      if(police.size() == 0){
        throw new IllegalStateException(String.format(
          "No place with localite %d and police %d found !", p.x(), p.y())
          );
      }
      var place = police.get(0).getPlace();
      var user = place.getClient();
      place.setClient(client);
      placeRepository.save(place);
      if(placeRepository.findByClient(user).size() == 0 && !user.isActive()){
        clientRepository.delete(user);
      }
    }
    for (var p : list2) {
      var police = wpoliceRepository.findByPolice(p.y())
      .stream().filter(pol -> pol.getPlace().getLocation().getId() == p.x()).toList();
      if(police.size() == 0){
        throw new IllegalStateException(String.format(
          "No place with localite %d and police %d found !", p.x(), p.y())
          );
      }
      var place = police.get(0).getPlace();
      var user = place.getClient();
      place.setClient(client);
      placeRepository.save(place);
      if(placeRepository.findByClient(user).size() == 0 && !user.isActive()){
        clientRepository.delete(user);
      }
    }
  }

  public abstract DuplicataResponse exportDuplicata(short localite,long police,LocalDate date);
  public abstract List<DuplicataResponse> exportDuplicatas(short localite,long police,LocalDate start,LocalDate end);

  protected boolean between(int s, int v, int e) {
    return v >= s && v <= e;
  }

  protected int f(int y, int m) {
    return 16 * y + m;
  }

  public Locality addLocation(short id,String agence){
    if(locationRepository.existsById(id)){
      var loc = locationRepository.findById(id).get();
      if(loc.getAgence().equals(agence)){
        return loc;
      }
      throw new IllegalStateException(String.format(
        "The agency corresponding to localite %d is %s !",id,agence
      ));
    }
    return locationRepository.save(new Locality(id, agence,new ArrayList<>()));
  }

  public ClientCategory addClientCategory(String name){
    return clientCategoryRepository.findByName(name).orElseGet(() ->
      clientCategoryRepository.save(new ClientCategory((short)0, name,new ArrayList<>()))
    );
  }

  public Client generateClient(String name,String category){
    String uuid = UUID.randomUUID().toString();
    while(clientRepository.existsByEmail(uuid)){
      uuid = UUID.randomUUID().toString();
    }
    return clientRepository.save( 
    new Client(
      (short)0,
      false,
      uuid,
      name,
      UUID.randomUUID().toString(),
      addClientCategory(category),
      new ArrayList<>(),
      new ArrayList<>(),
      new ArrayList<>()          
    )
    );
  }

  public List<Point> getElectricityPolicesByClient(Client client){
    return placeRepository.findByClient(client).stream()
    .filter(p -> p.getElectricityPolice().size() != 0)
    .map(p -> new Point(p.getLocation().getId(),p.getElectricityPolice().get(0).getPolice()))
    .toList();
  }

  public List<Point> getWaterPolicesByClient(Client client){
    return placeRepository.findByClient(client).stream()
    .filter(p -> p.getWaterPolice().size() != 0)
    .map(p -> new Point(p.getLocation().getId(),p.getWaterPolice().get(0).getPolice()))
    .toList();
  }

}
