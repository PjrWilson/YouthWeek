/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.org.wrington.youthweek.model;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author phil
 */
public class BookingImportDatabase {
  
  private static BookingImportDatabase instance = null;
  Map<Integer, ChildBookingImport> imports = new TreeMap<>();
  
  public static BookingImportDatabase getInstance() {
    if (instance == null) {
      instance = new BookingImportDatabase();
    }
    return instance;
  }
  
  public Collection<ChildBookingImport> getImports() {
    return imports.values();
  }
  
  public int addImports(List<ChildBookingImport> imports) {
    int replaced = 0;
    for (ChildBookingImport cbi : imports) {
      if (this.imports.containsKey(cbi.getSheetId())) {
        this.imports.replace(cbi.getSheetId(), cbi);
        ++replaced;
      }
      else {
        this.imports.put(cbi.getSheetId(), cbi);
      }
    }
    return replaced;
  }
  
  public void clear() {
    imports.clear();
  }
  
  public ChildBookingImport findBySheetId(int sheetId) {
    return imports.get(sheetId);
  }
}
