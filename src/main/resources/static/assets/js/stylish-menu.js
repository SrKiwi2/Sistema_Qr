/*
Template Name: Admin Template 
Author: Wrappixel

File: js
*/
// ==============================================================
// Auto select left navbar
// ==============================================================

$(function () {
  "use strict";
  var url = window.location + "";
  var path = url.replace(
    window.location.protocol + "//" + window.location.host + "/",
    ""
  );
  var element = $("ul#sidebarnav a").filter(function () {
    return this.href === url || this.href === path; // || url.href.indexOf(this.href) === 0;
  });
  element.parentsUntil(".sidebar-nav").each(function (index) {
    if ($(this).is("li") && $(this).children("a").length !== 0) {
      $(this).children("a").addClass("active");
      $(this).parent("ul#sidebarnav").length === 0
        ? $(this).addClass("active")
        : $(this).addClass("selected");
    } else if (!$(this).is("ul") && $(this).children("a").length === 0) {
      $(this).addClass("selected");
    } else if ($(this).is("ul")) {
      $(this).addClass("in");
    }
  });

  element.addClass("active");
  $("#sidebarnav a").on("click", function (e) {
    console.log("hello ...!!!!!!!!!!!");

    if (!$(this).hasClass("active")) {
      // hide any open menus and remove all other classes
      $("ul", $(this).parents("ul:first")).removeClass("in");
      $("a", $(this).parents("ul:first")).removeClass("active");

      // open our new menu and add the open class
      $(this).next("ul").addClass("in");
      $(this).addClass("active");
    } else if ($(this).hasClass("active")) {
      $(this).removeClass("active");
      $(this).parents("ul:first").removeClass("active");
      $(this).next("ul").removeClass("in");
    }
  });
  $("#sidebarnav >li >a.has-arrow").on("click", function (e) {
    e.preventDefault();
  });

  var menuid = element.closest("nav[class^=sidebar-nav").attr("id");
  var menu = menuid[menuid.length - 1];

  $("#menu-right-mini-" + menu).addClass("d-block");
  $("#mini-" + menu).addClass("selected");

  $(".mini-nav .mini-nav-item").on("click", function (e) {
    var id = $(this).attr("id");
    $(".mini-nav .mini-nav-item").removeClass("selected");
    $(this).addClass("selected");
    $(".sidebarmenu nav").removeClass("d-block");
    $("#menu-right-" + id).addClass("d-block");
    $("body").attr("data-sidebartype", "full");
  });

  $(".mini-nav .mini-nav-item").on("mouseenter", function () {
    var id = $(this).attr("id");
    let checkminisidebar = $(".sidebarmenu").hasClass("close");
    if (checkminisidebar) {
      //$("#mini-" + id).toggleClass("red");
      $(".sidebarmenu").addClass("hovermenus");
      $(".sidebar-nav").removeClass("left-none d-block");
      $("#menu-right-" + id).addClass("left-none d-block");
    }
  });
  $(".sidebarmenu").on("mouseleave", function () {
    var id = $(this).attr("id");
    let checkcloseclass = $(".sidebarmenu").hasClass("close");
    if (checkcloseclass) {
      //$("#mini-" + id).removeClass("red");
      $(".sidebarmenu").removeClass("hovermenus");
      $("#menu-right-" + id).removeClass("left-none d-block");
    }
  });

  $(".mini-nav-ul li.mini-nav-item").on("mouseenter", function () {
    var id = $(this).attr("id");
    //alert(id);
    let checkcloseclass = $(".sidebarmenu").hasClass("close");
    $(".mini-nav-ul li.mini-nav-item").removeClass("selected");
    if (checkcloseclass) {
      $("#" + id).addClass("selected");
    }
  });
});
