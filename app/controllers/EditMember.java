package controllers;

import models.Member;
import play.Logger;
import play.mvc.Controller;

/**
 * This is the EditMember class that handles the member editing page,
 *
 * @author Dave
 * @version (27 - May - 2022)
 */

public class EditMember extends Controller {
  public static void index() {
    if (session.isEmpty()) {
      render("login.html");
    } else {
      Logger.info("Rendering EditMember");
      Member member = Accounts.getLoggedInMember();
      Boolean updatedOK = true;
      render("edit-member.html", member, updatedOK);
    }
  }


}
