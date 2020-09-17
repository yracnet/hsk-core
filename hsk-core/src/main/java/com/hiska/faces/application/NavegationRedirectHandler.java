/**
 *   _   _ _     _         ____         __ _
 *  | | | (_)___| | ____ _/ ___|  ___  / _| |_
 *  | |_| | / __| |/ / _` \___ \ / _ \| |_| __|
 *  |  _  | \__ \   < (_| |___) | (_) |  _| |_
 *  |_| |_|_|___/_|\_\__,_|____/ \___/|_|  \__|
 *
 *  Copyright © 2020 HiskaSoft
 *  http://www.hiskasoft.com/licenses/LICENSE-2.0
 */
package com.hiska.faces.application;

import java.util.HashMap;
import java.util.Map;
import javax.faces.context.FacesContext;
import javax.faces.application.ConfigurableNavigationHandler;
import javax.faces.application.ConfigurableNavigationHandlerWrapper;
import com.hiska.faces.ViewKeeped;

public class NavegationRedirectHandler extends ConfigurableNavigationHandlerWrapper {
   private ConfigurableNavigationHandler wrapped;

   public NavegationRedirectHandler(ConfigurableNavigationHandler delegate) {
      this.wrapped = delegate;
   }

   @Override
   public ConfigurableNavigationHandler getWrapped() {
      return wrapped;
   }

   @Override
   public void handleNavigation(FacesContext context, String fromAction, String outcome) {
      if (outcome != null && !outcome.contains("faces-redirect")) {
         if (outcome.contains("?")) {
            outcome = outcome + "&faces-redirect=true";
         } else {
            outcome = outcome + "?faces-redirect=true";
         }
         Map<String, Object> dirMap = new HashMap<>();
         Map<String, Object> sessionScope = context.getExternalContext().getSessionMap();
         sessionScope.put("DIR_MAP", dirMap);
         Map<String, Object> viewMap = context.getViewRoot().getViewMap();
         viewMap.forEach((k, v) -> {
            ViewKeeped scope = v.getClass().getAnnotation(ViewKeeped.class);
            if (scope != null) {
               dirMap.put(k, v);
            }
         });
      }
      super.handleNavigation(context, fromAction, outcome);
   }
}
