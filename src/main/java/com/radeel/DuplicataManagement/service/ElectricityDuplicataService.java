package com.radeel.DuplicataManagement.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.radeel.DuplicataManagement.model.Client;
import com.radeel.DuplicataManagement.model.ElectricityDuplicata;
import com.radeel.DuplicataManagement.model.Location;
import com.radeel.DuplicataManagement.model.Month;
import com.radeel.DuplicataManagement.model.Place;
import com.radeel.DuplicataManagement.repository.ElectricityDuplicataRepository;
import com.radeel.DuplicataManagement.util.DuplicataResponse;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ElectricityDuplicataService extends DuplicataService{

  @Autowired
  private ElectricityDuplicataRepository repository;

  private static final String header = "NUM_LOC$NUM_SEC$NUM_TRN$NUM_ORD$NUM_POL$CAT$NOM$ADRESSE_CONSOMMATION$USAGE$NUMERO_COMPTEUR$COD_BANQUE$INDEX1$INDEX2_VALIDER$MOI$ANN$CONSOMMATION_TOTAL$TR1$PR1$MT1$TVA1$TR2$PR2$MT2$TVA2$TR3$PR3$MT3$TVA3$TR4$PR4$MT4$TVA4$TR5$PR5$MT5$TVA5$TR6$PR6$MT6$TVA6$GUICH$MNT_CONS$TF$TPPAN$TVA$MONTANT_TEC$TVA_TEC$MONTANT_TLC$TVA_TLC$TAXES$TVA_TAXES$MONTANT_FACTURE_HT$NET$NBF$DAT_PAIE$AGENCE$BON$TVA_BON$DATE_INDEX_1$DATE_INDEX2$NBJ$RFE_ELEC$TVA_RFE_ELEC";

  private enum EleVars{
    NUM_LOC, NUM_SEC, NUM_TRN, NUM_ORD, NUM_POL, CAT, NOM, ADRESSE_CONSOMMATION, USAGE, NUMERO_COMPTEUR, COD_BANQUE,
    INDEX1, INDEX2_VALIDER, MOI, ANN, CONSOMMATION_TOTAL, TR1, PR1, MT1, TVA1, TR2, PR2, MT2, TVA2, TR3, PR3, MT3, TVA3,
    TR4, PR4, MT4, TVA4, TR5, PR5, MT5, TVA5, TR6, PR6, MT6, TVA6, GUICH, MNT_CONS, TF, TPPAN, TVA, MONTANT_TEC,
    TVA_TEC, MONTANT_TLC, TVA_TLC, TAXES, TVA_TAXES, MONTANT_FACTURE_HT, NET, NBF, DAT_PAIE, AGENCE, BON, TVA_BON,
    DATE_INDEX_1, DATE_INDEX2, NBJ, RFE_ELEC, TVA_RFE_ELEC
  };

  private static final EnumMap<EleVars,Integer> ele = new EnumMap<>(EleVars.class);
  static{
    ele.put(EleVars.NUM_LOC, 0);
    ele.put(EleVars.NUM_SEC, 1);
    ele.put(EleVars.NUM_TRN, 2);
    ele.put(EleVars.NUM_ORD, 3);
    ele.put(EleVars.NUM_POL, 4);
    ele.put(EleVars.CAT, 5);
    ele.put(EleVars.NOM, 6);
    ele.put(EleVars.ADRESSE_CONSOMMATION, 7);
    ele.put(EleVars.USAGE, 8);
    ele.put(EleVars.NUMERO_COMPTEUR, 9);
    ele.put(EleVars.COD_BANQUE, 10);
    ele.put(EleVars.INDEX1, 11);
    ele.put(EleVars.INDEX2_VALIDER, 12);
    ele.put(EleVars.MOI, 13);
    ele.put(EleVars.ANN, 14);
    ele.put(EleVars.CONSOMMATION_TOTAL, 15);
    ele.put(EleVars.TR1, 16);
    ele.put(EleVars.PR1, 17);
    ele.put(EleVars.MT1, 18);
    ele.put(EleVars.TVA1, 19);
    ele.put(EleVars.TR2, 20);
    ele.put(EleVars.PR2, 21);
    ele.put(EleVars.MT2, 22);
    ele.put(EleVars.TVA2, 23);
    ele.put(EleVars.TR3, 24);
    ele.put(EleVars.PR3, 25);
    ele.put(EleVars.MT3, 26);
    ele.put(EleVars.TVA3, 27);
    ele.put(EleVars.TR4, 28);
    ele.put(EleVars.PR4, 29);
    ele.put(EleVars.MT4, 30);
    ele.put(EleVars.TVA4, 31);
    ele.put(EleVars.TR5, 32);
    ele.put(EleVars.PR5, 33);
    ele.put(EleVars.MT5, 34);
    ele.put(EleVars.TVA5, 35);
    ele.put(EleVars.TR6, 36);
    ele.put(EleVars.PR6, 37);
    ele.put(EleVars.MT6, 38);
    ele.put(EleVars.TVA6, 39);
    ele.put(EleVars.GUICH, 40);
    ele.put(EleVars.MNT_CONS, 41);
    ele.put(EleVars.TF, 42);
    ele.put(EleVars.TPPAN, 43);
    ele.put(EleVars.TVA, 44);
    ele.put(EleVars.MONTANT_TEC, 45);
    ele.put(EleVars.TVA_TEC, 46);
    ele.put(EleVars.MONTANT_TLC, 47);
    ele.put(EleVars.TVA_TLC, 48);
    ele.put(EleVars.TAXES, 49);
    ele.put(EleVars.TVA_TAXES, 50);
    ele.put(EleVars.MONTANT_FACTURE_HT, 51);
    ele.put(EleVars.NET, 52);
    ele.put(EleVars.NBF, 53);
    ele.put(EleVars.DAT_PAIE, 54);
    ele.put(EleVars.AGENCE, 55);
    ele.put(EleVars.BON, 56);
    ele.put(EleVars.TVA_BON, 57);
    ele.put(EleVars.DATE_INDEX_1, 58);
    ele.put(EleVars.DATE_INDEX2, 59);
    ele.put(EleVars.NBJ, 60);
    ele.put(EleVars.RFE_ELEC, 61);
    ele.put(EleVars.TVA_RFE_ELEC, 62);
  }

  private static int diff = 0;
  private static int order = -1;

  private static int getIndex(EleVars eleVars){
    int index = ele.get(eleVars);
    order = index <= ele.get(EleVars.CAT) ? index : index - diff;
    return order;
  }
  @Override
  public void saveDuplicata(String content) {
    List<String> parts = List.of(content.replaceAll(",",".").split("\\$"));
    if(parts.size() != 63 && parts.size() != 62){
      throw new IllegalStateException(String.format(
        "Missing data: %d of 63 info was found !",parts.size()
      ));
    }
    if(parts.size() == 62){
      diff = 1;
    }else{
      diff = 0;
    }
    try{
    Location location = addLocation(
      Short.parseShort(parts.get(getIndex(EleVars.NUM_LOC))),
      parts.get(getIndex(EleVars.AGENCE))
    );
    long police = Long.parseLong(parts.get(getIndex(EleVars.NUM_POL)));
    Place place;
    if(placeRepository.existsByLocationAndPoliceElectricity(location, police)){
      place = placeRepository.findByLocationAndPoliceElectricity(location, police).get();
    }else{
      String cat = diff == 0 ? parts.get(getIndex(EleVars.CAT)) : "A";
      String name = parts.get(getIndex(EleVars.NOM));
      Client client = generateClient(name, cat);
      place = Place.builder()
      .location(location)
      .sect(Short.parseShort(parts.get(getIndex(EleVars.NUM_SEC))))
      .trn(Short.parseShort(parts.get(getIndex(EleVars.NUM_TRN))))
      .ord(Short.parseShort(parts.get(getIndex(EleVars.NUM_ORD))))
      .policeElectricity(police)
      .address(parts.get(getIndex(EleVars.ADRESSE_CONSOMMATION)))
      .client(client)
      .electricityAccount(Long.parseLong(parts.get(getIndex(EleVars.NUMERO_COMPTEUR))))
      .build();
      placeRepository.saveAndFlush(place);
    }

    Month mon = monthRepository.findById(Short.parseShort(parts.get(getIndex(EleVars.MOI)))).orElseThrow(
      () -> new IllegalStateException("Invalid month !")
    );
    short year = Short.parseShort(parts.get(getIndex(EleVars.ANN)));
    if  (repository.existsByPlaceAndMonthAndYear(place,mon,year)) {
        throw new IllegalStateException(String.format(
          "The electricity duplicata with localite %d and police %d\n"
              + "already exists for %d/%d",
          place.getLocation().getId(), place.getPoliceElectricity(), year,
          mon.getId()));
    }

    LocalDate date = LocalDate.of(1, 1, 1);
    if(!parts.get(getIndex(EleVars.DAT_PAIE)).trim().equals("")){
      date = LocalDate.parse(parts.get(getIndex(EleVars.DAT_PAIE)),DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }
    
    var duplicata = new ElectricityDuplicata(
      (long)0,
      place,
      mon,
      year,
      Short.parseShort(parts.get(getIndex(EleVars.USAGE))),
      Integer.parseInt(parts.get(getIndex(EleVars.COD_BANQUE))),
      Long.parseLong(parts.get(getIndex(EleVars.INDEX1))),
      Long.parseLong(parts.get(getIndex(EleVars.INDEX2_VALIDER))),
      Long.parseLong(parts.get(getIndex(EleVars.CONSOMMATION_TOTAL))),
      Long.parseLong(parts.get(getIndex(EleVars.TR1))),
      Long.parseLong(parts.get(getIndex(EleVars.TR2))),
      Long.parseLong(parts.get(getIndex(EleVars.TR3))),
      Long.parseLong(parts.get(getIndex(EleVars.TR4))),
      Long.parseLong(parts.get(getIndex(EleVars.TR5))),
      Long.parseLong(parts.get(getIndex(EleVars.TR6))),
      new BigDecimal(parts.get(getIndex(EleVars.PR1))),
      new BigDecimal(parts.get(getIndex(EleVars.PR2))),
      new BigDecimal(parts.get(getIndex(EleVars.PR3))),
      new BigDecimal(parts.get(getIndex(EleVars.PR4))),
      new BigDecimal(parts.get(getIndex(EleVars.PR5))),
      new BigDecimal(parts.get(getIndex(EleVars.PR6))),
      new BigDecimal(parts.get(getIndex(EleVars.MT1))),
      new BigDecimal(parts.get(getIndex(EleVars.MT2))),
      new BigDecimal(parts.get(getIndex(EleVars.MT3))),
      new BigDecimal(parts.get(getIndex(EleVars.MT4))),
      new BigDecimal(parts.get(getIndex(EleVars.MT5))),
      new BigDecimal(parts.get(getIndex(EleVars.MT6))),
      new BigDecimal(parts.get(getIndex(EleVars.TVA1))),
      new BigDecimal(parts.get(getIndex(EleVars.TVA2))),
      new BigDecimal(parts.get(getIndex(EleVars.TVA3))),
      new BigDecimal(parts.get(getIndex(EleVars.TVA4))),
      new BigDecimal(parts.get(getIndex(EleVars.TVA5))),
      new BigDecimal(parts.get(getIndex(EleVars.TVA6))),
      Integer.parseInt(parts.get(getIndex(EleVars.GUICH))),
      new BigDecimal(parts.get(getIndex(EleVars.MNT_CONS))),
      new BigDecimal(parts.get(getIndex(EleVars.TF))),
      new BigDecimal(parts.get(getIndex(EleVars.TPPAN))),
      new BigDecimal(parts.get(getIndex(EleVars.TVA))),
      new BigDecimal(parts.get(getIndex(EleVars.MONTANT_TEC))),
      new BigDecimal(parts.get(getIndex(EleVars.TVA_TEC))),
      new BigDecimal(parts.get(getIndex(EleVars.MONTANT_TLC))),
      new BigDecimal(parts.get(getIndex(EleVars.TVA_TLC))),
      new BigDecimal(parts.get(getIndex(EleVars.TAXES))),
      new BigDecimal(parts.get(getIndex(EleVars.TVA_TAXES))),
      new BigDecimal(parts.get(getIndex(EleVars.MONTANT_FACTURE_HT))),
      new BigDecimal(parts.get(getIndex(EleVars.NET))),
      Integer.parseInt(parts.get(getIndex(EleVars.NBF))),
      date,
      Integer.parseInt(parts.get(getIndex(EleVars.BON))),
      new BigDecimal(parts.get(getIndex(EleVars.TVA_BON))),
      LocalDate.parse(parts.get(getIndex(EleVars.DATE_INDEX_1)),DateTimeFormatter.ofPattern("dd/MM/yyyy")),
      LocalDate.parse(parts.get(getIndex(EleVars.DATE_INDEX2)),DateTimeFormatter.ofPattern("dd/MM/yyyy")),
      Integer.parseInt(parts.get(getIndex(EleVars.NBJ))),
      new BigDecimal(parts.get(getIndex(EleVars.RFE_ELEC))),
      new BigDecimal(parts.get(getIndex(EleVars.TVA_RFE_ELEC)))
    );
    repository.save(duplicata);
    }
    catch(NumberFormatException exception){
      throw new IllegalStateException(order + "\t" + exception.getMessage());
    }
  }

  @Override
  public boolean saveDuplicata(String content, short localite, long police) {
    Scanner scanner = new Scanner(content);
    if (!scanner.hasNextLine()) {
      scanner.close();
      throw new IllegalStateException("The file is empty !");
    }
    if(!header.equals(scanner.nextLine())){
      scanner.close();
      throw new IllegalStateException("Invalid electricity duplicata header !");
    }
    while (scanner.hasNextLine()) {
      String line = scanner.nextLine();
      List<String> parts = List.of(line.replaceAll(",", ".").split("\\$"));
      if (parts.size() != 63 && parts.size() != 62) {
        scanner.close();
        throw new IllegalStateException(String.format(
            "Missing data: %d of 63 info was found !", parts.size()));
      }
      if (
        localite == Short.parseShort(parts.get(getIndex(EleVars.NUM_LOC))) &&
        police == Long.parseLong(parts.get(getIndex(EleVars.NUM_POL)))
      ) {
        saveDuplicata(line);
        scanner.close();
        return true;
      }
    }
    scanner.close();
    return false;
  }

  @Override
  public void saveDuplicatas(String content) {
    Scanner scanner = new Scanner(content);
    if (!scanner.hasNextLine()) {
      scanner.close();
      throw new IllegalStateException("The file is empty !");
    }
    if(!header.equals(scanner.nextLine())){
      scanner.close();
      throw new IllegalStateException("Invalid electricity duplicata header !");
    }
    while (scanner.hasNextLine()) {
      saveDuplicata(scanner.nextLine());
    }
    scanner.close();
  }

  @Override
  public DuplicataResponse exportDuplicata(short localite, long police, LocalDate date) {
    var location = locationRepository.findById(localite);
    if(!location.isPresent()){
      throw new IllegalStateException(String.format(
        "No electricity duplicata exists with localite %d !",localite
      ));
    }
    var place = placeRepository.findByLocationAndPoliceElectricity(
      location.get(),
      police
    );
    if(!place.isPresent()){
      throw new IllegalStateException(String.format(
        "No electricity duplicata exists with police %d !",police
      ));
    }
    return DuplicataResponse.fromElectricityDuplicata(
      repository.findByPlaceAndMonthAndYear(
        place.get(),
        monthRepository.findById((short)date.getMonthValue()).get(),
        (short)date.getYear()
      ).orElseThrow(
        () -> new IllegalStateException(String.format(
        "No electricity duplicata exists with localite %d and police %d for %d/%d !"
        ,localite,police,date.getYear(),monthRepository.findById((short)date.getMonthValue()).get()
        ))
      )
    );
  }

  @Override
  public List<DuplicataResponse> exportDuplicatas(short localite, long police, LocalDate start, LocalDate end) {
    var location = locationRepository.findById(localite);
    if(!location.isPresent()){
      return Collections.emptyList();
    }
    var place = placeRepository.findByLocationAndPoliceElectricity(
      location.get(),
      police
    );
    if(!place.isPresent()){
      return Collections.emptyList();
    }
    final int s = f(start.getYear(),start.getMonthValue());
    final int e = f(end.getYear(),end.getMonthValue());
    if(s > e){
      throw new IllegalStateException("The ending date should be after the starting date !");
    }
    return repository.findByPlace(place.get()).stream()
    .filter(d -> between(s,f(d.getYear(),d.getMonth().getId()),e))
    .map(DuplicataResponse::fromElectricityDuplicata)
    .toList();
  }
  
}
