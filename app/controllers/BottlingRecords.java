package controllers;

import java.util.List;

import javax.inject.Inject;

import models.BottlingRecord;
import models.HarvestRecord;
import models.User;
import models.Util;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.bottling_records.*;

@Security.Authenticated(Secured.class)
public class BottlingRecords extends Controller {
  @Inject FormFactory formFactory;
  
  public Result show(Long id) {
    BottlingRecord record = BottlingRecord.find.byId(id);
    
    return ok(show.render(record));
  }
  
  public Result add(Long harvestId) {
    return ok(add.render(formFactory.form(BottlingRecord.class), harvestId));
  }
  
  public Result create(Long harvestId) {
    Form<BottlingRecord> form = formFactory.form(BottlingRecord.class).bindFromRequest();
    
    if (form.hasErrors()) {
      return badRequest(add.render(form, harvestId));
    }
    
    HarvestRecord harvestRecord = HarvestRecord.find.byId(harvestId);
    User user = Util.getUser();
    if(harvestRecord == null || !harvestRecord.user.equals(user)) {
      return badRequest("Zugriff nicht erlaubt!");
    }
    
    BottlingRecord record = form.get();
    record.harvestRecord = harvestRecord;
    record.save();
      
    return redirect(routes.BottlingRecords.show(record.id));
  }
  
  public Result edit(Long id) {
    BottlingRecord record = BottlingRecord.find.byId(id);
    User user = Util.getUser();
    User bottlingUser = record.harvestRecord.user;
    
    if (!user.equals(bottlingUser)) {
      return badRequest("Zugriff nicht erlaubt!");
    }
    
    return ok(edit.render(record, formFactory.form(BottlingRecord.class).fill(record)));
  }
  
  public Result update(Long id) {
    BottlingRecord old_record = BottlingRecord.find.byId(id);
    User user = Util.getUser();
    User bottlingUser = old_record.harvestRecord.user;
    
    if (!user.equals(bottlingUser)) {
        return badRequest("Zugriff nicht erlaubt!");
    }
    
    Form<BottlingRecord> form = formFactory.form(BottlingRecord.class).bindFromRequest();

    if (form.hasErrors()) {
      return badRequest(edit.render(old_record, form));
    } 
    
    BottlingRecord record = form.get();
    record.id = old_record.id;
    record.update();
      
    return redirect(routes.BottlingRecords.show(record.id));
  }
  
  public Result delete(Long id) {
    BottlingRecord record = BottlingRecord.find.byId(id);
    User user = Util.getUser();
    User bottlingUser = record.harvestRecord.user;
    
    if (!user.equals(bottlingUser)) {
      return badRequest("Zugriff nicht erlaubt!");
    }
    
    record.delete();
    
    return redirect(routes.HarvestRecords.show(record.harvestRecord.id));
  }
}