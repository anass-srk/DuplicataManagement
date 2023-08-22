package com.radeel.DuplicataManagement.controller;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.springframework.http.HttpStatus;
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
import org.springframework.web.multipart.MultipartFile;

import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.NotBlank;

@Controller
@Validated
@Transactional
public class Duplicata {

  @ExceptionHandler({
    IllegalStateException.class,
    ConstraintViolationException.class,
    MethodArgumentNotValidException.class,
    MissingServletRequestParameterException.class
  })
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody String Error(Exception exception){
    String err = "";
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
    @RequestParam("files") MultipartFile[] files
    ) throws IOException{
    if(!gerance.equals("1") && !gerance.equals("2")){
      throw new IllegalStateException("Undefined gerance !");
    }
    if(files.length == 1 && files[0].isEmpty()){
      throw new IllegalStateException("Please select at least one file !");
    } 
    System.out.println(gerance.equals("1") ? "water" : "electricity");
    for(var file : files){
      System.out.println("File name: " + file.getOriginalFilename());
      String content = new String(file.getInputStream().readAllBytes(),StandardCharsets.UTF_8);
      System.out.println("content length:" + content.length());
    }
    return "import";
  }

}
