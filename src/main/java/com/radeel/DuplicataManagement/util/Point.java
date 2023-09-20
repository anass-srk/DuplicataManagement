package com.radeel.DuplicataManagement.util;

public record Point(short x,long y) {
  
  @Override
  public boolean equals(Object b){
    if(b == null || b.getClass() != getClass()){
      return false;
    }
    var p = (Point)b;
    return x == p.x() && y == p.y();
  }
}
