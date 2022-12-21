package controllers;

import javax.inject.Inject;

import com.typesafe.config.Config;
import models.BottlingRecord;
import models.HarvestRecord;
import models.User;
import models.Util;
import play.data.Form;
import play.data.FormFactory;
import play.i18n.MessagesApi;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;
import views.html.bottling_records.*;

@Security.Authenticated(Secured.class)
public class BottlingRecords extends Controller {
  private final FormFactory formFactory;
  private final Config config;
  private final MessagesApi messagesApi;

  @Inject
  public BottlingRecords(FormFactory formFactory, Config config, MessagesApi messagesApi) {
    this.formFactory = formFactory;
    this.config = config;
    this.messagesApi = messagesApi;
  }
  
  public Result show(Long id, Http.Request request) {
    BottlingRecord record = BottlingRecord.find.byId(id);
    
    return ok(show.render(record, request));
  }
  
  public Result add(Long harvestId, Http.Request request) {
    return ok(add.render(formFactory.form(BottlingRecord.class), harvestId, request, messagesApi.preferred(request)));
  }
  
  public Result create(Long harvestId, Http.Request request) {
    Form<BottlingRecord> form = formFactory.form(BottlingRecord.class).bindFromRequest(request);
    
    if (form.hasErrors()) {
      return badRequest(add.render(form, harvestId, request, messagesApi.preferred(request)));
    }
    
    HarvestRecord harvestRecord = HarvestRecord.find.byId(harvestId);
    User user = Util.getUser(request);
    if(harvestRecord == null || !harvestRecord.getUser().equals(user)) {
      return badRequest("Zugriff nicht erlaubt!");
    }
    
    BottlingRecord record = form.get();
    record.setHarvestRecord(harvestRecord);
    record.save();
      
    return redirect(routes.BottlingRecords.show(record.getId()));
  }
  
  public Result edit(Long id, Http.Request request) {
    BottlingRecord record = BottlingRecord.find.byId(id);
    User user = Util.getUser(request);
    User bottlingUser = record.getHarvestRecord().getUser();
    
    if (!user.equals(bottlingUser)) {
      return badRequest("Zugriff nicht erlaubt!");
    }
    
    return ok(edit.render(record, formFactory.form(BottlingRecord.class).fill(record), request, messagesApi.preferred(request)));
  }
  
  public Result update(Long id, Http.Request request) {
    BottlingRecord old_record = BottlingRecord.find.byId(id);
    User user = Util.getUser(request);
    User bottlingUser = old_record.getHarvestRecord().getUser();
    
    if (!user.equals(bottlingUser)) {
        return badRequest("Zugriff nicht erlaubt!");
    }
    
    Form<BottlingRecord> form = formFactory.form(BottlingRecord.class).bindFromRequest(request);

    if (form.hasErrors()) {
      return badRequest(edit.render(old_record, form, request, messagesApi.preferred(request)));
    } 
    
    BottlingRecord record = form.get();
    record.setId(old_record.getId());
    record.update();
      
    return redirect(routes.BottlingRecords.show(record.getId()));
  }
  
  public Result delete(Long id, Http.Request request) {
    BottlingRecord record = BottlingRecord.find.byId(id);
    User user = Util.getUser(request);
    User bottlingUser = record.getHarvestRecord().getUser();
    
    if (!user.equals(bottlingUser)) {
      return badRequest("Zugriff nicht erlaubt!");
    }
    
    record.delete();
    
    return redirect(routes.HarvestRecords.show(record.getHarvestRecord().getId()));
  }
}