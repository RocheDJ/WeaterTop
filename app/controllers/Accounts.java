package controllers;


import models.Member;
import play.Logger;
import play.mvc.Controller;


public class Accounts extends Controller {

  public static void signup() {
    render("signup.html");
  }

  public static void login() {
    render("login.html");
  }

  public static void register(String firstname, String lastname, String email, String password) {
    Logger.info("Registering new user " + email);
    Member member = new Member(firstname, lastname, email, password);
    member.save();
    redirect("/");
  }


  public static void update(String firstname, String lastname, String email, String passwordOld, String passwordNew,String passwordConfirm) {
    Logger.info("Up-dating " + email);
    Member member = null;
    Boolean updatedOK = false;
    if (session.contains("logged_in_Memberid")) {
      String memberId = session.get("logged_in_Memberid");
      member = Member.findById(Long.parseLong(memberId));
      //Only allow editing if old password is valid and new passwords and confirm are a match
      if((member.password.equals(passwordOld)) && (passwordNew.equals(passwordConfirm))){
        member.email=email;
        member.firstname=firstname;
        member.lastname=lastname;
        member.password=passwordNew;
        member.save();
        updatedOK =true;
      }
    }
    render("edit-member.html", member,updatedOK);
  }


  public static void authenticate(String email, String password) {
    Logger.info("Attempting to authenticate with " + email + ":" + password);

    Member member = Member.findByEmail(email);
    if ((member != null) && (member.checkPassword(password) == true)) {
      Logger.info("Authentication successful");
      session.put("logged_in_Memberid", member.id);
      redirect("/dashboard");
    } else {
      Logger.info("Authentication failed");
      redirect("/login");
    }
  }

  public static void logout() {
    session.clear();
    redirect("/");
  }

  public static Member getLoggedInMember() {
    Member member = null;
    if (session.contains("logged_in_Memberid")) {
      String memberId = session.get("logged_in_Memberid");
      member = Member.findById(Long.parseLong(memberId));
    } else {
      login();
    }
    return member;
  }
}