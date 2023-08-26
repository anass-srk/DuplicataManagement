package com.radeel.DuplicataManagement.util;

import java.util.List;
import com.radeel.DuplicataManagement.model.ElectricityDuplicata;
import com.radeel.DuplicataManagement.model.Gerance;
import com.radeel.DuplicataManagement.model.Place;
import com.radeel.DuplicataManagement.model.WaterDuplicata;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DuplicataResponse {
  
  private Gerance type;
  
  private String title;

  private List<String> data;

  public static DuplicataResponse fromElectricityDuplicata(ElectricityDuplicata duplicata){
    String title = duplicata.getYear() + "/" + duplicata.getMonth().getId();
    final Place place = duplicata.getPlace();
    String addr1,addr2;
    if(place.getAddress().length() <= 15){
      addr1 = place.getAddress();
      addr2 = "";
    }else{
      addr1 = place.getAddress().substring(0, 15);
      addr2 = place.getAddress().substring(15);
    }
    List<String> data = List.of(
      String.valueOf(place.getLocation().getId()),
      String.valueOf(place.getSect()),
      String.valueOf(place.getTrn()),
      String.valueOf(place.getOrd()),
      place.getClient().getUsername(),
      addr1,
      addr2,
      String.valueOf(place.getPoliceElectricity()),
      String.valueOf(place.getElectricityAccount()),
      String.format(
        "%d%d%d%d%d%d",
        duplicata.getYear(),
        duplicata.getMonth().getId(),
        place.getLocation().getId(),
        place.getSect(),
        place.getTrn(),
        place.getOrd()
      ),
      String.valueOf(duplicata.getMonth().getId()),
      String.valueOf(duplicata.getYear()),
      String.valueOf(duplicata.getUsage()),
      String.valueOf(duplicata.getIdx1()),
      String.valueOf(duplicata.getIdx2()),
      String.valueOf(duplicata.getCons()),
      String.valueOf(duplicata.getConstr1()),
      String.valueOf(duplicata.getConstr2()),
      String.valueOf(duplicata.getConstr3()),
      String.valueOf(duplicata.getConstr4()),
      String.valueOf(duplicata.getConstr5()),
      String.valueOf(duplicata.getConstr6()),
      String.valueOf(duplicata.getPrix1()),
      String.valueOf(duplicata.getPrix2()),
      String.valueOf(duplicata.getPrix3()),
      String.valueOf(duplicata.getPrix4()),
      String.valueOf(duplicata.getPrix5()),
      String.valueOf(duplicata.getPrix6()),
      String.valueOf(duplicata.getMnt1()),
      String.valueOf(duplicata.getMnt2()),
      String.valueOf(duplicata.getMnt3()),
      String.valueOf(duplicata.getMnt4()),
      String.valueOf(duplicata.getMnt5()),
      String.valueOf(duplicata.getMnt6()),
      String.valueOf(duplicata.getMnttva1()),
      String.valueOf(duplicata.getMnttva2()),
      String.valueOf(duplicata.getMnttva3()),
      String.valueOf(duplicata.getMnttva4()),
      String.valueOf(duplicata.getMnttva5()),
      String.valueOf(duplicata.getMnttva6()),
      String.valueOf(duplicata.getTva_rdf()),
      String.valueOf(duplicata.getTva_entr()),
      String.valueOf(duplicata.getTva_loc()),
      String.valueOf(duplicata.getRdf()),
      String.valueOf(duplicata.getTaxe_entretien()),
      String.valueOf(duplicata.getTaxe_location()),
      String.valueOf(duplicata.getMnt_ht()),
      String.valueOf(duplicata.getTva()),
      String.valueOf(duplicata.getTppan()),
      String.valueOf(duplicata.getTimbre()),
      String.valueOf(duplicata.getNet_apayer())
    );
    return new DuplicataResponse(Gerance.ELECTRICITY, title, data);
  }

  public static DuplicataResponse fromWaterDuplicate(WaterDuplicata duplicata){
    String title = duplicata.getYear() + "/" + duplicata.getMonth().getId();
    final Place place = duplicata.getPlace();
    String addr1,addr2;
    if(place.getAddress().length() <= 15){
      addr1 = place.getAddress();
      addr2 = "";
    }else{
      addr1 = place.getAddress().substring(0, 15);
      addr2 = place.getAddress().substring(15);
    }
    List<String> data = List.of(
      String.valueOf(place.getLocation().getId()),
      String.valueOf(place.getSect()),
      String.valueOf(place.getTrn()),
      String.valueOf(place.getOrd()),
      place.getClient().getUsername(),
      addr1,
      addr2,
      String.valueOf(place.getPoliceWater()),
      String.valueOf(place.getWaterAccount()),
      String.format(
        "%d%d%d%d%d%d",
        duplicata.getYear(),
        duplicata.getMonth().getId(),
        place.getLocation().getId(),
        place.getSect(),
        place.getTrn(),
        place.getOrd()
      ),
      String.valueOf(duplicata.getMonth().getId()),
      String.valueOf(duplicata.getYear()),
      String.valueOf(duplicata.getUsage()),
      String.valueOf(duplicata.getIndex1()),
      String.valueOf(duplicata.getIndex2()),
      String.valueOf(duplicata.getCons()),
      String.valueOf(duplicata.getConstr1()),
      String.valueOf(duplicata.getConstr2()),
      String.valueOf(duplicata.getConstr3()),
      String.valueOf(duplicata.getConstr4()),
      String.valueOf(duplicata.getConstr5()),
      String.valueOf(duplicata.getConstra1()),
      String.valueOf(duplicata.getConstra2()),
      String.valueOf(duplicata.getConstra3()),
      String.valueOf(duplicata.getPrix1()),
      String.valueOf(duplicata.getPrix2()),
      String.valueOf(duplicata.getPrix3()),
      String.valueOf(duplicata.getPrix4()),
      String.valueOf(duplicata.getPrix5()),
      String.valueOf(duplicata.getPrixa1()),
      String.valueOf(duplicata.getPrixa2()),
      String.valueOf(duplicata.getPrixa3()),
      String.valueOf(duplicata.getMntht1()),
      String.valueOf(duplicata.getMntht2()),
      String.valueOf(duplicata.getMntht3()),
      String.valueOf(duplicata.getMntht4()),
      String.valueOf(duplicata.getMntht5()),
      String.valueOf(duplicata.getMnthta1()),
      String.valueOf(duplicata.getMnthta2()),
      String.valueOf(duplicata.getMnthta3()),
      String.valueOf(duplicata.getMnttva1()),
      String.valueOf(duplicata.getMnttva2()),
      String.valueOf(duplicata.getMnttva3()),
      String.valueOf(duplicata.getMnttva4()),
      String.valueOf(duplicata.getMnttva5()),
      String.valueOf(duplicata.getMnttvaa1()),
      String.valueOf(duplicata.getMnttvaa2()),
      String.valueOf(duplicata.getMnttvaa3()),
      String.valueOf(duplicata.getTva_rdf_eau()),
      String.valueOf(duplicata.getTva_rdf_ass()),
      String.valueOf(duplicata.getRed_fix_eau()),
      String.valueOf(duplicata.getRed_fix_ass()),
      String.valueOf(duplicata.getMnt_ht()),
      String.valueOf(duplicata.getTva()),
      String.valueOf(duplicata.getTimbre()),
      String.valueOf(duplicata.getNet())
    );
    return new DuplicataResponse(Gerance.WATER,title,data);
  }
}
