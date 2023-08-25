package com.radeel.DuplicataManagement.controller;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MultipartFile;

import com.radeel.DuplicataManagement.service.DuplicataService;
import com.radeel.DuplicataManagement.service.ElectricityDuplicataService;
import com.radeel.DuplicataManagement.util.DuplicataResponse;

import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

@Controller
@Validated
@Transactional
public class DuplicataController {

  @Autowired
  private ElectricityDuplicataService electricityDuplicataService;

  

  private DuplicataService duplicataService;

  @ExceptionHandler({
    IllegalStateException.class,
    ConstraintViolationException.class,
    MethodArgumentNotValidException.class,
    MissingServletRequestParameterException.class,
    MethodArgumentTypeMismatchException.class
  })
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody String Error(Exception exception){
    String err = "";
    if(exception instanceof MethodArgumentTypeMismatchException){
      err += "Localite or police is invalid !";
    }
    if(exception instanceof MethodArgumentNotValidException){
      var ex = (MethodArgumentNotValidException)exception;
      String errors = "";
      for (final FieldError error : ex.getBindingResult().getFieldErrors()) {
        errors += error.getField() + error.getDefaultMessage() + '\n';
      }
      err += String.join("\n",errors);
    }
    if(exception instanceof ConstraintViolationException){
      var ex = (ConstraintViolationException)exception;
      String errors = "";
      for(final var cv : ex.getConstraintViolations()){
        errors += cv.getMessage() + '\n';
      }
      err += errors;
    }
    if(exception instanceof MissingServletRequestParameterException){
      err += ((MissingServletRequestParameterException)exception).getParameterName() + " parameter is missing !";
    }
    if(exception instanceof IllegalStateException){
      err += ((IllegalStateException)exception).getMessage();
    }
    return err;
  }

  @GetMapping("/import")
  public String duplicata(){
    return "import";
  }

  @PostMapping("/import")
  public String AddData(
    @NotBlank(message = "gerance must not be blank !") String gerance,
    @RequestParam("files") MultipartFile[] files,
    String check,
    String localite,
    String police
    ) throws IOException{
    if(!gerance.equals("1") && !gerance.equals("2")){
      throw new IllegalStateException("Undefined gerance !");
    }
    if(files.length == 1 && files[0].isEmpty()){
      throw new IllegalStateException("Please select at least one file !");
    } 
    boolean elect = gerance.equals("1") ? false : true;
    short loc = 0;
    long pol = 0;
    if(check != null){
      if(localite != null && police != null){
        try {
          loc = Short.parseShort(localite);
          pol = Long.parseLong(police);
          boolean found = false;
          for(var file : files){
            String content = new String(file.getInputStream().readAllBytes(),StandardCharsets.UTF_8);
            if(duplicataService.saveDuplicata(content,loc, pol)){
              found = true;
              break;
            }
          }
          if(!found){
            throw new IllegalStateException(String.format(
                "No duplicata with localite %d and police %d was found !", localite, police
            ));
          }
          return "import";
        } catch (NumberFormatException e) {
          throw new IllegalStateException("localite and police must be valid !");
        }
      }else{
        throw new IllegalStateException("localite and police must be valid !");
      }
    }
    for(var file : files){
      String content = new String(file.getInputStream().readAllBytes(),StandardCharsets.UTF_8);
      duplicataService.saveDuplicatas(content);
    }
    return "import";
  }

  @GetMapping("/export")
  public String Export(){
    return "export";
  }

  @PostMapping(value = "/export",produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public List<DuplicataResponse> exportData(
    @Valid @RequestParam short localite,
    @Valid @RequestParam("gerance") int g,
    @Valid @RequestParam long police,
    @Valid @RequestParam @NotBlank(message = "Do not mess with the frontend !") String flexRadioDefault,
    @Valid @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate m1,
    @Valid @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate sm,
    @Valid @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate em
  ){
    if(flexRadioDefault.equals("1")){
      if(m1 == null){
        throw new IllegalStateException("Missing date input !");
      }
      var duplicata = duplicataService.exportDuplicata(localite,police,m1);
      return Collections.singletonList(duplicata);
    }
    if(sm == null || em == null){
      throw new IllegalStateException("Missing date inputs !");
    }
    return duplicataService.exportDuplicatas(localite, police, sm, em);
  }

}
