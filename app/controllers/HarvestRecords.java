package controllers;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import models.HarvestRecord;
import models.User;
import models.Util;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.harvest_records.*;

@Security.Authenticated(Secured.class)
public class HarvestRecords extends Controller {
  @Inject FormFactory formFactory;
  
  public Result index() {
    List<HarvestRecord> records = HarvestRecord.find.where().eq("user", Util.getUser()).order().asc("id").findList();
    
    return ok(index.render(records));
  }
  
  public Result show(Long id) {
    HarvestRecord record = HarvestRecord.find.byId(id);
    
    return ok(show.render(record));
  }
  
  public Result add() {
    return ok(add.render(formFactory.form(HarvestRecord.class)));
  }
  
  public Result create() {
    Form<HarvestRecord> form = formFactory.form(HarvestRecord.class).bindFromRequest();

    if (form.hasErrors()) {
      return badRequest(add.render(form));
    } 
    
    HarvestRecord record = form.get();
    record.user = Util.getUser();
    record.save();
      
    return redirect(routes.HarvestRecords.show(record.id));
  }
  /*
  public Result edit(Long id) {
    HiveRecord record = HiveRecord.find.byId(id);
    User user = Util.getUser();
    Colony colony = record.colony;
    
    if (!user.equals(record.user)) {
      return badRequest("Zugriff nicht erlaubt!");
    }
    
    return ok(edit.render(record, formFactory.form(HiveRecord.class).fill(record), colony));
  }
  
  public Result update(Long id) {
    HiveRecord old_record = HiveRecord.find.byId(id);
    User user = Util.getUser();
    Colony colony = old_record.colony;
    
    if (!user.equals(old_record.user)) {
        return badRequest("Zugriff nicht erlaubt!");
    }
    
    Form<HiveRecord> form = formFactory.form(HiveRecord.class).bindFromRequest();

    if (form.hasErrors()) {
      return badRequest(edit.render(old_record, form, colony));
    } 
    
    HiveRecord record = form.get();
    record.id = old_record.id;
    record.colony = colony;
    record.user = Util.getUser();
    record.update();
      
    return redirect(routes.HiveRecords.show(colony.id));
  }
  
  public Result delete(Long id) {
    HiveRecord record = HiveRecord.find.byId(id);
    User user = Util.getUser();
    
    if (!user.equals(record.user)) {
      return badRequest("Zugriff nicht erlaubt!");
    }
    
    record.delete();
    
    return redirect(routes.HiveRecords.show(record.colony.id));
  }
  */
}