package com.radeel.DuplicataManagement.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.radeel.DuplicataManagement.model.Client;
import com.radeel.DuplicataManagement.model.Locality;
import com.radeel.DuplicataManagement.model.Month;
import com.radeel.DuplicataManagement.model.Place;
import com.radeel.DuplicataManagement.model.WaterDuplicata;
import com.radeel.DuplicataManagement.model.WaterPolice;
import com.radeel.DuplicataManagement.repository.WaterDuplicataRepository;
import com.radeel.DuplicataManagement.util.DuplicataResponse;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class WaterDuplicataService extends DuplicataService{

  @Autowired
  private WaterDuplicataRepository repository;

  private final static String header = "NUM_LOC$NUM_SEC$NUM_TRN$NUM_ORD$NUM_POL$CAT$NOM$ADRESSE_CONSOMMATION$USAGE$NUMERO_COMPTEUR$NBR_ROUE$COD_BANQUE$INDEX1$INDEX2_VALIDER$MOI$ANN$CONS$TR1$PR1$MT1$TVA1$TR2$PR2$MT2$TVA2$TR3$PR3$MT3$TVA3$TR4$PR4$MT4$TVA4$TR5$PR5$MT5$TVA5$TRA1$PRA1$MTA1$TVAA1$TRA2$PRA2$MTA2$TVAA2$TRA3$PRA3$MTA3$TVAA3$GUICH$TF$TVA$RDF_EAU$TVA_RDF$RDF_ASS$TVA_RFA$MONTANT_FACTURE_HT$NET$DATE_PAIE$AGENCE$DATE_INDEX_1$DATE_INDEX2$NBJ";

  private enum WatVars{
    NUM_LOC, NUM_SEC, NUM_TRN, NUM_ORD, NUM_POL, CAT, NOM, ADRESSE_CONSOMMATION, USAGE, NUMERO_COMPTEUR, NBR_ROUE,
    COD_BANQUE, INDEX1, INDEX2_VALIDER, MOI, ANN, CONS, TR1, PR1, MT1, TVA1, TR2, PR2, MT2, TVA2, TR3, PR3, MT3, TVA3,
    TR4, PR4, MT4, TVA4, TR5, PR5, MT5, TVA5, TRA1, PRA1, MTA1, TVAA1, TRA2, PRA2, MTA2, TVAA2, TRA3, PRA3, MTA3, TVAA3,
    GUICH, TF, TVA, RDF_EAU, TVA_RDF, RDF_ASS, TVA_RFA, MONTANT_FACTURE_HT, NET, DATE_PAIE, AGENCE, DATE_INDEX_1,
    DATE_INDEX2, NBJ
  };

  private final static EnumMap<WatVars,Integer> wat = new EnumMap<>(WatVars.class);
  static{
    int i = 0;
    for(var x : WatVars.values()){
      wat.put(x,i);
      ++i;
    }
  };

  private static int diff = 0;
  private static int order = -1;

  private static int getIndex(WatVars watVars) {
    int index = wat.get(watVars);
    order = index <= wat.get(watVars.CAT) ? index : index - diff;
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
    Locality location = addLocation(
      Short.parseShort(parts.get(getIndex(WatVars.NUM_LOC))),
      parts.get(getIndex(WatVars.AGENCE))
    );
    long police = Long.parseLong(parts.get(getIndex(WatVars.NUM_POL)));
    short sect = Short.parseShort(parts.get(getIndex(WatVars.NUM_SEC)));
    short trn = Short.parseShort(parts.get(getIndex(WatVars.NUM_TRN)));
    short ord = Short.parseShort(parts.get(getIndex(WatVars.NUM_ORD)));
    Place place = placeRepository.findByLocationAndSectAndTrnAndOrd(location, sect, trn, ord).orElseGet(
      () -> {
      String cat = diff == 0 ? parts.get(getIndex(WatVars.CAT)) : "A";
      String name = parts.get(getIndex(WatVars.NOM));
      Client client = generateClient(name, cat);
      return placeRepository.saveAndFlush(
      Place.builder()
      .location(location)
      .sect(Short.parseShort(parts.get(getIndex(WatVars.NUM_SEC))))
      .trn(Short.parseShort(parts.get(getIndex(WatVars.NUM_TRN))))
      .ord(Short.parseShort(parts.get(getIndex(WatVars.NUM_ORD))))
      .address(parts.get(getIndex(WatVars.ADRESSE_CONSOMMATION)))
      .client(client)
      .electricityPolice(new ArrayList<>())
      .waterPolice(new ArrayList<>())
      .build()
      );
      }
    );
    List<WaterPolice> waterPolice = place.getWaterPolice();
    if(waterPolice.size() == 0){
      waterPolice.add(wpoliceRepository.saveAndFlush(
        new WaterPolice(
          0,
          police,
          Long.parseLong(parts.get(getIndex(WatVars.NUMERO_COMPTEUR)).replaceAll("L","")),
          Integer.parseInt(parts.get(getIndex(WatVars.NBR_ROUE))),
          place
        )
      ));
    }

    Month mon = monthRepository.findById(Short.parseShort(parts.get(getIndex(WatVars.MOI)))).orElseThrow(
      () -> new IllegalStateException("Invalid month !")
    );
    short year = Short.parseShort(parts.get(getIndex(WatVars.ANN)));
    if  (repository.existsByPlaceAndMonthAndYear(place,mon,year)) {
        throw new IllegalStateException(String.format(
          "The electricity duplicata with localite %d and police %d\n"
              + "already exists for %d/%d",
          place.getLocation().getId(), waterPolice.get(0).getPolice(), year,
          mon.getId()));
    }

    LocalDate date = LocalDate.of(1, 1, 1);
    if(!parts.get(getIndex(WatVars.DATE_PAIE)).trim().equals("")){
      date = LocalDate.parse(parts.get(getIndex(WatVars.DATE_PAIE)),DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }

    var duplicata = new WaterDuplicata(
      0,
      place,
      mon,
      year,
      parts.get(getIndex(WatVars.USAGE)),
      Integer.parseInt(parts.get(getIndex(WatVars.COD_BANQUE))),
      Long.parseLong(parts.get(getIndex(WatVars.CONS))),
      Long.parseLong(parts.get(getIndex(WatVars.INDEX1))),
      Long.parseLong(parts.get(getIndex(WatVars.INDEX2_VALIDER))),
      Long.parseLong(parts.get(getIndex(WatVars.TR1))),
      Long.parseLong(parts.get(getIndex(WatVars.TR2))),
      Long.parseLong(parts.get(getIndex(WatVars.TR3))),
      Long.parseLong(parts.get(getIndex(WatVars.TR4))),
      Long.parseLong(parts.get(getIndex(WatVars.TR5))),
      new BigDecimal(parts.get(getIndex(WatVars.PR1))),
      new BigDecimal(parts.get(getIndex(WatVars.PR2))),
      new BigDecimal(parts.get(getIndex(WatVars.PR3))),
      new BigDecimal(parts.get(getIndex(WatVars.PR4))),
      new BigDecimal(parts.get(getIndex(WatVars.PR5))),
      new BigDecimal(parts.get(getIndex(WatVars.MT1))),
      new BigDecimal(parts.get(getIndex(WatVars.MT2))),
      new BigDecimal(parts.get(getIndex(WatVars.MT3))),
      new BigDecimal(parts.get(getIndex(WatVars.MT4))),
      new BigDecimal(parts.get(getIndex(WatVars.MT5))),
      new BigDecimal(parts.get(getIndex(WatVars.TVA1))),
      new BigDecimal(parts.get(getIndex(WatVars.TVA2))),
      new BigDecimal(parts.get(getIndex(WatVars.TVA3))),
      new BigDecimal(parts.get(getIndex(WatVars.TVA4))),
      new BigDecimal(parts.get(getIndex(WatVars.TVA5))),
      Long.parseLong(parts.get(getIndex(WatVars.TRA1))),
      Long.parseLong(parts.get(getIndex(WatVars.TRA2))),
      Long.parseLong(parts.get(getIndex(WatVars.TRA3))),
      new BigDecimal(parts.get(getIndex(WatVars.PRA1))),
      new BigDecimal(parts.get(getIndex(WatVars.PRA2))),
      new BigDecimal(parts.get(getIndex(WatVars.PRA3))),
      new BigDecimal(parts.get(getIndex(WatVars.MTA1))),
      new BigDecimal(parts.get(getIndex(WatVars.MTA2))),
      new BigDecimal(parts.get(getIndex(WatVars.MTA3))),
      new BigDecimal(parts.get(getIndex(WatVars.TVAA1))),
      new BigDecimal(parts.get(getIndex(WatVars.TVAA2))),
      new BigDecimal(parts.get(getIndex(WatVars.TVAA3))),
      Integer.parseInt(parts.get(getIndex(WatVars.GUICH))),
      new BigDecimal(parts.get(getIndex(WatVars.TF))),
      new BigDecimal(parts.get(getIndex(WatVars.TVA))),
      new BigDecimal(parts.get(getIndex(WatVars.RDF_EAU))),
      new BigDecimal(parts.get(getIndex(WatVars.RDF_ASS))),
      new BigDecimal(parts.get(getIndex(WatVars.TVA_RDF))),
      new BigDecimal(parts.get(getIndex(WatVars.TVA_RFA))),
      new BigDecimal(parts.get(getIndex(WatVars.MONTANT_FACTURE_HT))),
      new BigDecimal(parts.get(getIndex(WatVars.NET))),
      date,
      LocalDate.parse(parts.get(getIndex(WatVars.DATE_INDEX_1)),DateTimeFormatter.ofPattern("dd/MM/yyyy")),
      LocalDate.parse(parts.get(getIndex(WatVars.DATE_INDEX2)),DateTimeFormatter.ofPattern("dd/MM/yyyy")),
      Integer.parseInt(parts.get(getIndex(WatVars.NBJ)))
    );
    repository.save(duplicata);
    }catch(NumberFormatException exception){
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
      throw new IllegalStateException("Invalid water duplicata header !");
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
        localite == Short.parseShort(parts.get(getIndex(WatVars.NUM_LOC))) &&
        police == Long.parseLong(parts.get(getIndex(WatVars.NUM_POL)))
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
    if (!header.equals(scanner.nextLine())) {
      scanner.close();
      throw new IllegalStateException("Invalid water duplicata header !");
    }
    while (scanner.hasNextLine()) {
      saveDuplicata(scanner.nextLine());
    }
    scanner.close();
  }

  @Override
  public DuplicataResponse exportDuplicata(short localite, long police, LocalDate date) {
    var waterPolice = wpoliceRepository.findByPolice(police).stream()
    .filter(pol -> pol.getPlace().getLocation().getId() == localite).toList();
    if (waterPolice.size() == 0) {
      throw new IllegalStateException(String.format(
          "No water duplicata exists with localite %d and police %d !", localite, police));
    }
    var place = waterPolice.get(0).getPlace();
    return DuplicataResponse.fromWaterDuplicata(
        repository.findByPlaceAndMonthAndYear(
            place,
            monthRepository.findById((short) date.getMonthValue()).get(),
            (short) date.getYear()).orElseThrow(
                () -> new IllegalStateException(String.format(
                    "No water duplicata exists with localite %d and police %d for %d/%d !", localite, police,
                    date.getYear(),date.getMonthValue()))));
  }

  @Override
  public List<DuplicataResponse> exportDuplicatas(short localite, long police, LocalDate start, LocalDate end) {
    var waterElectricity = wpoliceRepository.findByPolice(police)
        .stream().filter(p -> p.getPlace().getLocation().getId() == localite)
        .toList();
    if (waterElectricity.size() == 0) {
      return Collections.emptyList();
    }
    var place = waterElectricity.get(0).getPlace();
    final int s = f(start.getYear(), start.getMonthValue());
    final int e = f(end.getYear(), end.getMonthValue());
    if (s > e) {
      throw new IllegalStateException("The ending date should be after the starting date !");
    }
    return repository.findByPlace(place).stream()
        .filter(d -> between(s, f(d.getYear(), d.getMonth().getId()), e))
        .map(DuplicataResponse::fromWaterDuplicata)
        .toList();
  }

}
