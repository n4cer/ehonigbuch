package controllers;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import com.typesafe.config.Config;
import models.HarvestRecord;
import models.HarvestType;
import models.User;
import models.Util;
import play.data.Form;
import play.data.FormFactory;
import play.i18n.MessagesApi;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;
import views.html.harvest_records.*;

import static play.libs.Scala.asScala;

@Security.Authenticated(Secured.class)
public class HarvestRecords extends Controller {
  private final FormFactory formFactory;
  private final Config config;
  private final MessagesApi messagesApi;

  @Inject
  public HarvestRecords(FormFactory formFactory, Config config, MessagesApi messagesApi) {
    this.formFactory = formFactory;
    this.config = config;
    this.messagesApi = messagesApi;
  }
  
  public Result index(Http.Request request) {
    List<HarvestRecord> records = HarvestRecord.find.query().where().eq("user", Util.getUser(request)).order().asc("id").findList();
    
    return ok(index.render(records, request));
  }
  
  public Result show(Long id, Http.Request request) {
    HarvestRecord record = HarvestRecord.find.byId(id);
    
    return ok(show.render(record, request));
  }
  
  public Result add(Http.Request request) {
    List<HarvestType> harvestTypes = HarvestType.find.all();
    
    return ok(add.render(formFactory.form(HarvestRecord.class), asScala(harvestTypes), request, messagesApi.preferred(request)));
  }
  
  public Result create(Http.Request request) {
    Form<HarvestRecord> form = formFactory.form(HarvestRecord.class).bindFromRequest(request);
    List<HarvestType> harvestTypes = HarvestType.find.all();

    if (form.hasErrors()) {
      return badRequest(add.render(form, asScala(harvestTypes), request, messagesApi.preferred(request)));
    } 
    
    HarvestRecord record = form.get();
    record.setUser(Util.getUser(request));
    record.save();
      
    return redirect(routes.HarvestRecords.show(record.getId()));
  }
  
  public Result edit(Long id, Http.Request request) {
    HarvestRecord record = HarvestRecord.find.byId(id);
    List<HarvestType> harvestTypes = HarvestType.find.all();
    User user = Util.getUser(request);
    
    if (!user.equals(record.getUser())) {
      return badRequest("Zugriff nicht erlaubt!");
    }
    
    return ok(edit.render(record, formFactory.form(HarvestRecord.class).fill(record), asScala(harvestTypes), request, messagesApi.preferred(request)));
  }
  
  public Result update(Long id, Http.Request request) {
    HarvestRecord old_record = HarvestRecord.find.byId(id);
    List<HarvestType> harvestTypes = HarvestType.find.all();
    User user = Util.getUser(request);
    
    if (!user.equals(old_record.getUser())) {
        return badRequest("Zugriff nicht erlaubt!");
    }
    
    Form<HarvestRecord> form = formFactory.form(HarvestRecord.class).bindFromRequest(request);

    if (form.hasErrors()) {
      return badRequest(edit.render(old_record, form, asScala(harvestTypes), request, messagesApi.preferred(request)));
    } 
    
    HarvestRecord record = form.get();
    record.setId(old_record.getId());
    record.setUser(Util.getUser(request));
    record.update();
      
    return redirect(routes.HarvestRecords.show(record.getId()));
  }
  
  public Result delete(Long id, Http.Request request) {
    HarvestRecord record = HarvestRecord.find.byId(id);
    User user = Util.getUser(request);
    
    if (!user.equals(record.getUser())) {
      return badRequest("Zugriff nicht erlaubt!");
    }
    
    record.delete();
    
    return redirect(routes.HarvestRecords.index());
  }
}