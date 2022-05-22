package controllers;

import models.Member;
import play.Logger;
import play.mvc.Controller;
import models.Station;

import java.util.List;

public class EditMember extends Controller {
  public static void index() {
    Logger.info("Rendering EditMember");
    Member member = Accounts.getLoggedInMember();
    render("edit-member.html", member);
  }


}
