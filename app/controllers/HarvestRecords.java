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
    List<HarvestRecord> records = HarvestRecord.find.query().where().eq("user", Util.getUser()).order().asc("id").findList();
    
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
  
  public Result edit(Long id) {
    HarvestRecord record = HarvestRecord.find.byId(id);
    User user = Util.getUser();
    
    if (!user.equals(record.user)) {
      return badRequest("Zugriff nicht erlaubt!");
    }
    
    return ok(edit.render(record, formFactory.form(HarvestRecord.class).fill(record)));
  }
  
  public Result update(Long id) {
    HarvestRecord old_record = HarvestRecord.find.byId(id);
    User user = Util.getUser();
    
    if (!user.equals(old_record.user)) {
        return badRequest("Zugriff nicht erlaubt!");
    }
    
    Form<HarvestRecord> form = formFactory.form(HarvestRecord.class).bindFromRequest();

    if (form.hasErrors()) {
      return badRequest(edit.render(old_record, form));
    } 
    
    HarvestRecord record = form.get();
    record.id = old_record.id;
    record.user = Util.getUser();
    record.update();
      
    return redirect(routes.HarvestRecords.show(record.id));
  }
  
  public Result delete(Long id) {
    HarvestRecord record = HarvestRecord.find.byId(id);
    User user = Util.getUser();
    
    if (!user.equals(record.user)) {
      return badRequest("Zugriff nicht erlaubt!");
    }
    
    record.delete();
    
    return redirect(routes.HarvestRecords.index());
  }
}