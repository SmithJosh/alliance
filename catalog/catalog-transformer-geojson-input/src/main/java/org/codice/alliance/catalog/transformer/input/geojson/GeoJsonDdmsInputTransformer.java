/**
 * Copyright (c) Codice Foundation
 *
 * <p>This is free software: you can redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation, either version 3 of
 * the License, or any later version.
 *
 * <p>This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details. A copy of the GNU Lesser General Public
 * License is distributed along with this program and can be found at
 * <http://www.gnu.org/licenses/lgpl.html>.
 */
package org.codice.alliance.catalog.transformer.input.geojson;

import static org.apache.commons.lang.StringUtils.isEmpty;

import ddf.catalog.data.Metacard;
import ddf.catalog.data.MetacardType;
import ddf.catalog.data.impl.MetacardImpl;
import ddf.catalog.transform.CatalogTransformerException;
import ddf.catalog.transform.InputTransformer;
import ddf.catalog.transformer.input.geojson.GeoJsonInputTransformer;
import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Converts standard GeoJSON (geojson.org) into a Metacard. The limitation on the GeoJSON is that it
 * must conform to the {@link ddf.catalog.data.impl.BasicTypes#BASIC_METACARD} {@link MetacardType}.
 */
public class GeoJsonDdmsInputTransformer extends GeoJsonInputTransformer {

  private static final Logger LOGGER = LoggerFactory.getLogger(GeoJsonDdmsInputTransformer.class);

  private InputTransformer ddmsInputTransformer;
  private static final String METACARD_TYPE_PROPERTY_KEY = "metacard-type";
  private static final String METACARD_METADATA_PROPERTY_KEY = "metadata";

  @Override
  public Metacard getBaseMetacard(Map<String, Object> properties, List<MetacardType> metacardTypes)
      throws CatalogTransformerException {
    Metacard metacard = null;

    final String metadata = (String) properties.get(METACARD_METADATA_PROPERTY_KEY);

    if (isDDMS20(metadata) && ddmsInputTransformer != null) {
      LOGGER.debug("Found DDMS in metadata property ... creating DDMS metacard");
      try {
        metacard =
            ddmsInputTransformer.transform(new ByteArrayInputStream(metadata.getBytes("UTF-8")));
      } catch (Exception ex) {
        // Log and ignore this - fall to original geojson input transformer behavior
        LOGGER.debug("Exception creating DDMS metacard", ex);
      }
    }

    if (metacard == null) {
      final String propertyTypeName = (String) properties.get(METACARD_TYPE_PROPERTY_KEY);

      if (isEmpty(propertyTypeName) || metacardTypes == null) {
        LOGGER.debug(
            "MetacardType specified in input is null or empty.  Assuming default MetacardType");
        metacard = new MetacardImpl();
      } else {
        MetacardType metacardType =
            metacardTypes
                .stream()
                .filter(type -> type.getName().equals(propertyTypeName))
                .findFirst()
                .orElseThrow(
                    () ->
                        new CatalogTransformerException(
                            "MetacardType specified in input has not been registered with the system.  Cannot parse input.  MetacardType name: "
                                + propertyTypeName));

        LOGGER.debug("Found registered MetacardType: {}", propertyTypeName);
        metacard = new MetacardImpl(metacardType);
      }
    }
    return metacard;
  }

  // TODO: Beef this check up
  private boolean isDDMS20(String metadata) {
    boolean ddms20Found = false;
    if (StringUtils.startsWith(metadata, "<ddms:Resource")) {
      ddms20Found = true;
    }
    LOGGER.trace("DDMS found in string: {}", ddms20Found);
    return ddms20Found;
  }

  public void setDdmsInputTransformer(InputTransformer ddmsInputTransformer) {
    this.ddmsInputTransformer = ddmsInputTransformer;
  }
}
