/*
 * Copyright 2012-2013 Continuuity,Inc. All Rights Reserved.
 */

package com.continuuity.passport.http.handlers;

import com.google.gson.JsonObject;

/**
 *
 */
public class Utils {

  //TODO: Clean up lot of these helper functions
  public static String getJsonError(String error) {
    JsonObject object = new JsonObject();
    object.addProperty("error",error);
    return object.toString();
  }

  public static String getJsonError(String error, String reason) {
    JsonObject object = new JsonObject();
    object.addProperty("error",error);
    object.addProperty("reason",reason);
    return object.toString();
  }

  public static String getJson(String status, String message) {
    JsonObject object = new JsonObject();
    object.addProperty("status",status);
    object.addProperty("message",message);
    return object.toString();
  }

  public static String getAuthenticatedJson(String result) {
    JsonObject object = new JsonObject();
    object.add("error",null);
    object.addProperty("result",result);
    return object.toString();

  }

  public static String getAuthenticatedJson(String error, String result) {
    JsonObject object = new JsonObject();
    object.addProperty("error",error);
    object.addProperty("result",result);
    return object.toString();
  }

  public static String getNonceJson(int result) {
    JsonObject object = new JsonObject();
    object.add("error",null);
    object.addProperty("result",result);
    return object.toString();
  }

  public static String getNonceJson(String error, int result) {
    JsonObject object = new JsonObject();
    object.addProperty("error",error);
    object.addProperty("result",result);
    return object.toString();
  }

  public static String getJsonOK() {
    JsonObject object = new JsonObject();
    object.add("error",null);
    return object.toString();
  }


}
