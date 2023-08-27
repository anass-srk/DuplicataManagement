package com.radeel.DuplicataManagement.util;

import java.time.LocalDate;

import com.radeel.DuplicataManagement.model.Gerance;
import com.radeel.DuplicataManagement.model.Request;
import com.radeel.DuplicataManagement.model.RequestState;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestResponse {
  
  private long id;

  private short localite;

  private long police;

  private Gerance type;

  private Long adminId;

  private long clientId;

  private RequestState state;

  private LocalDate requestDate;
  
  private LocalDate responseDate;

  public static RequestResponse fromRequest(Request request){

    var response = RequestResponse.builder()
    .id(request.getId())
    .localite(request.getLocalite())
    .police(request.getPolice())
    .type(Gerance.valueOf(request.getType().getName()))
    .adminId(null)
    .clientId(request.getClient().getId())
    .state(RequestState.valueOf(request.getStatus().getName()))
    .requestDate(request.getRequestDate())
    .build();

    if(!request.getStatus().getName().equals(RequestState.PENDING.name())){
      response.setAdminId(request.getAdmin().getId());
      response.setResponseDate(request.getResponseDate());
    }

    return response;
  }
}
