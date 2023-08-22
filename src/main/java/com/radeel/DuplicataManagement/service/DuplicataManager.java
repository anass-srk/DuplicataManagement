package com.radeel.DuplicataManagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.radeel.DuplicataManagement.model.Location;
import com.radeel.DuplicataManagement.repository.LocationRepository;
import com.radeel.DuplicataManagement.repository.MonthRepository;
import com.radeel.DuplicataManagement.repository.PlaceRepository;

@Service
public class DuplicataManager implements DuplicataService{

  @Autowired
  private LocationRepository locationRepository;

  @Autowired 
  private MonthRepository monthRepository;

  @Autowired
  private PlaceRepository placeRepository;

  @Autowired
  private UserService userService;

  public Location addLocation(Location location){
    if(locationRepository.existsById(location.getId())){
      var loc = locationRepository.findById(location.getId()).get();
      if(loc.getAgence().equals(location.getAgence())){
        return loc;
      }
      throw new IllegalStateException(String.format(
        "The agency corresponding to localite %d is %s !",location.getPlaces(),location.getAgence()
      ));
    }
    return locationRepository.save(location);
  }

  @Override
  public void importElectricityDuplicata(String content) {

  }
  
}
