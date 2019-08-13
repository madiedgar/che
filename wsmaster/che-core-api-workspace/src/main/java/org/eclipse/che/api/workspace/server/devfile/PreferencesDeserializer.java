/*
 * Copyright (c) 2012-2018 Red Hat, Inc.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   Red Hat, Inc. - initial API and implementation
 */
package org.eclipse.che.api.workspace.server.devfile;

import static java.lang.String.format;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Helps to deserialize multi-type preferences into {@code Component} as a {@code Map}.
 *
 * @author Max Shaposhnyk
 */
public class PreferencesDeserializer extends JsonDeserializer<Map<String, Serializable>> {

  @Override
  public Map<String, Serializable> deserialize(JsonParser jsonParser, DeserializationContext ctxt)
      throws IOException {
    Map<String, Serializable> result = new HashMap<>();
    jsonParser.nextToken();
    while (!JsonToken.END_OBJECT.equals(jsonParser.getCurrentToken())) {
      JsonToken currentToken = jsonParser.nextValue();
      switch (currentToken) {
        case VALUE_NUMBER_INT:
          result.put(jsonParser.getCurrentName(), jsonParser.getNumberValue());
          break;
        case VALUE_FALSE:
        case VALUE_TRUE:
          result.put(jsonParser.getCurrentName(), jsonParser.getValueAsBoolean());
          break;
        case VALUE_STRING:
          result.put(jsonParser.getCurrentName(), jsonParser.getValueAsString());
          break;
        default:
          throw new JsonParseException(
              jsonParser,
              format("Unexpected value of the preference '%s' ", jsonParser.getCurrentName()));
      }
      jsonParser.nextToken();
    }
    return result;
  }
}